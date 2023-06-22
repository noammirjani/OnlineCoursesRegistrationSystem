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
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class AdminService {

    @Autowired
    private CourseRepository courseRepository;
    @Autowired
    private CourseRegistrationRepository registrationRepository;

    public List<CourseRegistration> getAllRegistrations() {
        return registrationRepository.findAll();
    }

    public List<Course> getAllCourses() {
        return courseRepository.findAll();
    }

    public Course getCourse(long id) {
        return courseRepository.findById(id).orElseThrow(() -> new IllegalArgumentException(MessagesConstants.COURSE_NOT_FOUND + id));
    }

    public CourseRegistration getRegistration(long id) {
        return registrationRepository.findById(id).orElseThrow(() -> new IllegalArgumentException(MessagesConstants.REGISTRATION_NOT_FOUND + id));
    }

    public void addCourse(Course course){
        courseRepository.save(course);
    }

    @Transactional
    public void deleteCourse(Course course) {
        registrationRepository.deleteAllByCourse(course);
        courseRepository.delete(course);
    }

    public List<String> getStudentsNames(){
        Set<String> studentsNames = new HashSet<>();
        registrationRepository.findAll().forEach(registration -> studentsNames.add(registration.getStudent()));
        return new ArrayList<>(studentsNames);
    }

    public List<String> getCoursesNames(){
        List<String> courses = new ArrayList<>();
        courseRepository.findAll().forEach(c -> courses.add(c.getCourseName()));
        return courses;
    }

    public void deleteRegistration(CourseRegistration registration) {
        registrationRepository.delete(registration);
    }

    public void deleteAllRegistrations() {
        registrationRepository.deleteAll();
    }

    public List<CourseRegistration> searchRegistrations(String courseName, String student){
        if (!courseName.equals("") && !student.equals("") ){
            return registrationRepository.findByCourseNameAndStudent(courseName, student);
        } else if (courseName.equals("") && !student.equals("") ) {
            return registrationRepository.findByStudent(student);
        } else if (!courseName.equals("")){ // && student.equals("")
            return registrationRepository.findByCourseName(courseName);
        } else {
            return registrationRepository.findAll();
        }
    }

    @Transactional
    public void deleteSearchRegistrations(String courseName, String student){

        if (!courseName.equals("") && !student.equals("") ){
            Course c = courseRepository.findByCourseName(courseName);
            registrationRepository.deleteByCourseAndStudent(c, student);
        } else if (courseName.equals("") && !student.equals("") ) {
            registrationRepository.deleteByStudent(student);
        } else if (!courseName.equals("")){ // && student.equals("")
            Course c = courseRepository.findByCourseName(courseName);
            registrationRepository.deleteByCourse(c);
        } else {
            registrationRepository.deleteAll();
        }
    }

    public void editCourse(Course course, long id){

        course.setId(id);
        if(!course.isChangeCapacityValid(registrationRepository.countByCourse(course))){
            throw new CourseFullException(MessagesConstants.COURSE_FULL);
        }
        courseRepository.save(course);
    }


}
