package hac.controllers;

import hac.repo.course.Course;
import hac.repo.course.CourseRepository;
import hac.repo.coursesRegistrations.CourseRegistration;
import hac.repo.coursesRegistrations.CourseRegistrationRepository;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

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
    public String adminAddCoursePost(@Valid Course course, BindingResult result, RedirectAttributes redirectAttributes) {
        return saveCourse(course, result, redirectAttributes, "admin/addCourse", "Course added successfully!");
    }

    @GetMapping("/admin/coursesManage/editCourse/{id}")
    public String adminEditCourseGet(@PathVariable("id") long id, Model model) {
        Course course = findCourse(id);
        model.addAttribute("course", course);
        return "admin/editCourse";
    }

    @PostMapping("/admin/coursesManage/editCourse/{id}")
    public String adminEditCoursePost(@PathVariable("id") long id, @Valid Course course, BindingResult result, RedirectAttributes redirectAttributes) {
        course.setId(id);
        return saveCourse(course, result, redirectAttributes, "admin/editCourse", "Course edited successfully!");
    }


    @Transactional
    @PostMapping("/admin/coursesManage/deleteCourse/{id}")
    public String adminDeleteCourse(@PathVariable("id") long id, RedirectAttributes redirectAttributes) {
        Course course = findCourse(id);
        registrationRepository.deleteAllByCourse(course);
        repository.delete(course);
        redirectAttributes.addFlashAttribute("courseChange", "Course deleted successfully!");
        return "redirect:/admin/coursesManage";
    }

    private String saveCourse(Course course, BindingResult result, RedirectAttributes redirectAttributes, String errorPage, String successMessage) {
        if (result.hasErrors()){
            return errorPage;
        }
        repository.save(course);
        redirectAttributes.addFlashAttribute("courseChange", successMessage);
        return "redirect:/admin/coursesManage";
    }

    private Course findCourse(long id) {
        return repository.findById(id).orElseThrow(() -> new IllegalArgumentException("Invalid course Id:" + id));
    }

    private CourseRegistration findRegister(long id) {
        return registrationRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Invalid registration Id:" + id));
    }

    //-------- admin manage Registrations (students) --------------------
    @GetMapping("/admin/coursesRegistrationManage")
    public String adminCoursesRegistration (Model model) {

        model.addAttribute("courses", getCoursesNames());
        model.addAttribute("students", getStudentsNames());
        model.addAttribute("filteredData", registrationRepository.findAll());

        return "admin/coursesRegistrationManage";
    }

    @PostMapping("/admin/coursesRegistrationManage/deleteRegistration/{id}")
    public String adminDeleteRegistration(@PathVariable("id") long id, RedirectAttributes redirectAttributes) {

        CourseRegistration registration = findRegister(id);
        registrationRepository.delete(registration);
        redirectAttributes.addFlashAttribute("registrationChange", "Registration deleted successfully!");
        return "redirect:/admin/coursesRegistrationManage";
    }

    @PostMapping("/admin/coursesRegistrationManage/removeAllRegistration")
    public String adminDeleteAllRegistration(RedirectAttributes redirectAttributes) {

        registrationRepository.deleteAll();
        redirectAttributes.addFlashAttribute("registrationChange", "All registrations deleted successfully!");
        return "redirect:/admin/coursesRegistrationManage";
    }

    @PostMapping("/admin/coursesRegistrationManage/search")
    public String adminSearchRegistration(@RequestParam("courseName") String courseName,
                                          @RequestParam("studentName") String student,
                                          RedirectAttributes redirectAttributes) {
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

        redirectAttributes.addFlashAttribute("filteredData", registration);
        return "redirect:/admin/coursesRegistrationManage";
    }


    private List<String> getCoursesNames() {
        Iterable<Course> courseRegistrations = repository.findAll();
        List<String> courses = new ArrayList<>();
        courseRegistrations.forEach(c -> courses.add(c.getCourseName()));
        Set<String> uniqueCourseNameSet = new HashSet<>(courses);
        return new ArrayList<> (uniqueCourseNameSet);
    }


    private List<String> getStudentsNames() {
        Iterable<CourseRegistration> courseRegistration = registrationRepository.findAll();
        List<String> studentsNames = new ArrayList<>();
        courseRegistration.forEach(registration -> studentsNames.add(registration.getStudent()));
        Set<String> uniqueStudentNameSet = new HashSet<>(studentsNames);
        return new ArrayList<> (uniqueStudentNameSet);
    }
}