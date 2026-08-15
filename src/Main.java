import model.Student;
import java.util.ArrayList;
import dao.StudentDAO;
import java.sql.SQLException;

public class Main {
    public static void main(String[] args) throws SQLException{
        ArrayList<Student> students = new ArrayList<>();
        System.out.println("Student Management System");
        StudentDAO studentDAO = new StudentDAO();
        Student databaseStudent = studentDAO.findStudentById(101);
        System.out.println("Student : " + databaseStudent);
        Student student1 = new Student(
                101,
                "Abhay Chandra",
                "abhay@gmail.com",
                22,
                "CSE"
        );
        Student student2 = new Student(
                102,
                "Rahul",
                "rahul@gmail.com",
                21,
                "ECE"
        );
        Student student3 = new Student(
                103,
                "Aadi",
                "aadi@gmail.com",
                20,
                "CSE"
        );

        students.add(student1);
        students.add(student2);
        students.add(student3);
    }

    public static Student findStudentById(ArrayList<Student> students, int searchId){
        for (Student student : students) {
            if (student.getId() == searchId) {
                return student;
            }
        }
        return null;
    }
}
