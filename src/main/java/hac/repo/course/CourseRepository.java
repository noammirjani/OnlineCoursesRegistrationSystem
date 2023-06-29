package hac.repo.course;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

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

    /*
     * delete course by id
     * @param id the id of the course to be deleted.
     */
    void deleteById(Long id);

    /**
     * This method is used to get all the course names.
     * @return a list of all the course names.
     */
    @Query("SELECT DISTINCT c.courseName FROM Course c")
    List<String> findDistinctCourseNames();
}