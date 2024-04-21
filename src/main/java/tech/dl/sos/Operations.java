package tech.dl.sos;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class Operations {

	// This is our calculation, takes double type number, calculates logarithm,
	// then sine and then square root
	public static double calculate(double value) {
		return Math.sqrt(Math.sin(Math.log(value)));
	}

}
