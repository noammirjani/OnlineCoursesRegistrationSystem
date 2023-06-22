//package hac.listeners;
//
//import hac.repo.course.Course;
//import hac.repo.course.CourseFullException;
//import hac.repo.coursesRegistrations.CourseRegistration;
//import hac.repo.coursesRegistrations.CourseRegistrationRepository;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.data.relational.core.conversion.MutableAggregateChange;
//import org.springframework.data.relational.core.mapping.event.BeforeSaveCallback;
//import org.springframework.stereotype.Component;
//
//@Component
//public class CourseRegistrationBeforeSave implements BeforeSaveCallback<CourseRegistration> {
//
//    @Autowired
//    private CourseRegistrationRepository registrationRepository;
//
//    @Override
//    public CourseRegistration onBeforeSave(CourseRegistration cr, MutableAggregateChange<CourseRegistration> aggregateChange) {
//
//        Course course = cr.getCourse();
//        int registrations = registrationRepository.countByCourse(course);
//        if (course.isFull(registrations)) {
//            throw new CourseFullException(course.getCourseName());
//        }
//        return cr;
//    }
//}
//
