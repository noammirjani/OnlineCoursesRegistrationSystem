package hac.controllers;

import hac.repo.course.Course;
import hac.repo.course.CourseRepository;
import hac.repo.coursesRegistrations.CourseRegistration;
import hac.repo.coursesRegistrations.CourseRegistrationRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Controller
public class AdminController {
    @Autowired
    private CourseRepository repository;
    @Autowired
    private CourseRegistrationRepository registrationRepository;

    @GetMapping("/admin")
    public String adminIndex() {
        return "admin/index";
    }

    @GetMapping("/admin/coursesManage")
    public String adminCourses(Model model) {
        model.addAttribute("courses", repository.findAll());
        return "admin/coursesManage";
    }

    @GetMapping("/admin/coursesManage/addCourse")
    public String adminAddCourseGet(Course course, Model model) {
        model.addAttribute("course", course);
        return "admin/addCourse";
    }

    @PostMapping("/admin/coursesManage/addCourse")
    public String adminAddCoursePost(@Valid Course course, BindingResult result, Model model) {
        return saveCourse(course, result, model, "admin/addCourse", "Course added successfully!");
    }

    @GetMapping("/admin/coursesManage/editCourse/{id}")
    public String adminEditCourseGet(@PathVariable("id") long id, Model model) {
        Course course = findCourse(id);
        model.addAttribute("course", course);
        return "admin/editCourse";
    }

    @PostMapping("/admin/coursesManage/editCourse/{id}")
    public String adminEditCoursePost(@PathVariable("id") long id, @Valid Course course, BindingResult result, Model model) {
        course.setId(id);
        return saveCourse(course, result, model, "admin/editCourse", "Course edited successfully!");
    }

    @PostMapping("/admin/coursesManage/deleteCourse/{id}")
    public String adminDeleteCourse(@PathVariable("id") long id, Model model) {
        Course course = findCourse(id);
        try {
            repository.delete(course);
            model.addAttribute("courseChange", "Course deleted successfully!");
        } catch (Exception e) {
            model.addAttribute("error", "Course cannot be deleted");
            return "redirect:/admin/coursesManage";
        }
        model.addAttribute("courses", repository.findAll());
        return "redirect:/admin/coursesManage";
    }


    private String saveCourse(Course course, BindingResult result, Model model, String errorPage, String successMessage) {
        if (result.hasErrors()){
            return errorPage;
        }

        try{
            repository.save(course);
        } catch (Exception e) {
            // error needs to show on the same page
            model.addAttribute("error", "Course already exists");
            return errorPage;
        }

        model.addAttribute("courseChange", successMessage);
        model.addAttribute("courses", repository.findAll());
        return "admin/coursesManage";
    }

    private Course findCourse(long id) {
        return repository.findById(id).orElseThrow(() -> new IllegalArgumentException("Invalid course Id:" + id));
    }

    private CourseRegistration findCR(long id) {
        return registrationRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Invalid course registration Id:" + id));
    }


    //-------- admin manage Registrations (students) --------------------

    @GetMapping("/admin/coursesRegistrationManage")
    public String adminCoursesRegistration (Model model) {
        return getCourseRegisterModel(model, registrationRepository.findAll(), false);
    }

    @PostMapping("/admin/coursesRegistrationManage/deleteRegistration/{id}")
    public String adminDeleteRegistration(@PathVariable("id") long id, Model model) {

        CourseRegistration registration = registrationRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Invalid course Id:" + id));

        try {
            registrationRepository.delete(registration);
            model.addAttribute("registrationChange", "Registration deleted successfully!");
        } catch (Exception e) {
            model.addAttribute("error", "Registration cannot be deleted");
            return "redirect:admin/coursesRegistrationManage";
        }

        return getCourseRegisterModel(model, registrationRepository.findAll(), true);
    }

    @PostMapping("/admin/coursesRegistrationManage/removeAllRegistration")
    public String adminDeleteAllRegistration(Model model) {

        try{
            registrationRepository.deleteAll();
            model.addAttribute("registrationChange", "All registrations deleted successfully!");

        } catch (Exception e) {
            model.addAttribute("error", "cannot delete all registrations");
            return "admin/editCourse";
        }

        return getCourseRegisterModel(model, registrationRepository.findAll(), true);
    }

    @PostMapping("/admin/coursesRegistrationManage/search")
    public String adminSearchRegistration(@RequestParam("courseName") String courseName,
                                          @RequestParam("studentName") String student,
                                          Model model) {
        List<CourseRegistration> registration;

        if (!courseName.equals("") && !student.equals("") ){
            registration = registrationRepository.findByCourseNameAndStudent(courseName, student);
        } else if (courseName.equals("") && !student.equals("") ) {
            registration = registrationRepository.findByStudent(student);
        } else if (!courseName.equals("")){ // && student.equals("")
            registration = registrationRepository.findByCourseName(courseName);
        } else {
            registration = registrationRepository.findAll();
        }

        return getCourseRegisterModel(model, registration, true);
    }

    private String getCourseRegisterModel(Model model, List<CourseRegistration> filteredData, boolean isPost) {

        model.addAttribute("courses", getCoursesNames());
        model.addAttribute("students", getStudentsNames());
        model.addAttribute("filteredData", filteredData);

        if(isPost) {
            return "redirect:/admin/coursesRegistrationManage";
        }

        return "admin/coursesRegistrationManage";
    }

    private List<String> getCoursesNames() {
        Iterable<Course> courses = repository.findAll();
        List<String> uniqueCourseNames = new ArrayList<>();
        courses.forEach(course -> uniqueCourseNames.add(course.getCourseName()));
        Set<String> uniqueCourseNameSet = new HashSet<>(uniqueCourseNames);
        return new ArrayList<>(uniqueCourseNameSet);
    }

    private List<String> getStudentsNames() {
        Iterable<CourseRegistration> courseRegistration = registrationRepository.findAll();
        List<String> studentsNames = new ArrayList<>();
        courseRegistration.forEach(registration -> studentsNames.add(registration.getStudent()));
        Set<String> uniqueStudentNameSet = new HashSet<>(studentsNames);
        return new ArrayList<> (uniqueStudentNameSet);
    }
}
