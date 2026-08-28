package com.studentmanagement.service;
import com.studentmanagement.dao.StudentDAO;

import com.studentmanagement.model.Student;
import java.util.List;

import com.studentmanagement.exception.StudentManagementException;
import org.springframework.stereotype.Service;

@Service
public class StudentService {
    private final StudentDAO studentDAO;

    public StudentService(StudentDAO studentDAO){
        this.studentDAO = studentDAO;
    }
    public Student findStudentById(int id) throws StudentManagementException{
        return studentDAO.findStudentById(id);
    }

    public List<Student> getAllStudents() throws StudentManagementException{
        return studentDAO.getAllStudents();
    }
    public StudentOperationResult addStudent(Student student) throws StudentManagementException{
        ValidationResult validationResult = validateStudent(student);
        if(validationResult != ValidationResult.VALID){
            return new StudentOperationResult(OperationStatus.INVALID_DATA, validationResult);
        }
        if(findStudentById(student.getId()) == null){
            int rowAffected = studentDAO.addStudent(student);
            if(rowAffected == 1){
            return new StudentOperationResult(OperationStatus.SUCCESS , null);
            }else {
                throw new StudentManagementException("Student could not be added.");
            }
        }else{
            return new StudentOperationResult(OperationStatus.DUPLICATE_ID, null);
        }
    }

    public StudentOperationResult updateStudent(Student student) throws StudentManagementException{
        ValidationResult validationResult = validateStudent(student);
        if(validationResult != ValidationResult.VALID){
            return new StudentOperationResult(OperationStatus.INVALID_DATA , validationResult);
        }

        int rowAffected = studentDAO.updateStudent(student);
        if(rowAffected == 1){
            return new StudentOperationResult(OperationStatus.SUCCESS , null);
        }else{
            return new StudentOperationResult(OperationStatus.STUDENT_NOT_FOUND , null);
        }
    }

    public StudentOperationResult deleteStudent(int id) throws StudentManagementException{
        int rowAffected = studentDAO.deleteStudent(id);
        if(rowAffected == 1){
            return new StudentOperationResult(OperationStatus.SUCCESS , null);
        }else{
            return new StudentOperationResult(OperationStatus.STUDENT_NOT_FOUND , null);
        }
    }
    private ValidationResult validateStudent(Student student){
        if(student.getId() < 1){
            return ValidationResult.INVALID_ID;
        }
        if(student.getName().isBlank()){
            return ValidationResult.INVALID_NAME;
        }
        if (!student.getEmail().contains("@") || !student.getEmail().contains(".")) {
            return ValidationResult.INVALID_EMAIL;
        }
        if(student.getAge() < 1 || student.getAge() > 100){
            return ValidationResult.INVALID_AGE;
        }
        if (student.getCourse().isBlank()) {
            return ValidationResult.INVALID_COURSE;
        }
        return ValidationResult.VALID;
    }
}
