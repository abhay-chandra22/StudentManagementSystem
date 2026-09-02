package com.studentmanagement.controller;

import com.studentmanagement.service.OperationStatus;
import jdk.dynalink.Operation;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RestController;
import com.studentmanagement.service.StudentService;
import org.springframework.web.bind.annotation.GetMapping;
import com.studentmanagement.exception.StudentManagementException;
import com.studentmanagement.model.Student;
import java.util.List;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import com.studentmanagement.service.StudentOperationResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.http.HttpStatus;
import com.studentmanagement.service.OperationStatus;
import org.springframework.web.bind.annotation.PutMapping;

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

    @PostMapping("/students")
    public ResponseEntity<StudentOperationResult> addStudent(@RequestBody Student student) throws StudentManagementException{
        StudentOperationResult result = studentService.addStudent(student);
        if(result.getStatus() == OperationStatus.SUCCESS){
            return ResponseEntity.status(HttpStatus.CREATED).body(result);
        }else if(result.getStatus() == OperationStatus.INVALID_DATA){
            return ResponseEntity.badRequest().body(result);
        }else if(result.getStatus() == OperationStatus.DUPLICATE_ID){
            return ResponseEntity.status(HttpStatus.CONFLICT).body(result);
        }
        return ResponseEntity.badRequest().body(result);
    }

    @PutMapping("/students/{id}")
    public ResponseEntity<StudentOperationResult> updateStudent(@PathVariable int id , @RequestBody Student student) throws StudentManagementException{
        student.setId(id);
        StudentOperationResult result = studentService.updateStudent(student);
        if(result.getStatus() == OperationStatus.SUCCESS){
            return ResponseEntity.ok(result);
        }else if(result.getStatus() == OperationStatus.INVALID_DATA){
            return ResponseEntity.badRequest().body(result);
        }else if(result.getStatus() == OperationStatus.STUDENT_NOT_FOUND){
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.badRequest().body(result);
    }
}
