package utils.parsers;

import model.Address;
import model.identity.ID;
import model.Person;

import java.util.ArrayList;
import java.util.List;

public abstract class PersonParser<T extends Person> implements IParser<T> {

	public T parse(String data) {
		String[] details = data.split(",");
		String idStr = details[0];
		String[] fullName = details[1].split("\\s");
		String firstName = fullName[0];
		String lastName = fullName[1];
		Address address = new AddressParser().parse(details[2]);
		String phoneNumber = details[3];
		T newPerson = (T) createPerson(new ID(idStr), firstName, lastName,
				address, phoneNumber);
		return newPerson;

	}

	protected abstract Person createPerson(ID id, String firstName,
			String lastName, Address address, String phoneNumber);

}
