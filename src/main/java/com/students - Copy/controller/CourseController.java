package com.students.controller;

import com.students.dto.Course;
import com.students.service.CourseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/courses")
public class CourseController {

    @Autowired
    private CourseService courseService;

    @GetMapping
    public List<Course> getAllCourses() throws IOException {
        return courseService.getAllCourses();
    }

    @PostMapping
    public Course createCourse(@RequestBody Course course) throws IOException {
        return courseService.addCourse(course);
    }

    @PutMapping("/{id}")
    public Course updateCourse(@PathVariable int id, @RequestBody Course course) throws IOException {
        return courseService.updateCourse(id, course);
    }

    @DeleteMapping("/{id}")
    public void deactivateCourse(@PathVariable int id) throws IOException {
        courseService.deactivateCourse(id);
    }
}