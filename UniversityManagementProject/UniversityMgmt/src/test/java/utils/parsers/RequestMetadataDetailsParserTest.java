package utils.parsers;

import junit.framework.TestCase;
import model.identity.ID;
import model.requests.RequestMetadataDetails;

public class RequestMetadataDetailsParserTest extends TestCase {

	private RequestMetadataParser parser = new RequestMetadataParser();

	public void testParseLine() throws Exception {
		String line = "1,2";
		RequestMetadataDetails result = parser.parse(line);
		assertEquals(new ID("1"), result.getStudentId());
		assertEquals(new ID("2"), result.getClassId());
	}
}