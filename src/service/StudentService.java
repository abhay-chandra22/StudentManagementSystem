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
    public AddStudentResult addStudent(Student student) throws SQLException{
        if(!validateStudent(student)){
            return AddStudentResult.INVALID_DATA;
        }
        if(findStudentById(student.getId()) == null){
            studentDAO.addStudent(student);
            return AddStudentResult.SUCCESS;
        }else{
            return AddStudentResult.DUPLICATE_ID;
        }
    }

    public UpdateStudentResult updateStudent(Student student) throws SQLException{
        if(!validateStudent(student)){
            return UpdateStudentResult.INVALID_DATA;
        }
        if(findStudentById(student.getId()) != null) {
            studentDAO.updateStudent(student);
            return UpdateStudentResult.SUCCESS;
        }else{
            return UpdateStudentResult.STUDENT_NOT_FOUND;
        }
    }

    public DeleteStudentResult deleteStudent(int id) throws SQLException{
        if(findStudentById(id) != null) {
            studentDAO.deleteStudent(id);
            return DeleteStudentResult.SUCCESS;
        }else{
            return DeleteStudentResult.STUDENT_NOT_FOUND;
        }
    }
    private boolean validateStudent(Student student){
        if(student.getId() < 1){
            System.out.println("Invalid Student ID");
            return false;
        }
        if(student.getName().isBlank()){
            System.out.println("Invalid Student Name");
            return false;
        }
        if (!student.getEmail().contains("@") || !student.getEmail().contains(".")) {
            System.out.println("Invalid Email");
            return false;
        }
        if(student.getAge() < 1 || student.getAge() > 100){
            System.out.println("Invalid Age");
            return false;
        }
        if (student.getCourse().isBlank()) {
            System.out.println("Invalid Course");
            return false;
        }
        return true;
    }
}
