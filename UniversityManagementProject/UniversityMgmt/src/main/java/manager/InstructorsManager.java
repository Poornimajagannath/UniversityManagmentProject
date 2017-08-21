package manager;

import model.Instructor;
import model.identity.ID;
import service.FileDataReaderService;
import service.InstructorsService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Class to manage the data of instructors.
 * Holds in memory (for now) all the data related to instructors
 */
public class InstructorsManager {

	private InstructorsService service;

	public InstructorsManager(InstructorsService service) {
		this.service = service;
	}

	/**
	 * Reads the contents of the file related to the Instructors
	 * Add the read instructors to the system
	 *
	 */
	public void loadFromFile() {

		FileDataReaderService reader = new FileDataReaderService();
		List<Instructor> all = reader.readInstructors();

		all.forEach(instructor -> add(instructor));
	}

	/**
	 * Adds a new instructor to the system
	 * @param newInstructor An Instructor to add
	 */
	public void add(Instructor newInstructor) {
		this.service.add(newInstructor);

	}

	/**
	 * Retrieve an Instructor by the given ID
	 * @param instructorId The Instructor's ID to search for
	 * @return Instructor object if exists, null otherwise
	 */
	public Instructor get(final ID instructorId) {
		return service.getById(instructorId);
	}

	/**
	 * Triggers the clear for instructors table
	 */
	public void clear() {
		this.service.clearAll();
	}
}
