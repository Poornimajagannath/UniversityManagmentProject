package utils.parsers;

import junit.framework.TestCase;
import model.Address;

public class AddressParserTest extends TestCase {

	public void testParseSuccess() throws Exception {
		Address address = new AddressParser().parse("699 Sheffield Drive 59251");
		assertEquals(699, address.getHouseNumber());
		assertEquals("Sheffield Drive", address.getStreetName());
		assertEquals("59251", address.getZipCode());
	}

	public void testParseSuccess1() throws Exception {
		Address address = new AddressParser()
				.parse("699 Name1 Name2 Name3 Name4 59251");
		assertEquals(699, address.getHouseNumber());
		assertEquals("Name1 Name2 Name3 Name4", address.getStreetName());
		assertEquals("59251", address.getZipCode());
	}

}