package com.studentmanagement.controller;

import org.springframework.web.bind.annotation.RestController;
import com.studentmanagement.service.StudentService;
import org.springframework.web.bind.annotation.GetMapping;
import com.studentmanagement.exception.StudentManagementException;
import com.studentmanagement.model.Student;
import java.util.List;
import org.springframework.web.bind.annotation.PathVariable;

@RestController
public class StudentController {
    private final StudentService studentService;

    public StudentController(StudentService studentService){
        this.studentService = studentService;
    }

    @GetMapping("/students")
    public List<Student> getAllStudents() throws StudentManagementException{
        return studentService.getAllStudents();
    }

    @GetMapping("/students/{id}")
    public Student getStudent(@PathVariable int id) throws StudentManagementException{
        return studentService.findStudentById(id);
    }
}
