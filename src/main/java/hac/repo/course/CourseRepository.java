package hac.repo.course;

import org.springframework.data.jpa.repository.JpaRepository;

/**
 * This interface is used to access the course repository.
 */
public interface CourseRepository extends JpaRepository<Course, Long> {

    /**
     * This method is used to get a course by its name.
     * @param courseName the name of the course.
     * @return the course with the given name.
     */
    Course findByCourseName(String courseName);
}