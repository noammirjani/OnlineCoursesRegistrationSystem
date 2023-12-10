# OCR EDU - Online Course Registration System

## Table of Contents
- [Introduction](#introduction)
- [Image Preview](#Image-Preview)
- [Overview](#Overview)
- [Features](#features)
- [Installation](#installation)
  - [MAMP Setup](#mamp-setup)
  - [Database Configuration](#database-configuration)
  - [Project Configuration](#project-configuration)
  - [Accessing the Application](#accessing-the-application)
- [Usage](#usage)
- [Technologies Used](#technologies-used)
- [Database and Relations](#database-and-relations)
- [Tests](#tests)
- [Security](#security)
- [Contributing](#contributing)

## Introduction
OCR EDU is an Online Course Registration System developed using Java and Spring Boot. It's designed to facilitate course creation, management, and registration for administrators and students.

## Image Preview

| Home Screen | Courses Overview | User Home Screen | User Registered Courses |
|-------------|------------------|------------------|-------------------------|
| <img src="src/main/resources/static/images/homescreen.png" alt="Home Screen" width="200" height="150"/> | <img src="src/main/resources/static/images/courses overview.png" alt="Courses Overview" width="200" height="150"/> | <img src="src/main/resources/static/images/user home screen.png" alt="User Home Screen" width="200" height="150"/> | <img src="src/main/resources/static/images/User Registered courses.png" alt="User Registered Courses" width="200" height="150"/> |

| User Schedule Management | Admin Home Screen | Admin Manage Courses | Admin Manage Registration |
|--------------------------|-------------------|----------------------|---------------------------|
| <img src="src/main/resources/static/images/User schedule manage.png" alt="User Schedule Management" width="200" height="150"/> | <img src="src/main/resources/static/images/admin home screen.png" alt="Admin Home Screen" width="200" height="150"/> | <img src="src/main/resources/static/images/admin manage courses.png" alt="Admin Manage Courses" width="200" height="150"/> | <img src="src/main/resources/static/images/admin manage registration.png" alt="Admin Manage Registration" width="200" height="150"/> |


## Overview
The OCR EDU system provides a comprehensive platform for educational institutions to streamline the process of managing courses and student registrations. With a user-friendly interface and robust functionalities, it caters to the distinct needs of both administrators and students.

### Administrator Dashboard
Administrators have access to a feature-rich dashboard enabling them to efficiently oversee and manage courses and student registrations. They can perform tasks such as creating new courses, updating existing ones, managing capacities, viewing all courses and associated registrations, and employing various filters to streamline data retrieval.

### Student Interface
Students benefit from a simplified interface that allows them to explore available courses, register for their preferred ones, view their registered courses, and conveniently cancel registrations as needed. The system ensures a seamless experience for students navigating course selections and registrations.

## Features
- **Administrators:**
  - Create, update, and delete courses
  - View all courses and student registrations
  - Filter registrations by course name, student name, course status, and specific course-student pairs

- **Students:**
  - View available courses
  - Register for courses
  - View registered courses
  - Cancel course registrations

- **Authentication:**
  - Login and logout for administrators and students

## Installation

Before running the OCR EDU application, ensure the following configurations:

### MAMP Setup
1. **Create User in MAMP:**
  - Open MAMP and navigate to the MySQL tab.
  - Create a new user with appropriate privileges for the project.

2. **Start the MAMP Application:**
  - Launch the MAMP application.

### Database Configuration
3. **Configure `application.properties`:**
  - Open the file `src/main/resources/application.properties`.
  - For Mac users:
    ```properties
    spring.datasource.username=root
    spring.datasource.password=root
    ```
  - For Windows users:
    ```properties
    spring.datasource.username=root
    spring.datasource.password=
    ```
   (Leave the password blank)

4. **Create Database "ex5":**
  - Open PHPMyAdmin in the MySQL option.
  - Create a new database named 'ex5'.

### Project Configuration
5. **Set SDK for the Project:**
  - Open the project in IntelliJ IDEA.
  - Go to File -> Project Structure -> Project Settings -> Project.
  - Set the SDK to the appropriate Java SDK version.

6. **Run the Project:**
  - Navigate to `src/main/java/hac/Ex5TemplateApplication.java`.
  - Run the application using the running tool in the IDE (e.g., the green 'Run' button).

### Accessing the Application
7. **Access the Application:**
  - Go to `http://localhost:8080/` in your web browser.
  - Use the provided credentials for access.

### Credentials
- **Administrator:**
  - Username: admin@hac.com
  - Password: 12345
- **Students:**
  - Multiple student usernames and passwords provided:
    - username: noam@hac.com, password: 1234
    - username: ariel@hac.com, password: 1234
    - username: tomer@hac.com, password: 1234
    - username: yudi@hac.com, password: 1234


## Usage
### Administrators
- **Course Management:**
  - Create, update, and delete courses
  - View all courses and student registrations
  - Filter registrations

### Students
- **Course Enrollment:**
  - View available courses
  - Register for courses
  - View registered courses
  - Cancel course registrations

## Technologies Used
- Java
- Spring Boot
- Spring Security
- Spring Data JPA
- Thymeleaf
- Bootstrap
- MySQL
- Maven

## Database and Relations
### Bidirectional Association
We maintain bidirectional associations between `Course` and `CourseRegistration` entities for efficient data access and management.

### Schema - Important Data
- Uniqueness constraint on course registrations: `course_id` and `student_name`.

## Tests
Run tests for various scenarios including registration, editing, deletion, and validations.

## Security
Utilizes Spring Security for authentication and authorization to ensure restricted access for authorized users.

## Contributing
For contribution guidelines, refer to [CONTRIBUTING.md](CONTRIBUTING.md). Contributions, issues, and feature requests are welcome.
