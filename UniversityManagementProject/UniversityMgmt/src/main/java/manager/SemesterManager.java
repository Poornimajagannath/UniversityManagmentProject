package manager;

import model.Semester;
import model.enums.SemesterEnum;
import service.SemesterService;

/**
 * Class to manage current and next semester
 */
public class SemesterManager {

	private SemesterService semesterService;

	public SemesterManager(SemesterService semesterService) {
		this.semesterService = semesterService;
	}

	/**
	 * Designate the next semester
	 * This is calculated from the current semester
	 * @return The next semester
	 */
	public Semester designate() {
		Semester current = getCurrent();
		int next = current.getId() + 1;
		this.semesterService.setCurrent(next);
		Semester nextSemseter = new Semester(next, getType(next));
		return nextSemseter;
	}

	/**
	 * Retrieve the current semester
	 *
	 * @return current semester object
	 */
	public Semester getCurrent() {
		int id = semesterService.getCurrent();
		return new Semester(id, getType(id));
	}

	private SemesterEnum getType(int id) {
		return SemesterEnum.values()[id % 3];
	}

	/**
	 * Triggers the clear of currentSemester table
	 */
	public void clear() {
		this.semesterService.clear();
	}
}
