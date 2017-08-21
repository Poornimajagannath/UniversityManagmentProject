package manager;

import model.Course;
import model.Record;
import model.StatisticsSummarize;
import model.enums.GradeEnum;
import model.requests.Request;
import model.enums.RequestStatusEnum;
import model.identity.ID;
import model.requests.RequestMetadataDetails;
import service.FileDataReaderService;
import service.RequestsService;
import service.StatisticsHistoryService;
import utils.GradeUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Set;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.stream.Collectors;

/**
 * Class to manage request and to check requests
 * <p>
 */
public class CourseRequestManager {

	private static Predicate<GradeEnum> graterThanD = grade -> grade.greaterThanD();
	private static Predicate<GradeEnum> passedFilter = grade -> grade.passed();

	private final RecordsManager recordsManager;
	private final CourseManager courseManager;
	private final StudentsManger studentsManger;
	private final AssignmentsManager assignmentsManager;
	private final RequestsService requestsService;
	private final StatisticsHistoryService historyService;
	/**
	 * List of all approved requests
	 */
	private List<Request> approvedRequests = new ArrayList<>();
	private Queue<Request> waitilingList  = new LinkedList<>();
	private List<Request> rejected = new ArrayList<>();
	private List<Request> all = new ArrayList<>();

	/**
	 * Messages supliers
	 */
	private static final Supplier<String> alreadyTaken = () -> "student has already taken the course with a grade of C or higher";
	private static final Supplier<String> missingPrereq = () -> "student is missing one or more prerequisites";
	private static final Supplier<String> classFull = () -> "no remaining seats available for the course at this time";
	private static final Supplier<String> valid = () -> "request is valid";

	/**
	 * Default Counter. Depended on other managers
	 *
	 * @param courseManager
	 * @param recordsManager
	 * @param studentsManger
	 */
	public CourseRequestManager(final CourseManager courseManager,
			final RecordsManager recordsManager,
			final StudentsManger studentsManger,
			final AssignmentsManager assignmentsManager,
			final RequestsService requestsService,
			final StatisticsHistoryService historyService) {
		this.recordsManager = recordsManager;
		this.courseManager = courseManager;
		this.studentsManger = studentsManger;
		this.assignmentsManager = assignmentsManager;
		this.requestsService = requestsService;
		this.historyService = historyService;
	}

	/**
	 * Used to process the all the student-course requests
	 * Requests are read from the files from the given index
	 * @param index Starting point of the File read
	 * @return List of all the requests accepted or rejected
	 */
	public List<Request> processRequests(int index) {
		
		saveStatistics();
		clear();
		List<Request> result = new ArrayList<>();
		Queue<RequestMetadataDetails> allWaiting = requestsService.getAll();
		
		clearDb();
		FileDataReaderService reader = new FileDataReaderService();

		List<RequestMetadataDetails> requests = reader
				.readRequests(index);
		requests.forEach(requestMetadataDetails -> allWaiting.add(requestMetadataDetails));
		while(!allWaiting.isEmpty()) {
			RequestMetadataDetails request = allWaiting.poll();
			Request newRequest = addRequest(request.getStudentId(),
					request.getClassId());
			if(newRequest.getStatus().equals(RequestStatusEnum.ACCEPTED)) {
				approvedRequests.add(newRequest);
				updateCourseTakenSeats(newRequest.getClassId());
			} else if(newRequest.getStatus().equals(RequestStatusEnum.REJECTED_CAPACITY)) {
				waitilingList.add(newRequest);
			} else {
				rejected.add(newRequest);
			}
			result.add(newRequest);
		}
		saveWaitlistToDb();
		all.addAll(result);
		return result;
	}

	public List<Request> getAllProcesed() {
		return this.all;
	}

	public Queue<Request> getWaitingList() {
		return this.waitilingList;
	}

	public void clear() {
		this.approvedRequests.clear();
		this.waitilingList.clear();
		this.rejected.clear();
		this.all.clear();
	}

	/**
	 * Triggers the clear for requestsWaitlist table
	 */
	public void clearDb() {
		this.requestsService.clearAll();
	}

	/**
	 * Clears all statistic history
	 */
	public void clearStatistics() {
		this.historyService.clearAll();
	}

	/**
	 * Approves all the granted requests
	 */
	public void approveRequests() {

		Map<ID, List<Record>> newRecordsForStudent = new HashMap<>();

		for (Request request : approvedRequests) {
			// Create a new record
			Record newRecord = createRecord(request);
			List<Record> records = newRecordsForStudent
					.get(request.getStudentId());
			if(records == null) {
				records = new ArrayList<>();
			}
			// Update the grade
			calculateGrade(newRecord);
			records.add(newRecord);
			newRecordsForStudent.put(request.getStudentId(), records);
		}

		// Add to all records
		newRecordsForStudent.values()
				.stream()
				.flatMap(List::stream)
				.forEach(record -> recordsManager.add(record));
	}

	/**
	 * Getter for approvedRequests
	 * @return
	 */
	public List<Request> getApprovedRequests() {
		return approvedRequests;
	}

	/**
	 * Getter for rejected
	 *
	 * @return
	 */
	public List<Request> getRejected() {
		return rejected;
	}

	/**
	 * Returns StatisticsSummarize contains hisotry information for requests
	 *
	 * @return
	 */
	public StatisticsSummarize getStatisticsHisotry() {
		return getTotalStatistics();
	}

	/**
	 * Operation to generate a random grade and set it on the record
	 *
	 * @param record Updated record instance
	 */
	private void calculateGrade(Record record) {
		GradeEnum newGrade = GradeUtil.generateGrade();
		record.setGrade(newGrade);
	}

	/**
	 * Create a new record entry in the system
	 * Uses the request information to create a new academic record
	 *
	 * @param request This is the approved student course request
	 * @return The newly created record instance
	 */
	private Record createRecord(Request request) {
		ID studentId = request.getStudentId();
		ID courseId = request.getClassId();
		List<ID> instructors = assignmentsManager.getInstructorsForCurrentSemester(courseId);
		ID instructorId = instructors.get(0);
		String comment = "random comment";
		return new Record(studentId, courseId,instructorId, comment, GradeEnum.F);
	}

	/**
	 * Adds a new request for a given student and a classId
	 *
	 * @param studentId The student ID
	 * @param classId   The class ID
	 * @return Request object, null in case of error
	 */
	private Request addRequest(final ID studentId, final ID classId) {
		validateInput(studentId, classId);
		Request newRequest = null;
		newRequest = containsInApprovedList(studentId, classId);
		if (newRequest == null) {

			List<Record> existingRecords = recordsManager
					.getForStudentAndClass(studentId, classId);
			if (existingRecords != null && existingRecords.size() > 0) {
				boolean canRetake = canRetake(studentId, classId);

				if (canRetake) {
					newRequest = createNewRequest(studentId, classId);
				} else {
					RequestMetadataDetails metadata = new RequestMetadataDetails(
							studentId, classId);
					newRequest = new Request(metadata,
							RequestStatusEnum.REJECT_ALREADY_TAKEN,
							alreadyTaken);
				}
			} else {
				newRequest = createNewRequest(studentId, classId);
			}
		}
		return newRequest;
	}

	/**
	 * Check if the given student and class have a approved request entry
	 *
	 * @param studentId The student's ID
	 * @param classId   The class's ID
	 * @return The requests if such exists, null otherwise
	 */
	private Request containsInApprovedList(final ID studentId,
			final ID classId) {
		return approvedRequests.stream()
				.filter(req -> req.getStudentId().equals(studentId) && req
						.getClassId().equals(classId)).findAny().orElse(null);
	}

	/**
	 * Creates a new request for the given student and class
	 * Checks whether the request is valid or not
	 *
	 * @param studentId The student's ID
	 * @param classId   The class's ID
	 * @return The new Request instance
	 */
	private Request createNewRequest(final ID studentId, final ID classId) {
		Request newRequest;
		ID missingPreq = getMissingPreq(studentId, classId);
		RequestMetadataDetails metadata = new RequestMetadataDetails(studentId,
				classId);
		if (missingPreq == null) {
			Course course = courseManager.get(classId);
			boolean available = checkAvailableSeats(course);
			if (available) {
				newRequest = new Request(metadata, RequestStatusEnum.ACCEPTED,
						valid);
			} else {
				newRequest = new Request(metadata,
						RequestStatusEnum.REJECTED_CAPACITY, classFull);
			}
		} else { // found missing preq
			newRequest = new Request(metadata,
					RequestStatusEnum.REJECTED_PREREQUISITE, missingPrereq);

		}
		return newRequest;
	}

	/**
	 * For a given course check if there are seats available for granting a request
	 *
	 * @param course The Course to be checked for availability
	 * @return true if there is at least one seat available, false otherwise
	 */
	private boolean checkAvailableSeats(Course course) {
		return assignmentsManager.checkAvailableSeats(course.getId());
	}

	/**
	 * Search for missing preq for the given student and for the given class
	 *
	 * @param studentId The student's ID
	 * @param classId   The class's ID
	 * @return ID if a prerequisite for the given class is missing for the give
	 * student, null otherwise
	 */
	private ID getMissingPreq(final ID studentId, final ID classId) {
		Course course = courseManager.get(classId);

		ID missingPreq = null;
		if (course.hasPrerequisites()) {
			Set<ID> prerequisites = course.getPrerequisites();
			// Map<ClassId, Record>
			Map<ID, List<Record>> forStudent = recordsManager
					.getForStudent(studentId);

			Predicate<ID> tookCourse = id -> forStudent.containsKey(id);
			Predicate<ID> didntTakeCourse = tookCourse.negate();

			Predicate<ID> passedAtleastOne = id -> hasPassedGrade(forStudent.get(id));
			Predicate<ID> didntPassOneTime = passedAtleastOne.negate();

			Predicate<ID> tookButDidntPass = tookCourse.and(didntPassOneTime);

			missingPreq = prerequisites
					.stream()
					.filter(didntTakeCourse.or(tookButDidntPass))
					.findFirst()
					.orElse(null);
		}
		return missingPreq;
	}

	/**
	 * Checks if the given list contains atleast one passed test
	 *
	 * @param records
	 * @return
	 */
	private boolean hasPassedGrade(List<Record> records) {
		long numberOfPassedGrades = records.stream().map(record -> record.getGrade())
				.filter(passedFilter).count();
		return numberOfPassedGrades > 0;
	}

	/**
	 * Checks if the given student can retake the given class
	 *
	 * @param studentId The student's ID
	 * @param classId   The class's ID
	 * @return True if the given student can retake, flase otherwise
	 */
	private boolean canRetake(final ID studentId, final ID classId) {
		List<Record> records = recordsManager.getForStudentAndClass(studentId, classId);
		boolean result = true;
		if (records != null) {
			result = !hasGradesGreaterThanD(records);
		}
		return result;
	}

	/**
	 * Checks if the the given exists contains a record with a grade
	 * greater than D.
	 *
	 * @param records
	 * @return
	 */
	private boolean hasGradesGreaterThanD(List<Record> records) {

		long numberOfGradesGreaterThanD =
				records.stream()
						.map(record -> record.getGrade())
						.filter(graterThanD)
						.count();
		return numberOfGradesGreaterThanD > 0;
	}

	/**
	 * Update the given class with the given student.
	 * Update the number of taken seats for a course in the given semester
	 *
	 * @param courseId   The class's ID
	 */
	private void updateCourseTakenSeats(final ID courseId) {
		assignmentsManager.updateTakenSeats(courseId);

	}

	/**
	 * Validate the input of studentId and classId
	 * In case one of them is missing in the system,
	 * IllegalArgumentException exception will be thrown
	 *
	 * @param studentId The student's ID
	 * @param classId   The class's ID
	 * @throws IllegalArgumentException In case the above are not llegal in
	 *                                  the system
	 */
	private void validateInput(final ID studentId, final ID classId)
			throws IllegalArgumentException {
		if (studentsManger.get(studentId) == null
				|| courseManager.get(classId) == null) {
			throw new IllegalArgumentException("Illegal ids");
		}
	}

	/**
	 * Save the requests that were valid but not granted in the current semester
	 */
	private void saveWaitlistToDb() {
		waitilingList.forEach(request -> requestsService.add(request.getMetadata()));
	}

	/**
	 * Update the history statistics
	 */
	private void saveStatistics() {
		StatisticsSummarize newHisotry = getTotalStatistics();
		historyService.add(newHisotry);
	}

	/**
	 * Counts the total statistics: current + history
	 *
	 * @return
	 */
	private StatisticsSummarize getTotalStatistics() {
		StatisticsSummarize history = historyService.getHistory();
		int granted = 0;
		int rejected = 0;
		int waiting = 0;
		if (history != null) {
			granted += history.getGranted();
			rejected += history.getFailed();
			waiting += history.getWait();
		}
		granted += getApprovedRequests().size();
		rejected += getRejected().size();
		waiting += getWaitingList().size();
		return new StatisticsSummarize(granted,
				rejected, waiting);
	}

}
