package utils.parsers;

import junit.framework.TestCase;
import model.enums.GradeEnum;
import model.Record;

public class RecordParserTest extends TestCase {

	RecordParser parser = new RecordParser();

	public void testParse() throws Exception {
		Record record = parser
				.parse("15,24,3,consistently makes good choices in all parts of the school day,A");
		assertEquals("15", record.getStudentId().toString());
		assertEquals("24", record.getClassId().toString());
		assertEquals("3", record.getInstructorId().toString());
		assertEquals(
				"consistently makes good choices in all parts of the school day",
				record.getComments());
		assertEquals(GradeEnum.A, record.getGrade());
	}

	public void testParse1() throws Exception {
		Record record = parser
				.parse("14,29,3,produces writing ... [ed. “shortened”] ... the past few weeks,C");
		assertEquals("14", record.getStudentId().toString());
		assertEquals("29", record.getClassId().toString());
		assertEquals("3", record.getInstructorId().toString());
		assertEquals(
				"produces writing ... [ed. “shortened”] ... the past few weeks",
				record.getComments());
		assertEquals(GradeEnum.C, record.getGrade());
	}

}