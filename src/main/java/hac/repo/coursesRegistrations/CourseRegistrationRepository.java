package hac.repo.coursesRegistrations;

import hac.repo.course.Course;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import java.util.List;

public interface CourseRegistrationRepository extends JpaRepository<CourseRegistration, Long>{

      List <CourseRegistration> findByCourseNameAndStudent(String courseName, String student);
      List <CourseRegistration> findByStudent(String student);
      List <CourseRegistration> findByCourseName( String courseName);

      int countByCourseName(String courseName);

     @Query("SELECT c FROM Course c JOIN CourseRegistration cr ON c.courseName = cr.courseName WHERE cr.student = :studentName")
     List<Course> findCoursesByStudent(String studentName);

}
