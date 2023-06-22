package hac.repo.course;

import org.springframework.data.jpa.repository.JpaRepository;

/**
 * This interface is used to access the course repository.
 */
public interface CourseRepository extends JpaRepository<Course, Long> {

    Course findByCourseName(String courseName);
}