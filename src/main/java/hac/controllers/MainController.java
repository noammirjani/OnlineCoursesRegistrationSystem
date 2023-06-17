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
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;




@Controller
public class MainController {
  //  private static Logger logger = LoggerFactory.getLogger(MainController.class);

    @Autowired
    private CourseRepository repository;

    @Autowired
    private CourseRegistrationRepository registrationRepository;

    //------------------- admin urls -------------------

    @GetMapping("/admin")
    public String adminIndex() {
        return "admin/index";
    }

    //-------- admin manage courses --------------------

    @GetMapping("/admin/coursesManage")
    public String adminCourses(Model model) {
        Iterable<Course> courses = repository.findAll();
        model.addAttribute("courses", courses);
        return "admin/coursesManage";
    }

    @GetMapping("/admin/coursesManage/addCourse")
    public String adminAddCourseGet(Course course, Model model) {
        model.addAttribute("course", course);
        return "admin/addCourse";
    }

    @PostMapping("/admin/coursesManage/addCourse")
    public String adminAddCoursePost(@Valid Course course, BindingResult result, Model model) {
        if (result.hasErrors()){
            System.out.println("validation errors: " + result.getAllErrors());
            return "admin/addCourse";
        }

        repository.save(course);
        model.addAttribute("addedCourse", true);
        model.addAttribute("courses", repository.findAll());
        return "admin/coursesManage";
    }

    @PostMapping("/admin/coursesManage/deleteCourse/{id}")
    public String adminDeleteCourse(@PathVariable("id") long id, Model model) {
        Course course = repository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid course Id:" + id));
        repository.delete(course);
        model.addAttribute("courses", repository.findAll());
        return "admin/coursesManage";
    }

    @GetMapping("/admin/coursesManage/editCourse/{id}")
    public String adminEditCourseGet(@PathVariable("id") long id, Model model) {
        Course course = repository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid course Id:" + id));
        model.addAttribute("course", course);
        return "admin/editCourse";
    }

    @PostMapping("/admin/coursesManage/editCourse/{id}")
    public String adminEditCoursePost(@Valid Course course, BindingResult result, Model model) {
        if (result.hasErrors()){
            System.out.println("validation errors: " + result.getAllErrors());
            return "admin/addCourse";
        }

        // Get the existing course from the database.
        Course existingCourse = repository.findById(course.getId())
                .orElseThrow(() -> new IllegalArgumentException("Invalid course Id:" + course.getId()));

        // Update the properties of the existing course.
        existingCourse.setCourseCode(course.getCourseCode());
        existingCourse.setCourseName(course.getCourseName());
        existingCourse.setProfessor(course.getProfessor());
        existingCourse.setYear(course.getYear());
        existingCourse.setSemester(course.getSemester());
        existingCourse.setCapacity(course.getCapacity());
        existingCourse.setOverview(course.getOverview());

        // Save the updated course back to the database.
        repository.save(existingCourse);

        model.addAttribute("editedCourse", true);
        model.addAttribute("courses", repository.findAll());
        return "admin/coursesManage";

    }

    //-------- admin manage Registrations (students) --------------------

    private List<String> getUniqueCourseNames(Iterable<CourseRegistration> courseRegistration) {
        List<String> uniqueCourseNames = new ArrayList<>();
        courseRegistration.forEach(registration -> uniqueCourseNames.add(registration.getCourseName()));
        Set<String> uniqueCourseNameSet = new HashSet<>(uniqueCourseNames);
        return new ArrayList<>(uniqueCourseNameSet);
    }

    private List<String> getStudentsNames(Iterable<CourseRegistration> courseRegistration) {
        List<String> studentsNames = new ArrayList<>();
        courseRegistration.forEach(registration -> studentsNames.add(registration.getStudent()));
        return studentsNames;
    }


    @GetMapping("/admin/coursesRegistrationManage")
    public String adminCoursesRegistration (Model model) {

        //Iterable<CourseRegistration> courseRegistration = registrationRepository.findAll();

        model.addAttribute("courses", getUniqueCourseNames(registrationRepository.findAll()));
        model.addAttribute("students", getStudentsNames(registrationRepository.findAll()));
        model.addAttribute("filteredData", registrationRepository.findAll());
        return "admin/coursesRegistrationManage";
    }

    @PostMapping("/admin/coursesRegistrationManage/deleteRegistration/{id}")
    public String adminDeleteRegistration(@PathVariable("id") long id, Model model) {
        CourseRegistration registration = registrationRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid course Id:" + id));
        registrationRepository.delete(registration);

        model.addAttribute("courses", getUniqueCourseNames(registrationRepository.findAll()));
        model.addAttribute("students", getStudentsNames(registrationRepository.findAll()));
        model.addAttribute("filteredData", registrationRepository.findAll());
        model.addAttribute("registrationDeleted", true);
        return "admin/coursesRegistrationManage";
    }

    @PostMapping("/admin/coursesRegistrationManage/removeAllRegistration")
    public String adminDeleteAllRegistration(Model model) {

        registrationRepository.deleteAll();
//        model.addAttribute("courses", getUniqueCourseNames(registrationRepository.findAll()));
//        model.addAttribute("students", getStudentsNames(registrationRepository.findAll()));
//        model.addAttribute("filteredData", registrationRepository.findAll());
        model.addAttribute("filteredData", registrationRepository.findAll());
        model.addAttribute("removeAllRegistrations", true);
        return "admin/coursesRegistrationManage";
    }

    @PostMapping("/admin/coursesRegistrationManage/search")
    public String adminSearchRegistration(@RequestParam("courseName") String courseName,
                                          @RequestParam("studentName") String student,
                                          Model model) {
        List<CourseRegistration> registration = null;

        if (!courseName.equals("") && !student.equals("") ){
             registration = registrationRepository.findByCourseNameAndAndStudent(courseName, student);
        } else if (courseName.equals("") && !student.equals("") ) {
            registration = registrationRepository.findByStudent(student);
        }
        else if (!courseName.equals("") && student.equals("") ){
            registration = registrationRepository.findByCourseName(courseName);
        }
        else {
            registration = registrationRepository.findAll();
        }

        if(registration.size() == 0)
            model.addAttribute("noSearchResult", true);
        else
            model.addAttribute("noSearchResult", false);

                model.addAttribute("courses", getUniqueCourseNames(registrationRepository.findAll()));
        model.addAttribute("students", getStudentsNames(registrationRepository.findAll()));
        model.addAttribute("filteredData", registration);
        return "admin/coursesRegistrationManage";
    }


    ///****************************************************************************************

    //------------------- user urls --------------------
    @GetMapping("/user")
    public String userIndex() {
        return "user/index";
    }


    //------------------ get request to menu items --------------------
//    @GetMapping("/")
//    public String index() {  // add Principal principal to argument to show loged user details
//        return "index";
//    }
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

    @GetMapping("/errorpage")
    public String error(Exception ex, Model model) {
        model.addAttribute("errorMessage", ex.getMessage());
        return "error";
    }

}
