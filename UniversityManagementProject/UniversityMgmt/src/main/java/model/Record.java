package model;

import model.enums.GradeEnum;
import model.identity.ID;

public class Record {

	/* The student id whic the given record belongs to */
	private final ID studentId;
	/* The class id whic the given record belongs to */
	private final ID classId;
	/* The instructor id whic the given record belongs to */
	private final ID instructorId;
	/* The comment which was made by the instructor */
	private String comments;
	/* The grade which was made by the instructor */
	private GradeEnum grade;

	/**
	
	 * @param studentId
	 * @param classId
	 * @param instructorId
	 * @param comments
	 * @param grade
	 */
	public Record(final ID studentId, final ID classId, final ID instructorId,
			String comments, GradeEnum grade) {
		this.studentId = studentId;
		this.classId = classId;
		this.instructorId = instructorId;
		this.comments = comments;
		this.grade = grade;
	}

	public ID getStudentId() {
		return studentId;
	}

	public ID getClassId() {
		return classId;
	}

	public ID getInstructorId() {
		return instructorId;
	}

	public String getComments() {
		return comments;
	}

	public GradeEnum getGrade() {
		return grade;
	}

	public static void displayRecord(Record record) {
		StringBuilder builder = new StringBuilder();
		builder.append(record.getStudentId())
				.append(", ")
				.append(record.getClassId())
				.append(", ")
				.append(record.getInstructorId())
				.append(", ")
				.append(record.getComments())
				.append(", ")
				.append(record.getGrade());
		System.out.println(builder.toString());

	}
	@Override
	public boolean equals(Object obj) {
		boolean result = false;
		if (obj instanceof Record) {
			Record other = (Record) obj;
			result = other.getStudentId().equals(this.getStudentId())
					&& other.getInstructorId().equals(this.getInstructorId())
					&& other.getClassId().equals(this.getClassId());
		}
		return result;
	}

	public void setGrade(GradeEnum grade) {
		this.grade = grade;
	}
}
