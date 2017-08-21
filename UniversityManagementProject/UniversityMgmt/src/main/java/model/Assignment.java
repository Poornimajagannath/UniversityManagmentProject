package model;

import model.identity.ID;

public class Assignment {

	private final ID instructorId;
	private final ID classId;
	private final int capacity;

	public Assignment(final ID instructorId, final ID classId,
			final int capacity) {
		this.instructorId = instructorId;
		this.classId = classId;
		this.capacity = capacity;
	}

	public ID getInstructorId() {
		return instructorId;
	}

	public ID getClassId() {
		return classId;
	}

	public int getCapacity() {
		return capacity;
	}
}
