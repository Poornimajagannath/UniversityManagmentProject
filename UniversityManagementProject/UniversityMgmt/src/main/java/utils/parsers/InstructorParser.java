package utils.parsers;

import model.Address;
import model.identity.ID;
import model.Instructor;
import model.Person;

import java.util.List;

class InstructorParser extends PersonParser<Instructor> {

	@Override
	protected Person createPerson(ID id, String firstName, String lastName,
			Address address, String phoneNumber) {
		return new Instructor(id, firstName, lastName, address, phoneNumber);
	}
}
