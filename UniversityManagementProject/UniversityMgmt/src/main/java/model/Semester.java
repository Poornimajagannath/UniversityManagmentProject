package model;

import model.enums.SemesterEnum;

public class Semester {

	private final int id;
	private SemesterEnum semesterEnum;

	public Semester(final int id, SemesterEnum semesterEnum) {
		this.id = id;
		this.semesterEnum = semesterEnum;
	}

	public int getId() {
		return id;
	}

	public SemesterEnum getSemesterEnum() {
		return semesterEnum;
	}
}
