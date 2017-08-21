package manager;

import model.identity.ID;
import model.Record;
import service.FileDataReaderService;
import service.RecordsService;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Manager which holds all records and can query it to get information
 */
public class RecordsManager {

	private RecordsService service;

	public RecordsManager(RecordsService service) {
		this.service = service;
	}

	/**
	 * Reads the contents of the  file related to the Records
	 * Add the read courses to the system
	 *
	 */
	public void loadFromFile() {
		FileDataReaderService reader = new FileDataReaderService();

		reader.readRecords().forEach(record -> add(record));
	}

	/**
	 * Adds a new record to the system
	 
	 * @param record The record to add
	 */
	public void add(Record record) {
		this.service.add(record);
	}

	/**
	 * Gets records for a given student
	 *
	 * @param studentID The Student's ID to search for
	 * @return Map of ClassId and the given students records
	 */
	public Map<ID, List<Record>> getForStudent(final ID studentID) {
		return this.service.getForStudent(studentID)
				.stream()
				.collect(Collectors.groupingBy(o -> o.getClassId()));
	}

	/**
	 * Search for a specific Records for a given student and class
	 * @param studentId The student ID
	 * @param classId   The class ID
	 * @return Record object in cases such exists, null otherwise
	 */
	public List<Record> getForStudentAndClass(final ID studentId, final ID classId) {
		return service.getForStudentAndCourse(studentId, classId);
	}

	/**
	 * Retrieve all the records in the system
	 *
	 * @return A list of all the records in the records table
	 */
	public List<Record> getAll() {
		return service.getAll();
	}

	/**
	 * Triggers the clear for records table
	 */
	public void clear() {
		this.service.clearAll();
	}
}
