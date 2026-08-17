import service.StudentService;
import java.sql.SQLException;
import java.util.List;
import java.util.Scanner;
import model.Student;

public class Main {
    public static void main(String[] args) throws SQLException{
        System.out.println("Student Management System");

        StudentService studentService = new StudentService();

        Scanner scanner = new Scanner(System.in);
        System.out.println("\n===== Student Management System =====");


        int choice = 0;
        while(choice != 6) {


            System.out.println("1. Add Student");
            System.out.println("2. View All Students");
            System.out.println("3. Find Student");
            System.out.println("4. Update Student");
            System.out.println("5. Delete Student");
            System.out.println("6. Exit");
            System.out.print("Enter your choice: ");

            choice = scanner.nextInt();

            switch (choice) {
                case 1:
                    System.out.print("Enter Student ID: ");
                    int id = scanner.nextInt();
                    System.out.print("Enter Student Name: ");
                    String name = scanner.next();
                    System.out.print("Enter Student Email: ");
                    String email = scanner.next();
                    System.out.print("Enter Student Age: ");
                    int age = scanner.nextInt();
                    System.out.print("Enter Student Course: ");
                    String course = scanner.next();

                    Student student = new Student(id, name, email, age, course);
                    studentService.addStudent(student);
                    break;
                case 2:
                    List<Student> students = studentService.getAllStudents();
                    for(Student s : students){
                        System.out.println(s);
                    }
                    break;
                case 3:
                    System.out.print("Enter Student ID: ");
                    int searchId = scanner.nextInt();
                    Student foundStudent = studentService.findStudentById(searchId);
                    if(foundStudent != null){
                        System.out.println("Student found:  " + foundStudent);
                    }else{
                        System.out.println("Student not found");
                    }
                    break;
                case 4:
                    System.out.print("Enter Student Id to update: ");
                    int updateId = scanner.nextInt();
                    System.out.print("Enter new Student Name: ");
                    String updateName = scanner.next();
                    System.out.print("Enter new Student Email: ");
                    String updateEmail = scanner.next();
                    System.out.print("Enter new Student Age: ");
                    int updateAge = scanner.nextInt();
                    System.out.print("Enter new Student Course: ");
                    String updateCourse = scanner.next();
                    Student updatedStudent = new Student(updateId, updateName , updateEmail , updateAge , updateCourse);
                    studentService.updateStudent(updatedStudent);
                    break;
                case 5:
                    System.out.println("Delete Student selected");
                    break;
                case 6:
                    System.out.println("Exiting...");
                    break;
                default:
                    System.out.println("Invalid choice");
                    break;
            }
        }
    }
}
