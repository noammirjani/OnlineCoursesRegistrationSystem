package hac.repo.course;


public class CourseFullException extends RuntimeException {
    public CourseFullException(String courseName) {
        super("Course " + courseName + " is full!");
    }
}
