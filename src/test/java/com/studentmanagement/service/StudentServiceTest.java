
package com.studentmanagement.service;

import com.studentmanagement.dao.StudentDAO;
import com.studentmanagement.exception.StudentManagementException;
import com.studentmanagement.model.Student;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.never;

@ExtendWith(MockitoExtension.class)
class StudentServiceTest {

    @Mock
    private StudentDAO studentDAO;

    @InjectMocks
    private StudentService studentService;


    // 1. Successfully add a student

    @Test
    void shouldAddStudentSuccessfully() throws StudentManagementException {

        Student student = new Student(
                201, "Aman", "aman@gmail.com", 21, "CSE"
        );

        when(studentDAO.findStudentById(201)).thenReturn(null);
        when(studentDAO.addStudent(student)).thenReturn(1);

        StudentOperationResult result =
                studentService.addStudent(student);

        assertEquals(OperationStatus.SUCCESS, result.getStatus());

        verify(studentDAO).addStudent(student);
    }


    // 2. Reject a duplicate student ID

    @Test
    void shouldRejectDuplicateStudentId() throws StudentManagementException {

        Student student = new Student(
                201, "Aman", "aman@gmail.com", 21, "CSE"
        );

        Student existingStudent = new Student(
                201, "Rahul", "rahul@gmail.com", 22, "ECE"
        );

        when(studentDAO.findStudentById(201))
                .thenReturn(existingStudent);

        StudentOperationResult result =
                studentService.addStudent(student);

        assertEquals(
                OperationStatus.DUPLICATE_ID,
                result.getStatus()
        );

        verify(studentDAO, never()).addStudent(student);
    }


    // 3. Reject a blank student name

    @Test
    void shouldRejectStudentWithInvalidName() throws StudentManagementException {

        Student student = new Student(
                202, "", "aman@gmail.com", 21, "CSE"
        );

        StudentOperationResult result =
                studentService.addStudent(student);

        assertEquals(
                OperationStatus.INVALID_DATA,
                result.getStatus()
        );

        verify(studentDAO, never()).findStudentById(202);
        verify(studentDAO, never()).addStudent(student);
    }


    // 4. Reject an invalid email

    @Test
    void shouldRejectStudentWithInvalidEmail() throws StudentManagementException {

        Student student = new Student(
                203, "Aman", "invalid-email", 21, "CSE"
        );

        StudentOperationResult result =
                studentService.addStudent(student);

        assertEquals(
                OperationStatus.INVALID_DATA,
                result.getStatus()
        );

        verify(studentDAO, never()).findStudentById(203);
        verify(studentDAO, never()).addStudent(student);
    }


    // 5. Reject an invalid age

    @Test
    void shouldRejectStudentWithInvalidAge() throws StudentManagementException {

        Student student = new Student(
                204, "Aman", "aman@gmail.com", 0, "CSE"
        );

        StudentOperationResult result =
                studentService.addStudent(student);

        assertEquals(
                OperationStatus.INVALID_DATA,
                result.getStatus()
        );

        verify(studentDAO, never()).findStudentById(204);
        verify(studentDAO, never()).addStudent(student);
    }


    // 6. Reject a blank course

    @Test
    void shouldRejectStudentWithInvalidCourse() throws StudentManagementException {

        Student student = new Student(
                205, "Aman", "aman@gmail.com", 21, ""
        );

        StudentOperationResult result =
                studentService.addStudent(student);

        assertEquals(
                OperationStatus.INVALID_DATA,
                result.getStatus()
        );

        verify(studentDAO, never()).findStudentById(205);
        verify(studentDAO, never()).addStudent(student);
    }


    // 7. Successfully update a student

    @Test
    void shouldUpdateStudentSuccessfully() throws StudentManagementException {

        Student student = new Student(
                206, "Aman Updated", "aman.updated@gmail.com", 22, "ECE"
        );

        when(studentDAO.updateStudent(student)).thenReturn(1);

        StudentOperationResult result =
                studentService.updateStudent(student);

        assertEquals(OperationStatus.SUCCESS, result.getStatus());

        verify(studentDAO).updateStudent(student);
    }


    // 8. Handle updating a nonexistent student

    @Test
    void shouldReturnNotFoundWhenUpdatingStudent() throws StudentManagementException {

        Student student = new Student(
                207, "Aman", "aman@gmail.com", 21, "CSE"
        );

        when(studentDAO.updateStudent(student)).thenReturn(0);

        StudentOperationResult result =
                studentService.updateStudent(student);

        assertEquals(
                OperationStatus.STUDENT_NOT_FOUND,
                result.getStatus()
        );

        verify(studentDAO).updateStudent(student);
    }


    // 9. Successfully delete a student

    @Test
    void shouldDeleteStudentSuccessfully() throws StudentManagementException {

        when(studentDAO.deleteStudent(208)).thenReturn(1);

        StudentOperationResult result =
                studentService.deleteStudent(208);

        assertEquals(OperationStatus.SUCCESS, result.getStatus());

        verify(studentDAO).deleteStudent(208);
    }


    // 10. Handle deleting a nonexistent student

    @Test
    void shouldReturnNotFoundWhenDeletingStudent() throws StudentManagementException {

        when(studentDAO.deleteStudent(209)).thenReturn(0);

        StudentOperationResult result =
                studentService.deleteStudent(209);

        assertEquals(
                OperationStatus.STUDENT_NOT_FOUND,
                result.getStatus()
        );

        verify(studentDAO).deleteStudent(209);
    }
}
