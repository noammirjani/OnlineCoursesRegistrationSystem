package hac.controllers;
import org.springframework.stereotype.Controller;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ExceptionHandler;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import java.security.Principal;


/** this is a test controller, delete/replace it when you start working on your project */
@Controller
public class MainController {

    private static Logger logger = LoggerFactory.getLogger(MainController.class);

    @RequestMapping("/")
    public String index() {  // add Principal principal to argument to show loged user details
        return "index";
    }

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @GetMapping("/register")
    public String register() {
        return "register";
    }

    @GetMapping("/courses")
    public String courses() {
        return "courses";
    }

    @RequestMapping("/user")
    public String userIndex() {
        return "user/index";
    }

}
