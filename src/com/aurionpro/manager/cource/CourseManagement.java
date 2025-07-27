package com.aurionpro.manager.cource;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.InputMismatchException;
import java.util.Scanner;

import com.aurionpro.constant.Table;
import com.aurionpro.constant.Table_ID;
import com.aurionpro.constant.Table_Map;
import com.aurionpro.database.DBManager;
import com.aurionpro.util.Printer;

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
				System.out.println("3> Add New Subject");
				System.out.println("4> Add Subject to Course");
				System.out.println("5> Delete Course");
				System.out.println("6> View Subjects of Course");
				System.out.println("7> View Students of Course");
				System.out.println("8> Show All Subjects");
				System.out.println("9> Delete Subject");
				System.out.println("10> Exit");

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
					addSubjectToCourse();
					break;
				}
				case 5: {
					deleteCourse();
					break;
				}
				case 6: {
					showSubjectOfCourse();
					break;
				}
				case 7: {
					showStudentsOfCourse();
					break;
				}
				case 8: {
					showAllSubject();
					break;
				}
				case 9: {
					deleteSubject();
					break;
				}
				case 10: {
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

	private void deleteSubject() {
		showAllSubject();
		try {
			System.out.print("Enter Subject ID: ");
			int id = scanner.nextInt();
			if (dbManager.isExist(id, Table_ID.subject_id, Table.Subjects)) {
				dbManager.delete(id, Table_ID.subject_id, Table.Subjects);
			} else {
				System.out.println(id + " this Subject Id Not Exist");
			}

		} catch (InputMismatchException e) {
			System.out.println("\nPlease enter numbers where required.");
			scanner.nextLine();
		} catch (Exception e) {
			System.out.println("\nSomething went wrong: " + e.getMessage());
			e.printStackTrace();
		}
	}

	private void showStudentsOfCourse() {
		try {
			System.out.println();
			System.out.print("Enter Course ID: ");
			int courseId = scanner.nextInt();
			if (!dbManager.isExist(courseId, Table_ID.course_id, Table.Courses)) {
				System.out.println("Course not exist");
			}
			String sql = "SELECT * FROM get_students_by_course_id(?)";

			try (PreparedStatement stmt = connection.prepareStatement(sql)) {
				stmt.setInt(1, courseId);
				ResultSet rs = stmt.executeQuery();
				Printer.printTable(rs);
			} catch (SQLException e) {
				System.out.println(" Error fetching subjects: " + e.getMessage());
			}
		} catch (InputMismatchException e) {
			System.out.println("Invalid input format. Please enter numbers only.");
			scanner.next();
		} catch (Exception e) {
			System.out.println("An unexpected error occurred.");
			e.printStackTrace();
		}

	}

	public void showSubjectOfCourse() {
		try {
			System.out.println();
			System.out.print("Enter Course ID: ");
			int courseId = scanner.nextInt();
			if (!dbManager.isExist(courseId, Table_ID.course_id, Table.Courses)) {
				System.out.println("Course not exist");
				return ;
			}
			String sql = "SELECT * FROM get_subjects_by_course_id(?)";

			try (PreparedStatement stmt = connection.prepareStatement(sql)) {
				stmt.setInt(1, courseId);
				ResultSet rs = stmt.executeQuery();
				Printer.printTable(rs);
			} catch (SQLException e) {
				System.out.println(" Error fetching subjects: " + e.getMessage());
			}
		} catch (InputMismatchException e) {
			System.out.println("Invalid input format. Please enter numbers only.");
			scanner.next();
		} catch (Exception e) {
			System.out.println("An unexpected error occurred.");
			e.printStackTrace();
		}

	}

	public void addSubjectToCourse() {
		showAllCourse();

		try {
			System.out.println();
			System.out.print("Enter Course ID: ");
			int courseId = scanner.nextInt();

			if (!dbManager.isExist(courseId, Table_ID.course_id, Table.Courses)) {
				System.out.println("Course does not exist.");
				return;
			}

			while (true) {
				showAllSubject();
				System.out.println("Enter Subject ID to add to this course:");
				System.out.println("Enter 0 to exit.");
				int subjectId = scanner.nextInt();

				if (subjectId == 0) {
					return;
				}

				if (!dbManager.isExist(subjectId, Table_ID.subject_id, Table.Subjects)) {
					System.out.println("Subject does not exist.");
					continue;
				}

				if (dbManager.isMapExist(subjectId, courseId, Table_ID.subject_id, Table_ID.course_id,
						Table_Map.course_subject_map)) {
					System.out.println("This subject is already assigned to this course.");
					continue;
				}

				String insertQuery = "INSERT INTO course_subject_map (course_id, subject_id) VALUES (?, ?)";

				try (PreparedStatement preparedStatement = connection.prepareStatement(insertQuery)) {
					preparedStatement.setInt(1, courseId);
					preparedStatement.setInt(2, subjectId);

					int rowsInserted = preparedStatement.executeUpdate();
					if (rowsInserted > 0) {
						System.out.println("Subject added to course successfully.");
					} else {
						System.out.println("Failed to add subject to course.");
					}
				} catch (SQLException e) {
					System.out.println("Database error occurred while adding subject to course.");
					e.printStackTrace();
				}
			}

		} catch (InputMismatchException e) {
			System.out.println("Invalid input format. Please enter numbers only.");
			scanner.next();
		} catch (Exception e) {
			System.out.println("An unexpected error occurred.");
			e.printStackTrace();
		}
	}

	public void addNewSubject() {
		try {
			System.out.print("Enter Subject Name: ");
			String subjectName = scanner.next().trim();

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
		showAllCourse();
		try {
			System.out.print("Enter Course ID: ");
			int id = scanner.nextInt();
			System.out.println();
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
					System.out.println("Course added  successfully.");
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
			Printer.printTable(resultSet);

		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public void showAllSubject() {
		String query = "SELECT * FROM Subjects";
		try (PreparedStatement preparedStatement = connection.prepareStatement(query);
				ResultSet resultSet = preparedStatement.executeQuery()) {
			Printer.printTable(resultSet);

		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

}
