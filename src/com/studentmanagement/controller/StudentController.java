package com.studentmanagement.controller;

import org.springframework.web.bind.annotation.RestController;
import com.studentmanagement.service.StudentService;
import org.springframework.web.bind.annotation.GetMapping;
import com.studentmanagement.exception.StudentManagementException;
import com.studentmanagement.model.Student;
import java.util.List;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.http.ResponseEntity;

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
    public ResponseEntity<Student> getStudent(@PathVariable int id) throws StudentManagementException{
        Student student = studentService.findStudentById(id);
        if(student != null){
            return ResponseEntity.ok(student);
        }else{
            return ResponseEntity.notFound().build();
        }
    }
}
