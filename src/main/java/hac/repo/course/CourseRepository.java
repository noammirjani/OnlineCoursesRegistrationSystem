package hac.repo.course;

import org.springframework.data.jpa.repository.JpaRepository;

/* default scope of this Bean is "singleton" */
public interface CourseRepository extends JpaRepository<Course, Long> {

    /** SOME EXAMPLES:
     *  defining some queries using the method names
     *  Spring will implement the method for us based on the method name
     *  https://docs.spring.io/spring-data/jpa/docs/current/reference/html/#jpa.query-methods
     */
//    List<Course> findByUserName(String userName);
//    List<Course> findUserByUserName(String userName);
//    List<Course> findByEmail(String email);
//    List<Course> findByUserNameAndEmailNF(String userName, String email);
//    List<Course> findFirst10ByOrderByUserNameDesc(); // find first 10 users ordered by userName desc

}