package com.students.dto;

public class Course {

    private Integer courseId;
    private String courseName;
    private double price;
    private String imageFileName;
    private boolean active;

    // Required for Jackson (Spring's JSON <-> Java converter) to build objects from request bodies
    public Course() {
    }

    public Course(Integer courseId, String courseName, double price, String imageFileName, boolean active) {
        this.courseId = courseId;
        this.courseName = courseName;
        this.price = price;
        this.imageFileName = imageFileName;
        this.active = active;
    }

    public Integer getCourseId() {
        return courseId;
    }

    public void setCourseId(Integer courseId) {
        this.courseId = courseId;
    }

    public String getCourseName() {
        return courseName;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getImageFileName() {
        return imageFileName;
    }

    public void setImageFileName(String imageFileName) {
        this.imageFileName = imageFileName;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }
}