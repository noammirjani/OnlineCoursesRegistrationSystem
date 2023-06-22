package hac.controllers;

import hac.constants.MessagesConstants;
import hac.repo.course.CourseFullException;
import hac.services.AdminService;
import hac.repo.course.Course;
import hac.repo.coursesRegistrations.CourseRegistration;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private AdminService service;

    @GetMapping
    public String adminIndex() {
        return "admin/index";
    }

    @GetMapping("/coursesManage")
    public String adminCourses(Model model) {
        model.addAttribute("courses", service.getAllCourses());
        return "admin/coursesManage";
    }

    @GetMapping("/coursesManage/addCourse")
    public String adminAddCourseGet(Course course, Model model) {
        model.addAttribute("course", course);
        return "admin/addCourse";
    }

    @PostMapping("/coursesManage/addCourse")
    public String adminAddCoursePost(@Valid Course course, BindingResult result, RedirectAttributes redirectAttributes) {
        if (result.hasErrors()){
            return "admin/addCourse";
        }
        service.addCourse(course);
        redirectAttributes.addFlashAttribute("courseChange", MessagesConstants.COURSE_ADD);
        return "redirect:/admin/coursesManage";
    }

    @GetMapping("/coursesManage/editCourse/{id}")
    public String adminEditCourseGet(@PathVariable("id") long id, Model model) {
        model.addAttribute("course", service.getCourse(id));
        return "admin/editCourse";
    }

    @PostMapping("/coursesManage/editCourse/{id}")
    public String adminEditCoursePost(@PathVariable("id") long id, @Valid Course course, BindingResult result, RedirectAttributes redirectAttributes, Model model) {
        if (result.hasErrors()){
            return "admin/editCourse";
        }

        try{
            service.editCourse(course, id);
        }catch (CourseFullException e) {
            model.addAttribute("error", MessagesConstants.COURSE_FULL + " you cannot lower the capacity");
            return "admin/editCourse";
        }

        redirectAttributes.addFlashAttribute("courseChange", MessagesConstants.COURSE_EDIT);
        return "redirect:/admin/coursesManage";
    }

    @PostMapping("/coursesManage/deleteCourse/{id}")
    public String adminDeleteCourse(@PathVariable("id") long id, RedirectAttributes redirectAttributes) {
        Course course = service.getCourse(id);
        service.deleteCourse(course);
        redirectAttributes.addFlashAttribute("courseChange", MessagesConstants.COURSE_DELETE);
        return "redirect:/admin/coursesManage";
    }

    @GetMapping("/coursesRegistrationManage")
    public String adminCoursesRegistration (Model model) {
        model.addAttribute("courses", service.getCoursesNames());
        model.addAttribute("students", service.getStudentsNames());
        return "admin/coursesRegistrationManage";
    }

    @GetMapping("/coursesRegistrationManage/allRegistrations")
    public String adminCoursesRegistrationAll (RedirectAttributes redirectAttributes) {
        redirectAttributes.addFlashAttribute("filteredData", service.getAllRegistrations());
        return "redirect:/admin/coursesRegistrationManage";
    }


    @PostMapping("/coursesRegistrationManage/deleteRegistration/{id}")
    public String adminDeleteRegistration(@PathVariable("id") long id,
                                          @RequestParam("courseName") String courseName,
                                          @RequestParam("studentName") String student,
                                          RedirectAttributes redirectAttributes) {
        CourseRegistration registration = service.getRegistration(id);
        service.deleteRegistration(registration);
        redirectAttributes.addFlashAttribute("registrationChange", MessagesConstants.REGISTRATION_DELETE);

        return "redirect:/admin/coursesRegistrationManage/research?courseName=" + courseName + "&studentName=" + student;
    }

    @GetMapping("/coursesRegistrationManage/research")
    public String adminResearchRegistration(@RequestParam("courseName") String courseName,
                                            @RequestParam("studentName") String student,
                                            Model model) {
        List<CourseRegistration> registrations = service.searchRegistrations(courseName, student);
        model.addAttribute("filteredData", registrations);

        // Pass the parameters back to the model so that they can be used to refill the select elements
        model.addAttribute("courseName", courseName);
        model.addAttribute("studentName", student);

        // Perform usual setup for the manage page
        model.addAttribute("courses", service.getCoursesNames());
        model.addAttribute("students", service.getStudentsNames());

        return "admin/coursesRegistrationManage";
    }


    @PostMapping("/coursesRegistrationManage/removeAllRegistrations")
    public String adminDeleteAllRegistration(RedirectAttributes redirectAttributes) {
        service.deleteAllRegistrations();
        redirectAttributes.addFlashAttribute("registrationChange", MessagesConstants.REGISTRATION_DELETE_ALL);
        return "redirect:/admin/coursesRegistrationManage/allRegistrations";
    }

    @PostMapping("/coursesRegistrationManage/removeSearchRegistrations")
    public String adminDeleteSearchRegistrations(@RequestParam("courseName") String courseName,
                                                 @RequestParam("studentName") String student,
                                                 RedirectAttributes redirectAttributes) {
        service.deleteSearchRegistrations(courseName, student);
        redirectAttributes.addFlashAttribute("registrationChange", MessagesConstants.REGISTRATION_DELETE_ALL);
        return "redirect:/admin/coursesRegistrationManage/research?courseName=" + courseName + "&studentName=" + student;
    }

    @PostMapping("/coursesRegistrationManage/search")
    public String adminSearchRegistration(@RequestParam("courseName") String courseName,
                                          @RequestParam("studentName") String student,
                                          RedirectAttributes redirectAttributes) {
        List<CourseRegistration> registrations = service.searchRegistrations(courseName, student);
        redirectAttributes.addFlashAttribute("filteredData", registrations);
        return "redirect:/admin/coursesRegistrationManage/research?courseName=" + courseName + "&studentName=" + student;
    }


    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public String handleException(Exception ex, Model model) {
        String err = (ex != null ? ex.getMessage() : " -- Unknown error --");
        String errorMessage = "An unexpected error occurred: " + err;
        model.addAttribute("errorMessage", errorMessage);
        return "error";
    }
}
