package hac.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

/** this is a test controller, delete/replace it when you start working on your project */
@Controller
public class Default {

    @GetMapping("/")
    public String index(Model model) {
        model.addAttribute("greeting", "Hello World");
        return "index";
    }
}
