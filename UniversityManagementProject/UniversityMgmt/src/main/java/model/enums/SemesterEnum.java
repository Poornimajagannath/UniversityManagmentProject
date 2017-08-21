package model.enums;


public enum SemesterEnum {

	SPRING("Spring"),
	SUMMER("Summer"),
	FALL("Fall");
	private String displayName;

	SemesterEnum(String displayName) {
		this.displayName = displayName;
	}

	public String getDisplayName() {
		return this.displayName;
	}

	public static SemesterEnum getByDisplayName(String semesterName) {
		SemesterEnum result = null;
		for(SemesterEnum semester: SemesterEnum.values()) {
			if(semester.getDisplayName().equals(semesterName)) {
				result = semester;
				break;
			}
		}
		return result;
	}
}
