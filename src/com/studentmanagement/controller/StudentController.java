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
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;


@OpenAPIDefinition(
        info = @Info(
                title = "Student Management System API",
                description = "REST API for managing student records",
                version = "1.0"
        )
)

@RestController
public class StudentController {
    private final StudentService studentService;
    private final PaginationValidator paginationValidator;

    public StudentController(StudentService studentService , PaginationValidator paginationValidator){
        this.studentService = studentService;
        this.paginationValidator = paginationValidator;
    }

    @Operation(
            summary = "Get all students",
            description = "Retrieves all students from the database"
    )
    @ApiResponse(
            responseCode = "200",
            description = "Students retrieved successfully"
    )
    @ApiResponse(
            responseCode = "500",
            description = "Database operation failed",
            content = @Content(
                    schema = @Schema(implementation = ErrorResponse.class)
            )
    )
    @GetMapping("/students")
    public List<Student> getAllStudents() throws StudentManagementException{
        return studentService.getAllStudents();
    }

    @Operation(
            summary = "Get student by ID",
            description = "Retrieves a student using their unique ID"
    )
    @ApiResponse(
            responseCode = "200",
            description = "Student retrieved successfully",
            content = @Content(
                    schema = @Schema(implementation = Student.class)
            )
    )
    @ApiResponse(
            responseCode = "404",
            description = "Student not found",
            content = @Content
    )
    @GetMapping("/students/{id}")
    public ResponseEntity<Student> getStudent(@PathVariable int id) throws StudentManagementException{
        Student student = studentService.findStudentById(id);
        if(student != null){
            return ResponseEntity.ok(student);
        }else{
            return ResponseEntity.notFound().build();
        }
    }

    @Operation(
            summary = "Add a new student",
            description = "Creates a new student record"
    )
    @ApiResponse(
            responseCode = "201",
            description = "Student created successfully",
            content = @Content(
                    schema = @Schema(implementation = StudentOperationResult.class)
            )
    )
    @ApiResponse(
            responseCode = "400",
            description = "Invalid student data",
            content = @Content(
                    schema = @Schema(implementation = StudentOperationResult.class)
            )
    )
    @ApiResponse(
            responseCode = "409",
            description = "Student ID already exists",
            content =  @Content(
                    schema = @Schema(implementation = StudentOperationResult.class)
            )
    )
    @ApiResponse(
            responseCode = "500",
            description = "Database operation failed",
            content = @Content(
                    schema = @Schema(implementation = ErrorResponse.class)
            )
    )
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

    @Operation(
            summary = "Update a student",
            description = "Updates a existing student using their ID"
    )
    @ApiResponse(
            responseCode = "200",
            description = "Student Updated successfully",
            content = @Content(
                    schema = @Schema(implementation = StudentOperationResult.class)
            )
    )
    @ApiResponse(
            responseCode = "400",
            description = "Invalid student data",
            content = @Content(
                    schema = @Schema(implementation = StudentOperationResult.class)
            )
    )
    @ApiResponse(
            responseCode = "404",
            description = "Student not found",
            content = @Content
    )
    @ApiResponse(
            responseCode = "500",
            description = "Database Operation Failed",
            content = @Content(
                    schema = @Schema(implementation = ErrorResponse.class)
            )
    )
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

    @Operation(
            summary = "Delete a student",
            description = "Deletes a student using their unique ID"
    )
    @ApiResponse(
            responseCode = "204",
            description = "Student deleted successfully",
            content = @Content
    )
    @ApiResponse(
            responseCode = "404",
            description = "Student not found",
            content = @Content
    )
    @ApiResponse(
            responseCode = "500",
            description = "Database operation failed",
            content = @Content(
                    schema = @Schema(implementation = ErrorResponse.class)
            )
    )
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

    @Operation(
            summary = "Search students",
            description = "Finds students by course and age"
    )
    @ApiResponse(
            responseCode = "200",
            description = "Students found successfully"
    )
    @ApiResponse(
            responseCode = "400",
            description = "Invalid query parameter",
            content = @Content(
                    schema = @Schema(implementation = ErrorResponse.class)
            )
    )
    @ApiResponse(
            responseCode = "500",
            description = "Database operation failed",
            content = @Content(
                    schema = @Schema(implementation = ErrorResponse.class)
            )
    )
    @GetMapping("/students/search")
    public List<Student> searchStudents(@RequestParam String course , @RequestParam int age) throws StudentManagementException{
        return studentService.findStudentsByCourseAndAge(course , age);
    }

    @Operation(
            summary = "Get paginated students",
            description = "Retrieves students with pagination and optional sorting"
    )
    @ApiResponse(
            responseCode = "200",
            description = "Students retrieved successfully",
            content = @Content(
                    schema = @Schema(implementation = StudentPageResponse.class)
            )
    )
    @ApiResponse(
            responseCode = "400",
            description = "Invalid pagination or sorting parameters",
            content = @Content(
                    schema = @Schema(implementation = ErrorResponse.class)
            )
    )
    @ApiResponse(
            responseCode = "500",
            description = "Database operation failed",
            content = @Content(
                    schema = @Schema(implementation = ErrorResponse.class)
            )
    )
    @GetMapping("/students/page")
    public ResponseEntity<?> getStudentsByPage(
            @Parameter(
                    name = "page",
                    description = "Zero-based page number"
            )
            @RequestParam int page,
            @Parameter(
                    name = "size",
                    description = "Number of students per page"
            )
            @RequestParam int size,
            @Parameter(
                    name = "sortBy",
                    description = "Field used for sorting: id, name, email, age, or course"
            )
            @RequestParam(defaultValue = "id") String sortBy,
            @Parameter(
                    name = "sortDir",
                    description = "Sorting direction: asc or desc"
            )
            @RequestParam(defaultValue = "asc") String sortDir)
            throws StudentManagementException {

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

    @Operation(
            summary = "Search students with pagination",
            description = "Finds students by course and age with pagination and optional sorting"
    )
    @ApiResponse(
            responseCode = "200",
            description = "Students retrieved successfully",
            content = @Content(
                    schema = @Schema(implementation = StudentPageResponse.class)
            )
    )
    @ApiResponse(
            responseCode = "400",
            description = "Invalid course, age, pagination, or sorting parameters",
            content = @Content(
                    schema = @Schema(implementation = ErrorResponse.class)
            )
    )
    @ApiResponse(
            responseCode = "500",
            description = "Database operation failed",
            content = @Content(
                    schema = @Schema(implementation = ErrorResponse.class)
            )
    )
    @GetMapping("/students/search/page")
    public ResponseEntity<?> searchStudentsPage(
            @Parameter(
                    name = "course",
                    description = "Course to search for"
            )
            @RequestParam String course ,
            @Parameter(
                    name = "age",
                    description = "Age of the students to search for"
            )
            @RequestParam int age ,
            @Parameter(
                    name = "page",
                    description = "Zero-based page number"
            )@RequestParam int page ,
            @Parameter(
                    name = "size",
                    description = "Number of students per page"
            )
            @RequestParam int size ,
            @Parameter(
                    name = "sortBy",
                    description = "Field used for sorting: id, name, email, age, or course"
            )
            @RequestParam(defaultValue = "id") String sortBy ,
            @Parameter(
                    name = "sortDir",
                    description = "Sorting direction: asc or desc"
            )
            @RequestParam(defaultValue = "asc") String sortDir)
            throws StudentManagementException{
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
