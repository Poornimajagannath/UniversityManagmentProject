package manager;

import model.Student;
import model.identity.ID;
import service.FileDataReaderService;
import service.StudentsService;

import java.util.List;

/**
 * Manager which holds all Students and can query it to get information
 */
public class StudentsManger {

	private StudentsService studentsService;

	public StudentsManger(StudentsService studentsService) {
		this.studentsService = studentsService;
	}

	/**
	 * Reads the contents of the csv file related to the Students
	 * Add the read courses to the system
	 *
	 */
	public void loadFromFile() {
		FileDataReaderService reader = new FileDataReaderService();
		List<Student> all = reader.readStudents();
		all.forEach(student -> add(student));

	}

	/**
	 * Adds a new student to the system
	 * @param student The new student to add
	 */
	public void add(Student student) {
		this.studentsService.add(student);
	}

	/**
	 * Try to fetch a Student for the given ID
	 * @param studentId The student's ID
	 * @return Student object
	 */
	public Student get(final ID studentId) {
		return studentsService.getById(studentId);
	}

	/**
	 * Triggers the clear for students table
	 */
	public void clear() {
		this.studentsService.clearAll();
	}
}
