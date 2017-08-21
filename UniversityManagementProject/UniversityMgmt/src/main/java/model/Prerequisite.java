package model;

import model.identity.ID;

public class Prerequisite {

	private final ID prerequisiteId;
	private final ID classId;

	public Prerequisite(final ID prerequisiteId, final ID classId) {
		this.prerequisiteId = prerequisiteId;
		this.classId = classId;
	}

	public ID getClassId() {
		return classId;
	}

	public ID getPrerequisiteId() {
		return prerequisiteId;
	}
}
