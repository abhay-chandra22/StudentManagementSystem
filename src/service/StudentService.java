package service;
import dao.StudentDAO;
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
    public void addStudent(Student student) throws SQLException{
        if(student.getId() < 1){
            System.out.println("Invalid Student ID");
            return;
        }
        if(student.getName().isBlank()){
            System.out.println("Invalid Student Name");
            return;
        }
        if (!student.getEmail().contains("@") || !student.getEmail().contains(".")) {
            System.out.println("Invalid Email");
            return;
        }
        if(student.getAge() < 1 || student.getAge() > 100){
            System.out.println("Invalid Age");
            return;
        }
        if (student.getCourse().isBlank()) {
            System.out.println("Invalid Course");
            return;
        }
        if(findStudentById(student.getId()) == null){
            studentDAO.addStudent(student);
        }else{
            System.out.println("Student already exists");
        }
    }

    public void updateStudent(Student student) throws SQLException{
        if(findStudentById(student.getId()) != null) {
            studentDAO.updateStudent(student);
        }else{
            System.out.println("Student doesn't exist");
        }
    }

    public void deleteStudent(int id) throws SQLException{
        if(findStudentById(id) != null) {
            studentDAO.deleteStudent(id);
        }else{
            System.out.println("Student doesn't exist");
        }
    }
}
