package com.studentmanagement.dao;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.ResultSet;
import com.studentmanagement.model.Student;

import java.util.ArrayList;
import java.util.List;

import com.studentmanagement.exception.StudentManagementException;
import org.springframework.stereotype.Repository;

@Repository
public class StudentDAO {
    private final String url = "jdbc:mysql://localhost:3306/student_management";
    private final String user = "root";
    private final String password = System.getenv("DB_PASSWORD");

    private Connection getConnection() throws SQLException {
        Connection conn = DriverManager.getConnection(url, user, password);
        return conn;
    }

    public int addStudent(Student student) throws StudentManagementException{
        String sql = "INSERT INTO students (id , name , email , age , course) VALUES (?,?,?,?,?)";
        try(Connection conn = getConnection()){
            try (PreparedStatement preparedStatement = conn.prepareStatement(sql)){
                preparedStatement.setInt(1,student.getId());
                preparedStatement.setString(2, student.getName());
                preparedStatement.setString(3, student.getEmail());
                preparedStatement.setInt(4, student.getAge());
                preparedStatement.setString(5, student.getCourse());
                int rowAffected = preparedStatement.executeUpdate();
                return rowAffected;
            }
        }catch(SQLException e){
            throw new StudentManagementException("Database operation failed" , e);
        }
    }

    public int updateStudent(Student student) throws StudentManagementException{
        String sql = "UPDATE students SET name = ?, email = ?, age = ?, course = ? WHERE id = ?";
        try(Connection conn = getConnection()){
            try(PreparedStatement preparedStatement = conn.prepareStatement(sql)){
                preparedStatement.setString(1, student.getName());
                preparedStatement.setString(2, student.getEmail());
                preparedStatement.setInt(3 , student.getAge());
                preparedStatement.setString(4, student.getCourse());
                preparedStatement.setInt(5, student.getId());
                int rowAffected = preparedStatement.executeUpdate();
                return rowAffected;
            }
        }
        catch(SQLException e){
            throw new StudentManagementException("Database operation failed" , e);
        }
    }

    public int deleteStudent(int id) throws StudentManagementException{
            String sql = "DELETE FROM students WHERE id = ?";
            try(Connection conn = getConnection()){
               try(PreparedStatement preparedStatement = conn.prepareStatement(sql)){
                   preparedStatement.setInt(1, id);
                   int rowAffected = preparedStatement.executeUpdate();
                   return rowAffected;
               }
            }
            catch(SQLException e){
                throw new StudentManagementException("Database operation failed" , e);
            }
    }

    public Student findStudentById(int id) throws StudentManagementException {
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
        catch(SQLException e){
            throw new StudentManagementException("Database operation failed" , e);
        }
    }

    public List<Student> getAllStudents() throws StudentManagementException{
        List<Student> students = new ArrayList<>();
        String sql = "SELECT * FROM students";
        try(
                Connection conn = getConnection();
                PreparedStatement preparedStatement = conn.prepareStatement(sql)
        ){
            try(
                    ResultSet resultSet = preparedStatement.executeQuery()
            ){
                while(resultSet.next()){
                    int studentId = resultSet.getInt("id");
                    String name = resultSet.getString("name");
                    String email = resultSet.getString("email");
                    int age = resultSet.getInt("age");
                    String course = resultSet.getString("course");
                    Student student = new Student(studentId, name, email, age, course);
                    students.add(student);
                }
            }
        }
        catch (SQLException e){
            throw new StudentManagementException("Database operation failed" , e);
        }
        return students;
    }
}
