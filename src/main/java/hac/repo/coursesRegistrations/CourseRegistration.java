package hac.repo.coursesRegistrations;

import hac.repo.course.Course;
import jakarta.persistence.*;

import jakarta.validation.constraints.*;
import jakarta.persistence.UniqueConstraint;
import java.io.Serializable;
import java.util.List;

/**
 * CourseRegistration entity
 */
@Entity
@Table(uniqueConstraints = { @UniqueConstraint(name = "UniqueCourseNameAndStudent", columnNames = { "course_id", "student" }) })
public class CourseRegistration implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    @ManyToOne
    @JoinColumn(name = "course_id", nullable = false)
    @NotNull(message = "Course Name cannot be NULL")
    private Course course;


    @NotEmpty(message = "Student Name is required")
    @NotNull(message = "Student Name cannot be NULL")
    @Size(max = 100, message = "Student Name must be no more than 100 characters")
    private String student;

    public CourseRegistration (Course course, String student){
        this.course = course;
        this.student = student;
    }

    public CourseRegistration (){}

    public Long getId() {return id;}

    public Course getCourse() {
        return course;
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

    public void setCourse(Course course) {
        this.course = course;
    }

    public String toString() {
        return "CourseRegistration{" + "id=" + id + ", course=" + course + ", student='" + student + '\'' + '}';
    }
}
