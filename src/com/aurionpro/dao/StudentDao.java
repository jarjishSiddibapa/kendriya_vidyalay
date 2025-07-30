package com.aurionpro.dao;

import com.aurionpro.constant.Table;
import com.aurionpro.constant.Table_ID;
import com.aurionpro.constant.Table_Map;
import com.aurionpro.database.DBManager;
import com.aurionpro.model.Student;
import com.aurionpro.model.StudentProfile;
import com.aurionpro.util.Printer;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class StudentDao {

	private Connection connection;
	private DBManager dbManager;

	public StudentDao(Connection connection, DBManager dbManager) {
		this.connection = connection;
		this.dbManager = dbManager;
	}

	public int addStudent(Student student, StudentProfile profile) {
		String studentSQL = "INSERT INTO students (name, mobile_number, dob) VALUES (?, ?, ?)";
		String profileSQL = "INSERT INTO student_profiles (city, email, guardian_number, blood_group, student_id) VALUES (?, ?, ?, ?, ?)";

		try (PreparedStatement studentStmt = connection.prepareStatement(studentSQL, Statement.RETURN_GENERATED_KEYS)) {
			studentStmt.setString(1, student.getName());
			studentStmt.setString(2, student.getMobileNumber());
			studentStmt.setDate(3, student.getDob());

			int rows = studentStmt.executeUpdate();
			if (rows > 0) {
				ResultSet keySet = studentStmt.getGeneratedKeys();
				if (keySet.next()) {
					int studentId = keySet.getInt(1);
					try (PreparedStatement profileStmt = connection.prepareStatement(profileSQL)) {
						profileStmt.setString(1, profile.getCity());
						profileStmt.setString(2, profile.getEmail());
						profileStmt.setString(3, profile.getGuardianNumber());
						profileStmt.setString(4, profile.getBloodGroup());
						profileStmt.setInt(5, studentId);

						int profileRows = profileStmt.executeUpdate();
						if (profileRows > 0) {
							Printer.printSuccessMessage("Student and profile added successfully.");
							return studentId;
						} else {
							Printer.printErrorMessage("Profile creation failed.");
						}
					}
				}
			}
		} catch (SQLException e) {
			Printer.printErrorMessage("Database error while adding student: " + e.getMessage());
			e.printStackTrace();
		}
		return -1;
	}

	public void showAllStudents() {
		String sql = "SELECT s.student_id, s.name, s.mobile_number, s.dob, sp.city, sp.email, sp.guardian_number, sp.blood_group "
				+ "FROM students s LEFT JOIN student_profiles sp ON s.student_id = sp.student_id WHERE s.is_active = TRUE";
		try (PreparedStatement stmt = connection.prepareStatement(sql); ResultSet rs = stmt.executeQuery()) {
			Printer.printTable(rs);
		} catch (SQLException e) {
			Printer.printErrorMessage("Error fetching students: " + e.getMessage());
		}
	}

	public void searchStudentById(int studentId) {
		String sql = "SELECT s.student_id, s.name, s.mobile_number, s.dob, sp.city, sp.email, sp.guardian_number, sp.blood_group "
				+ "FROM students s LEFT JOIN student_profiles sp ON s.student_id = sp.student_id "
				+ "WHERE s.student_id = ? AND s.is_active = TRUE";
		try (PreparedStatement stmt = connection.prepareStatement(sql)) {
			stmt.setInt(1, studentId);
			ResultSet rs = stmt.executeQuery();
			Printer.printTable(rs);
		} catch (SQLException e) {
			Printer.printErrorMessage("Error searching student: " + e.getMessage());
		}
	}

	public void deleteStudentById(int studentId) {
		if (dbManager.isExist(studentId, Table_ID.student_id, Table.Students)) {
			dbManager.delete(studentId, Table_ID.student_id, Table.Students);
		} else {
			Printer.printErrorMessage("Student ID not found: " + studentId);
		}
	}

	public void assignCourseToStudent(int studentId, int courseId) {
		String sql = "INSERT INTO student_course_map (student_id, course_id) VALUES (?, ?)";
		try (PreparedStatement stmt = connection.prepareStatement(sql)) {
			stmt.setInt(1, studentId);
			stmt.setInt(2, courseId);
			int rows = stmt.executeUpdate();
			if (rows > 0) {
				Printer.printSuccessMessage("Course assigned to student.");
			} else {
				Printer.printErrorMessage("Assignment failed.");
			}
		} catch (SQLException e) {
			Printer.printErrorMessage("Error assigning course: " + e.getMessage());
		}
	}

	public void viewCoursesOfStudent(int studentId) {
		String sql = "SELECT c.course_id, c.course_name, c.course_fees, c.course_description FROM courses c "
				+ "JOIN student_course_map scm ON c.course_id = scm.course_id "
				+ "WHERE scm.student_id = ? AND c.is_active = TRUE AND scm.is_active = TRUE";
		try (PreparedStatement stmt = connection.prepareStatement(sql)) {
			stmt.setInt(1, studentId);
			try (ResultSet rs = stmt.executeQuery()) {
				Printer.printTable(rs);
			}
		} catch (SQLException e) {
			Printer.printErrorMessage("Error fetching student's courses: " + e.getMessage());
		}
	}

	// Checks and utilities
	public boolean isStudentExist(int studentId) {
		return dbManager.isExist(studentId, Table_ID.student_id, Table.Students);
	}

	public boolean isCourseExist(int courseId) {
		return dbManager.isExist(courseId, Table_ID.course_id, Table.Courses);
	}

	public boolean isStudentCourseMapExist(int studentId, int courseId) {
		return dbManager.isMapExist(studentId, courseId, Table_ID.student_id, Table_ID.course_id,
				Table_Map.student_course_map);
	}

	public void showAllCourses() {
		String sql = "SELECT course_id, course_name FROM courses WHERE is_active = TRUE";
		try (PreparedStatement stmt = connection.prepareStatement(sql); ResultSet rs = stmt.executeQuery()) {
			Printer.printTable(rs);
		} catch (SQLException e) {
			Printer.printErrorMessage("Database error while listing courses.");
		}
	}
}
