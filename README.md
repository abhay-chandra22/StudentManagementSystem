# Student Management System

A RESTful backend application built with **Java, Spring Boot, and MySQL** to manage student records. The project demonstrates layered backend architecture, JDBC database access, input validation, consistent HTTP responses, pagination and sorting, API documentation, and automated unit testing with JUnit 5 and Mockito.

> **Project status:** Core REST API and service-layer unit tests are implemented. AI integration and Docker support are not implemented yet.

## Features

- **Student CRUD:** create, retrieve, update, and delete student records.
- **Search:** find students by course and age.
- **Pagination:** retrieve records page by page with total-result metadata.
- **Sorting:** sort by supported student fields in ascending or descending order.
- **Input validation:** checks student fields and pagination/sorting parameters.
- **Centralized error handling:** structured error responses for malformed requests and application/database errors.
- **Safe SQL access:** parameterized `PreparedStatement` queries; sortable database columns are selected from an allowlist.
- **API documentation:** interactive Swagger UI and OpenAPI specification.
- **Automated tests:** JUnit 5 tests with Mockito mocks so service-layer tests do not require a live MySQL connection.
- **Externalized database configuration:** database password is read from the `DB_PASSWORD` environment variable rather than stored in source code.

## Tech Stack

| Technology | Purpose |
|---|---|
| Java 23 | Programming language |
| Spring Boot 3.5.5 | Application framework |
| Spring Web | REST endpoints and embedded web server |
| Spring JDBC / JDBC | Database connection management and SQL access |
| MySQL | Relational database |
| MySQL Connector/J 9.4.0 | MySQL JDBC driver |
| springdoc-openapi 2.8.17 | Swagger UI and OpenAPI documentation |
| JUnit 5 | Unit testing |
| Mockito | Mocking dependencies in unit tests |
| Maven | Dependency and build management |
| Postman | Manual REST API testing |

## Architecture

The application uses a layered structure to separate HTTP handling, business logic, and database access.

```text
src
├── main
│   ├── java
│   │   └── com.studentmanagement
│   │       ├── controller
│   │       │   └── StudentController.java
│   │       ├── dao
│   │       │   └── StudentDAO.java
│   │       ├── exception
│   │       │   ├── ErrorResponse.java
│   │       │   ├── GlobalExceptionHandler.java
│   │       │   └── StudentManagementException.java
│   │       ├── model
│   │       │   ├── Student.java
│   │       │   └── StudentPageResponse.java
│   │       ├── service
│   │       │   ├── OperationStatus.java
│   │       │   ├── PaginationValidationResult.java
│   │       │   ├── PaginationValidator.java
│   │       │   ├── StudentOperationResult.java
│   │       │   ├── StudentService.java
│   │       │   └── ValidationResult.java
│   │       └── StudentManagementSystemApplication.java
│   └── resources
│       └── application.properties
└── test
    └── java
        └── com.studentmanagement.service
            ├── PaginationValidatorTest.java
            ├── StudentServiceTest.java
            └── StudentServiceQueryTest.java
```

### Layer responsibilities

- **Controller:** maps HTTP requests to service calls and returns appropriate HTTP status codes.
- **Service:** applies student validation and business rules, and builds paginated responses.
- **DAO:** runs parameterized SQL queries using a Spring-managed `DataSource`.
- **Model / response classes:** represent student records, pagination metadata, operation outcomes, and errors.
- **Global exception handler:** converts supported exceptions into structured HTTP error responses.
- **Tests:** check pagination validation, service behavior, search, and pagination without requiring a real database for these unit tests.

## Prerequisites

Install and configure:

- JDK 23
- Maven (or IntelliJ IDEA with Maven support)
- MySQL Server and MySQL Workbench (optional, for database administration)
- Postman (optional, for API testing)

## Setup and Run

### 1. Clone the repository

```bash
git clone https://github.com/abhay-chandra22/StudentManagementSystem.git
cd StudentManagementSystem
```

### 2. Create the database and table

Run the following SQL in MySQL Workbench:

```sql
CREATE DATABASE student_management;
USE student_management;

CREATE TABLE students (
    id INT PRIMARY KEY,
    name VARCHAR(100),
    email VARCHAR(100),
    age INT,
    course VARCHAR(50)
);
```

The `id` column is the primary key and must be unique.

### 3. Configure the database password

The file `src/main/resources/application.properties` should contain:

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/student_management
spring.datasource.username=root
spring.datasource.password=${DB_PASSWORD}
```

Set the `DB_PASSWORD` environment variable to the password for your local MySQL `root` account. **Do not replace `${DB_PASSWORD}` with a real password in the committed file.**

For a temporary PowerShell session, you can set it before starting the application:

```powershell
$env:DB_PASSWORD="your_mysql_password"
```

Alternatively, set `DB_PASSWORD` in the IntelliJ Run Configuration for `StudentManagementSystemApplication`.

### 4. Run the application

From the project directory, with `DB_PASSWORD` set in the same terminal:

```bash
mvn spring-boot:run
```

Or run `StudentManagementSystemApplication.java` from IntelliJ IDEA. The API is available at `http://localhost:8080` by default.

### 5. Run automated tests

```bash
mvn test
```

The current test suite contains 21 unit tests across three classes:

- `PaginationValidatorTest` — pagination and sorting parameter validation.
- `StudentServiceTest` — create/update/delete outcomes and student validation.
- `StudentServiceQueryTest` — student lookup, search, and paginated response metadata.

These tests use Mockito to simulate DAO behavior and do not require a live database for the tested service logic.

## REST API Reference

Base URL: `http://localhost:8080`

| Method | Endpoint | Purpose | Typical successful response |
|---|---|---|---|
| `GET` | `/students` | Retrieve all students | `200 OK` |
| `GET` | `/students/{id}` | Retrieve a student by ID | `200 OK` |
| `POST` | `/students` | Create a student | `201 Created` |
| `PUT` | `/students/{id}` | Update an existing student | `200 OK` |
| `DELETE` | `/students/{id}` | Delete a student | `204 No Content` |
| `GET` | `/students/search?course=CSE&age=22` | Search by course and age | `200 OK` |
| `GET` | `/students/page?page=0&size=3` | Retrieve a page of students with optional sorting | `200 OK` |
| `GET` | `/students/search/page?course=CSE&age=22&page=0&size=3&sortBy=name&sortDir=asc` | Search with pagination and sorting | `200 OK` |

### Pagination and sorting parameters

For `/students/page` and `/students/search/page`:

| Parameter | Required? | Description |
|---|---|---|
| `page` | Yes | Zero-based page index; must be `0` or greater |
| `size` | Yes | Number of records per page; must be greater than `0` |
| `sortBy` | No | Field to sort by: `id`, `name`, `email`, `age`, or `course`; defaults to `id` |
| `sortDir` | No | `asc` or `desc`; defaults to `asc` |

Pagination responses include the returned `students` list and metadata such as `page`, `size`, `totalStudents`, and `totalPages`.

### Example request body

Use this JSON body with `POST /students`:

```json
{
  "id": 201,
  "name": "Aman Sharma",
  "email": "aman@example.com",
  "age": 21,
  "course": "CSE"
}
```

For `PUT /students/{id}`, provide the student's fields as JSON; the ID in the URL identifies the student being updated.

### Common response codes

- `200 OK` — request completed successfully.
- `201 Created` — student created.
- `204 No Content` — student deleted successfully; response body is empty.
- `400 Bad Request` — invalid student data, malformed request body, or invalid query/pagination parameters.
- `404 Not Found` — requested student does not exist.
- `409 Conflict` — student ID already exists during creation.
- `500 Internal Server Error` — an application/database operation failed.

## API Documentation

With the application running, open:

- **Swagger UI:** [http://localhost:8080/swagger-ui/index.html](http://localhost:8080/swagger-ui/index.html)
- **OpenAPI JSON:** [http://localhost:8080/v3/api-docs](http://localhost:8080/v3/api-docs)

Swagger UI lets you inspect the documented endpoints and try requests against your local application.

## Testing Approach

- **Unit tests:** JUnit 5 and Mockito test validation and service logic using controlled DAO responses.
- **Manual API testing:** Postman has been used to check CRUD endpoints, search, pagination, sorting, invalid input, and HTTP status codes.
- **Database-backed checks:** running the application locally verifies the configured MySQL connection and actual SQL behavior.

## Security and Configuration Notes

- Never commit actual passwords, API keys, or other secrets.
- The database password is supplied through `DB_PASSWORD`.
- DAO queries use prepared statements for values, while sorting identifiers are chosen from an allowlist.
- This project currently focuses on backend CRUD and API behavior; authentication/authorization is not yet implemented.

## Planned Improvements

Possible future enhancements—not included in the current implementation—include:

- AI-powered natural-language student search using validated structured filters.
- Controller/API tests using Spring MockMvc.
- Authentication and authorization with Spring Security.
- Docker support and a repeatable deployment setup.
- Continuous integration to run tests automatically on pushes and pull requests.

## Author

**Abhay Chandra**  
GitHub: [@abhay-chandra22](https://github.com/abhay-chandra22)

## License

No license has been added yet. 
