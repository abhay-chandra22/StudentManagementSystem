import model.Student;
import java.util.ArrayList;
import java.util.Scanner;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Main {
    public static void main(String[] args){
        ArrayList<Student> students = new ArrayList<>();
        System.out.println("Student Management System");
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

        Scanner sc = new Scanner(System.in);
        System.out.print("Enter Student ID: ");
        int searchId = sc.nextInt();
        Student foundStudent = findStudentById(students , searchId);
        if(foundStudent != null){
            System.out.println("Student found : " + foundStudent);
        }else{
            System.out.println("Student not found");
        }

        String url = "jdbc:mysql://localhost:3306/student_management";
        String user = "root";
        String password = "Abhay@2004";

        try{
            Connection connection = DriverManager.getConnection(
                    url,
                    user,
                    password
            );
            System.out.println("Connected to Database successfully");
            connection.close();
        }
        catch(SQLException e){
            System.out.println("Database connection failed");
            e.printStackTrace();
        }


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
