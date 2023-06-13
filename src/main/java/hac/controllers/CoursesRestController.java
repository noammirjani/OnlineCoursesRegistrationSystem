//package hac.controllers;
//
//import hac.repo.Course;
//import hac.repo.CourseRepository;
//import jakarta.validation.Valid;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.web.bind.annotation.*;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.RequestMapping;
////import java.util.HashMap;
////import java.util.Map;
////import jakarta.validation.ConstraintViolationException;
////import org.springframework.web.bind.annotation.ExceptionHandler;
////import org.springframework.http.HttpStatus;
////import org.springframework.validation.FieldError;
////import org.springframework.web.bind.MethodArgumentNotValidException;
////import org.springframework.web.bind.annotation.ResponseStatus;
//
//
//
//@RestController
//@RequestMapping("/api")
//public class CoursesRestController {
//    @Autowired
//    private CourseRepository repository;
//
//     /**
//      * http://localhost:8080/api/addCourse
//      * In this rest endpoint we handle validation errors with a custom exception handler
//      * using postman try sending the following post params:
//      * { "userName": "asdasd", "email": "asdasd", "visits":"2"}
//      * { "userName": "", "email": "asdasd", "visits":"2"}
//      */
////    @PostMapping("/addCourse")
////    public Course addUserRest(@Valid @RequestBody Course course) {
////        repository.save(course);
////        return course;
////    }
//
////
////     @GetMapping(value = { "/Courses", "/admin" })
////     public Iterable<Course> getCourses() {
////         return repository.findAll();
////     }
////
//
////    // then comment out the exception handler below and rerun the request,
////    // you will see that the response is a 400 bad request returned by spring
////    // in other words, if you don't handle the validation errors, Spring will return a 400 bad request
////    // writing a custom eception handler allows us to return the errors in a more user friendly way
////    // and also allows us to return any other Http response, even a 200 OK response instead of a 400 bad request
//////
////    @ResponseStatus(HttpStatus.BAD_REQUEST)
////    @ExceptionHandler({MethodArgumentNotValidException.class, ConstraintViolationException.class})
////    public Map<String, String> handleValidationExceptions(MethodArgumentNotValidException ex) {
////        Map<String, String> errors = new HashMap<>();
////        ex.getBindingResult().getAllErrors().forEach((error) -> {
////            String fieldName = ((FieldError) error).getField();
////            String errorMessage = error.getDefaultMessage();
////            errors.put(fieldName, errorMessage);
////        });
////        return errors;
////    }
//}