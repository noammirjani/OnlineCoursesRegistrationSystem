package hac.controllers;

import hac.repo.course.CourseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;


@Controller
public class MainController {

    @Autowired
    private CourseRepository repository;

    @GetMapping("/")
    public String home(Model model) {
        model.addAttribute("activeMenu", "home");
        return "index";
    }

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @GetMapping("/courses")
    public String main(Model model) {
        model.addAttribute("coursesData", repository.findAll());
        model.addAttribute("activeMenu", "courses");
        return "courses";
    }

    @GetMapping("/about-us")
    public String register(Model model) {
        model.addAttribute("activeMenu", "aboutUs");
        return "about-us";
    }

    @ExceptionHandler({Exception.class})
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public String handleException(Exception ex, Model model) {

        //logger.error("Exception during execution of SpringSecurity application", ex);
        String errorMessage = (ex != null ? ex.getMessage() : "Unknown error");

        model.addAttribute("errorMessage", errorMessage);
        return "error";
    }

    @GetMapping("/403")
    public String forbidden() {
        return "403";
    }

    @GetMapping("/error-page")
    public String error(Exception ex, Model model) {
        model.addAttribute("errorMessage", ex.getMessage());
        return "error";
    }

}
