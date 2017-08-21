package utils.parsers;

import junit.framework.TestCase;
import model.Course;
import model.enums.SemesterEnum;

public class CourseParserTest extends TestCase {

	CourseParser parser = new CourseParser();

	public void testParse() throws Exception {
		Course course = parser
				.parse("8,Computer architecture,Fall,Spring");
		assertEquals("8", course.getId().toString());
		assertEquals("8", course.getId().toString());
		assertEquals("Computer architecture",
				course.getName());
		assertEquals("", course.getDescription());
		assertEquals(2, course.getSemesters().size());
		assertTrue(course.getSemesters().contains(SemesterEnum.FALL));
		assertTrue(course.getSemesters().contains(SemesterEnum.SPRING));
		assertTrue(!course.getSemesters().contains(SemesterEnum.SUMMER));
	}

	public void testParse1() throws Exception {
		Course course = parser.parse("8,Computer architecture");
		assertEquals("8", course.getId().toString());
		assertEquals("8", course.getId().toString());
		assertEquals("Computer architecture",
				course.getName());
		assertEquals("", course.getDescription());
		assertEquals(0, course.getSemesters().size());
	}

	public void testParse2() throws Exception {
		Course course = parser
				.parse("8,Computer architecture,Fall,Spring,Summer");
		assertEquals("8", course.getId().toString());
		assertEquals("8", course.getId().toString());
		assertEquals("Computer architecture",
				course.getName());
		assertEquals("", course.getDescription());
		assertEquals(3, course.getSemesters().size());
		assertTrue(course.getSemesters().contains(SemesterEnum.FALL));
		assertTrue(course.getSemesters().contains(SemesterEnum.SPRING));
		assertTrue(course.getSemesters().contains(SemesterEnum.SUMMER));
	}

	public void testParse3() throws Exception {
		Course course = parser.parse("8,Computer architecture,Fall");
		assertEquals("8", course.getId().toString());
		assertEquals("8", course.getId().toString());
		assertEquals("Computer architecture", course.getName());
		assertEquals("", course.getDescription());
		assertEquals(1, course.getSemesters().size());
		assertTrue(course.getSemesters().contains(SemesterEnum.FALL));
	}
}