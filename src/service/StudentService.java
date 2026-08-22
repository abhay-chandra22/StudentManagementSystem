package service;
import dao.StudentDAO;

import java.lang.management.OperatingSystemMXBean;
import java.sql.SQLException;
import model.Student;
import java.util.List;

public class StudentService {
    private final StudentDAO studentDAO = new StudentDAO();
    public Student findStudentById(int id) throws SQLException{
        return studentDAO.findStudentById(id);
    }

    public List<Student> getAllStudents() throws SQLException{
        return studentDAO.getAllStudents();
    }
    public StudentOperationResult addStudent(Student student) throws SQLException{
        ValidationResult validationResult = validateStudent(student);
        if(validationResult != ValidationResult.VALID){
            return new StudentOperationResult(OperationStatus.INVALID_DATA, validationResult);
        }
        if(findStudentById(student.getId()) == null){
            int rowAffected = studentDAO.addStudent(student);
            if(rowAffected == 1){
            return new StudentOperationResult(OperationStatus.SUCCESS , null);
            }else {
                throw new SQLException("Student could not be added.");
            }
        }else{
            return new StudentOperationResult(OperationStatus.DUPLICATE_ID, null);
        }
    }

    public StudentOperationResult updateStudent(Student student) throws SQLException{
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

    public StudentOperationResult deleteStudent(int id) throws SQLException{
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
