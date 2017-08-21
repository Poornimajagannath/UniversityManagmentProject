package utils.parsers;

import model.Course;
import model.identity.ID;
import model.enums.SemesterEnum;

public class CourseParser implements IParser<Course> {

	@Override
	public Course parse(String data) {
		String[] details = data.split(",");
		Course course = new Course(new ID(details[0]), details[1], "");
		for (int i = 2; i < details.length; i++) {
			SemesterEnum semester = SemesterEnum.getByDisplayName(details[i]);
			if (semester != null) {
				course.addSemester(semester);
			}
		}
		return course;

	}

}
