package com.aurionpro.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;

import com.aurionpro.model.Teacher;
import com.aurionpro.model.TeacherProfile;

public class TeacherDAO {
	private final Connection databaseConnection;

	public TeacherDAO(Connection databaseConnection) {
		this.databaseConnection = databaseConnection;
	}

	public boolean addTeacherWithProfile(Teacher teacher, TeacherProfile teacherProfile) throws SQLException {
		String teacherInsertQuery = "INSERT INTO teachers (name, mobile_number, dob, salary) VALUES (?, ?, ?, ?) RETURNING teacher_id";
		String profileInsertQuery = "INSERT INTO teacher_profiles (city, email, alternate_number, blood_group, teacher_id) VALUES (?, ?, ?, ?, ?)";

		try (PreparedStatement teacherInsertStatement = databaseConnection.prepareStatement(teacherInsertQuery);) {
			databaseConnection.setAutoCommit(false);

			teacherInsertStatement.setString(1, teacher.getName());
			teacherInsertStatement.setString(2, teacher.getMobileNumber());
			teacherInsertStatement.setDate(3, teacher.getDateOfBirth());
			teacherInsertStatement.setDouble(4, teacher.getSalary());

			try (ResultSet generatedKeys = teacherInsertStatement.executeQuery()) {
				if (generatedKeys.next()) {
					int generatedTeacherId = generatedKeys.getInt(1);

					try (PreparedStatement profileInsertStatement = databaseConnection
							.prepareStatement(profileInsertQuery)) {
						profileInsertStatement.setString(1, teacherProfile.getCity());
						profileInsertStatement.setString(2, teacherProfile.getEmail());

						if (teacherProfile.getAlternateNumber() == null
								|| teacherProfile.getAlternateNumber().trim().isEmpty()) {
							profileInsertStatement.setNull(3, Types.VARCHAR);
						} else {
							profileInsertStatement.setString(3, teacherProfile.getAlternateNumber());
						}

						profileInsertStatement.setString(4, teacherProfile.getBloodGroup());
						profileInsertStatement.setInt(5, generatedTeacherId);

						profileInsertStatement.executeUpdate();
					}

					databaseConnection.commit();
					return true;
				} else {
					databaseConnection.rollback();
					return false;
				}
			} catch (SQLException e) {
				databaseConnection.rollback();
				throw e;
			} finally {
				databaseConnection.setAutoCommit(true);
			}
		}
	}

	public List<Teacher> getAllTeachers() throws SQLException {
		String selectQuery = "SELECT * FROM teachers WHERE is_active = TRUE";
		List<Teacher> teacherList = new ArrayList<>();

		try (Statement statement = databaseConnection.createStatement();
				ResultSet resultSet = statement.executeQuery(selectQuery);) {
			while (resultSet.next()) {
				teacherList.add(mapToTeacher(resultSet));
			}
		}
		return teacherList;
	}

	public List<String> getSubjectsByTeacherId(int teacherId) throws SQLException {
		String selectQuery = "SELECT s.subject_name FROM teacher_subject_map tsm "
				+ "JOIN subjects s ON s.subject_id = tsm.subject_id "
				+ "WHERE tsm.teacher_id = ? AND tsm.is_active = TRUE AND s.is_active = TRUE";
		List<String> subjects = new ArrayList<>();

		try (PreparedStatement preparedStatement = databaseConnection.prepareStatement(selectQuery)) {
			preparedStatement.setInt(1, teacherId);

			try (ResultSet resultSet = preparedStatement.executeQuery()) {
				while (resultSet.next()) {
					subjects.add(resultSet.getString(1));
				}
			}
		}
		return subjects;
	}

	public Teacher getTeacherById(int teacherId) throws SQLException {
		String selectQuery = "SELECT * FROM teachers WHERE teacher_id = ? AND is_active = TRUE";
		try (PreparedStatement preparedStatement = databaseConnection.prepareStatement(selectQuery)) {
			preparedStatement.setInt(1, teacherId);
			try (ResultSet resultSet = preparedStatement.executeQuery()) {
				if (resultSet.next()) {
					return mapToTeacher(resultSet);
				}
			}
		}
		return null;
	}

	public boolean assignSubject(int teacherId, int subjectId) throws SQLException {
		// Check that teacher and subject exist and are active
		String teacherActiveCheck = "SELECT is_active FROM teachers WHERE teacher_id = ?";
		String subjectActiveCheck = "SELECT is_active FROM subjects WHERE subject_id = ?";

		try (PreparedStatement teacherActiveStatement = databaseConnection.prepareStatement(teacherActiveCheck);
				PreparedStatement subjectActiveStatement = databaseConnection.prepareStatement(subjectActiveCheck);) {
			teacherActiveStatement.setInt(1, teacherId);
			subjectActiveStatement.setInt(1, subjectId);

			try (ResultSet teacherActiveResultSet = teacherActiveStatement.executeQuery();
					ResultSet subjectActiveResultSet = subjectActiveStatement.executeQuery();) {
				if (!teacherActiveResultSet.next() || !teacherActiveResultSet.getBoolean(1)) {
					return false; // Teacher inactive or not found
				}
				if (!subjectActiveResultSet.next() || !subjectActiveResultSet.getBoolean(1)) {
					return false; // Subject inactive or not found
				}
			}
		}

		// Insert or reactivate mapping
		String assignQuery = "INSERT INTO teacher_subject_map (teacher_id, subject_id, is_active) "
				+ "VALUES (?, ?, TRUE) "
				+ "ON CONFLICT (teacher_id, subject_id) DO UPDATE SET is_active = TRUE, updated_at = NOW()";

		try (PreparedStatement preparedStatement = databaseConnection.prepareStatement(assignQuery)) {
			preparedStatement.setInt(1, teacherId);
			preparedStatement.setInt(2, subjectId);
			int updated = preparedStatement.executeUpdate();
			return updated > 0;
		}
	}

	public boolean softDeleteTeacher(int teacherId) throws SQLException {
		String deleteQuery = "UPDATE teachers SET is_active = FALSE WHERE teacher_id = ?";
		try (PreparedStatement preparedStatement = databaseConnection.prepareStatement(deleteQuery)) {
			preparedStatement.setInt(1, teacherId);
			int updated = preparedStatement.executeUpdate();
			return updated > 0;
		}
	}

	private Teacher mapToTeacher(ResultSet resultSet) throws SQLException {
		Teacher teacher = new Teacher();
		teacher.setTeacherId(resultSet.getInt("teacher_id"));
		teacher.setName(resultSet.getString("name"));
		teacher.setMobileNumber(resultSet.getString("mobile_number"));
		teacher.setDateOfBirth(resultSet.getDate("dob"));
		teacher.setSalary(resultSet.getDouble("salary"));
		teacher.setIsActive(resultSet.getBoolean("is_active"));
		teacher.setCreatedAt(resultSet.getTimestamp("created_at"));
		teacher.setUpdatedAt(resultSet.getTimestamp("updated_at"));
		return teacher;
	}
}
