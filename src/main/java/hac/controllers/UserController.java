package hac.controllers;

import hac.repo.course.Course;
import hac.repo.course.CourseRepository;
import hac.repo.coursesRegistrations.CourseRegistration;
import hac.repo.coursesRegistrations.CourseRegistrationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.ArrayList;
import java.util.List;

@Controller
public class UserController {

    @Autowired
    private CourseRepository repository;

    @Autowired
    private CourseRegistrationRepository registrationRepository;

    @GetMapping("/user")
    public String userIndex() {
        return "user/index";
    }

    @GetMapping("/user/coursesRegistration")
    public String userCoursesRegistration(Model model) {
        model.addAttribute("courses", repository.findAll());
        model.addAttribute("ownedCourses", getOwnedCourseNames());
        return "user/coursesRegistration";
    }

    @PostMapping("/user/coursesRegistration/addCourse/{id}")
    public String userCoursesRegistrationPost(@PathVariable("id") long id, RedirectAttributes redirectAttributes) {

        Course course = findCourse(id);

        try{
            if(registrationRepository.countByCourseName(course.getCourseName()) >= course.getCapacity()) {
                redirectAttributes.addFlashAttribute("error", "Course is full!");
            }else{
                CourseRegistration cr = new CourseRegistration(course.getCourseName(), getAuthenticatedUserName());
                registrationRepository.save(cr);
                redirectAttributes.addFlashAttribute("scheduleChange", course.getCourseName() + " added to your schedule.");
            }
        }catch(Exception e){
            redirectAttributes.addFlashAttribute("error", "You are already registered for this course!");
        }

        return "redirect:/user/coursesRegistration";
    }


    @PostMapping("/user/coursesRegistration/removeCourse/{id}")
    public String userDeleteCourse(@PathVariable("id") long id, RedirectAttributes redirectAttributes) {
        Course course = findCourse(id);
        CourseRegistration cr = registrationRepository.findByCourseNameAndStudent(course.getCourseName(), getAuthenticatedUserName()).get(0);
        registrationRepository.delete(cr);
        redirectAttributes.addFlashAttribute("scheduleChange", course.getCourseName() + " removed from your schedule.");
        return "redirect:/user/coursesRegistration";
    }

    @GetMapping("/user/myCourses")
    public String userMyCourses(Model model) {
        model.addAttribute("courses", registrationRepository.findCoursesByStudent(getAuthenticatedUserName()));
        return "user/myCourses";
    }

    @PostMapping("/user/myCourses/removeCourse/{id}")
    public String userRemoveCourse(@PathVariable("id") long id, RedirectAttributes redirectAttributes) {

        Course course = findCourse(id);
        CourseRegistration cr = registrationRepository.findByCourseNameAndStudent(course.getCourseName(), getAuthenticatedUserName()).get(0);
        registrationRepository.delete(cr);
        redirectAttributes.addFlashAttribute("scheduleChange", course.getCourseName() + " removed from your schedule.");

        return "redirect:/user/myCourses";
    }

    private String getAuthenticatedUserName() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return authentication.getName();
    }

    private List<String> getOwnedCourseNames() {
        List<String> ownedCourseNames = new ArrayList<>();
        for (CourseRegistration cr : registrationRepository.findByStudent(getAuthenticatedUserName())) {
            ownedCourseNames.add(cr.getCourseName());
        }
        return ownedCourseNames;
    }

    private Course findCourse(Long id) {
        return repository.findById(id).orElseThrow(() -> new IllegalArgumentException("Invalid course Id:" + id));
    }
}