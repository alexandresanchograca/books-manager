# Books Manager API

A Spring Boot application that provides RESTful web services for managing books, book editions, and book defects in a MySQL database.

## Project Description

This application exposes web services that allow you to:
- **Manage Books**: Create, read, update, and delete books with their edition information
- **Manage Book Editions**: Handle different editions of books with ISBN, title, author, and edition number
- **Manage Book Defects**: Track quality issues and defects for specific book editions

The API is built with Spring Boot 3.5.3, uses JPA/Hibernate for data persistence, and includes comprehensive validation and error handling.

## Prerequisites

- Java 21
- Maven 3.6+
- MySQL 8.0+ (or use the provided Docker Compose setup)

## Quick Start

### 1. Database Setup

The project includes a `compose.yml` file in the root directory that sets up a MySQL database container:

```bash
docker-compose up -d
```

This will start a MySQL database on `localhost:3306` with the credentials configured in `application.properties`.

### 2. Build and Run

#### Build the application:
```bash
mvn clean package
```

#### Run tests:
```bash
mvn test
```

#### Run the application:
```bash
mvn spring-boot:run
```

#### Run the packaged JAR:
```bash
java -jar target/books-manager-0.0.1-SNAPSHOT.jar
```

## API Documentation

The application includes **Swagger/OpenAPI documentation** that is automatically generated from the external `openapi.yaml` specification file.

### Access Swagger UI:
- **URL**: http://localhost:8080/swagger-ui.html
- **API Docs**: http://localhost:8080/api-docs

The Swagger UI provides:
- Interactive API documentation
- Try-it-out functionality for testing endpoints
- Request/response schemas
- Error response documentation

## API Endpoints

### Book Management (`/api/v1/books`)
- `GET /` - Get all books
- `POST /` - Create a new book
- `PATCH /` - Update book information
- `GET /{batchNumber}/{isbn}` - Get book by batch number and ISBN
- `DELETE /{batchNumber}/{isbn}` - Delete book by batch number and ISBN

### Book Edition Management (`/api/v1/book-editions`)
- `GET /` - Get all book editions
- `POST /` - Create a new book edition
- `PATCH /` - Update book edition information
- `GET /{isbn}` - Get book edition by ISBN
- `DELETE /{isbn}` - Delete book edition by ISBN

### Book Defect Management (`/api/v1/book-defects`)
- `GET /` - Get all book defects
- `POST /` - Create a new book defect

## Configuration

The application configuration is in `src/main/resources/application.properties`:
- Database connection settings
- JPA/Hibernate configuration
- Swagger UI settings

## Project Structure

```
src/
├── main/
│   ├── java/com/alexandre/books_manager/
│   │   ├── controller/     # REST controllers
│   │   ├── dto/           # Data Transfer Objects
│   │   ├── model/         # JPA entities
│   │   ├── repository/    # Data access layer
│   │   ├── service/       # Business logic
│   │   └── converter/     # DTO-Entity converters
│   └── resources/
│       ├── openapi.yaml   # API specification
│       └── application.properties
└── test/                  # Unit and integration tests
```

## Technologies Used

- **Spring Boot 3.5.3** - Application framework
- **Spring Data JPA** - Data persistence
- **MySQL** - Database
- **SpringDoc OpenAPI** - API documentation
- **Maven** - Build tool
- **Docker Compose** - Database containerization
