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

import java.util.List;


/**
 * This class is used to access the course registration repository.
 */
@Service
public class AdminService {

    @Autowired
    private CourseRepository courseRepository;
    @Autowired
    private CourseRegistrationRepository registrationRepository;

    /**
     * This method is used to get all the course registrations.
     * @return a list of all the course registrations.
     */
    public List<CourseRegistration> getAllRegistrations() {
        return registrationRepository.findAll();
    }

    /**
     * This method is used to get all the courses.
     * @return a list of all the courses.
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
     * This method is used to get a course registration by its id.
     * @param id the id of the course registration.
     * @return the course registration with the given id.
     */
    public CourseRegistration getRegistration(long id) {
        return registrationRepository.findById(id).orElseThrow(() -> new IllegalArgumentException(MessagesConstants.REGISTRATION_NOT_FOUND + id));
    }

    /**
     * This method is used to get a course registration by its id.
     * @param course the course of the course registration.
     */
    public void addCourse(Course course){

        courseRepository.save(course);
    }

    /**
     * This method is used to add a course registration.
     * @param course the course registration to be added.
     */
    @Transactional
    public void deleteCourse(Course course) {

        courseRepository.deleteById(course.getId());
    }

    /**
     * This method is used to get all the students names.
     * @return a list of all the students names.
     */
    public List<String> getStudentsNames(){

        return registrationRepository.findDistinctStudentNames();
    }

    /**
     * This method is used to get all the courses.
     * @return a list of all the courses.
     */
    public List<String> getCoursesNames(){

        return courseRepository.findDistinctCourseNames();
    }

    /**
     * This method is used to add a course registration.
     * @param registration the course registration to be added.
     */
    public void deleteRegistration(CourseRegistration registration) {

        registrationRepository.delete(registration);
    }

    /**
     * This method is used to delete all the course registrations.
     */
    public void deleteAllRegistrations() {

        registrationRepository.deleteAll();
    }

    /**
     * This method is used to search for course registrations.
     * @param courseName the name of the course registration to be searched.
     * @param student the student of the course registration to be searched.
     * @return a list of course registrations that match the search criteria.
     */
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

    /**
     * This method is used to delete a course registration.
     * @param courseName the name of the course registration to be deleted.
     * @param student the student of the course registration to be deleted.
     */
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

    /**
     * This method is used to edit a course registration.
     * @param course the course registration to be edited.
     * @param id the id of the course registration to be edited.
     */
    @Transactional
    public void editCourse(Course course, long id){
        course.setId(id);

        // validation
        if(!course.isChangeCapacityValid(registrationRepository.countByCourse(course))){
            throw new CourseFullException(MessagesConstants.COURSE_FULL);
        }
        courseRepository.save(course);
    }

    /**
     * This method is used to delete all the courses. (the registrations are deleted automatically)
     */
    public void deleteAllCourses() {
    	courseRepository.deleteAll();
    }
}
