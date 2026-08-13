import model.Student;
import java.util.ArrayList;
import java.util.Scanner;

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

        for(Student student : students){
            System.out.println(student);

        }

        System.out.println("Number of Students : " + students.size());
        System.out.println("Third Student : " + students.get(2));


        Scanner sc = new Scanner(System.in);
        System.out.print("Enter Student ID: ");
        int searchId = sc.nextInt();
        boolean found = false;
        System.out.println("Searching for student with ID " + searchId);
        for (Student student : students) {
            if (student.getId() == searchId) {
                System.out.println("Student found: " + student);
                found = true;
            }
        }
        if(!found){
            System.out.println("Student not found");
        }


    }
}
