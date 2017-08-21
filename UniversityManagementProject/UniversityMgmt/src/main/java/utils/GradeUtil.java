package utils;

import model.enums.GradeEnum;

import java.util.Random;

public class GradeUtil {

	private static Random random = new Random();

	/**
	 * Generates a random grade based on:
	 * prob(A) = 0.30, prob(B) = 0.40, prob(C) = 0.10, prob(D) = 0.10,
	 * and prob(F) = 0.10
	 * Using the Random to get random number between [0,9]
	 * @return
	 */
	public static GradeEnum generateGrade() {
		GradeEnum result;
		int number = random.nextInt(10);
		if (number <= 2) {
			result = GradeEnum.A;
		} else if (number >= 3 && number <= 6) {
			result = GradeEnum.B;
		} else if (number == 7) {
			result = GradeEnum.C;
		} else if (number == 8) {
			result = GradeEnum.D;
		} else {
			result = GradeEnum.F;
		}
		return result;
	}

}
