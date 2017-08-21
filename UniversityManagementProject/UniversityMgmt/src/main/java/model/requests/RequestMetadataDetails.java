package model.requests;

import model.identity.ID;

public class RequestMetadataDetails {

	private final ID studentId;
	private final ID classId;

	
	public RequestMetadataDetails(final ID studentId, final ID classId) {
		this.studentId = studentId;
		this.classId = classId;
	}

	
	public ID getStudentId() {
		return studentId;
	}

	
	public ID getClassId() {
		return classId;
	}

}
