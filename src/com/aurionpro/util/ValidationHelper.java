package com.aurionpro.util;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.regex.Pattern;

public class ValidationHelper {

	private static final Set<String> VALID_BLOOD_GROUPS = new HashSet<>(
			Arrays.asList("A+", "A-", "B+", "B-", "AB+", "AB-", "O+", "O-"));

	private static final Pattern EMAIL_PATTERN = Pattern.compile("^[\\w.-]+@[\\w.-]+\\.[A-Za-z]{2,}$");
	private static final Pattern MOBILE_PATTERN = Pattern.compile("^[6-9]\\d{9}$");

	public static boolean isValidEmail(String email) {
		return email != null && EMAIL_PATTERN.matcher(email).matches();
	}

	public static boolean isValidMobileNumber(String mobile) {
		return mobile != null && MOBILE_PATTERN.matcher(mobile).matches();
	}

	public static boolean isValidBloodGroup(String bloodGroup) {
		return bloodGroup != null && VALID_BLOOD_GROUPS.contains(bloodGroup.toUpperCase());
	}

	public static boolean isNonEmpty(String value) {
		return value != null && !value.trim().isEmpty();
	}

	public static boolean isPositiveSalary(double salary) {
		return salary > 0;
	}
}
