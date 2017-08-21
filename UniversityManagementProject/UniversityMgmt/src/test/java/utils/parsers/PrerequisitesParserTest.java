package utils.parsers;

import junit.framework.TestCase;
import model.Prerequisite;
import model.identity.ID;

public class PrerequisitesParserTest extends TestCase {

	PrerequisitesParser parser = new PrerequisitesParser();

	public void testParseLine() throws Exception {
		String line = "1,2";
		Prerequisite result = parser.parse(line);
		assertEquals(new ID("1"), result.getPrerequisiteId());
		assertEquals(new ID("2"), result.getClassId());

	}
}