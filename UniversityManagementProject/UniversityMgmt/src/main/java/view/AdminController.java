package view;

import model.Assignment;
import model.Course;
import model.Record;
import model.Semester;
import model.StatisticsSummarize;
import model.Student;
import model.requests.Request;
import service.Administrator;

import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class AdminController {

	private Administrator administrator ;

	public AdminController(String csvBasePath) {
		this.administrator = new Administrator(csvBasePath);
	}

	public void run(String startMode) {
		Semester semester;
		if (startMode.equals("initial")) {
			administrator.initialize();
			semester = administrator.designateSemester();
		} else if (startMode.equals("resume")) {
			semester = administrator.resume();

		} else if (startMode.equals("stop")) {
			administrator.stop();
			return;
		} else {
			System.out.println("Illegal input");
			return;
		}

		Scanner scanner = new Scanner(System.in);

		//add  here
		System.out.println(
				"beginning instructor selections for Semester " + semester
						.getId());
		System.out.println(administrator.getAnalyzedData());
		while (true) {

			// print statistics

			System.out.print("$roster selection > ");

			String[] values = (scanner.nextLine().split(","));

			String operation = values[0].trim();
			if ("add".equalsIgnoreCase(operation)) {
				String value = values[1].trim();
				System.out.println(administrator.select(Integer.parseInt(value))
						.getMessage());
			} else if ("delete".equalsIgnoreCase(operation)) {
				String value = values[1].trim();
				administrator.remove(Integer.parseInt(value));
				System.out.println("instructor released!");
			} else if ("display".equalsIgnoreCase(operation)) {
				displayAssignmentSelection();
			} else if ("submit".equalsIgnoreCase(operation)) {
				System.out.println(
						"selections finalized - now processing requests for Semester "
								+ semester.getId());
				printRequestsProcessingResult();
				System.out.println("$continue simulation? [yes/no]: ");
				String asnwer = scanner.next();
				if (asnwer.equals("no")) {
					System.out.println("simulation halted");
					break;
				} else {
					System.out.println("association analysis of previous course selections");
					semester = administrator.designateSemester();
					System.out.println(administrator.getAnalyzedData());
					System.out.println(
							"beginning instructor selections for Semester "
									+ semester.getId());
					scanner.nextLine();
				}
			}
		}

		administrator.stop();
	}

	private void displayAssignmentSelection() {
		System.out.println("%--- selected ---");
		displayAssignment(administrator.getSelected());
		System.out.println("%--- unselected ---");
		displayAssignment(administrator.getNotSelected());
	}

	private void displayAssignment(Map<Integer, Assignment> assignments) {
		if (assignments.size() > 0) {
			assignments.entrySet()
					.forEach(entry -> printAssignmentEntry(entry));
		}
	}

	private void printAssignmentEntry(Map.Entry<Integer, Assignment> entry) {
		Assignment assignment = entry.getValue();
		StringBuilder builder = new StringBuilder().append(entry.getKey())
				.append(": ").append(assignment.getInstructorId().toString())
				.append(", ").append(assignment.getClassId().toString())
				.append(", ").append(assignment.getCapacity());
		System.out.println(builder.toString());
	}

	private void printRequestsProcessingResult() {

		List<Request> requests = administrator.processRequests();
		printEmptyLine();
		System.out.println("Processed Requests");
		for (Request request : requests)
			System.out.println(
					"request (" + request.getStudentId().toString() + ", " + ""
							+ request.getClassId().toString() + "): " + request
							.getStatusMessage());
		administrator.approveRequests();

		printEmptyLine();
		StatisticsSummarize currentStatistics = administrator
				.getCurrentStatistics();
		System.out.println("Semester Statistics");
		printStatistics(currentStatistics);
		System.out.println("Total Statistics");
		StatisticsSummarize history = administrator.getHistory();
		printStatistics(history);

		List<Record> records = administrator.getAllRecords();
		printEmptyLine();
		printRecords(records);

		printEmptyLine();
		printWaitingList();
	}

	private void printWaitingList() {
		System.out.println("WaitListed Requests");
		for (Request request : administrator.getWaitilingList()) {

			Student student = administrator.getStudent(request.getStudentId());
			Course course = administrator.getCourse(request.getClassId());
			StringBuilder builder = new StringBuilder();
			builder.append(student.getId()).append(", ")
					.append(student.getFirstName().toUpperCase()).append(" ")
					.append(student.getLastName().toUpperCase()).append(", ")
					.append(course.getId()).append(", ")
					.append(course.getName());
			System.out.println(builder.toString());
		}
	}

	private void printRecords(List<Record> records) {
		System.out.println("Academic Records");
		for (Record record : records)
			System.out.println(record.getStudentId().toString() + ", " + record
					.getClassId().toString() + ", " + record.getInstructorId()
					.toString() + ", " + record.getComments() + ", " + record
					.getGrade());
	}

	private void printStatistics(StatisticsSummarize currentStatistics) {
		StringBuilder builder = new StringBuilder();
		builder.append("Examined: ")
				.append(currentStatistics.getTotal())
				.append(" Granted: ")
				.append(currentStatistics.getGranted())
				.append(" Failed: ")
				.append(currentStatistics.getFailed())
				.append(" Wait Listed: ")
				.append(currentStatistics.getWait());
		System.out.println(builder.toString());
	}

	private void printEmptyLine() {
		System.out.println();
	}

}
