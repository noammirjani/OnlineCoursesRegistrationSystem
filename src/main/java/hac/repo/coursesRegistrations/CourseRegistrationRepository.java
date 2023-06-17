package hac.repo.coursesRegistrations;

import hac.repo.coursesRegistrations.CourseRegistration;
import org.springframework.data.jpa.repository.JpaRepository;
public interface CourseRegistrationRepository extends JpaRepository<CourseRegistration, Long>{

    /** SOME EXAMPLES:
     *  defining some queries using the method names
     *  Spring will implement the method for us based on the method name
     *  https://docs.spring.io/spring-data/jpa/docs/current/reference/html/#jpa.query-methods
     */
//    List<Course> findByUserName(String userName);
//    List<Course> findUserByUserName(String userName);
//    List<Course> findByEmail(String email);
//    List<Course> findByUserNameAndEmail(String userName, String email);
//    List<Course> findFirst10ByOrderByUserNameDesc(); // find first 10 users ordered by userName desc

}
