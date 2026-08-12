import model.Student;

public class Main {
    public static void main(String[] args){
        System.out.println("Student Management System");
        Student st = new Student(
                101,
                "Abhay Chandra",
                "abhay@gmail.com",
                22,
                "CSE"
        );
        System.out.println(st);
    }
}
