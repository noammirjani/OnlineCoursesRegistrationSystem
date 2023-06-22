package hac.services;

import hac.constants.MessagesConstants;
import hac.repo.course.Course;
import hac.repo.course.CourseFullException;
import hac.repo.course.CourseRepository;
import hac.repo.coursesRegistrations.CourseRegistration;
import hac.repo.coursesRegistrations.CourseRegistrationRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;


@Service
public class UserAdminService {

    @Autowired
    private CourseRepository courseRepository;
    @Autowired
    private CourseRegistrationRepository registrationRepository;

    /**
     * This method is used to get all the course registrations.
     * @return a list of all the course registrations.
     */
    public List<Course> getAllCourses() {
        return courseRepository.findAll();
    }

    /**
     * This method is used to get a course by its id.
     * @param id the id of the course.
     * @return the course with the given id.
     */
    public Course getCourse(long id) {
        return courseRepository.findById(id).orElseThrow(() -> new IllegalArgumentException(MessagesConstants.COURSE_NOT_FOUND + id));
    }

    /**
     * Deletes a course registration.
     *
     * @param registration The course registration to be deleted.
     */
    public void deleteRegistration(CourseRegistration registration) {
        registrationRepository.delete(registration);
    }

    /**
     * Adds a new course registration for a student.
     *
     * @param course  The course to be registered for.
     * @param student The name of the student.
     * @throws CourseFullException if the course is already full and cannot accept more registrations.
     */
    @Transactional
    public void addRegistration(Course course, String student) throws CourseFullException {

        int registrations = registrationRepository.countByCourse(course);
        if (course.isFull(registrations)) {
            throw new CourseFullException(course.getCourseName());
        }

        CourseRegistration cr = new CourseRegistration(course, student);
        registrationRepository.save(cr);
    }

    /**
     * Retrieves a course registration for a specific course and student.
     *
     * @param course  The course.
     * @param student The student.
     * @return The course registration for the given course and student.
     */
    public CourseRegistration getRegistration(Course course, String student) {
        return registrationRepository.findByCourseAndStudent(course, student).get(0);
    }

    /**
     * This method is used to get all the courses of a student.
     * @param student the student.
     * @return a list of all the courses of the student.
     */
    public List<Course> getStudentRegistrations(String student) {
        return registrationRepository.findCoursesByStudent(student);
    }

    /**
     * This method is used to get all the courses of a student.
     * @param student the student.
     * @return a list of all the courses of the student.
     */
    public List<String> getStudentCoursesNames(String student) {
        List<String> courses = new ArrayList<>();
        getStudentRegistrations(student).forEach(c -> courses.add(c.getCourseName()));
        return courses;
    }
}
