package com.studentmanagement;

import org.springframework.stereotype.Component;
import com.studentmanagement.service.StudentService;

@Component
public class StudentTest {

    private final StudentService studentService;

    public StudentTest(StudentService studentService) {
        this.studentService = studentService;
    }
}