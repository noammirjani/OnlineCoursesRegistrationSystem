package hac.services;

import hac.repo.course.Course;
import hac.repo.coursesRegistrations.CourseRegistration;
import hac.repo.course.CourseRepository;
import hac.repo.coursesRegistrations.CourseRegistrationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class AdminService {

    @Autowired
    private CourseRepository repository;
    @Autowired
    private CourseRegistrationRepository registrationRepository;

    public List<Course> findAllCourses(){
        return repository.findAll();
    }

    public void saveCourse(Course course){
        repository.save(course);
    }

    public Course findCourse(long id){
        return repository.findById(id).orElseThrow(() -> new IllegalArgumentException("Invalid course Id:" + id));
    }

    public void deleteCourse(Course course){
        repository.delete(course);
    }

    @Transactional
    public void deleteAllRegistrationsByCourse(Course course){
        registrationRepository.deleteAllByCourse(course);
    }

    public CourseRegistration findRegistration(long id){
        return registrationRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Invalid registration Id:" + id));
    }

    public List<String> getCoursesNames(){
        Iterable<Course> courseRegistrations = repository.findAll();
        List<String> courses = new ArrayList<>();
        courseRegistrations.forEach(c -> courses.add(c.getCourseName()));
        Set<String> uniqueCourseNameSet = new HashSet<>(courses);
        return new ArrayList<> (uniqueCourseNameSet);
    }

    public List<String> getStudentsNames(){
        Iterable<CourseRegistration> courseRegistration = registrationRepository.findAll();
        List<String> studentsNames = new ArrayList<>();
        courseRegistration.forEach(registration -> studentsNames.add(registration.getStudent()));
        Set<String> uniqueStudentNameSet = new HashSet<>(studentsNames);
        return new ArrayList<> (uniqueStudentNameSet);
    }

    public void deleteRegistration(CourseRegistration registration){
        registrationRepository.delete(registration);
    }

    public void deleteAllRegistrations(){
        registrationRepository.deleteAll();
    }

    public List<CourseRegistration> searchRegistration(String courseName, String student){
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
}
