package com.aurionpro.manager.cource;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.InputMismatchException;
import java.util.Scanner;

import com.aurionpro.constant.Table;
import com.aurionpro.constant.Table_ID;
import com.aurionpro.database.DBManager;
import com.aurionpro.utils.print.Print;

public class CourseManagement {
	private Connection connection;
	private Scanner scanner;
	private PreparedStatement prepareStatement;
	private DBManager dbManager;

	public CourseManagement(Connection connection, Scanner scanner, DBManager dbManager) {
		this.connection = connection;
		this.scanner = scanner;
		this.dbManager = dbManager;
	}

	public void start() {

		while (true) {
			try {
				System.out.println();

				System.out.println("1> Show All Courses");
				System.out.println("2> Add New Courses");
				System.out.println("3> Create Subject");
				System.out.println("4> Add Subject to Course");
				System.out.println("5> Delete Course");
				System.out.println("6> View Course");
				System.out.println("7> Exit");

				int input = scanner.nextInt();
				switch (input) {
				case 1: {
					showAllCourse();
					break;
				}
				case 2: {
					addNewCourse();
					break;
				}
				case 3: {
					addNewSubject();
					break;
				}
				case 4: {
					addNewSubject();
					break;
				}
				case 5: {
					deleteCourse();
					break;
				}
				case 6: {
					showAllCourse();
					break;
				}
				case 7: {
					System.out.println("Exiting From Course Management ... ");
					return;
				}
				default:
					throw new IllegalArgumentException("Unexpected value: " + input);
				}

			} catch (InputMismatchException e) {
				System.out.println("\nPlease enter numbers where required.");
				scanner.nextLine();
			} catch (Exception e) {
				System.out.println("\nSomething went wrong: " + e.getMessage());
				e.printStackTrace();
			}

		}
	}

	private void addNewSubject() {
		try {
			System.out.print("Enter Subject Name: ");
			String subjectName = scanner.next();

			if (dbManager.isStringExist(subjectName, "subject_name", Table.Subjects)) {
				System.out.println(subjectName + " already exists.");
				return;
			}

			String insertQuery = "INSERT INTO Subjects (subject_name) VALUES (?)";
			try (PreparedStatement preparedStatement = connection.prepareStatement(insertQuery)) {
				preparedStatement.setString(1, subjectName);

				int rowsInserted = preparedStatement.executeUpdate();
				if (rowsInserted > 0) {
					System.out.println("Subject added successfully.");
				} else {
					System.out.println("Failed to add subject.");
				}
			} catch (SQLException e) {
				System.out.println("Database error occurred.");
				e.printStackTrace();
			}

		} catch (InputMismatchException e) {
			System.out.println("Invalid input format.");
		} catch (Exception e) {
			System.out.println("Unexpected error occurred.");
			e.printStackTrace();
		}
	}

	public void deleteCourse() {
		try {
			System.out.print("Enter Course ID: ");
			int id = scanner.nextInt();
			if (dbManager.isExist(id, Table_ID.course_id, Table.Courses)) {
				dbManager.delete(id, Table_ID.course_id, Table.Courses);
			} else {
				System.out.println(id + " this Course Id Not Exist");
			}

		} catch (InputMismatchException e) {
			System.out.println("\nPlease enter numbers where required.");
			scanner.nextLine();
		} catch (Exception e) {
			System.out.println("\nSomething went wrong: " + e.getMessage());
			e.printStackTrace();
		}
	}

	public void addNewCourse() {
		try {
			System.out.print("Enter Course Name: ");
			String courseName = scanner.next();

			System.out.print("Enter Course Fees: ");
			int courseFees = scanner.nextInt();

			System.out.print("Enter Course Description: ");
			String courseDescription = scanner.next();

			String insertQuery = "INSERT INTO courses (course_name, course_fees, course_description) VALUES (?, ?, ?)";

			try (PreparedStatement preparedStatement = connection.prepareStatement(insertQuery)) {
				preparedStatement.setString(1, courseName);
				preparedStatement.setInt(2, courseFees);
				preparedStatement.setString(3, courseDescription);

				int rowsInserted = preparedStatement.executeUpdate();
				if (rowsInserted > 0) {
					System.out.println("Course added successfully.");
				} else {
					System.out.println("Failed to add course.");
				}
			}

		} catch (InputMismatchException e) {
			System.out.println("Invalid input format.");
		} catch (SQLException e) {
			System.out.println("Database error occurred.");
			e.printStackTrace();
		} catch (Exception e) {
			System.out.println("Unexpected error occurred.");
			e.printStackTrace();
		}
	}

	public void showAllCourse() {
		String query = "SELECT * FROM courses";
		try (PreparedStatement preparedStatement = connection.prepareStatement(query);
				ResultSet resultSet = preparedStatement.executeQuery()) {
			    Print.printTable(resultSet);

		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

}
