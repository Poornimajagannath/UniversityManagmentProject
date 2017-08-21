package model.enums;


public enum GradeEnum {
	A, B, C, D, F;

	public boolean passed() {
		return this != GradeEnum.F;
	}

	public boolean greaterThanD() {
		return this != F && this != D;
	}
}
