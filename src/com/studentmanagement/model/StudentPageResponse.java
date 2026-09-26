package com.studentmanagement.model;

import java.util.List;

public class StudentPageResponse {
    private List<Student> students;
    private int page;
    private int size;
    private int totalStudents;
    private int totalPages;

    public StudentPageResponse(List<Student> students , int page , int size , int totalStudents , int totalPages){
        this.students = students;
        this.page = page;
        this.size = size;
        this.totalStudents = totalStudents;
        this.totalPages = totalPages;
    }

    public List<Student> getStudents() {
        return students;
    }

    public int getPage() {
        return page;
    }

    public int getSize() {
        return size;
    }

    public int getTotalStudents() {
        return totalStudents;
    }

    public int getTotalPages() {
        return totalPages;
    }
}
