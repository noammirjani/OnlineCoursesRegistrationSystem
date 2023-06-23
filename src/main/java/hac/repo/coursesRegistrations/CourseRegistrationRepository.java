package hac.repo.coursesRegistrations;

import hac.repo.course.Course;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import java.util.List;

/**
 * This interface is used to access the course registration repository.
 */
public interface CourseRegistrationRepository extends JpaRepository<CourseRegistration, Long>{

      List <CourseRegistration> findByCourseAndStudent(Course course, String student);

      @Query("SELECT cr FROM CourseRegistration cr WHERE cr.course.courseName = :courseName AND cr.student = :student")
      List <CourseRegistration> findByCourseNameAndStudent(String courseName, String student);

      List <CourseRegistration> findByStudent(String student);

      @Query("SELECT cr FROM CourseRegistration cr WHERE cr.course.courseName = :courseName")
      List <CourseRegistration> findByCourseName(String courseName);

     @Query("SELECT c FROM Course c JOIN CourseRegistration cr ON c = cr.course WHERE cr.student = :studentName")
     List<Course> findCoursesByStudent(String studentName);

      int countByCourse(Course course);

      @Modifying
      void deleteAllByCourse(Course course);

      void deleteByCourseAndStudent(Course course, String student);
      void deleteByStudent(String student);
      void deleteByCourse(Course course);
}
