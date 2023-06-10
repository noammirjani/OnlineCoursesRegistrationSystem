package hac.repo;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import org.springframework.data.relational.core.sql.In;

import java.io.Serializable;

/**
 * a purchase is a record of a user buying a product. You should not need to edit this file
 * but if you feel like you need to, please get in touch with the teacher.
 */
@Entity
public class Course implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotEmpty(message = "Course Code is mandatory")
    @PositiveOrZero(message = "Payment must be positive or zero")
    private Integer courseCode;

    @NotEmpty(message = "Course name is mandatory")
    private String courseName;

    @NotEmpty(message = "Semester is mandatory")
    private String semester;

    @PositiveOrZero(message = "Capacity must be positive or zero")
    private Integer capacity = 0;

    @NotEmpty(message = "Professor name is mandatory")
    private String professor;

    @Positive(message = "Year must be positive")
    private Integer year = 0;

    public Course(Integer courseCode, String courseName, String semester,
                  Integer capacity, String professor, Integer year) {
        this.courseCode = courseCode;
        this.courseName = courseName;
        this.semester = semester;
        this.capacity = capacity;
        this.professor = professor;
        this.year = year;
    }

    public Course() {

    }

    // getters and setters
    public Long getId() {
        return id;
    }

    public Integer getCourseCode() {
        return courseCode;
    }

    public String getCourseName() {
        return courseName;
    }

    public String getSemester() {
        return semester;
    }

    public Integer getCapacity() {
        return capacity;
    }

    public String getProfessor() {
        return professor;
    }

    public Integer getYear() {
        return year;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setCourseCode(Integer courseCode) {
        this.courseCode=courseCode;
    }

    public void setCourseName(String courseName) {
        this.courseName=courseName;
    }

    public void setSemester(String semester) {
        this.semester=semester;
    }

    public void  setCapacity( Integer capacity) {
        this.capacity=capacity;
    }

    public void setProfessor(String professor) {
        this.professor=professor;
    }

    public void setYear(Integer year) {
        this.year=year;
    }


}

