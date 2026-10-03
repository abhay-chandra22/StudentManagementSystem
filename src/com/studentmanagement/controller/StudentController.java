package com.studentmanagement.controller;

import com.studentmanagement.model.StudentPageResponse;
import com.studentmanagement.service.*;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.GetMapping;
import com.studentmanagement.exception.StudentManagementException;
import com.studentmanagement.model.Student;
import java.util.List;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestParam;
import com.studentmanagement.exception.ErrorResponse;

@RestController
public class StudentController {
    private final StudentService studentService;
    private final PaginationValidator paginationValidator;

    public StudentController(StudentService studentService , PaginationValidator paginationValidator){
        this.studentService = studentService;
        this.paginationValidator = paginationValidator;
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

        PaginationValidationResult validationResult = paginationValidator.validate(page,size,sortBy,sortDir);
        if(validationResult != PaginationValidationResult.VALID){
            String message;
            switch(validationResult){
                case INVALID_PAGE :
                    message = "Page number cannot be negative";
                    break;
                case INVALID_SIZE:
                    message = "Page size must be greater than 0";
                    break;
                case INVALID_SORT_FIELD:
                    message = "Invalid sort field";
                    break;
                case INVALID_SORT_DIRECTION:
                    message = "Invalid sort direction";
                    break;
                default :
                    message = "Invalid Pagination Parameters";
            }
            ErrorResponse errorResponse = new ErrorResponse(400 , message);
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

        PaginationValidationResult validationResult = paginationValidator.validate(page,size,sortBy,sortDir);
        if(validationResult != PaginationValidationResult.VALID){
            String message;
            switch(validationResult){
                case INVALID_PAGE :
                    message = "Page number cannot be negative";
                    break;
                case INVALID_SIZE:
                    message = "Page size must be greater than 0";
                    break;
                case INVALID_SORT_FIELD:
                    message = "Invalid sort field";
                    break;
                case INVALID_SORT_DIRECTION:
                    message = "Invalid sort direction";
                    break;
                default :
                    message = "Invalid Pagination Parameters";
            }
            ErrorResponse errorResponse = new ErrorResponse(400 , message);
            return ResponseEntity.badRequest().body(errorResponse);
        }

        StudentPageResponse response = studentService.getStudentsByCourseAndAgePage(course , age , page , size , sortBy , sortDir);
        return ResponseEntity.ok(response);
    }

}
