package com.students.controller;

import com.students.dto.StudentRegistrationRequest;
import com.students.service.SheetsService;
import org.springframework.web.bind.annotation.*;
@RestController
@RequestMapping("/api/students")
@CrossOrigin(origins = "http://localhost:5173")
public class StudentController {

    private final SheetsService sheetsService;

    public StudentController(SheetsService sheetsService) {
        this.sheetsService = sheetsService;
    }

    @PostMapping("/register")
    public String register(@RequestBody StudentRegistrationRequest request) throws Exception {
        sheetsService.appendStudent(request);
        return "Student registered successfully";
    }
}