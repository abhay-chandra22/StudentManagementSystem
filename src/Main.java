import service.StudentService;
import java.sql.SQLException;
import java.util.List;
import java.util.Scanner;
import model.Student;
import java.util.InputMismatchException;
import service.AddStudentResult;
import service.UpdateStudentResult;

public class Main {
    public static void main(String[] args){
        System.out.println("Student Management System");

        StudentService studentService = new StudentService();

        Scanner scanner = new Scanner(System.in);
        System.out.println("\n===== Student Management System =====");


        int choice = 0;
        while(choice != 6) {

            try {
                System.out.println("1. Add Student");
                System.out.println("2. View All Students");
                System.out.println("3. Find Student");
                System.out.println("4. Update Student");
                System.out.println("5. Delete Student");
                System.out.println("6. Exit");
                System.out.print("Enter your choice: ");

                choice = readInt(scanner);

                switch (choice) {
                    case 1:
                        System.out.print("Enter Student ID: ");
                        int id = readInt(scanner);
                        System.out.print("Enter Student Name: ");
                        String name = scanner.nextLine();
                        System.out.print("Enter Student Email: ");
                        String email = scanner.nextLine();
                        System.out.print("Enter Student Age: ");
                        int age = readInt(scanner);
                        System.out.print("Enter Student Course: ");
                        String course = scanner.nextLine();

                        Student student = new Student(id, name, email, age, course);
                        AddStudentResult result = studentService.addStudent(student);
                        if (result == AddStudentResult.SUCCESS) {
                            System.out.println("Student Added Successfully");
                        } else if (result == AddStudentResult.INVALID_DATA) {
                            System.out.println("Invalid Student Data");
                        } else if (result == AddStudentResult.DUPLICATE_ID) {
                            System.out.println("Student ID already exists");
                        }
                        break;
                    case 2:
                        List<Student> students = studentService.getAllStudents();
                        for (Student s : students) {
                            System.out.println(s);
                        }
                        break;
                    case 3:
                        System.out.print("Enter Student ID: ");
                        int searchId = readInt(scanner);
                        Student foundStudent = studentService.findStudentById(searchId);
                        if (foundStudent != null) {
                            System.out.println("Student found:  " + foundStudent);
                        } else {
                            System.out.println("Student not found");
                        }
                        break;
                    case 4:
                        System.out.print("Enter Student Id to update: ");
                        int updateId = readInt(scanner);
                        System.out.print("Enter new Student Name: ");
                        String updateName = scanner.nextLine();
                        System.out.print("Enter new Student Email: ");
                        String updateEmail = scanner.nextLine();
                        System.out.print("Enter new Student Age: ");
                        int updateAge = readInt(scanner);
                        System.out.print("Enter new Student Course: ");
                        String updateCourse = scanner.nextLine();
                        Student updatedStudent = new Student(updateId, updateName, updateEmail, updateAge, updateCourse);
                        UpdateStudentResult updatedResult = studentService.updateStudent(updatedStudent);
                        if(updatedResult == UpdateStudentResult.SUCCESS){
                            System.out.println("Student Updated Successfully");
                        }else if(updatedResult == UpdateStudentResult.INVALID_DATA){
                            System.out.println("Invalid Student Data");
                        }else if (updatedResult == UpdateStudentResult.STUDENT_NOT_FOUND) {
                            System.out.println("Student Not Found");
                        }
                        break;
                    case 5:
                        System.out.print("Enter Student Id to delete: ");
                        int deleteId = readInt(scanner);
                        studentService.deleteStudent(deleteId);
                        break;
                    case 6:
                        System.out.println("Exiting...");
                        break;
                    default:
                        System.out.println("Invalid choice");
                        break;
                }
            }catch(SQLException e){
                System.out.println("Database Error. Please try again.");
            }
        }
    }
    public static int readInt(Scanner scanner){
        while(true){
            try{
                int value = scanner.nextInt();
                scanner.nextLine();
                return value;
            }catch(InputMismatchException e){
                System.out.print("Please enter a valid number: ");
                scanner.nextLine();
            }
        }
    }
}
