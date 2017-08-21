package model;

public enum AssignmentResult {

	SUCCESS("instructor selected!"),
	ALREADY_SELECTED("instructor already selected to teach a course"),
	NO_AVAILABLE("all instructors selected - no available hiring positions");


	private String message;

	AssignmentResult(String message) {
		this.message = message;
	}

	public String getMessage() {
		return this.message;
	}

}
