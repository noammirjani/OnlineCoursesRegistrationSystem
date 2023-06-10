package hac.controllers;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestMapping;


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

    @RequestMapping("/user")
    public String userIndex() {
        return "user/index";
    }

}
