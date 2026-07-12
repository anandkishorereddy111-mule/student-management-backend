package com.students;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;
@EnableAsync
@SpringBootApplication(scanBasePackages = {"com.students", "com.students.service", "com.students.config"})
public class StudentManagementBackendApplication {
    public static void main(String[] args) {
        SpringApplication.run(StudentManagementBackendApplication.class, args);
    }
}