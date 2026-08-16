import model.Student;

import dao.StudentDAO;
import java.sql.SQLException;

public class Main {
    public static void main(String[] args) throws SQLException{
        System.out.println("Student Management System");
        StudentDAO studentDAO = new StudentDAO();
        Student databaseStudent = studentDAO.findStudentById(101);
        System.out.println("Student : " + databaseStudent);
        /*Student newStudent = new Student(
                104,
                "Priya",
                "priya@gmail.com",
                20,
                "CSE"
        );
        studentDAO.addStudent(newStudent);*/
        /*Student updatedStudent = new Student(
                104,
                "Priya",
                "priya@gmail.com",
                21,
                "CSE"
        );
        studentDAO.updateStudent(updatedStudent);*/

        //studentDAO.deleteStudent(104);
    }
}
