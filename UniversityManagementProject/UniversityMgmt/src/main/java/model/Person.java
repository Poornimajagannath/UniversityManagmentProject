package model;

import model.identity.ID;

public abstract class Person {

	/* Unique identifier for the person. This attribute is immutable and
	* being set only in C'tor and once*/
	private final ID id;
	/* Person' first name */
	private final String firstName;
	/* Person' last name */
	private final String lastName;
	/* Person' full Address */
	private Address address;
	/* Person' phone number */
	private String phoneNumber;

	/**
	 * C'tor for Person
	 *
	 * @param id           The id for the person
	 * @param firstName    The person's first name
	 * @param lastName     The person's last name
	 * @param address      The person's address
	 * @param phoneNumber The person's list of phone numbers
	 */
	Person(final ID id, String firstName, String lastName, Address address,
			String phoneNumber) {
		this.id = id;
		this.firstName = firstName;
		this.lastName = lastName;
		this.address = address;
		this.phoneNumber = phoneNumber;
	}

	public ID getId() {
		return id;
	}

	public String getFirstName() {
		return firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public Address getAddress() {
		return address;
	}

	public String getPhoneNumber() {
		return phoneNumber;
	}

	@Override
	public boolean equals(Object obj) {
		boolean result = false;
		if (obj instanceof Person) {
			Person other = (Person) obj;
			result = (this.getId().equals(other.getId()));
		}
		return result;
	}
}
