package model;

public class Address {

	/* The house number */
	private int houseNumber;
	/* The address street name*/
	private String streetName;
	/* The address  code*/
	private String zipCode;

	
	public Address(int hostNumber, String streetName, String zipCode) {
		this.houseNumber = hostNumber;
		this.streetName = streetName;
		this.zipCode = zipCode;
	}

	public int getHouseNumber() {
		return houseNumber;
	}

	public String getStreetName() {
		return streetName;
	}

	public String getZipCode() {
		return zipCode;
	}

}
