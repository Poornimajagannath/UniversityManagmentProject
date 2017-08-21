package utils.parsers;

import junit.framework.TestCase;
import model.Assignment;
import model.identity.ID;
import org.junit.Test;

import static org.junit.Assert.*;

public class AssignmentParserTest extends TestCase {

	private AssignmentParser parser = new AssignmentParser();

	public void testReadLine() throws Exception {
		String line = "2,13,2";
		Assignment result = parser.parse(line);
		assertEquals(new ID("2"), result.getInstructorId());
		assertEquals(new ID("13"), result.getClassId());
		assertEquals(2, result.getCapacity());
	}
}