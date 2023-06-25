# OCR EDU - Online Course Registration System 

## Authors
* Name: Ariel Amon  Email: arielam@edu.hac.ac.il 208244038
* Name:  Noam Mirjani  Email: noammir@edu.hac.ac.il 315216515

## Description

This project is an Online Course Registration System developed using Java and Spring Boot. It's designed to facilitate course creation, management and registration for administrators and students.

### General information

The system is designed to be used by two types of users: administrators and students. Administrators can create, update and delete courses, as well as view all courses and all students. Students can view all courses and register for courses. The system is designed to be used by multiple administrators and students at the same time.

### Functionality

The system provides the following functionality:
* Administrators can create, update and delete courses
* Administrators can view all courses
* Administrators can view all students registrations
* Administrators can view all registration by filters of course name, student name, course status and spastic course+student
* Students can view all courses
* Students can register for courses
* Students can view their registered courses
* Students can cancel their registration for courses
* Administrators and students can log in and log out


## Installation

(database creation, etc)
1. clone the project from git
```
git clone
```
2. Create a database in PHPMyAdmin ex5
3. Run the project:
4. Go to http://localhost:8080/ in your browser
5. Login and enjoy!


In order to initialize the project make sure to:

When you open the project, if intelliJ propose to "Load Maven Project" do it. You can later reload maven with the "M" icon on the right of the screen, or by right clicking on the pom.xml file and selecting "Maven -> Reload project".
You see red lines in the code? Go to File -> Project Structure -> Project Settings -> Project -> SDK -> and choose your Java SDK
Still see red stuff? Open the same dialog and click on "Fix" if you see some
Edit your configuration "ex5" at the top right. Make sure the "Main class" is set to "hac.DemoApplication" and that Java is set.
* for Mac users password = root and username = root
* for Windows users password =  and username = root

## Useful information

* Administrator: username: admin@hac.com, password: 12345
* Student: username: noam@hac.com, password: 1234
* Student: username: ariel@hac.com, password: 1234
* Student: username: tomer@hac.com, password: 1234
* Student: username: yudi@hac.com, password: 1234

## Technologies Used

* Java
* Spring Boot
* Spring Security
* Spring Data JPA
* Thymeleaf
* Bootstrap
* MySQL
* Maven

## Database


The system uses a relational database with two main entities, Course and CourseRegistration. There is a many-to-one relationship between these two entities, where many CourseRegistration entities can be associated with one Course. The CourseRepository and CourseRegistrationRepository provide methods to interact with the database, with a mix of standard JPA methods and custom queries.

### Course Table
The course table holds data about different courses available for registration. It has the following columns:

* id: The primary key of the course.
* name: The name of the course (unique).
* code: The code of the course instructor.
* capacity: The maximum capacity of students allowed in the course.
* year: The start date of the course.
* semester: The end date of the course.
* professor: The professor of the course.
The name column should hold a unique value for each course. This means that no two courses can have the same name.

### Registration Table
The registration table holds data about the registrations made by students for various courses. It has the following columns:

* id: The primary key of the registration.
* student: The email of the student -> caused by usage of spring security and the fact that we do not have a student table.
* course_id: The foreign key referencing the id column in the course table.
The course_id column establishes a many-to-one relationship between registrations and courses. Multiple registrations can be associated with the same course.

### Database Schema - important data
The uniqueness constraint in the registration table is applied to the combination of course_id and student_name. This means that each student can only be registered once for a specific course.

## Security

The system uses Spring Security for authentication and authorization, ensuring that only authorized administrators and students can perform certain operations.