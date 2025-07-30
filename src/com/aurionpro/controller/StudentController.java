package com.aurionpro.controller;

import com.aurionpro.dao.StudentDao;
import com.aurionpro.database.DBManager;
import com.aurionpro.model.Student;
import com.aurionpro.model.StudentProfile;
import com.aurionpro.util.Printer;
import com.aurionpro.util.ValidationHelper;

import java.sql.Connection;
import java.sql.Date;
import java.util.Scanner;

public class StudentController {

	private final StudentDao studentDao;
	private final Scanner scanner;

	public StudentController(Connection connection, Scanner scanner, DBManager dbManager) {
		this.studentDao = new StudentDao(connection, dbManager);
		this.scanner = scanner;
	}

	public void start() {
		while (true) {
			System.out.println("\n========== Student Management ==========");
			System.out.println("1> Add new student");
			System.out.println("2> Show all students");
			System.out.println("3> Search a student by ID");
			System.out.println("4> Delete a student by ID");
			System.out.println("5> Assign a course to student");
			System.out.println("6> View assigned courses");
			System.out.println("7> Exit");
			System.out.print("Enter your choice: ");

			String input = scanner.nextLine();
			int choice;
			try {
				choice = Integer.parseInt(input.trim());
			} catch (NumberFormatException e) {
				System.out.println("Invalid number format.");
				continue;
			}

			switch (choice) {
			case 1:
				addNewStudentWithProfile();
				break;
			case 2:
				studentDao.showAllStudents();
				break;
			case 3:
				searchStudentById();
				break;
			case 4:
				deleteStudentById();
				break;
			case 5:
				assignCourseToStudent();
				break;
			case 6:
				viewCoursesOfStudent();
				break;
			case 7:
				System.out.println("Exiting Student Management...");
				return;
			default:
				System.out.println("Invalid choice.");
			}
		}
	}

	private void addNewStudentWithProfile() {
		try {
			System.out.print("Enter student name: ");
			String name = scanner.nextLine().trim();

			System.out.print("Enter mobile number (10 digits starting with 6-9): ");
			String mobile = scanner.nextLine().trim();

			System.out.print("Enter date of birth (YYYY-MM-DD): ");
			String dobStr = scanner.nextLine().trim();

			if (!ValidationHelper.isNonEmpty(name)) {
				System.out.println("Name must not be empty.");
				return;
			}
			if (!ValidationHelper.isValidMobileNumber(mobile)) {
				System.out.println("Invalid mobile number format.");
				return;
			}

			Date dob;
			try {
				dob = Date.valueOf(dobStr);
			} catch (Exception e) {
				System.out.println("Invalid date format.");
				return;
			}

			System.out.print("Enter city: ");
			String city = scanner.nextLine().trim();

			System.out.print("Enter email: ");
			String email = scanner.nextLine().trim();

			System.out.print("Enter guardian's mobile number: ");
			String guardianNumber = scanner.nextLine().trim();

			System.out.print("Enter blood group (A+, A-, B+, B-, AB+, AB-, O+, O-): ");
			String bloodGroup = scanner.nextLine().trim();

			if (!ValidationHelper.isValidEmail(email)) {
				System.out.println("Invalid email format.");
				return;
			}
			if (!ValidationHelper.isValidBloodGroup(bloodGroup)) {
				System.out.println("Invalid blood group.");
				return;
			}

			Student student = new Student(name, mobile, dob);
			StudentProfile profile = new StudentProfile(city, email, guardianNumber, bloodGroup, 0);

			int newStudentId = studentDao.addStudent(student, profile);
			if (newStudentId != -1) {
				System.out.println("New student added with ID: " + newStudentId);
			} else {
				System.out.println("Failed to add new student.");
			}
		} catch (Exception e) {
			System.out.println("Error while adding student: " + e.getMessage());
			e.printStackTrace();
		}
	}

	private void searchStudentById() {
		try {
			System.out.print("Enter student ID: ");
			int id = Integer.parseInt(scanner.nextLine().trim());
			studentDao.searchStudentById(id);
		} catch (NumberFormatException e) {
			System.out.println("Invalid ID format.");
		}
	}

	private void deleteStudentById() {
		try {
			System.out.print("Enter student ID to delete: ");
			int id = Integer.parseInt(scanner.nextLine().trim());
			studentDao.deleteStudentById(id);
		} catch (NumberFormatException e) {
			System.out.println("Invalid ID format.");
		}
	}

	private void assignCourseToStudent() {
		try {
			System.out.print("Enter student ID: ");
			int studentId = Integer.parseInt(scanner.nextLine().trim());
			if (!studentDao.isStudentExist(studentId)) {
				System.out.println("Student does not exist.");
				return;
			}
			System.out.println("\nAvailable courses:");
			studentDao.showAllCourses();

			System.out.print("Enter course ID to assign: ");
			int courseId = Integer.parseInt(scanner.nextLine().trim());

			if (!studentDao.isCourseExist(courseId)) {
				System.out.println("Course does not exist.");
				return;
			}
			if (studentDao.isStudentCourseMapExist(studentId, courseId)) {
				System.out.println("Student is already assigned to this course.");
				return;
			}
			studentDao.assignCourseToStudent(studentId, courseId);
		} catch (NumberFormatException e) {
			System.out.println("Invalid input. Please enter numbers only.");
		}
	}

	private void viewCoursesOfStudent() {
		try {
			System.out.print("Enter student ID: ");
			int studentId = Integer.parseInt(scanner.nextLine().trim());
			if (!studentDao.isStudentExist(studentId)) {
				System.out.println("Student does not exist.");
				return;
			}
			studentDao.viewCoursesOfStudent(studentId);
		} catch (NumberFormatException e) {
			System.out.println("Invalid input. Please enter numbers only.");
		}
	}
}
