package hac.controllers;

import hac.constants.MessagesConstants;
import hac.repo.course.Course;
import hac.repo.course.CourseFullException;
import hac.repo.coursesRegistrations.CourseRegistration;
import hac.services.UserAdminService;
import org.hibernate.JDBCException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;


@Controller
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserAdminService service;

    @GetMapping
    public String userIndex() {
        return "user/index";
    }

    @GetMapping("/coursesRegistration")
    public String userCoursesRegistration(Model model) {
        model.addAttribute("courses", service.getAllCourses());
        model.addAttribute("ownedCourses", service.getStudentCoursesNames(getUserName()));
        return "user/coursesRegistration";
    }

    @PostMapping("/coursesRegistration/addCourse/{id}")
    public String userCoursesRegistrationPost(@PathVariable("id") long id, RedirectAttributes redirectAttributes) {

        Course course = service.getCourse(id);

        try{
            service.addRegistration(course, getUserName());
            redirectAttributes.addFlashAttribute("scheduleChange",  MessagesConstants.REGISTRATION_ADD);
        }catch (CourseFullException e){
            redirectAttributes.addFlashAttribute("error", MessagesConstants.COURSE_FULL);
        }catch(Exception e){
            redirectAttributes.addFlashAttribute("error", MessagesConstants.ALREADY_REGISTERED);
        }

        return "redirect:/user/coursesRegistration";
    }

    @PostMapping("/coursesRegistration/removeCourse/{id}")
    public String userDeleteCourse(@PathVariable("id") long id, RedirectAttributes redirectAttributes) {
        removeCourseRegistration(id);
        redirectAttributes.addFlashAttribute("scheduleChange", MessagesConstants.REGISTRATION_DELETE);
        return "redirect:/user/coursesRegistration";
    }

    @GetMapping("/myCourses")
    public String userMyCourses(Model model) {
        model.addAttribute("courses", service.getStudentRegistrations(getUserName()));
        return "user/myCourses";
    }

    @PostMapping("/myCourses/removeCourse/{id}")
    public String userRemoveCourse(@PathVariable("id") long id, RedirectAttributes redirectAttributes) {
        removeCourseRegistration(id);
        redirectAttributes.addFlashAttribute("scheduleChange", MessagesConstants.REGISTRATION_DELETE);
        return "redirect:/user/myCourses";
    }

    private String getUserName() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return authentication.getName();
    }

    private void removeCourseRegistration(long id){
        Course course = service.getCourse(id);
        CourseRegistration cr = service.getRegistration(course, getUserName());
        service.deleteRegistration(cr);
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