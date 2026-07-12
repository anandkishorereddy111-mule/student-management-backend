package com.students.service;

import com.google.api.services.sheets.v4.Sheets;
import com.google.api.services.sheets.v4.model.ValueRange;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.students.dto.Course;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
public class CourseService {

    private static final String SPREADSHEET_ID = "11T9gl1TBBLsUqBW4V9bkDqsJQKgQou1YE_eyNqEeV0c"; // same one SheetsService already uses
    private static final String RANGE = "Courses!A2:E";

    @Autowired
    private Sheets sheetsClient; // reuse the same Sheets bean your SheetsService already injects

    // Read all courses from the sheet
    public List<Course> getAllCourses() throws IOException {
        ValueRange response = sheetsClient.spreadsheets().values()
                .get(SPREADSHEET_ID, RANGE)
                .execute();

        List<List<Object>> rows = response.getValues();
        List<Course> courses = new ArrayList<>();

        if (rows == null || rows.isEmpty()) {
            return courses; // no courses yet, return empty list rather than null
        }

        for (List<Object> row : rows) {
            int courseId = Integer.parseInt(row.get(0).toString());
            String courseName = row.get(1).toString();
            double price = Double.parseDouble(row.get(2).toString());
            String imageFileName = row.get(3).toString();
            boolean active = Boolean.parseBoolean(row.get(4).toString());

            courses.add(new Course(courseId, courseName, price, imageFileName, active));
        }

        return courses;
    }

    // Add a new course - auto-generates the next CourseId
    public Course addCourse(Course course) throws IOException {
        List<Course> existing = getAllCourses();

        int nextId = existing.stream()
                .mapToInt(Course::getCourseId)
                .max()
                .orElse(0) + 1;

        course.setCourseId(nextId);
        course.setActive(true); // new courses are active by default

        List<Object> newRow = List.of(
                course.getCourseId(),
                course.getCourseName(),
                course.getPrice(),
                course.getImageFileName(),
                course.isActive()
        );

        ValueRange body = new ValueRange().setValues(List.of(newRow));

        sheetsClient.spreadsheets().values()
                .append(SPREADSHEET_ID, RANGE, body)
                .setValueInputOption("USER_ENTERED")
                .execute();

        return course;
    }

    // Update an existing course's fields (finds the row by CourseId, overwrites it)
    public Course updateCourse(int courseId, Course updated) throws IOException {
        int rowIndex = findRowIndexByCourseId(courseId); // returns row NUMBER in the sheet (1-based, including header)

        updated.setCourseId(courseId); // don't let the request body change the ID

        List<Object> updatedRow = List.of(
                updated.getCourseId(),
                updated.getCourseName(),
                updated.getPrice(),
                updated.getImageFileName(),
                updated.isActive()
        );

        String updateRange = "Courses!A" + rowIndex + ":E" + rowIndex;
        ValueRange body = new ValueRange().setValues(List.of(updatedRow));

        sheetsClient.spreadsheets().values()
                .update(SPREADSHEET_ID, updateRange, body)
                .setValueInputOption("USER_ENTERED")
                .execute();

        return updated;
    }

    // Soft-delete: flips Active to FALSE instead of removing the row
    public void deactivateCourse(int courseId) throws IOException {
        List<Course> existing = getAllCourses();

        Course target = existing.stream()
                .filter(c -> c.getCourseId() == courseId)
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Course not found: " + courseId));

        target.setActive(false);
        updateCourse(courseId, target);
    }

    // Helper: find the actual sheet row number for a given CourseId
    private int findRowIndexByCourseId(int courseId) throws IOException {
        List<Course> existing = getAllCourses();

        for (int i = 0; i < existing.size(); i++) {
            if (existing.get(i).getCourseId() == courseId) {
                return i + 2; // +2 because data starts at row 2 (row 1 is headers) and list is 0-indexed
            }
        }
        throw new RuntimeException("Course not found: " + courseId);
    }
}