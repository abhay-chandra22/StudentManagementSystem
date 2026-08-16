import model.Student;

import service.StudentService;
import java.sql.SQLException;

public class Main {
    public static void main(String[] args) throws SQLException{
        System.out.println("Student Management System");
        StudentService studentService = new StudentService();
        Student databaseStudent = studentService.findStudentById(103);
        System.out.println("Student : " + databaseStudent);
    }
}
