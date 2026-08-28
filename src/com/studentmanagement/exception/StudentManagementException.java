package com.studentmanagement.exception;

public class StudentManagementException extends Exception{
    public StudentManagementException(String message){
        super(message); // SUPER MEANS - > "Pass this message to the parent Exception class."
    }
    public StudentManagementException(String message , Throwable cause){
        super(message,cause);
    }
}
