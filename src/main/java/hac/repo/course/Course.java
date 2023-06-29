package hac.repo.course;

import hac.repo.coursesRegistrations.CourseRegistration;
import jakarta.persistence.*;

import jakarta.validation.constraints.*;


import java.io.Serializable;
import java.util.Set;

/**
 * Course entity
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
    @NotNull(message = "Course Name cannot be NULL")
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
    @NotNull(message = "Professor Name cannot be NULL")
    @Pattern(regexp="^[a-zA-Z ]+$", message="Professor name can contain only letters")
    private String professor;

    @NotNull(message = "Year is required")
    @Min(value = 2000, message = "Year must be at least 2000")
    @Max(value = 2023, message = "Year must be at most 2023")
    private Integer year;

    @NotEmpty(message = "Overview text is mandatory")
    @NotNull(message = "Overview text cannot be NULL")
    @Size(max = 255, message = "Overview text must be less than 255 characters")
    private String overview;

    @OneToMany(mappedBy = "course", cascade = CascadeType.ALL)
    private Set<CourseRegistration> registrations;


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

    public void setRegistrations(Set<CourseRegistration> registrations) {
        this.registrations = registrations;
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

    public Set<CourseRegistration> getRegistrations() {
        return registrations;
    }

    /**
     * Checks if the course is full.
     *
     * @param currRegistered The current number of registered students.
     * @return True if the course is full, false otherwise.
     */
    public Boolean isFull(Integer currRegistered) {
        return currRegistered >= capacity;
    }

    /**
     * Checks if the course capacity is valid.
     *
     * @param currRegistered The current number of registered students.
     * @return True if the course capacity is valid, false otherwise.
     */
    public Boolean isChangeCapacityValid(Integer currRegistered) {
        return currRegistered <= capacity;
    }

    /**
     * Returns the string representation of the Course object.
     *
     * @return The string representation of the Course object.
     */
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

