package hac.controllers;

import hac.repo.course.Course;
import hac.repo.course.CourseRepository;
import hac.repo.coursesRegistrations.CourseRegistration;
import hac.repo.coursesRegistrations.CourseRegistrationRepository;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;


@Controller
public class MainController {

    @Autowired
    private CourseRepository repository;

    @Autowired
    private CourseRegistrationRepository registrationRepository;

    //------------------- admin urls -------------------



    ///****************************************************************************************

    //------------------- user urls --------------------
    @GetMapping("/user")
    public String userIndex() {
        return "user/index";
    }

    @GetMapping("/user/coursesRegistration")
    public String userCoursesRegistration(Model model) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userName = authentication.getName();

        model.addAttribute("courses", repository.findAll());

        List<String> ownedCourseNames = new ArrayList<>();
        for (CourseRegistration cr : registrationRepository.findByStudent(userName)) {
            ownedCourseNames.add(cr.getCourseName());
        }

        model.addAttribute("ownedCourses",ownedCourseNames );

        return "user/coursesRegistration";
    }

    @PostMapping("/user/coursesRegistration/addCourse/{id}")
    public String userCoursesRegistrationPost(@PathVariable("id") long id, Model model) {

        Course course = repository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid course Id:" + id));

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userName = authentication.getName();


        try{
            CourseRegistration cr = new CourseRegistration(course.getCourseName(), userName);
            registrationRepository.save(cr);
            model.addAttribute("scheduleChange", course.getCourseName() + " added to your schedule.");
        }
        catch (Exception e) {
            model.addAttribute("error", "Course is full!");
            return "admin/editCourse";
        }

        List<String> ownedCourseNames = new ArrayList<>();
        for (CourseRegistration crr : registrationRepository.findByStudent(userName)) {
            ownedCourseNames.add(crr.getCourseName());
        }

        model.addAttribute("ownedCourses", ownedCourseNames);
        model.addAttribute("courses", repository.findAll());

        return "redirect:/user/coursesRegistration";
    }

    @PostMapping("/user/coursesRegistration/removeCourse/{id}")
    public String userDeleteCourse(@PathVariable("id") long id, Model model) {
        Course course = repository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid course Id:" + id));

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userName = authentication.getName();

        try{
            CourseRegistration cr = registrationRepository.findByCourseNameAndStudent(course.getCourseName(), userName).get(0);
            registrationRepository.delete(cr);

            model.addAttribute("scheduleChange", course.getCourseName()+ " removed from your schedule.");
        } catch (Exception e) {
            model.addAttribute("error", "cannot delete course");
        }

        model.addAttribute("courses", repository.findAll());

        List<String> ownedCourseNames = new ArrayList<>();
        for (CourseRegistration crr : registrationRepository.findByStudent(userName)) {
            ownedCourseNames.add(crr.getCourseName());
        }

        model.addAttribute("ownedCourses", ownedCourseNames);

        return "redirect:/user/coursesRegistration";
    }

    @GetMapping("/user/myCourses")
    public String userMyCourses(Model model) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userName = authentication.getName();

        model.addAttribute("courses", registrationRepository.findCoursesByStudent(userName));
        return "user/myCourses";
    }

    @PostMapping("/user/myCourses/removeCourse/{id}")
    public String userRemoveCourse(@PathVariable("id") long id, Model model) {

        Course course = repository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid course Id:" + id));

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userName = authentication.getName();
        CourseRegistration cr = registrationRepository.findByCourseNameAndStudent(course.getCourseName(), userName).get(0);

        try{
            registrationRepository.delete(cr);
            model.addAttribute("scheduleChange", course.getCourseName()+ " removed from your schedule.");
        } catch (Exception e) {
            model.addAttribute("error ", "cannot delete registration");
        }

        model.addAttribute("courses", registrationRepository.findCoursesByStudent(userName));

        return "redirect:/user/myCourses";
    }


    //------------------ get request to menu items --------------------
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


    //-------------------- error handling --------------------
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
