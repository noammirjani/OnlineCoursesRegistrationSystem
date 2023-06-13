package hac.controllers;

import hac.repo.Course;
import hac.repo.CourseRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import java.security.Principal;


/** this is a test controller, delete/replace it when you start working on your project */
@Controller
public class MainController {

    private static Logger logger = LoggerFactory.getLogger(MainController.class);
    @Autowired
    private CourseRepository repository;

    @RequestMapping("/")
    public String index() {  // add Principal principal to argument to show loged user details
        return "index";
    }

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @GetMapping("/about-Us")
    public String register() {
        return "about-Us";
    }

@GetMapping("/courses")
public String main(Model model) {
    model.addAttribute("coursesData", repository.findAll());
    return "courses";
}

    @RequestMapping("/user")
    public String userIndex() {
        return "user/index";
    }

    @RequestMapping("/admin")
    public String adminIndex() {
        return "admin/index";
    }

    @RequestMapping("/admin/coursesManage")
    public String adminCourses(Model model) {
        Iterable<Course> courses = repository.findAll();
        model.addAttribute("courses", courses);
        return "admin/coursesManage";
    }

    @RequestMapping("/403")
    public String forbidden() {
        return "403";
    }

    @ExceptionHandler({Exception.class})
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public String handleException(Exception ex, Model model) {

        //logger.error("Exception during execution of SpringSecurity application", ex);
        String errorMessage = (ex != null ? ex.getMessage() : "Unknown error");

        model.addAttribute("errorMessage", errorMessage);
        return "error";
    }

    @RequestMapping("/errorpage")
    public String error(Exception ex, Model model) {
        model.addAttribute("errorMessage", ex.getMessage());
        return "error";
    }

}
