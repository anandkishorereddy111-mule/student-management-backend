package com.students.controller;

import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.*;
import java.util.UUID;

@RestController
@RequestMapping("/api/courses")
public class ImageUploadController {

    // Absolute path to the student-facing frontend's public/images folder
    private static final String IMAGE_UPLOAD_DIR =
        "C:\\Users\\harib\\Desktop\\student-management-backend\\student-management-frontend\\public\\images\\";

    @PostMapping("/upload-image")
    public String uploadImage(@RequestParam("file") MultipartFile file) throws IOException {

        // Guard: reject empty uploads early
        if (file.isEmpty()) {
            throw new IOException("Uploaded file is empty");
        }

        // Generate a unique filename so two admins uploading "banner.png" never overwrite each other
        String originalName = file.getOriginalFilename();
        String extension = originalName.substring(originalName.lastIndexOf("."));
        String uniqueFileName = UUID.randomUUID().toString() + extension;

        // Make sure the folder exists (safety net, in case it was deleted)
        File dir = new File(IMAGE_UPLOAD_DIR);
        if (!dir.exists()) {
            dir.mkdirs();
        }

        // Write the file bytes to disk
        Path targetPath = Paths.get(IMAGE_UPLOAD_DIR + uniqueFileName);
        Files.write(targetPath, file.getBytes());

        // Return just the filename — this is what gets stored in the Sheet's imageFileName column
        return uniqueFileName;
    }
}