package model;

import model.identity.ID;

import java.util.List;

public class Instructor extends Person {

	public Instructor(final ID id, String firstName, String lastName,
			Address address, String phoneNumber) {
		super(id, firstName, lastName, address, phoneNumber);
	}

}
