package manager;

import model.Course;
import model.Prerequisite;
import model.identity.ID;
import service.CoursesService;
import service.FileDataReaderService;

import java.util.List;

/**
 * Class to manage the data of courses.
 * Holds in memory (for now) all the data related to courses
 * Exposes functionality regarding updating seats and displaying available seats
 * <p>
 */
public class CourseManager {

	private CoursesService coursesService;

	public CourseManager(CoursesService coursesService) {
		this.coursesService = coursesService;
	}

	/**
	 * Reads the contents of the csv file related to the Courses
	 * Add the read courses to the system
	 * Updates the prerequisites on these courses
	 *
	 */
	public void loadFromFile() {

		FileDataReaderService reader = new FileDataReaderService();
		List<Course> allCourses = reader.readCourses();
		allCourses.forEach(course -> coursesService.add(course));
		List<Prerequisite> prerequisites = reader.readPrereq();
		prerequisites.forEach(prerequisite -> coursesService.addPrerequisite(prerequisite));
	}

	/**
	 * Adds a new CourseClass to the system. Makes a new entry into the course table
	 *
	 * @param course Course object
	 */
	public void add(Course course) {
		this.coursesService.add(course);
	}

	/**
	 * Retrieve a Course by its ID
	 * Updates the list of prerequisites for the given course
	 *
	 * @param courseId The course ID.
	 * @return Course instance if successful, null otherwise
	 */
	public Course get(final ID courseId) {
		Course course = this.coursesService.getById(courseId);
		List<ID> prerequisites = this.coursesService.getPrerequisites(courseId);
		prerequisites.forEach(id -> course.addPrerequiste(id));
		return course;
	}

	/**
	 * Triggers the clear for prerequisites and courses tables
	 */
	public void clear() {
		this.coursesService.clearAll();

	}

}
