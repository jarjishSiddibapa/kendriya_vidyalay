package com.aurionpro.manager.student;

import com.aurionpro.database.Database;
import com.aurionpro.model.Student;
import com.aurionpro.model.StudentProfile;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class StudentManager {
	private Connection conn;

	public StudentManager() {
		Database db = new Database();
		db.connect();
		this.conn = db.connection;
		if (this.conn == null) {
			System.out.println("Failed to establish database connection!");
			throw new RuntimeException("DB Connection is null");
		}
	}

	// 1. Add new student (& profile)
	public boolean addStudent(Student s, StudentProfile p) throws SQLException {
		String sql1 = "INSERT INTO students (name, mobile_number, dob) VALUES (?, ?, ?)";
		String sql2 = "INSERT INTO student_profiles (city, email, guardian_number, blood_group, student_id) VALUES (?, ?, ?, ?, ?)";
		try (PreparedStatement stmt1 = conn.prepareStatement(sql1, Statement.RETURN_GENERATED_KEYS)) {
			stmt1.setString(1, s.getName());
			stmt1.setString(2, s.getMobileNumber());
			stmt1.setDate(3, new java.sql.Date(s.getDob().getTime()));
			int rows = stmt1.executeUpdate();
			if (rows == 0)
				return false;
			try (ResultSet rs = stmt1.getGeneratedKeys()) {
				if (rs.next()) {
					s.setStudentId(rs.getInt(1));
					try (PreparedStatement stmt2 = conn.prepareStatement(sql2)) {
						stmt2.setString(1, p.getCity());
						stmt2.setString(2, p.getEmail());
						stmt2.setString(3, p.getGuardianNumber());
						stmt2.setString(4, p.getBloodGroup());
						stmt2.setInt(5, s.getStudentId());
						stmt2.executeUpdate();
					}
				}
			}
		}
		return true;
	}

	// 2. Show all students (with profiles)
	public List<String> showAllStudents() throws SQLException {
		String sql = "SELECT s.student_id, s.name, s.mobile_number, s.dob, p.city, p.email, p.guardian_number, p.blood_group FROM students s LEFT JOIN student_profiles p ON s.student_id=p.student_id";
		List<String> out = new ArrayList<>();
		try (PreparedStatement stmt = conn.prepareStatement(sql); ResultSet rs = stmt.executeQuery()) {
			while (rs.next()) {
				out.add(String.format(
						"ID:%d | Name:%s | Mobile:%s | DOB:%s\n Profile: City:%s Email:%s Guardian:%s BloodGroup:%s",
						rs.getInt(1), rs.getString(2), rs.getString(3), rs.getDate(4), rs.getString(5), rs.getString(6),
						rs.getString(7), rs.getString(8)));
			}
		}
		return out;
	}

	// 3. Search student by ID or mobile
	public String searchStudent(int id, String mobile) throws SQLException {
		String sql = "SELECT s.student_id, s.name, s.mobile_number, s.dob, p.city, p.email, p.guardian_number, p.blood_group "
				+ "FROM students s LEFT JOIN student_profiles p ON s.student_id=p.student_id WHERE s.student_id=? OR s.mobile_number=?";
		try (PreparedStatement stmt = conn.prepareStatement(sql)) {
			stmt.setInt(1, id);
			stmt.setString(2, mobile);
			try (ResultSet rs = stmt.executeQuery()) {
				if (rs.next()) {
					return String.format(
							"ID:%d | Name:%s | Mobile:%s | DOB:%s\n Profile: City:%s Email:%s Guardian:%s BloodGroup:%s",
							rs.getInt(1), rs.getString(2), rs.getString(3), rs.getDate(4), rs.getString(5),
							rs.getString(6), rs.getString(7), rs.getString(8));
				}
			}
		}
		return "No student found";
	}

	// 4. Delete student
	public boolean deleteStudent(int id) throws SQLException {
		String sql = "DELETE FROM students WHERE student_id=?";
		try (PreparedStatement stmt = conn.prepareStatement(sql)) {
			stmt.setInt(1, id);
			return stmt.executeUpdate() > 0;
		}
	}

	// 5. Assign course
	public boolean assignCourse(int studentId, int courseId) throws SQLException {
		String sql = "INSERT IGNORE INTO student_course_map (student_id, course_id) VALUES (?, ?)"; // IGNORE works on
																									// MySQL
		try (PreparedStatement stmt = conn.prepareStatement(sql)) {
			stmt.setInt(1, studentId);
			stmt.setInt(2, courseId);
			return stmt.executeUpdate() > 0;
		} catch (SQLException e) {
			// Duplicate entry
			if (e.getErrorCode() == 1062) { // MySQL duplicate entry
				return false;
			}
			throw e;
		}
	}

	// 6. View assigned courses for a student
	public List<String> getCoursesByStudent(int studentId) throws SQLException {
		String sql = "SELECT c.course_id, c.course_name FROM courses c JOIN student_course_map m ON c.course_id=m.course_id WHERE m.student_id=?";
		List<String> out = new ArrayList<>();
		try (PreparedStatement stmt = conn.prepareStatement(sql)) {
			stmt.setInt(1, studentId);
			try (ResultSet rs = stmt.executeQuery()) {
				while (rs.next())
					out.add(rs.getInt(1) + " - " + rs.getString(2));
			}
		}
		return out;
	}
}
