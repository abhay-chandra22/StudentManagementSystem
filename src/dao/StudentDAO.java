package dao;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.ResultSet;
import model.Student;


public class StudentDAO {
    private final String url = "jdbc:mysql://localhost:3306/student_management";
    private final String user = "root";
    private final String password = "YOUR_PASSWORD";

    private Connection getConnection() throws SQLException {
        Connection conn = DriverManager.getConnection(url, user, password);
        return conn;
    }
    public Student findStudentById(int id) throws SQLException {
        String sql = "SELECT * FROM students WHERE id = ?";

        try(
                Connection conn = getConnection();
                PreparedStatement preparedStatement = conn.prepareStatement(sql)
        ) {
            preparedStatement.setInt(1, id);
            try(
                    ResultSet resultSet = preparedStatement.executeQuery()
            ) {
                if (resultSet.next()) {
                    int studentId = resultSet.getInt("id");
                    String name = resultSet.getString("name");
                    String email = resultSet.getString("email");
                    int age = resultSet.getInt("age");
                    String course = resultSet.getString("course");
                    Student student = new Student(studentId, name, email, age, course);
                    return student;
                } else {
                    return null;
                }
            }
        }
    }
}
