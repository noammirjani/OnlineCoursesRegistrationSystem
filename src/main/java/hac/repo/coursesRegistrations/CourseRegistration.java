package hac.repo.coursesRegistrations;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;

import java.io.Serializable;

@Entity
public class CourseRegistration implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    @NotEmpty(message = "Course Name is required")
    @Size(max = 50, message = "Course Name must be no more than 50 characters")
    private String courseName;

    @NotEmpty(message = "Course Name is required")
    //@Pattern(regexp="^[a-zA-Z0-9 ]+$", message="Course Name cannot contain special characters")
    @Size(max = 100, message = "Student Name must be no more than 100 characters")
    private String student;

    public CourseRegistration (){}

    public CourseRegistration (String courseName, String student){
        this.courseName = courseName;
        this.student = student;

    }

    public String getCourseName() {
        return courseName;
    }

    public String getStudent() {
        return student;
    }

    public void setStudent(String student) {
        this.student = student;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }
}
