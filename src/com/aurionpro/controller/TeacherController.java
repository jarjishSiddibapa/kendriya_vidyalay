package com.aurionpro.controller;

import java.sql.Connection;
import java.sql.Date;
import java.util.List;
import java.util.Scanner;

import com.aurionpro.dao.TeacherDAO;
import com.aurionpro.model.Teacher;
import com.aurionpro.model.TeacherProfile;
import com.aurionpro.util.Printer;
import com.aurionpro.util.ValidationHelper;

public class TeacherController {
	private final Connection connection;
	private final Scanner scanner;
	private final TeacherDAO teacherDAO;

	public TeacherController(Connection connection, Scanner scanner, TeacherDAO teacherDAO) {
		this.connection = connection;
		this.scanner = scanner;
		this.teacherDAO = teacherDAO;
	}

	public void start() {
		while (true) {
			Printer.printMenu("Teacher Menu", List.of("1> Add teacher", "2> Show all teachers", "3> Assign subject",
					"4> Show teacher's subjects", "5> Search teacher by ID", "6> Delete teacher", "7> Back"));

			Printer.printPrompt("Enter your choice:");
			int choice = 0;
			try {
				choice = Integer.parseInt(scanner.nextLine());
			} catch (Exception exception) {
				Printer.printErrorMessage("Please enter a valid number.");
				continue;
			}

			try {
				switch (choice) {
				case 1 -> addTeacher();
				case 2 -> showAllTeachers();
				case 3 -> assignSubject();
				case 4 -> showSubjects();
				case 5 -> searchTeacherById();
				case 6 -> deleteTeacher();
				case 7 -> {
					return;
				}
				default -> Printer.printErrorMessage("Invalid choice!");
				}
			} catch (Exception exception) {
				Printer.printErrorMessage("Error: " + exception.getMessage());
			}
		}
	}

	private void addTeacher() throws Exception {
		Printer.printHeader("Add New Teacher");

		String name, mobileNumber, dateOfBirthString, city, email, alternateNumber, bloodGroup;
		double salary;

		do {
			Printer.printPrompt("Enter name:");
			name = scanner.nextLine();
		} while (!ValidationHelper.isNonEmpty(name));

		do {
			Printer.printPrompt("Enter mobile number (10 digits starting 6-9):");
			mobileNumber = scanner.nextLine();
			if (!ValidationHelper.isValidMobileNumber(mobileNumber)) {
				Printer.printErrorMessage("Invalid mobile number format!");
				mobileNumber = "";
			}
		} while (mobileNumber.isEmpty());

		Date dateOfBirth = null;
		do {
			Printer.printPrompt("Enter date of birth (YYYY-MM-DD):");
			dateOfBirthString = scanner.nextLine();
			try {
				dateOfBirth = Date.valueOf(dateOfBirthString);
			} catch (Exception exception) {
				Printer.printErrorMessage("Invalid date! Please use YYYY-MM-DD format.");
			}
		} while (dateOfBirth == null);

		do {
			Printer.printPrompt("Enter salary (positive number):");
			try {
				salary = Double.parseDouble(scanner.nextLine());
			} catch (Exception exception) {
				salary = -1;
			}
			if (!ValidationHelper.isPositiveSalary(salary)) {
				Printer.printErrorMessage("Salary must be greater than 0.");
			}
		} while (!ValidationHelper.isPositiveSalary(salary));

		Printer.printHeader("Teacher Profile Details");

		do {
			Printer.printPrompt("Enter city:");
			city = scanner.nextLine();
		} while (!ValidationHelper.isNonEmpty(city));

		do {
			Printer.printPrompt("Enter email:");
			email = scanner.nextLine();
			if (!ValidationHelper.isValidEmail(email)) {
				Printer.printErrorMessage("Invalid email format!");
				email = "";
			}
		} while (email.isEmpty());

		Printer.printPrompt("Enter [Optional] alternate number (or hit Enter):");
		alternateNumber = scanner.nextLine();
		if (!alternateNumber.trim().isEmpty() && !ValidationHelper.isValidMobileNumber(alternateNumber)) {
			Printer.printErrorMessage("Alternate number is not valid--ignoring it.");
			alternateNumber = "";
		}

		do {
			Printer.printPrompt("Enter blood group (A+, A-, B+, B-, AB+, AB-, O+, O-):");
			bloodGroup = scanner.nextLine().toUpperCase();
			if (!ValidationHelper.isValidBloodGroup(bloodGroup)) {
				Printer.printErrorMessage("Invalid blood group.");
				bloodGroup = "";
			}
		} while (bloodGroup.isEmpty());

		Teacher teacher = new Teacher(0, name, mobileNumber, dateOfBirth, salary, true, null, null);
		TeacherProfile teacherProfile = new TeacherProfile(0, city, email,
				alternateNumber.isEmpty() ? null : alternateNumber, bloodGroup, 0, true);

		try {
			if (teacherDAO.addTeacherWithProfile(teacher, teacherProfile)) {
				Printer.printSuccessMessage("Teacher added successfully with profile.");
			} else {
				Printer.printErrorMessage("Error adding teacher (possible duplicate mobile/email).");
			}
		} catch (Exception exception) {
			Printer.printErrorMessage("Could not save teacher. " + exception.getMessage());
		}
	}

	private void showAllTeachers() throws Exception {
		List<Teacher> teacherList = teacherDAO.getAllTeachers();
		Printer.printHeader("All Teachers");
		Printer.printTable(teacherList);
	}

	private void assignSubject() throws Exception {
		Printer.printHeader("Assign Subject to Teacher");

		int teacherId = readPositiveInt("Enter teacher ID: ");
		int subjectId = readPositiveInt("Enter subject ID: ");

		if (teacherDAO.assignSubject(teacherId, subjectId)) {
			Printer.printSuccessMessage("Subject assigned to teacher.");
		} else {
			Printer.printErrorMessage("Invalid teacher/subject, not active, or already assigned.");
		}
	}

	private void showSubjects() throws Exception {
		Printer.printHeader("Show Teacher's Subjects");

		int teacherId = readPositiveInt("Enter teacher ID: ");
		List<String> subjects = teacherDAO.getSubjectsByTeacherId(teacherId);
		if (!subjects.isEmpty()) {
			Printer.printInfoMessage("Subjects taught by this teacher:");
			for (String subjectName : subjects) {
				System.out.println("- " + subjectName);
			}
		} else {
			Printer.printInfoMessage("No subjects assigned or teacher not found.");
		}
	}

	private void searchTeacherById() throws Exception {
		Printer.printHeader("Search Teacher By ID");

		int teacherId = readPositiveInt("Enter teacher ID: ");
		Teacher teacher = teacherDAO.getTeacherById(teacherId);
		if (teacher != null) {
			Printer.printTeacher(teacher);
		} else {
			Printer.printInfoMessage("Teacher not found.");
		}
	}

	private void deleteTeacher() throws Exception {
		Printer.printHeader("Delete Teacher (Soft Delete)");

		int teacherId = readPositiveInt("Enter teacher ID to delete: ");
		if (teacherDAO.softDeleteTeacher(teacherId)) {
			Printer.printSuccessMessage("Teacher deleted (soft delete).");
		} else {
			Printer.printErrorMessage("Teacher not found or already deleted.");
		}
	}

	private int readPositiveInt(String prompt) {
		int value = -1;
		do {
			try {
				Printer.printPrompt(prompt);
				value = Integer.parseInt(scanner.nextLine());
			} catch (Exception exception) {
				value = -1;
			}
			if (value <= 0) {
				Printer.printErrorMessage("Value must be a positive integer.");
			}
		} while (value <= 0);
		return value;
	}
}
