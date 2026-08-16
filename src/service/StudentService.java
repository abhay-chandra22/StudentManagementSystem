package service;
import dao.StudentDAO;
import java.sql.SQLException;
import model.Student;

public class StudentService {
    private final StudentDAO studentDAO = new StudentDAO();
    public Student findStudentById(int id) throws SQLException{
        return studentDAO.findStudentById(id);
    }

    public void addStudent(Student student) throws SQLException{
        studentDAO.addStudent(student);
    }

    public void updateStudent(Student student) throws SQLException{
        studentDAO.updateStudent(student);
    }

    public void deleteStudent(int id) throws SQLException{
        studentDAO.deleteStudent(id);
    }
}
