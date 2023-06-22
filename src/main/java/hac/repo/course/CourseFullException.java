package hac.repo.course;

/**
 * This exception is thrown when a course is full and cannot accept new registrations.
 */
public class CourseFullException extends RuntimeException {
    public CourseFullException(String courseName) {
        super("Course " + courseName + " is full!");
    }
}
