package hac.repo.coursesRegistrations;

import jakarta.persistence.*;

import jakarta.validation.constraints.*;
import jakarta.persistence.UniqueConstraint;
import java.io.Serializable;

@Entity
@Table(uniqueConstraints = { @UniqueConstraint(name = "UniqueCourseNameAndStudent", columnNames = { "courseName", "student" }) })
public class CourseRegistration implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    @NotEmpty(message = "Course Name is required")
    @Size(max = 50, message = "Course Name must be no more than 50 characters")
    private String courseName;

    @NotEmpty(message = "Course Name is required")
    @Size(max = 100, message = "Student Name must be no more than 100 characters")
    private String student;

    public CourseRegistration (String courseName, String student){
        this.courseName = courseName;
        this.student = student;

    }

    public CourseRegistration (){}
    public Long getId() {return id;}


    public String getCourseName() {
        return courseName;
    }

    public String getStudent() {
        return student;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setStudent(String student) {
        this.student = student;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

}
