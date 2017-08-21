package model;

import model.identity.ID;

public class CourseAssignment {

	private final ID courseId;
	private final int semesterId;
	private int maxSeats;

	public CourseAssignment(ID courseId, int semesterId, int maxSeats) {
		this.courseId = courseId;
		this.semesterId = semesterId;
		this.maxSeats = maxSeats;
	}

	public ID getCourseId() {
		return courseId;
	}

	public int getSemesterId() {
		return semesterId;
	}

	public int getMaxSeats() {
		return maxSeats;
	}

}
