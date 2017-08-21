package service;

import manager.AssignmentsManager;
import manager.CourseManager;
import manager.InstructorsManager;
import manager.RecordsManager;
import manager.CourseRequestManager;
import manager.SemesterManager;
import manager.StudentsManger;
import model.Assignment;
import model.AssignmentResult;
import model.Course;
import model.Record;
import model.Semester;
import model.StatisticsSummarize;
import model.Student;
import model.identity.ID;
import model.requests.Request;
import utils.Configuration;

import java.util.List;
import java.util.Map;
import java.util.Queue;

/**
 * Main class to hold on the main logic of the system.
 * The UI controller is interacting with this class for all operations
 */
public class Administrator {

	/** DB connection service */
	private PersistenceService persistenceService;
	/** Managers */
	private SemesterManager semesterManager;
	private StudentsManger studentsManger;
	private InstructorsManager instructorsManager;
	private CourseManager courseManager;
	private RecordsManager recordsManager;
	private AssignmentsManager assignmentsManager;
	private CourseRequestManager courseRequestManager;

	/** Library connector */
	private WekaConnector wekaConnector;

	/**
	 * C'tor
	 *
	 * @param csvBasePath Base folder for CSV files
	 */
	public Administrator(String csvBasePath) {
		Configuration.getInstance().setCsvBasePath(csvBasePath);
		initializeDBConnection();
		connectToDb();
		initializeManagers();
	}

	/**
	 * Init method which initilize information regarding students, instructors,
	 * courses and records
	 */
	public void initialize() {
		connectToDb();
		clearAll();
		loadAllFromFile();
	}

	/**
	 * Designate the next semester
	 * This is calculated from the current semester
	 *
	 * @return The next semester
	 */
	public Semester designateSemester() {
		Semester designate = this.semesterManager.designate();
		clearAssignments();
		readAssignments();
		return designate;
	}

	/**
	 * Disconnect from DB and close the connection
	 */
	public void stop() {
		this.persistenceService.disconnect();
	}

	/**
	 * Resume method for the simulation. Can be called only after initlize
	 * and stop were called
	 * @return The Semester object which is will be used
	 */
	public Semester resume() {
		connectToDb();
		return designateSemester();
	}

	/**
	 * Select method for assignment selection
	 * @param selectedIndex
	 * @return
	 */
	public AssignmentResult select(int selectedIndex) {
		return this.assignmentsManager.select(selectedIndex);
	}

	/**
	 * Remove method for assignment remove
	 * @param selectedIndex
	 * @return
	 */
	public void remove(int selectedIndex) {
		this.assignmentsManager.remove(selectedIndex);
	}

	/**
	 * Getter for a list of instructor assingment which were selected by the user
	 * @return
	 */
	public Map<Integer, Assignment> getSelected() {
		return this.assignmentsManager.getSelected();
	}

	/**
	 * Getter for a list of instructor assingment which were not selected
	 * by the user
	 * @return
	 */
	public Map<Integer, Assignment> getNotSelected() {
		return this.assignmentsManager.getNotSelected();
	}

	/**
	 * Used to process the all the student-course requests
	 * Requests are read from the files from the given index
	 * @return List of all the requests accepted or rejected
	 */
	public List<Request> processRequests() {
		applyAssignments();
		int semesterId = semesterManager.getCurrent().getId();
		List<Request> result = this.courseRequestManager
				.processRequests(semesterId);
		return result;
	}

	/**
	 * Getter for all requests which they are on waiting list
	 * @return
	 */
	public Queue<Request> getWaitilingList() {
		return this.courseRequestManager.getWaitingList();
	}

	/**
	 * Getter for all records in the system
	 *
	 * @return
	 */
	public List<Record> getAllRecords() {
		return recordsManager.getAll();
	}

	/**
	 * Fetch all current records from DB, process them to it will match
	 * the structure for , and then send it to  algorithm
	 * to analyze the data
	 *
	 * @return String representation of the  output
	 */
	public String getAnalyzedData() {
		return this.wekaConnector.analyze();
	}

	/**
	 * Approves all the granted requests
	 */
	public void approveRequests() {
		this.courseRequestManager.approveRequests();
	}

	/**
	 * Try to fetch a Student for the given ID
	 *
	 * @param studentId The student's ID
	 * @return Student object
	 */
	public Student getStudent(ID studentId) {
		return this.studentsManger.get(studentId);
	}

	/**
	 * Retrieve a Course by its ID
	 *
	 * @param courseId The course ID.
	 * @return Course instance if successful, null otherwise
	 */
	public Course getCourse(ID courseId) {
		return this.courseManager.get(courseId);
	}

	/**
	 * Get the statistic history
	 *
	 * @return
	 */
	public StatisticsSummarize getHistory() {
		return courseRequestManager.getStatisticsHisotry();
	}

	/**
	 * Get the current semester statistics
	 *
	 * @return
	 */
	public StatisticsSummarize getCurrentStatistics() {
		int granted = courseRequestManager.getApprovedRequests().size();
		int rejected = courseRequestManager.getRejected().size();
		int waiting = courseRequestManager.getWaitingList().size();
		return new StatisticsSummarize(granted, rejected, waiting);

	}

	/**
	 * Returns all requests which were processed already
	 *
	 * @return
	 */
	public List<Request> getAllProcessed() {
		return this.courseRequestManager.getAllProcesed();
	}

	/**
	 * Returns the current semester
	 *
	 * @return
	 */
	public Semester getCurrentSemester() {
		return semesterManager.getCurrent();
	}

	/**
	 * Read assignment for the current semester
	 */
	private void readAssignments() {
		int semesterId = semesterManager.getCurrent().getId();
		this.assignmentsManager.readFromFile(semesterId);
	}

	/**
	 * Apply all selected assignments
	 */
	private void applyAssignments() {
		this.assignmentsManager.applyAssignments();
	}

	/**
	 * Clear all selected and not selected assignments
	 */
	private void clearAssignments() {
		this.assignmentsManager.clearAll();
	}

	/**
	 * Clear all stored information
	 */
	public void clearAll() {
		this.courseRequestManager.clearDb();
		this.courseRequestManager.clearStatistics();
		this.recordsManager.clear();
		this.assignmentsManager.clearAll();
		this.courseManager.clear();
		this.instructorsManager.clear();
		this.studentsManger.clear();
		this.semesterManager.clear();
	}

	/**
	 * Initialize all managers
	 */
	private void initializeManagers() {
		initializeSemesterManager();
		initializeStudentManager();
		initializeInstructorsManager();
		initializeCouresManager();
		initializeRecordsManager();
		initializeAssignmentManager();
		initializeRequestsManager();
		initializeWekaConnector();

	}

	/**
	 * Initialize the Weka connector
	 */
	private void initializeWekaConnector() {
		this.wekaConnector = new WekaConnector(this.recordsManager);
	}

	/**
	 * Initialize the assignment manager and its dependencies
	 */
	private void initializeAssignmentManager() {
		CourseAssignmentsService courseAssignmentsService = new CourseAssignmentsService(persistenceService);
		CourseInstructorAssignmentsService instructorAssignmentsService = new CourseInstructorAssignmentsService(persistenceService);
		this.assignmentsManager = new AssignmentsManager(courseAssignmentsService, instructorAssignmentsService, semesterManager);

	}

	/**
	 * Initialize the request manager and its dependencies
	 */
	private void initializeRequestsManager() {
		RequestsService requestsService = new RequestsService(
				persistenceService);
		StatisticsHistoryService historyService = new StatisticsHistoryService(persistenceService);
		this.courseRequestManager = new CourseRequestManager(this.courseManager,
				this.recordsManager, this.studentsManger, this.assignmentsManager, requestsService, historyService);
	}

	/**
	 * Initialize the record manager and its dependencies
	 */
	private void initializeRecordsManager() {
		RecordsService recordsService = new RecordsService(persistenceService);
		this.recordsManager = new RecordsManager(recordsService);
	}

	/**
	 * Load all initial information from files
	 */
	private void loadAllFromFile() {
		this.studentsManger.loadFromFile();
		this.instructorsManager.loadFromFile();
		this.courseManager.loadFromFile();
		this.recordsManager.loadFromFile();
	}

	/**
	 * Initialize the course manager and its dependencies
	 */
	private void initializeCouresManager() {
		CoursesService coursesService = new CoursesService(persistenceService);
		this.courseManager = new CourseManager(coursesService);
	}

	/**
	 * Initialize the instructor manager and its dependencies
	 */
	private void initializeInstructorsManager() {
		InstructorsService instructorsService = new InstructorsService(
				persistenceService);
		this.instructorsManager = new InstructorsManager(instructorsService);
	}

	/**
	 * Initialize the student manager and its dependencies
	 */
	private void initializeStudentManager() {
		StudentsService studentsService = new StudentsService(
				persistenceService);
		this.studentsManger = new StudentsManger(studentsService);
	}

	/**
	 * Initialize the semester manager and its dependencies
	 */
	private void initializeSemesterManager() {
		SemesterService semesterService = new SemesterService(
				persistenceService);
		this.semesterManager = new SemesterManager(semesterService);
	}

	/**
	 * Initialize the main DB service for connection
	 */
	private void initializeDBConnection() {
		persistenceService = new PersistenceService();
	}

	/**
	 * Open the connection to DB
	 */
	private void connectToDb() {
		persistenceService.connect();
	}

}
