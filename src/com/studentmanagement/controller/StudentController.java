package com.studentmanagement.controller;

import com.studentmanagement.model.StudentPageResponse;
import com.studentmanagement.service.OperationStatus;

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
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestParam;
import com.studentmanagement.exception.ErrorResponse;
import org.springframework.web.servlet.View;

@RestController
public class StudentController {
    private final StudentService studentService;
    private final View error;

    public StudentController(StudentService studentService, View error){
        this.studentService = studentService;
        this.error = error;
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

    @DeleteMapping("/students/{id}")
    public ResponseEntity<StudentOperationResult> deleteStudent(@PathVariable int id) throws StudentManagementException{
        StudentOperationResult result = studentService.deleteStudent(id);
        if(result.getStatus() == OperationStatus.SUCCESS){
            return ResponseEntity.noContent().build();
        }else if(result.getStatus() == OperationStatus.STUDENT_NOT_FOUND){
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.badRequest().body(result);
    }

    @GetMapping("/students/search")
    public List<Student> searchStudents(@RequestParam String course , @RequestParam int age) throws StudentManagementException{
        return studentService.findStudentsByCourseAndAge(course , age);
    }

    @GetMapping("/students/page")
    public ResponseEntity<?> getStudentsByPage(@RequestParam int page , @RequestParam int size , @RequestParam(defaultValue = "id") String sortBy , @RequestParam(defaultValue = "asc") String sortDir) throws StudentManagementException{
        if(page < 0){
            ErrorResponse errorResponse = new ErrorResponse(400 , "Page number cannot be negative");
            return ResponseEntity.badRequest().body(errorResponse);
        }
        if(size <= 0){
            ErrorResponse errorResponse = new ErrorResponse(400 , "Size cannot be less than 1");
            return ResponseEntity.badRequest().body(errorResponse);
        }

        if(!sortBy.equals("id") && !sortBy.equals("name") && !sortBy.equals("email") && !sortBy.equals("age") && !sortBy.equals("course")){
            ErrorResponse errorResponse = new ErrorResponse(400 , "Invalid sort field");
            return ResponseEntity.badRequest().body(errorResponse);
        }

        if(!sortDir.equals("asc") && !sortDir.equals("desc")){
            ErrorResponse errorResponse = new ErrorResponse(400, "Invalid sort direction");
            return ResponseEntity.badRequest().body(errorResponse);
        }

        StudentPageResponse response = studentService.getStudentsPage(page , size , sortBy , sortDir);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/students/search/page")
    public ResponseEntity<?> searchStudentsPage(@RequestParam String course , @RequestParam int age , @RequestParam int page , @RequestParam int size , @RequestParam(defaultValue = "id") String sortBy , @RequestParam(defaultValue = "asc") String sortDir) throws StudentManagementException{
        if(course.isBlank()){
            ErrorResponse errorResponse = new ErrorResponse(400, "Course cannot be blank");
            return ResponseEntity.badRequest().body(errorResponse);
        }
        if(age <= 0){
            ErrorResponse errorResponse = new ErrorResponse(400, "Age must be greater than 0");
            return ResponseEntity.badRequest().body(errorResponse);
        }

        if(page < 0){
            ErrorResponse errorResponse = new ErrorResponse(400 , "Page number cannot be negative");
            return ResponseEntity.badRequest().body(errorResponse);
        }
        if(size <= 0){
            ErrorResponse errorResponse = new ErrorResponse(400 , "Size cannot be less than 1");
            return ResponseEntity.badRequest().body(errorResponse);
        }
        if(!sortBy.equals("id") && !sortBy.equals("name") && !sortBy.equals("email") && !sortBy.equals("age") && !sortBy.equals("course")) {
            ErrorResponse errorResponse = new ErrorResponse(400, "Invalid sort field");
            return ResponseEntity.badRequest().body(errorResponse);
        }
        if(!sortDir.equals("asc") && !sortDir.equals("desc")){
            ErrorResponse errorResponse = new ErrorResponse(400, "Invalid sort direction");
            return ResponseEntity.badRequest().body(errorResponse);
        }

        StudentPageResponse response = studentService.getStudentsByCourseAndAgePage(course , age , page , size , sortBy , sortDir);
        return ResponseEntity.ok(response);
    }

}
