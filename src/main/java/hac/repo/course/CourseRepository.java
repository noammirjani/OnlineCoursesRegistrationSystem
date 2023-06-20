package hac.repo.course;

import org.springframework.data.jpa.repository.JpaRepository;

/* default scope of this Bean is "singleton" */
public interface CourseRepository extends JpaRepository<Course, Long> {

}