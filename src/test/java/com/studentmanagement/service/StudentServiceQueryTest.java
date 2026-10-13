
package com.studentmanagement.service;

import com.studentmanagement.dao.StudentDAO;
import com.studentmanagement.exception.StudentManagementException;
import com.studentmanagement.model.Student;
import com.studentmanagement.model.StudentPageResponse;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class StudentServiceQueryTest {

    @Mock
    private StudentDAO studentDAO;

    @InjectMocks
    private StudentService studentService;


    // 1. Find a student by ID

    @Test
    void shouldFindStudentById() throws StudentManagementException {

        Student student = new Student(
                101, "Abhay", "abhay@gmail.com", 22, "CSE"
        );

        when(studentDAO.findStudentById(101))
                .thenReturn(student);

        Student result = studentService.findStudentById(101);

        assertEquals(student, result);
        verify(studentDAO).findStudentById(101);
    }


    // 2. Get all students

    @Test
    void shouldReturnAllStudents() throws StudentManagementException {

        List<Student> students = List.of(
                new Student(101, "Abhay", "abhay@gmail.com", 22, "CSE"),
                new Student(102, "Rahul", "rahul@gmail.com", 23, "ECE")
        );

        when(studentDAO.getAllStudents()).thenReturn(students);

        List<Student> result = studentService.getAllStudents();

        assertEquals(students, result);
        verify(studentDAO).getAllStudents();
    }


    // 3. Search students by course

    @Test
    void shouldFindStudentsByCourse() throws StudentManagementException {

        List<Student> students = List.of(
                new Student(101, "Abhay", "abhay@gmail.com", 22, "CSE")
        );

        when(studentDAO.findStudentsByCourse("CSE"))
                .thenReturn(students);

        List<Student> result = studentService.findStudentsByCourse("CSE");

        assertEquals(students, result);
        verify(studentDAO).findStudentsByCourse("CSE");
    }


    // 4. Search students by course and age

    @Test
    void shouldFindStudentsByCourseAndAge() throws StudentManagementException {

        List<Student> students = List.of(
                new Student(101, "Abhay", "abhay@gmail.com", 22, "CSE")
        );

        when(studentDAO.findStudentsByCourseAndAge("CSE", 22))
                .thenReturn(students);

        List<Student> result =
                studentService.findStudentsByCourseAndAge("CSE", 22);

        assertEquals(students, result);
        verify(studentDAO).findStudentsByCourseAndAge("CSE", 22);
    }


    // 5. Return paginated students with metadata

    @Test
    void shouldReturnPaginatedStudents() throws StudentManagementException {

        List<Student> students = List.of(
                new Student(101, "Abhay", "abhay@gmail.com", 22, "CSE")
        );

        when(studentDAO.getAllStudentsByPage(1, 10, "name", "desc"))
                .thenReturn(students);

        when(studentDAO.countStudents()).thenReturn(23);

        StudentPageResponse response =
                studentService.getStudentsPage(1, 10, "name", "desc");

        assertEquals(students, response.getStudents());
        assertEquals(1, response.getPage());
        assertEquals(10, response.getSize());
        assertEquals(23, response.getTotalStudents());
        assertEquals(3, response.getTotalPages());

        verify(studentDAO)
                .getAllStudentsByPage(1, 10, "name", "desc");

        verify(studentDAO).countStudents();
    }


    // 6. Return filtered, paginated students with metadata

    @Test
    void shouldReturnFilteredPaginatedStudents()
            throws StudentManagementException {

        List<Student> students = List.of(
                new Student(101, "Abhay", "abhay@gmail.com", 22, "CSE"),
                new Student(103, "Aman", "aman@gmail.com", 22, "CSE")
        );

        when(studentDAO.findStudentsByCourseAndAge(
                "CSE", 22, 0, 2, "name", "asc"))
                .thenReturn(students);

        when(studentDAO.countStudentsByCourseAndAge("CSE", 22))
                .thenReturn(5);

        StudentPageResponse response =
                studentService.getStudentsByCourseAndAgePage(
                        "CSE", 22, 0, 2, "name", "asc");

        assertEquals(students, response.getStudents());
        assertEquals(0, response.getPage());
        assertEquals(2, response.getSize());
        assertEquals(5, response.getTotalStudents());
        assertEquals(3, response.getTotalPages());

        verify(studentDAO).findStudentsByCourseAndAge(
                "CSE", 22, 0, 2, "name", "asc");

        verify(studentDAO).countStudentsByCourseAndAge("CSE", 22);
    }
}
