package hac.controllers;

import hac.repo.course.CourseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;


@Controller
public class MainController {

    @Autowired
    private CourseRepository repository;

    /**
     * Handles the request for the home page.
     *
     * @param model The model object.
     * @return The view name for the home page.
     */
    @GetMapping("/")
    public String home(Model model) {
        model.addAttribute("activeMenu", "home");
        return "index";
    }

    /**
     * Handles the request for the login page.
     *
     * @return The view name for the login page or redirect to the appropriate role-based page.
     */
    @GetMapping("/login")
    public String login() {
        return getAutoPage("login");
    }

    /**
     * Handles the request for the courses page.
     *
     * @param model The model object.
     * @return The view name for the courses page.
     */
    @GetMapping("/courses")
    public String main(Model model) {
        model.addAttribute("coursesData", repository.findAll());
        model.addAttribute("activeMenu", "courses");
        return "courses";
    }

    /**
     * Handles the request for the about-us page.
     *
     * @param model The model object.
     * @return The view name for the about-us page.
     */
    @GetMapping("/about-us")
    public String register(Model model) {
        model.addAttribute("activeMenu", "aboutUs");
        return "about-us";
    }

    /**
     * Handles the request for the contact-us page.
     * @return The view name for the contact-us page.
     */
    @GetMapping("/home-page")
    public String homePage() {
         return getAutoPage("/");
    }

    /**
     * Handles the request for the forbidden page.
     *
     * @return The view name for the forbidden page.
     */
    @GetMapping("/403")
    public String forbidden() {
        return "403";
    }

    /**
     * Exception handler for handling any unexpected exceptions.
     *
     * @param ex     The exception object.
     * @param model  The model object.
     * @return The view name for the error page.
     */
    @GetMapping("/error-page")
    public String error(Exception ex, Model model) {
        model.addAttribute("errorMessage", ex.getMessage());
        return "error";
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

    /**
     * Exception handler for handling any unexpected exceptions.
     *
     * @param elsePage The view name for the error page.
     * @return The view name for the error page.
     */
    private String getAutoPage(String elsePage) {

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        if (!(auth instanceof AnonymousAuthenticationToken) && auth.isAuthenticated()) {
            for (GrantedAuthority authority : auth.getAuthorities()) {
                if (authority.getAuthority().equals("ROLE_ADMIN")) {
                    return "redirect:/admin";
                }
            }
            return "redirect:/user";
        }

        return elsePage;
    }
}
