import model.Student;

import dao.StudentDAO;
import java.sql.SQLException;

public class Main {
    public static void main(String[] args) throws SQLException{
        System.out.println("Student Management System");
        StudentDAO studentDAO = new StudentDAO();
        Student databaseStudent = studentDAO.findStudentById(101);
        System.out.println("Student : " + databaseStudent);
    }
}
