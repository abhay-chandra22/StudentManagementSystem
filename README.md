# Student Management System

A RESTful backend application built with **Java, Spring Boot, MySQL, JDBC, and Maven** for managing student records.

The project started as a console-based Java application and was progressively converted into a layered Spring Boot REST API. It now includes CRUD operations, validation, exception handling, searching, pagination, sorting, and Swagger/OpenAPI documentation.

## Features

- Create a student
- Get all students
- Get a student by ID
- Update a student
- Delete a student
- Search students by course and age
- Pagination with page number and page size
- Sorting by `id`, `name`, `email`, `age`, or `course`
- Ascending and descending sorting
- Combined filtering + pagination + sorting
- Request validation
- Custom exception handling
- Global exception handling with `@ControllerAdvice`
- Consistent JSON error responses
- Swagger / OpenAPI documentation

## Tech Stack

| Technology | Usage |
|---|---|
| Java 23 | Programming language |
| Spring Boot 3.5.5 | Backend framework |
| Spring Web | REST APIs |
| Maven | Dependency management and build |
| MySQL | Relational database |
| JDBC | Database connectivity |
| PreparedStatement | Parameterized SQL queries |
| Springdoc OpenAPI | Swagger / API documentation |
| Postman | API testing |

## Project Architecture

```text
Client (Postman / Swagger UI)
             |
             v
       Controller Layer
             |
             v
        Service Layer
             |
             v
          DAO Layer
             |
             v
           MySQL
```

### Package Structure

```text
com.studentmanagement
├── controller
│   └── StudentController.java
├── dao
│   └── StudentDAO.java
├── exception
│   ├── ErrorResponse.java
│   ├── GlobalExceptionHandler.java
│   └── StudentManagementException.java
├── model
│   ├── Student.java
│   └── StudentPageResponse.java
├── service
│   ├── OperationStatus.java
│   ├── PaginationValidationResult.java
│   ├── PaginationValidator.java
│   ├── StudentOperationResult.java
│   ├── StudentService.java
│   └── ValidationResult.java
└── StudentManagementSystemApplication.java
```

## Responsibilities of Each Layer

### Controller

Handles HTTP requests and responses.

Examples:

```text
GET
POST
PUT
DELETE
```

It also performs request-level validation and maps application results to HTTP status codes.

### Service

Contains business logic such as:

- Student validation
- Duplicate ID checking
- Pagination calculation
- Combining filtering, sorting, and pagination

### DAO

Handles database operations using JDBC:

- `Connection`
- `PreparedStatement`
- `ResultSet`
- SQL queries

### Exception Layer

Provides:

- Custom application exception
- Global exception handling
- Consistent JSON error responses

## Database Setup

Create the database:

```sql
CREATE DATABASE student_management;
```

Select it:

```sql
USE student_management;
```

Create the students table:

```sql
CREATE TABLE students(
    id INT PRIMARY KEY,
    name VARCHAR(100),
    email VARCHAR(100),
    age INT,
    course VARCHAR(50)
);
```

Example data:

```sql
INSERT INTO students (id, name, email, age, course)
VALUES
(101, 'Abhay Chandra', 'abhay@gmail.com', 22, 'CSE'),
(102, 'Rahul Singh', 'rahul123@gmail.com', 23, 'CSE'),
(103, 'Rahul', 'rahul@gmail.com', 22, 'CSE'),
(104, 'Ayush', 'ayush@gmail.com', 20, 'CSE'),
(105, 'Arya', 'arya@gmail.com', 22, 'CSE');
```

## Configuration

The application currently connects to:

```text
jdbc:mysql://localhost:3306/student_management
```

The database username is:

```text
root
```

The MySQL password is read from the environment variable:

```text
DB_PASSWORD
```

### IntelliJ IDEA

Add `DB_PASSWORD` to the Run Configuration environment variables before starting the application.

Do **not** commit the actual database password to GitHub.

## Running the Application

### 1. Clone the repository

```bash
git clone https://github.com/abhay-chandra22/StudentManagementSystem.git
cd StudentManagementSystem
```


### 2. Run the Spring Boot application

Run:

```text
StudentManagementSystemApplication
```

The application runs on:

```text
http://localhost:8080
```

## REST API Endpoints

### Get all students

```http
GET /students
```

Example:

```text
http://localhost:8080/students
```

Response:

```text
200 OK
```

---

### Get student by ID

```http
GET /students/{id}
```

Example:

```text
http://localhost:8080/students/103
```

Responses:

```text
200 OK
404 Not Found
```

---

### Add a student

```http
POST /students
```

Example request body:

```json
{
    "id": 109,
    "name": "Anuj",
    "email": "anuj23@gmail.com",
    "age": 22,
    "course": "CSE"
}
```

Responses:

```text
201 Created
400 Bad Request
409 Conflict
500 Internal Server Error
```

---

### Update a student

The student ID is taken from the URL.

```http
PUT /students/{id}
```

Example:

```http
PUT /students/109
```

Request body:

```json
{
    "name": "Anuj Kumar",
    "email": "anujkumar@gmail.com",
    "age": 23,
    "course": "CSE"
}
```

Responses:

```text
200 OK
400 Bad Request
404 Not Found
500 Internal Server Error
```

---

### Delete a student

```http
DELETE /students/{id}
```

Example:

```text
http://localhost:8080/students/109
```

Responses:

```text
204 No Content
404 Not Found
500 Internal Server Error
```

---

### Search by course and age

```http
GET /students/search?course=CSE&age=22
```

Example:

```text
http://localhost:8080/students/search?course=CSE&age=22
```

Returns students matching both conditions.

---

## Pagination

The pagination endpoint supports pagination and sorting.

```http
GET /students/page?page=0&size=3
```

Default sorting:

```text
sortBy=id
sortDir=asc
```

Example with sorting:

```http
GET /students/page?page=0&size=3&sortBy=name&sortDir=asc
```

Supported `sortBy` values:

```text
id
name
email
age
course
```

Supported `sortDir` values:

```text
asc
desc
```

Example response:

```json
{
    "students": [
        {
            "id": 101,
            "name": "Abhay Chandra",
            "email": "abhay@gmail.com",
            "age": 22,
            "course": "CSE"
        }
    ],
    "page": 0,
    "size": 3,
    "totalStudents": 8,
    "totalPages": 3
}
```

Invalid pagination or sorting parameters return:

```text
400 Bad Request
```

---

## Search + Pagination + Sorting

The combined endpoint supports filtering, pagination, and sorting in one request:

```http
GET /students/search/page?course=CSE&age=22&page=0&size=3&sortBy=name&sortDir=asc
```

This means:

```text
course = CSE
age = 22
page = 0
size = 3
sortBy = name
sortDir = asc
```

The response contains the matching students and pagination metadata.

## Error Handling

The application uses a global exception handler with `@ControllerAdvice`.

Examples of handled errors:

```text
StudentManagementException
→ 500 Internal Server Error

HttpMessageNotReadableException
→ 400 Bad Request

MethodArgumentTypeMismatchException
→ 400 Bad Request
```

Example error response:

```json
{
    "status": 400,
    "message": "Invalid value for parameter: age"
}
```

This keeps error responses consistent across the API.

## Swagger / OpenAPI

Interactive API documentation:

```text
http://localhost:8080/swagger-ui/index.html
```

OpenAPI specification:

```text
http://localhost:8080/v3/api-docs
```

Swagger documents:

- API information
- HTTP methods
- Request parameters
- Request bodies
- Response codes
- Response schemas
- Pagination and sorting parameters

## API Testing

The API has been tested using **Postman**.

Swagger UI can also be used to execute requests directly from the browser.

Typical test cases include:

```text
Valid CRUD requests
Invalid student data
Duplicate student ID
Student not found
Invalid JSON request body
Invalid query parameter types
Invalid page number
Invalid page size
Invalid sort field
Invalid sort direction
Pagination across multiple pages
Filtering with pagination and sorting
```

## Git Workflow

The project is maintained on the `master` branch.

Meaningful milestones are committed and pushed to GitHub throughout development so the repository history reflects the project's progression.

## Future Improvements

Planned improvements for making the project more production-ready include:

- Externalize database configuration into Spring configuration
- Introduce DTOs for request/response separation
- Add automated tests with JUnit and Spring testing
- Improve validation with Jakarta Bean Validation
- Improve logging and monitoring
- Dockerize the application
- Add Spring Security and JWT authentication
- Explore microservices architecture
- Deploy the application to AWS

## Author

**Abhay Chandra**

B.Tech Computer Science and Engineering Student

GitHub:

```text
https://github.com/abhay-chandra22/StudentManagementSystem
```
