package hac.repo.course;

import jakarta.persistence.*;

import jakarta.validation.constraints.*;


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

    @NotNull(message = "Course Code is required")
    @Min(value = 0, message = "Course Code cannot be negative")
    @Column(unique = true)
    private Integer courseCode;

    @NotEmpty(message = "Course Name is required")
    //@Pattern(regexp="^[a-zA-Z0-9 ]+$", message="Course Name cannot contain special characters")
    @Size(max = 50, message = "Course Name must be no more than 50 characters")
    @Column(unique = true)
    private String courseName;

    @NotNull(message = "Num of semesters is required")
    @Min(value = 1, message = "Num of semesters must be at least 1")
    @Max(value = 3, message = "Num of semesters must be at most 3")
    private Integer semester;

    @NotNull(message = "Capacity must be positive or zero")
    @Max(value = 100, message = "Capacity must be at most 100")
    @Min(value = 1, message = "Capacity must be at least 1")
    private Integer capacity;

    @NotEmpty(message = "Professor name is mandatory")
    @Pattern(regexp="^[a-zA-Z ]+$", message="Professor name can contain only letters")
    private String professor;

    @NotNull(message = "Year is required")
    @Min(value = 2000, message = "Year must be at least 2000")
    @Max(value = 2023, message = "Year must be at most 2023")
    private Integer year;

    @NotEmpty(message = "Overview text is mandatory")
    @Size(max = 255, message = "Overview text must be less than 500 characters")
    private String overview;


    public Course(Integer courseCode, String courseName, Integer semester,
                  Integer capacity, String professor, Integer year, String overview) {
        this.courseCode = courseCode;
        this.courseName = courseName;
        this.semester = semester;
        this.capacity = capacity;
        this.professor = professor;
        this.year = year;
        this.overview = overview;
    }

    public Course() {}

    // getters and setters
    public Long getId() {return id;}
    public Integer getCourseCode() {
        return courseCode;
    }
    public String getCourseName() {
        return courseName;
    }
    public Integer getSemester() {
        return semester;
    }
    public Integer getCapacity() {
        return capacity;
    }
    public String getProfessor() {
        return professor;
    }
    public String getOverview() {return overview;}
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
    public void setSemester(Integer semester) {this.semester=semester;}
    public void  setCapacity( Integer capacity) {
        this.capacity=capacity;
    }
    public void setProfessor(String professor) {
        this.professor=professor;
    }
    public void setYear(Integer year) {
        this.year=year;
    }
    public void setOverview(String overview) {
        this.overview=overview;
    }

    public Boolean isFull(Integer currRegistered) {
        return currRegistered >= capacity;
    }

    public Boolean isChangeCapacityValid(Integer currRegistered) {
        return currRegistered <= capacity;
    }

    @Override
    public String toString() {
        return "Course{" +
                "id=" + id +
                ", courseCode=" + courseCode +
                ", courseName='" + courseName + '\'' +
                ", semester=" + semester +
                ", capacity=" + capacity +
                ", professor='" + professor + '\'' +
                ", year=" + year +
                ", overview='" + overview + '\'' +
                '}';
    }
}

