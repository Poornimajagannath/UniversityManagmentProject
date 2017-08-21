package manager;

import model.Assignment;
import model.AssignmentResult;
import model.CourseAssignment;
import model.identity.ID;
import service.CourseAssignmentsService;
import service.CourseInstructorAssignmentsService;
import service.FileDataReaderService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Manager to perform operations related to Assignment Model
 * Holds the assignments in-memory
 */
public class AssignmentsManager {

	private Map<Integer, Assignment> selected;
	private Map<Integer, Assignment> notSelected;


	private CourseAssignmentsService assignmentsService;
	private CourseInstructorAssignmentsService instructorAssignmentsService;
	private SemesterManager semesterManager;

	public AssignmentsManager(CourseAssignmentsService assignmentsService,
			CourseInstructorAssignmentsService instructorAssignmentsService,
			SemesterManager semesterManager) {
		selected = new HashMap<>();
		notSelected = new HashMap<>();
		this.assignmentsService = assignmentsService;
		this.instructorAssignmentsService = instructorAssignmentsService;
		this.semesterManager = semesterManager;
	}

	/**
	 * Reads the contents of the csv file related to the Assignments
	 *
	 * @param index The starting index to start reading from
	 */
	public void readFromFile(int index) {
		FileDataReaderService reader = new FileDataReaderService();
		List<Assignment> assignments = reader.readAssignments(index);
		int i = 0;
		for (Assignment assignment : assignments) {
			notSelected.put(i, assignment);
			i++;
		}

	}

	public AssignmentResult select(int selectedIndex) {
		AssignmentResult result;
		if (selected.size() <= 4) {
			Assignment toAdd = notSelected.get(selectedIndex);
			if (toAdd != null && !isInstructorSelected(
					toAdd.getInstructorId())) {
				selected.put(selectedIndex, toAdd);
				notSelected.remove(selectedIndex);
				result = AssignmentResult.SUCCESS;
			} else {
				result = AssignmentResult.ALREADY_SELECTED;
			}
		} else {
			result = AssignmentResult.NO_AVAILABLE;
		}
		return result;
	}

	public void remove(int order) {
		Assignment assignment = selected.get(order);
		if (assignment != null) {
			notSelected.put(order, assignment);
			selected.remove(order);
		}
	}

	/**
	 * Adds more seats to the given courseId
	 * @param courseId The course ID
	 * @param additional The number of seats to be added
	 */
	public void addSeats(final ID courseId, final int additional) {
		int semesterId = getCurrentSemester();
		this.assignmentsService.add(new CourseAssignment(courseId, semesterId, additional));
	}

	/**
	 * Check for seat availability for a given course for current semester
	 *
	 * @param courseId The course ID
	 * @return True is seats are available for assignment, Otherwise false
	 */
	public boolean checkAvailableSeats(ID courseId) {
		boolean result = false;
		int currentSemesterId = getCurrentSemester();
		int maxSeats = assignmentsService.getMaxSeats(courseId, currentSemesterId);
		int takenSeats = assignmentsService.getTakenSeats(courseId, currentSemesterId);
		result = maxSeats > takenSeats;
		return result;
	}

	/**
	 * Update the number of taken seats for a course in the given semester
	 * @param courseId The course ID
	 */
	public void updateTakenSeats(ID courseId) {
		int currentSemesterId = getCurrentSemester();
		assignmentsService.incrementTaken(courseId, currentSemesterId);
	}

	/**
	 * Fetch the List of Instructors available for Assignment for a course
	 * @param courseId The course ID
	 * @return A List of Instructor IDs
	 */
	public List<ID> getInstructorsForCurrentSemester(ID courseId) {
		int currentSemester = getCurrentSemester();
		return this.instructorAssignmentsService.getByCourse(courseId, currentSemester)
				.stream()
				.map(assg -> assg.getClassId())
				.collect(Collectors.toList());
	}

	/**
	 * Triggers the clear for courseInstructorAssignment and courseAssignment table
	 * Triggers the clear in-memory stored assignments
	 */
	public void clearAll() {
		selected.clear();
		notSelected.clear();
		this.assignmentsService.clearAll();
		this.instructorAssignmentsService.clearAll();
	}

	public Map<Integer, Assignment> getSelected() {
		return selected;
	}

	public Map<Integer, Assignment> getNotSelected() {
		return notSelected;
	}

	/**
	 * Add the assignment for an instructor for the current semester
	 * @param assignment The Assignment object
	 */
	public void assignInstructor(Assignment assignment) {
		this.instructorAssignmentsService.add(assignment, getCurrentSemester());
	}

	/**
	 * Apply all selected assignments
	 */
	public void applyAssignments() {
		selected.values().forEach(assignment -> applyAssignment(assignment));
	}

	/**
	 * Apply the given assignment.
	 * Add instructor to course and add available seats to the course
	 *
	 * @param assignment The Assignment object
	 */
	private void applyAssignment(Assignment assignment) {
		assignInstructor(assignment);
		addSeats(assignment.getClassId(), assignment.getCapacity());
	}

	/**
	 * Check if an instructor is already assigned for the given semester
	 * @param instructorId The instructor ID
	 * @return True if the given Instructor is already assigned
	 */
	private boolean isInstructorSelected(ID instructorId) {
		long count = this.selected.values().stream()
				.filter(assignment -> assignment.getInstructorId()
						.equals(instructorId)).count();
		return count > 0;
	}

	private int getCurrentSemester() {
		return semesterManager.getCurrent().getId();
	}

}
