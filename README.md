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

### Bidirectional Association
In the context of the database relations within our project, we maintain a bidirectional association between the Course and CourseRegistration entities. This association enables efficient access to related data from either end of the relationship.

The primary reason we opted for a bidirectional association is due to the nature of our data and its use within the application:

In many scenarios, when querying CourseRegistration records, we need to fetch corresponding Course data. The ManyToOne relationship in the CourseRegistration entity simplifies this task by automatically resolving the related Course data without the need for additional queries or manual joins.
In turn, the OneToMany relationship in the Course entity provides an easy way to fetch all CourseRegistration records associated with a specific Course. This relationship is crucial in use cases where we need to track all registrations for a particular course.
Moreover, Hibernate automatically manages the synchronization of these relationships. When we update one side of the relationship, Hibernate ensures that the other side is also updated. This synchronization saves us from manually updating both sides of the relationship, thus reducing the risk of data inconsistency.


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

### Tests
Please we recommend you to run your own test on our site. 
We would like you to test the following scenarios:
* Register to a course that is not full
* Register to a course that is full
* Edit a course capacity that is not full
* Edit a course capacity that is full
* Edit a course (without changing the capacity) -> with or without students registered to the course
  * in this case we that the edit will be saved, and all the registrations to this course will be saved as they was
  * we didnt want to remove all the registrations because we thought that it is not fair to the students that registered to the course before the edit, it doesnt suppose to affect them such little changes
* Delete a course that is not full
* Delete a course that is full
* Delete all registrations
* Delete all search registration 
* Delete a specific registration
* Add a course with a name that already exists (or course code)
* Add a course with a name that does not exist (or course code)
* Add or edit a course with a capital letters of existing course name (or course code)
* there are several of validation on the forms inputs that we would like you to test
  *   course name and course code must be unique
  *   year 2000-2023
  *   semester 1-3
  *   capacity 1-100
  *   professor name letters only (and space)
  *   overview 255 characters max
  *   course code, year, semester, capacity positive values only

** this is not a full list of tests, please feel free to add more tests, we will pass them :)

## Security

The system uses Spring Security for authentication and authorization, ensuring that only authorized administrators and students can perform certain operations.