package utils.parsers;

import model.Address;
import model.Person;
import model.identity.ID;
import model.Student;

import java.util.List;

public class StudentParser extends PersonParser<Student> {

	@Override
	protected Person createPerson(ID id, String firstName, String lastName,
			Address address, String phoneNumber) {
		return new Student(id, firstName, lastName, address, phoneNumber);
	}
}
