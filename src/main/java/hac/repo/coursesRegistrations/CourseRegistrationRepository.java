package hac.repo.coursesRegistrations;

import hac.repo.course.Course;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import java.util.List;

/**
 * This interface is used to access the course registration repository.
 */
public interface CourseRegistrationRepository extends JpaRepository<CourseRegistration, Long>{

    /**
         * This method is used to get a course registration by its course and student.
         * @param course the course of the course registration.
         * @param student the student of the course registration.
         * @return the course registration with the given course and student.
         */
      List <CourseRegistration> findByCourseAndStudent(Course course, String student);

        /**
         * This method is used to get all the course registrations of a student.
         * @param student the name of the student.
         * @return a list of all the course registrations of the student.
         */
      List <CourseRegistration> findByStudent(String student);

        /**
         * This method is used to get the number of students registered to a course.
         * @param course the course to get the number of students registered to it.
         * @return the number of students registered to the course.
         */
      int countByCourse(Course course);


      /**
       * This method is used to delete a course registration.
       * @param course the course registration to be deleted.
       * @param student the student of the course registration to be deleted.
       */
      void deleteByCourseAndStudent(Course course, String student);

      /**
       * This method is used to delete all the course registrations.
       */
      void deleteByStudent(String student);

      /**
       * This method is used to delete all the course registrations.
       */
      void deleteByCourse(Course course);

    /**
     * This method is used to get all the course registrations of a course.
     * @param courseName the name of the course.
     * @return a list of all the course registrations of the course.
     */
    @Query("SELECT cr FROM CourseRegistration cr WHERE cr.course.courseName = :courseName")
    List <CourseRegistration> findByCourseName(String courseName);

    /**
     * This method is used to get all the course registrations of a student.
     * @param studentName the name of the student.
     * @return a list of all the course registrations of the student.
     */
    @Query("SELECT cr.course FROM CourseRegistration cr WHERE cr.student = :studentName")
    List<Course> findCoursesByStudent(String studentName);

    /**
     * This method is used to get a course registration by its student.
     * @param student the student of the course registration.
     * @return the course registration with the given student.
     */
    @Query("SELECT cr FROM CourseRegistration cr WHERE cr.course.courseName = :courseName AND cr.student = :student")
    List <CourseRegistration> findByCourseNameAndStudent(String courseName, String student);

    /**
     * This method is used to get all the course registrations students..
     * @return a list of all the course registrations students.
     */
    @Query("SELECT DISTINCT r.student FROM CourseRegistration r")
    List<String> findDistinctStudentNames();


    /**
     * This method is used to get all the course registrations courses names..
     * @return a list of all the course registrations courses names.
     */
    @Query("SELECT cr.course.courseName FROM CourseRegistration cr WHERE cr.student = :studentName")
    List<String> findCoursesNamesByStudent(String studentName);

}
