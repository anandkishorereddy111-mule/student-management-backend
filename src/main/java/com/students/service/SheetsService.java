package com.students.service;

import com.google.api.services.sheets.v4.Sheets;
import com.google.api.services.sheets.v4.model.ValueRange;
import com.students.dto.StudentRegistrationRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@Service
public class SheetsService {

    private final Sheets sheets;
    private final EmailService emailService;

   private final String spreadsheetId = "11T9gl1TBBLsUqBW4V9bkDqsJQKgQou1YE_eyNqEeV0c";

    public SheetsService(Sheets sheets, EmailService emailService) {
        this.sheets = sheets;
        this.emailService = emailService;
    }

    // 1. Existing method to append student data
    public void appendStudent(StudentRegistrationRequest req) throws Exception {
        // Prepare the row data
        List<Object> row = Arrays.asList(
                req.getName(),
                req.getPhone(),
                req.getEmail(),
                req.getCourse(),
                "Pending",
                LocalDateTime.now().toString()
        );
        
        ValueRange body = new ValueRange().setValues(Collections.singletonList(row));
        
        // Append to Google Sheets
        sheets.spreadsheets().values()
                .append(spreadsheetId,"sheet1!A1", body)
                .setValueInputOption("RAW")
                .execute();

        // Send email confirmation
        emailService.sendRegistrationConfirmation(req.getEmail(), req.getName());
    }

    // 2. New method to update payment status by email
    public boolean updateStatusByEmail(String email) throws Exception {
        // Read all existing rows from the sheet
        ValueRange response = sheets.spreadsheets().values()
                .get(spreadsheetId, "sheet1!A2:F1000") // A2 skips header row; F covers all 6 columns
                .execute();

        List<List<Object>> rows = response.getValues();
        if (rows == null) return false;

        // Find the row where Column C (index 2) matches the email
        for (int i = 0; i < rows.size(); i++) {
            List<Object> row = rows.get(i);
            if (row.size() > 2 && row.get(2).toString().equalsIgnoreCase(email)) {

                // Row found — calculate its actual sheet row number
                int sheetRowNumber = i + 2; // +2 because data starts at row 2, and i is 0-indexed

                // Update only the Status cell (Column E) in that row
                ValueRange statusUpdate = new ValueRange()
                        .setValues(Collections.singletonList(
                                Collections.singletonList("Complete")
                        ));

                sheets.spreadsheets().values()
                        .update(spreadsheetId, "sheet1!E" + sheetRowNumber, statusUpdate)
                        .setValueInputOption("RAW")
                        .execute();

                return true; // successfully updated
            }
        }

        return false; // no matching email found
    }
}