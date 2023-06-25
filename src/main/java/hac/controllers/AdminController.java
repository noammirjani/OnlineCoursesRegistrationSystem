package hac.controllers;

import hac.constants.MessagesConstants;
import hac.repo.course.CourseFullException;
import hac.services.AdminService;
import hac.repo.course.Course;
import hac.repo.coursesRegistrations.CourseRegistration;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;


/**
 * Controller class for handling administrative operations.
 */
@Controller
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private AdminService service;

    /**
     * Handles the request for the admin index page.
     *
     * @return The view name for the admin index page.
     */
    @GetMapping
    public String adminIndex() {
        return "admin/index";
    }

    /**
     * Handles the request for the admin courses management page.
     *
     * @param model The model for the view.
     * @return The view name for the admin courses management page.
     */
    @GetMapping("/coursesManage")
    public String adminCourses(Model model) {
        model.addAttribute("courses", service.getAllCourses());
        return "admin/coursesManage";
    }

    /**
     * Handles the request for the admin add course page.
     *
     * @param course The course object for the form.
     * @param model  The model for the view.
     * @return The view name for the admin add course page.
     */
    @GetMapping("/coursesManage/addCourse")
    public String adminAddCourseGet(Course course, Model model) {
        model.addAttribute("course", course);
        return "admin/addCourse";
    }

    /**
     * Handles the request for the admin add course page.
     *
     * @param course              The course object for the form.
     * @param result              The binding result for the form.
     * @param redirectAttributes  The redirect attributes for the view.
     * @param model               The model for the view.
     * @return The view name for the admin add course page.
     */
    @PostMapping("/coursesManage/addCourse")
    public String adminAddCoursePost(@Valid Course course, BindingResult result, RedirectAttributes redirectAttributes, Model model) {
        if (result.hasErrors()){
            return "admin/addCourse";
        }

        try{
            service.addCourse(course);
        }catch (DataIntegrityViolationException e) {
            model.addAttribute("error", MessagesConstants.COURSE_EXIST);
            return "admin/addCourse";
        }

        redirectAttributes.addFlashAttribute("courseChange", MessagesConstants.COURSE_ADD);
        return "redirect:/admin/coursesManage";
    }

    /**
     * Handles the request for the admin edit course page.
     *
     * @param id    The id of the course to edit.
     * @param model The model for the view.
     * @return The view name for the admin edit course page.
     */
    @GetMapping("/coursesManage/editCourse/{id}")
    public String adminEditCourseGet(@PathVariable("id") long id, Model model) {
        model.addAttribute("course", service.getCourse(id));
        return "admin/editCourse";
    }

    /**
     * Handles the request for the admin edit course page.
     *
     * @param id                  The id of the course to edit.
     * @param course              The course object for the form.
     * @param result              The binding result for the form.
     * @param redirectAttributes  The redirect attributes for the view.
     * @param model               The model for the view.
     * @return The view name for the admin edit course page.
     */
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
        }catch (DataIntegrityViolationException e) {
            if (e.getCause() instanceof org.hibernate.exception.ConstraintViolationException) {
                model.addAttribute("error", MessagesConstants.COURSE_EXIST);
            }
            else throw e;
            return "admin/editCourse";
        }

        redirectAttributes.addFlashAttribute("courseChange", MessagesConstants.COURSE_EDIT);
        return "redirect:/admin/coursesManage";
    }

    /**
     * Handles the request for the admin delete course page.
     *
     * @param id                  The id of the course to delete.
     * @param redirectAttributes  The redirect attributes for the view.
     * @return The view name for the admin delete course page.
     */
    @PostMapping("/coursesManage/deleteCourse/{id}")
    public String adminDeleteCourse(@PathVariable("id") long id, RedirectAttributes redirectAttributes) {
        Course course = service.getCourse(id);
        service.deleteCourse(course);
        redirectAttributes.addFlashAttribute("courseChange", MessagesConstants.COURSE_DELETE);
        return "redirect:/admin/coursesManage";
    }

    /**
     * Handles the request for the admin courses registration management page.
     *
     * @param model The model for the view.
     * @return The view name for the admin courses registration management page.
     */
    @GetMapping("/coursesRegistrationManage")
    public String adminCoursesRegistration (Model model) {
        model.addAttribute("courses", service.getCoursesNames());
        model.addAttribute("students", service.getStudentsNames());
        return "admin/coursesRegistrationManage";
    }

    /**
     * Handles the request for retrieving all registrations.
     *
     * @param redirectAttributes  The redirect attributes object for flash attributes.
     * @return The redirect view name for managing course registrations.
     */
    @GetMapping("/coursesRegistrationManage/allRegistrations")
    public String adminCoursesRegistrationAll (RedirectAttributes redirectAttributes) {
        redirectAttributes.addFlashAttribute("filteredData", service.getAllRegistrations());
        return "redirect:/admin/coursesRegistrationManage";
    }

    /**
     * Handles the POST request for deleting a registration.
     *
     * @param id                   The ID of the registration to be deleted.
     * @param courseName           The name of the course associated with the registration.
     * @param student              The name of the student associated with the registration.
     * @param redirectAttributes   The redirect attributes object for flash attributes.
     * @return The redirect view name for managing course registrations with search parameters.
     */
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

    /**
     * Handles the GET request for searching registrations.
     *
     * @param courseName    The name of the course to search for registrations.
     * @param student       The name of the student to search for registrations.
     * @param model         The model object.
     * @return The view name for managing course registrations with search results.
     */
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

    /**
     * Handles the POST request for deleting all registrations.
     *
     * @param redirectAttributes  The redirect attributes object for flash attributes.
     * @return The redirect view name for managing course registrations with all registrations.
     */
    @PostMapping("/coursesRegistrationManage/removeAllRegistrations")
    public String adminDeleteAllRegistration(RedirectAttributes redirectAttributes) {
        service.deleteAllRegistrations();
        redirectAttributes.addFlashAttribute("registrationChange", MessagesConstants.REGISTRATION_DELETE_ALL);
        return "redirect:/admin/coursesRegistrationManage/allRegistrations";
    }

    /**
     * Handles the POST request for deleting all search registrations based on search parameters.
     *
     * @param courseName           The name of the course to search for registrations.
     * @param student              The name of the student to search for registrations.
     * @param redirectAttributes   The redirect attributes object for flash attributes.
     * @return The redirect view name for managing course registrations with search parameters.
     */
    @PostMapping("/coursesRegistrationManage/removeSearchRegistrations")
    public String adminDeleteSearchRegistrations(@RequestParam("courseName") String courseName,
                                                 @RequestParam("studentName") String student,
                                                 RedirectAttributes redirectAttributes) {
        service.deleteSearchRegistrations(courseName, student);
        redirectAttributes.addFlashAttribute("registrationChange", MessagesConstants.REGISTRATION_SEARCH_DELETE);
        return "redirect:/admin/coursesRegistrationManage/research?courseName=" + courseName + "&studentName=" + student;
    }

    /**
     * Handles the POST request for searching registrations.
     *
     * @param courseName           The name of the course to search for registrations.
     * @param student              The name of the student to search for registrations.
     * @param redirectAttributes   The redirect attributes object for flash attributes.
     * @return The redirect view name for managing course registrations with search results.
     */
    @PostMapping("/coursesRegistrationManage/search")
    public String adminSearchRegistration(@RequestParam("courseName") String courseName,
                                          @RequestParam("studentName") String student,
                                          RedirectAttributes redirectAttributes) {
        List<CourseRegistration> registrations = service.searchRegistrations(courseName, student);
        redirectAttributes.addFlashAttribute("filteredData", registrations);
        return "redirect:/admin/coursesRegistrationManage/research?courseName=" + courseName + "&studentName=" + student;
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
