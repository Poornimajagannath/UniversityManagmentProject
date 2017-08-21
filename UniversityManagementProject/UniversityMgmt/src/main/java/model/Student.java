package model;

import model.identity.ID;

public class Student extends Person {

	/* The current student's GPA score */
	private double gpa = 0.0;

	public Student(final ID id, String firstName, String lastName,
			Address address, String phoneNumber) {
		super(id, firstName, lastName, address, phoneNumber);
	}

	public double getGpa() {
		return gpa;
	}

	public void setGpa(double gpa) {
		this.gpa = gpa;
	}
}
