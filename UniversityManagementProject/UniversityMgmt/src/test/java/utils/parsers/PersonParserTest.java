package utils.parsers;

import junit.framework.TestCase;
import model.Instructor;
import model.Person;
import model.Student;

public class PersonParserTest extends TestCase {

	public void testParseStudent() throws Exception {
		PersonParser parser = new StudentParser();
		Person person = parser
				.parse("2,EVERETT KIM,699 Sheffield Drive 59251,8041174317");
		assertTrue(person instanceof Student);
		assertEquals("2", person.getId().toString());
		assertEquals("EVERETT", person.getFirstName());
		assertEquals("KIM", person.getLastName());
		assertEquals(699, person.getAddress().getHouseNumber());
		assertEquals("Sheffield Drive", person.getAddress().getStreetName());
		assertEquals("59251", person.getAddress().getZipCode());
		assertEquals("8041174317", person.getPhoneNumber());
	}

	public void testParseInstructor() throws Exception {
		PersonParser parser = new InstructorParser();
		Person person = parser
				.parse("2,EVERETT KIM,699 Sheffield Drive 59251,8041174317");
		assertTrue(person instanceof Instructor);
		assertEquals("2", person.getId().toString());
		assertEquals("EVERETT", person.getFirstName());
		assertEquals("KIM", person.getLastName());
		assertEquals(699, person.getAddress().getHouseNumber());
		assertEquals("Sheffield Drive", person.getAddress().getStreetName());
		assertEquals("59251", person.getAddress().getZipCode());
		assertEquals("8041174317", person.getPhoneNumber());
	}

}