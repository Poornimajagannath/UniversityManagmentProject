package utils.parsers;

import java.util.HashMap;
import java.util.Map;

public enum ParserEnum {

	STUDENT, INSTRUCTOR, COURSE, RECORD, ASSIGNMENT, PREREQ, REQUEST;

	private static Map<ParserEnum, IParser> parsers = new HashMap<>();

	static {
		parsers.put(STUDENT, new StudentParser());
		parsers.put(INSTRUCTOR, new InstructorParser());
		parsers.put(COURSE, new CourseParser());
		parsers.put(RECORD, new RecordParser());
		parsers.put(ASSIGNMENT, new AssignmentParser());
		parsers.put(PREREQ, new PrerequisitesParser());
		parsers.put(REQUEST, new RequestMetadataParser());
	}

	public <T> T parse(String data) {
		return (T)parsers.get(this).parse(data);
	}
}
