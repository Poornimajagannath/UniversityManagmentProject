package utils;

import junit.framework.TestCase;
import model.Course;
import utils.parsers.ParserEnum;

import java.util.List;

public class InputReaderTest extends TestCase {

	public void testReadCourses() throws Exception {
		List<Course> read = InputReader
				.read(ParserEnum.COURSE, "/Users/shiranmaor/Google Drive/Studies/SAAD/As#3/dev/working_system/src/test/resources/courses.csv");
		assertNotNull(read);
		assertEquals(15, read.size());
	}

	public void testReadStudents() throws Exception {
		List<Course> read = InputReader
				.read(ParserEnum.STUDENT, "/Users/shiranmaor/Google Drive/Studies/SAAD/As#3/dev/working_system/src/test/resources/students.csv");
		assertNotNull(read);
		assertEquals(10, read.size());
	}

	public void testReadRecords() throws Exception {
		List<Course> read = InputReader
				.read(ParserEnum.RECORD, "/Users/shiranmaor/Google Drive/Studies/SAAD/As#3/dev/working_system/src/test/resources/records.csv");
		assertNotNull(read);
		assertEquals(6, read.size());
	}

	public void testReadInstructors() throws Exception {
		List<Course> read = InputReader
				.read(ParserEnum.INSTRUCTOR, "/Users/shiranmaor/Google Drive/Studies/SAAD/As#3/dev/working_system/src/test/resources/instructors.csv");
		assertNotNull(read);
		assertEquals(5, read.size());
	}
}