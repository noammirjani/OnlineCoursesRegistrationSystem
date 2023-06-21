package hac.controllers;

import hac.services.AdminService;
import hac.repo.course.Course;
import hac.repo.coursesRegistrations.CourseRegistration;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
public class AdminController {

    @Autowired
    private AdminService adminService;

    @GetMapping("/admin")
    public String adminIndex() {
        return "admin/index";
    }

    @GetMapping("/admin/coursesManage")
    public String adminCourses(Model model) {
        model.addAttribute("courses", adminService.findAllCourses());
        return "admin/coursesManage";
    }

    @GetMapping("/admin/coursesManage/addCourse")
    public String adminAddCourseGet(Course course, Model model) {
        model.addAttribute("course", course);
        return "admin/addCourse";
    }

    @PostMapping("/admin/coursesManage/addCourse")
    public String adminAddCoursePost(@Valid Course course, BindingResult result, RedirectAttributes redirectAttributes) {
        if (result.hasErrors()){
            return "admin/addCourse";
        }
        adminService.saveCourse(course);
        redirectAttributes.addFlashAttribute("courseChange", "Course added successfully!");
        return "redirect:/admin/coursesManage";
    }

    @GetMapping("/admin/coursesManage/editCourse/{id}")
    public String adminEditCourseGet(@PathVariable("id") long id, Model model) {
        Course course = adminService.findCourse(id);
        model.addAttribute("course", course);
        return "admin/editCourse";
    }

    @PostMapping("/admin/coursesManage/editCourse/{id}")
    public String adminEditCoursePost(@PathVariable("id") long id, @Valid Course course, BindingResult result, RedirectAttributes redirectAttributes) {
        if (result.hasErrors()){
            return "admin/editCourse";
        }
        course.setId(id);
        adminService.saveCourse(course);
        redirectAttributes.addFlashAttribute("courseChange", "Course edited successfully!");
        return "redirect:/admin/coursesManage";
    }

    @PostMapping("/admin/coursesManage/deleteCourse/{id}")
    public String adminDeleteCourse(@PathVariable("id") long id, RedirectAttributes redirectAttributes) {
        Course course = adminService.findCourse(id);
        adminService.deleteAllRegistrationsByCourse(course);
        adminService.deleteCourse(course);
        redirectAttributes.addFlashAttribute("courseChange", "Course deleted successfully!");
        return "redirect:/admin/coursesManage";
    }

    @GetMapping("/admin/coursesRegistrationManage")
    public String adminCoursesRegistration (Model model) {
        model.addAttribute("courses", adminService.getCoursesNames());
        model.addAttribute("students", adminService.getStudentsNames());
        model.addAttribute("filteredData", adminService.findAllRegistrations());
        return "admin/coursesRegistrationManage";
    }

    @PostMapping("/admin/coursesRegistrationManage/deleteRegistration/{id}")
    public String adminDeleteRegistration(@PathVariable("id") long id, RedirectAttributes redirectAttributes) {
        CourseRegistration registration = adminService.findRegistration(id);
        adminService.deleteRegistration(registration);
        redirectAttributes.addFlashAttribute("registrationChange", "Registration deleted successfully!");
        return "redirect:/admin/coursesRegistrationManage";
    }

    @PostMapping("/admin/coursesRegistrationManage/removeAllRegistration")
    public String adminDeleteAllRegistration(RedirectAttributes redirectAttributes) {
        adminService.deleteAllRegistrations();
        redirectAttributes.addFlashAttribute("registrationChange", "All registrations deleted successfully!");
        return "redirect:/admin/coursesRegistrationManage";
    }

    @PostMapping("/admin/coursesRegistrationManage/search")
    public String adminSearchRegistration(@RequestParam("courseName") String courseName,
                                          @RequestParam("studentName") String student,
                                          RedirectAttributes redirectAttributes) {
        List<CourseRegistration> registration = adminService.searchRegistration(courseName, student);
        redirectAttributes.addFlashAttribute("filteredData", registration);
        return "redirect:/admin/coursesRegistrationManage";
    }
}
