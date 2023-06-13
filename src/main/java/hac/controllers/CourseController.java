package hac.controllers;

import hac.repo.Course;
import hac.repo.CourseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class CourseController {

    /* example: injecting a default value from the application.properties  file */
//    @Value( "${demo.coursename}" )
//    private String someProperty;

    /* inject via its type the User repo bean - a singleton */
    @Autowired
    private CourseRepository repository;

    @GetMapping("/courses")
    public String main(Model model) {
        System.out.print("*************************************************");
        System.out.print(repository.findAll());
        model.addAttribute("coursesData", repository.findAll());
        return "courses";
    }

}
