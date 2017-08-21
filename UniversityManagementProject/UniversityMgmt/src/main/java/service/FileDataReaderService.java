package service;

import model.Assignment;
import model.Course;
import model.Instructor;
import model.Prerequisite;
import model.Record;
import model.Student;
import model.requests.RequestMetadataDetails;
import utils.Configuration;
import utils.InputReader;
import utils.parsers.ParserEnum;

import java.util.List;

public class FileDataReaderService {

	private String studentsFilePath;
	private String instructorsFilePath;
	private String coursesFilePath;
	private String recordsFilePath;
	private String prereqsFilePath;
	private String assignmentsBaseFilePath;
	private String requestsBaseFilePath;

	public FileDataReaderService() {
		this.studentsFilePath = Configuration.getInstance().getCsvBasePath() + "/students.csv";
		this.instructorsFilePath = Configuration.getInstance().getCsvBasePath()  + "/instructors.csv";
		this.coursesFilePath = Configuration.getInstance().getCsvBasePath()  + "/courses.csv";
		this.recordsFilePath = Configuration.getInstance().getCsvBasePath() + "/records.csv";
		this.prereqsFilePath = Configuration.getInstance().getCsvBasePath() + "/prereqs.csv";
		this.assignmentsBaseFilePath = Configuration.getInstance().getCsvBasePath() + "/assignments_";
		this.requestsBaseFilePath = Configuration.getInstance().getCsvBasePath() + "/requests_";
	}

	public List<RequestMetadataDetails> readRequests(int index) {
		StringBuilder builder = new StringBuilder(requestsBaseFilePath);
		builder.append(index).append(".csv");
		return InputReader.read(ParserEnum.REQUEST, builder.toString());
	}

	public List<Assignment> readAssignments(int index) {
		StringBuilder builder = new StringBuilder(assignmentsBaseFilePath);
		builder.append(index).append(".csv");
		return InputReader.read(ParserEnum.ASSIGNMENT, builder.toString());
	}

	public List<Prerequisite> readPrereq() {
		return InputReader.read(ParserEnum.PREREQ, prereqsFilePath);
	}

	public List<Record> readRecords() {
		return InputReader.read(ParserEnum.RECORD, recordsFilePath);
	}

	public List<Course> readCourses() {
		return InputReader.read(ParserEnum.COURSE, coursesFilePath);
	}

	public List<Student> readStudents() {
		return InputReader.read(ParserEnum.STUDENT, studentsFilePath);
	}

	public List<Instructor> readInstructors() {
		return InputReader.read(ParserEnum.INSTRUCTOR, instructorsFilePath);
	}

}
