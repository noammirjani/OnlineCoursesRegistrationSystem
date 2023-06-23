package hac.controllers;

import hac.constants.MessagesConstants;
import hac.repo.course.Course;
import hac.repo.course.CourseFullException;
import hac.repo.coursesRegistrations.CourseRegistration;
import hac.services.UserService;
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
    private UserService service;

    /**
     * Handles the request for the user index page.
     *
     * @return The view name for the user index page.
     */
    @GetMapping
    public String userIndex() {
        return "user/index";
    }

    /**
     * Handles the request for managing course registrations.
     *
     * @param model The model object.
     * @return The view name for managing course registrations.
     */
    @GetMapping("/coursesRegistration")
    public String userCoursesRegistration(Model model) {
        model.addAttribute("courses", service.getAllCourses());
        model.addAttribute("ownedCourses", service.getStudentCoursesNames(getUserName()));
        return "user/coursesRegistration";
    }

    /**
     * Handles the POST request for adding a course registration.
     *
     * @param id                  The ID of the course to be registered.
     * @param redirectAttributes  The redirect attributes object for flash attributes.
     * @return The redirect view name for managing course registrations.
     */
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

    /**
     * Handles the POST request for removing a course registration.
     *
     * @param id                  The ID of the course registration to be removed.
     * @param redirectAttributes  The redirect attributes object for flash attributes.
     * @return The redirect view name for managing course registrations.
     */
    @PostMapping("/coursesRegistration/removeCourse/{id}")
    public String userDeleteCourse(@PathVariable("id") long id, RedirectAttributes redirectAttributes) {
        removeCourseRegistration(id);
        redirectAttributes.addFlashAttribute("scheduleChange", MessagesConstants.REGISTRATION_DELETE);
        return "redirect:/user/coursesRegistration";
    }

    /**
     * Handles the request for the user's registered courses.
     *
     * @param model The model object.
     * @return The view name for the user's registered courses.
     */
    @GetMapping("/myCourses")
    public String userMyCourses(Model model) {
        model.addAttribute("courses", service.getStudentRegistrations(getUserName()));
        return "user/myCourses";
    }

    /**
     * Handles the POST request for removing a registered course.
     *
     * @param id                  The ID of the registered course to be removed.
     * @param redirectAttributes  The redirect attributes object for flash attributes.
     * @return The redirect view name for the user's registered courses.
     */
    @PostMapping("/myCourses/removeCourse/{id}")
    public String userRemoveCourse(@PathVariable("id") long id, RedirectAttributes redirectAttributes) {
        removeCourseRegistration(id);
        redirectAttributes.addFlashAttribute("scheduleChange", MessagesConstants.REGISTRATION_DELETE);
        return "redirect:/user/myCourses";
    }

    /**
     * Retrieves the username of the currently authenticated user.
     *
     * @return The username of the authenticated user.
     */
    private String getUserName() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return authentication.getName();
    }

    /**
     * Removes the registration of a course for the current user.
     *
     * @param id The ID of the course to remove the registration for.
     */
    private void removeCourseRegistration(long id){
        Course course = service.getCourse(id);
        CourseRegistration cr = service.getRegistration(course, getUserName());
        service.deleteRegistration(cr);
    }

    /**
     * Exception handler for handling any unexpected exceptions.
     *
     * @param ex     The exception object.
     * @param model  The model object.
     * @return The view name for the error page.
     */
    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public String handleException(Exception ex, Model model) {
        String err = (ex != null ? ex.getMessage() : " -- Unknown error --");
        String errorMessage = "An unexpected error occurred: " + err;
        model.addAttribute("errorMessage", errorMessage);
        return "error";
    }
}