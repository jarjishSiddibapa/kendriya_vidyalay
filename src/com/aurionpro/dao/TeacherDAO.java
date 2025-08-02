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
		PreparedStatement teacherInsertStatement = null;
		PreparedStatement profileInsertStatement = null;
		ResultSet generatedKeys = null;

		try {
			databaseConnection.setAutoCommit(false);

			teacherInsertStatement = databaseConnection.prepareStatement(teacherInsertQuery);
			teacherInsertStatement.setString(1, teacher.getName());
			teacherInsertStatement.setString(2, teacher.getMobileNumber());
			teacherInsertStatement.setDate(3, teacher.getDateOfBirth());
			teacherInsertStatement.setDouble(4, teacher.getSalary());
			generatedKeys = teacherInsertStatement.executeQuery();

			if (generatedKeys.next()) {
				int generatedTeacherId = generatedKeys.getInt(1);
				profileInsertStatement = databaseConnection.prepareStatement(profileInsertQuery);
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
				databaseConnection.commit();
				return true;
			}
			databaseConnection.rollback();
			return false;
		} catch (SQLException exception) {
			databaseConnection.rollback();
			throw exception;
		} finally {
			if (generatedKeys != null) {
				generatedKeys.close();
			}
			if (teacherInsertStatement != null) {
				teacherInsertStatement.close();
			}
			if (profileInsertStatement != null) {
				profileInsertStatement.close();
			}
			databaseConnection.setAutoCommit(true);
		}
	}

	public List<Teacher> getAllTeachers() throws SQLException {
		String selectQuery = "SELECT * FROM teachers WHERE is_active = TRUE";
		Statement statement = databaseConnection.createStatement();
		ResultSet resultSet = statement.executeQuery(selectQuery);
		List<Teacher> teacherList = new ArrayList<>();
		while (resultSet.next()) {
			teacherList.add(mapToTeacher(resultSet));
		}
		resultSet.close();
		statement.close();
		return teacherList;
	}

	public List<String> getSubjectsByTeacherId(int teacherId) throws SQLException {
		String selectQuery = "SELECT s.subject_name FROM teacher_subject_map tsm JOIN subjects s ON s.subject_id=tsm.subject_id WHERE tsm.teacher_id=? AND tsm.is_active=TRUE";
		PreparedStatement preparedStatement = databaseConnection.prepareStatement(selectQuery);
		preparedStatement.setInt(1, teacherId);
		ResultSet resultSet = preparedStatement.executeQuery();
		List<String> subjects = new ArrayList<>();
		while (resultSet.next()) {
			subjects.add(resultSet.getString(1));
		}
		resultSet.close();
		preparedStatement.close();
		return subjects;
	}

	public Teacher getTeacherById(int teacherId) throws SQLException {
		String selectQuery = "SELECT * FROM teachers WHERE teacher_id = ? AND is_active = TRUE";
		PreparedStatement preparedStatement = databaseConnection.prepareStatement(selectQuery);
		preparedStatement.setInt(1, teacherId);
		ResultSet resultSet = preparedStatement.executeQuery();
		Teacher teacher = null;
		if (resultSet.next()) {
			teacher = mapToTeacher(resultSet);
		}
		resultSet.close();
		preparedStatement.close();
		return teacher;
	}

	public boolean assignSubject(int teacherId, int subjectId) throws SQLException {
		String teacherActiveCheck = "SELECT is_active FROM teachers WHERE teacher_id = ?";
		String subjectActiveCheck = "SELECT is_active FROM subjects WHERE subject_id = ?";
		PreparedStatement teacherActiveStatement = databaseConnection.prepareStatement(teacherActiveCheck);
		PreparedStatement subjectActiveStatement = databaseConnection.prepareStatement(subjectActiveCheck);
		teacherActiveStatement.setInt(1, teacherId);
		subjectActiveStatement.setInt(1, subjectId);
		ResultSet teacherActiveResultSet = teacherActiveStatement.executeQuery();
		ResultSet subjectActiveResultSet = subjectActiveStatement.executeQuery();
		boolean valid = false;
		if (teacherActiveResultSet.next() && subjectActiveResultSet.next()) {
			if (teacherActiveResultSet.getBoolean(1) && subjectActiveResultSet.getBoolean(1)) {
				valid = true;
			}
		}
		teacherActiveResultSet.close();
		subjectActiveResultSet.close();
		teacherActiveStatement.close();
		subjectActiveStatement.close();
		if (!valid) {
			return false;
		}
		String assignQuery = "INSERT INTO teacher_subject_map (teacher_id, subject_id, is_active) VALUES (?, ?, TRUE) "
				+ "ON CONFLICT (teacher_id, subject_id) DO UPDATE SET is_active=TRUE";
		PreparedStatement preparedStatement = databaseConnection.prepareStatement(assignQuery);
		preparedStatement.setInt(1, teacherId);
		preparedStatement.setInt(2, subjectId);
		int updated = preparedStatement.executeUpdate();
		preparedStatement.close();
		return updated > 0;
	}

	public boolean softDeleteTeacher(int teacherId) throws SQLException {
		String deleteQuery = "UPDATE teachers SET is_active = FALSE WHERE teacher_id = ?";
		PreparedStatement preparedStatement = databaseConnection.prepareStatement(deleteQuery);
		preparedStatement.setInt(1, teacherId);
		int updated = preparedStatement.executeUpdate();
		preparedStatement.close();
		return updated > 0;
	}

	private Teacher mapToTeacher(ResultSet resultSet) throws SQLException {
		Teacher teacher = new Teacher();
		teacher.setTeacherId(resultSet.getInt("teacher_id"));
		teacher.setName(resultSet.getString("name"));
		teacher.setMobileNumber(resultSet.getString("mobile_number"));
		teacher.setDateOfBirth(resultSet.getDate("dob"));
		teacher.setSalary(resultSet.getDouble("salary"));
		teacher.setIsActive(resultSet.getBoolean("is_active"));
		teacher.setCreatedAt(resultSet.getDate("created_at"));
		teacher.setUpdatedAt(resultSet.getDate("updated_at"));
		return teacher;
	}
}