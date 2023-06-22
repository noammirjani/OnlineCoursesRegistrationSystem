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

    public List<Course> getAllCourses() {
        return courseRepository.findAll();
    }

    public Course getCourse(long id) {
        return courseRepository.findById(id).orElseThrow(() -> new IllegalArgumentException(MessagesConstants.COURSE_NOT_FOUND + id));
    }

    public void deleteRegistration(CourseRegistration registration) {
        registrationRepository.delete(registration);
    }

    @Transactional
    public void addRegistration(Course course, String student) throws CourseFullException {

        int registrations = registrationRepository.countByCourse(course);
        if (course.isFull(registrations)) {
            throw new CourseFullException(course.getCourseName());
        }

        CourseRegistration cr = new CourseRegistration(course, student);
        registrationRepository.save(cr);
    }

    public CourseRegistration getRegistration(Course course, String student) {
        return registrationRepository.findByCourseAndStudent(course, student).get(0);
    }

    public List<Course> getStudentRegistrations(String student) {
        return registrationRepository.findCoursesByStudent(student);
    }

    public List<String> getStudentCoursesNames(String student) {
        List<String> courses = new ArrayList<>();
        getStudentRegistrations(student).forEach(c -> courses.add(c.getCourseName()));
        return courses;
    }
}
