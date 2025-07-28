package com.aurionpro.dao;

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
import com.aurionpro.model.Course;
import com.aurionpro.model.Subject;
import com.aurionpro.util.Printer;

public class CourseDao {
	private Connection connection;
	private PreparedStatement prepareStatement;
	private DBManager dbManager;

	public CourseDao(Connection connection, DBManager dbManager) {
		this.connection = connection;
		this.dbManager = dbManager;
	}

	public void deleteSubject(int id) {
		if (dbManager.isExist(id, Table_ID.subject_id, Table.Subjects)) {
			dbManager.delete(id, Table_ID.subject_id, Table.Subjects);
		} else {
			System.out.println(id + " this Subject Id Not Exist");
		}
	}

	public void showStudentsOfCourse(int courseId) {
		String sql = "SELECT * FROM get_students_by_course_id(?) ";

		try (PreparedStatement stmt = connection.prepareStatement(sql)) {
			stmt.setInt(1, courseId);
			ResultSet rs = stmt.executeQuery();
			Printer.printTable(rs);
		} catch (SQLException e) {
			System.out.println(" Error fetching subjects: " + e.getMessage());
		}

	}

	public void showSubjectOfCourse(int courseId) {

		if (!dbManager.isExist(courseId, Table_ID.course_id, Table.Courses)) {
			System.out.println("Course not exist");
			return;
		}
		String sql = "SELECT * FROM get_subjects_by_course_id(?) ";

		try (PreparedStatement stmt = connection.prepareStatement(sql)) {
			stmt.setInt(1, courseId);
			ResultSet rs = stmt.executeQuery();
			Printer.printTable(rs);
		} catch (SQLException e) {
			System.out.println(" Error fetching subjects: " + e.getMessage());
		}

	}

	public void addSubjectToCourse(int subject_id, int course_id) {

		String insertQuery = "INSERT INTO course_subject_map (course_id, subject_id) VALUES (?, ?)";
		try (PreparedStatement preparedStatement = connection.prepareStatement(insertQuery)) {
			preparedStatement.setInt(1, course_id);
			preparedStatement.setInt(2, subject_id);

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

	public void addNewSubject(Subject subject) {
		try {

			String subjectName = subject.getSubjectName();
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

		} catch (Exception e) {
			System.out.println("Unexpected error occurred.");
			e.printStackTrace();
		}
	}

	public void deleteCourse(int id) {
		if (dbManager.isExist(id, Table_ID.course_id, Table.Courses)) {
			dbManager.delete(id, Table_ID.course_id, Table.Courses);
		} else {
			System.out.println(id + " this Course Id Not Exist");
		}
	}

	public void addNewCourse(Course course) {
		try {
			String insertQuery = "INSERT INTO courses (course_name, course_fees, course_description) VALUES (?, ?, ?)";
			try (PreparedStatement preparedStatement = connection.prepareStatement(insertQuery)) {
				preparedStatement.setString(1, course.getCourseName());
				preparedStatement.setInt(2, course.getCoursefees());
				preparedStatement.setString(3, course.getCourseDescription());

				int rowsInserted = preparedStatement.executeUpdate();
				if (rowsInserted > 0) {
					System.out.println("Course added  successfully.");
				} else {
					System.out.println("Failed to add course.");
				}
			}

		} catch (SQLException e) {
			System.out.println("Database error occurred.");
			e.printStackTrace();
		} catch (Exception e) {
			System.out.println("Unexpected error occurred.");
			e.printStackTrace();
		}
	}

	public void showAllCourse() {
		String query = "SELECT course_id,course_name,course_fees,course_description FROM courses where  is_active = TRUE";
		try (PreparedStatement preparedStatement = connection.prepareStatement(query);
				ResultSet resultSet = preparedStatement.executeQuery()) {
			Printer.printTable(resultSet);

		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public void showAllSubject() {
		String query = "SELECT  subject_id ,subject_name FROM Subjects where  is_active = TRUE";
		try (PreparedStatement preparedStatement = connection.prepareStatement(query);
				ResultSet resultSet = preparedStatement.executeQuery()) {
			Printer.printTable(resultSet);

		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public void deleteSubjectFromCourse(int subjectId, int courseId) {
	    String updateQuery = "UPDATE course_subject_map SET is_active = FALSE WHERE course_id = ? AND subject_id = ?";
	    try (PreparedStatement preparedStatement = connection.prepareStatement(updateQuery)) {
	        preparedStatement.setInt(1, courseId);
	        preparedStatement.setInt(2, subjectId);

	        int rowsUpdated = preparedStatement.executeUpdate();
	        if (rowsUpdated > 0) {
	            System.out.println("Subject soft-deleted from course successfully.");
	        } else {
	        	System.out.println("Failed to soft-deleted subject to course.");
	        }
	    } catch (SQLException e) {
	        System.out.println("Database error occurred while soft-deleting subject from course.");
	        e.printStackTrace();
	    }
	}

	public void assignTeacherToCourseSubject(int teacherId, int courseId, int subjectId) {
		 String query = "INSERT INTO teacher_course_subject_map (teacher_id, course_id, subject_id) VALUES (?, ?, ?)";
		 try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
	        preparedStatement.setInt(1, teacherId);
	        preparedStatement.setInt(2, courseId);
	        preparedStatement.setInt(3, subjectId);

	        int rowsInserted = preparedStatement.executeUpdate();
	        if (rowsInserted > 0) {
	            System.out.println("Teacher assigned to subject in course successfully.");
	        } else {
	            System.out.println("Assignment failed.");
	        }
	    } catch (SQLException e) {
	        System.out.println("Error while assigning teacher to subject in course.");
	        e.printStackTrace();
	    }
	}
	
	public void unassignTeacherFromCourseSubject( int courseId, int subjectId) {
	    String query = "UPDATE teacher_course_subject_map SET is_active = FALSE WHERE  course_id = ? AND subject_id = ?";
	    try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
	        preparedStatement.setInt(1, courseId);
	        preparedStatement.setInt(2, subjectId);

	        int rowsUpdated = preparedStatement.executeUpdate();
	        if (rowsUpdated > 0) {
	            System.out.println("Teacher unassigned from subject in course (soft delete).");
	        } else {
	            System.out.println("No matching assignment found or already inactive.");
	        }
	    } catch (SQLException e) {
	        System.out.println("Error while unassigning teacher.");
	        e.printStackTrace();
	    }
	}
	
	public void showSubjectAndTeacherOfCourse(int courseId) {
	    String query = "SELECT * FROM get_subjects_and_teacher_by_course_id(?)";
	    try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
	        preparedStatement.setInt(1, courseId);
	        try (ResultSet resultSet = preparedStatement.executeQuery()) {
	            Printer.printTable(resultSet);
	        }
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }
	}
	
	public void showAllTeacherForSubject(int subjectId) {
	    String query = "SELECT * FROM get_teachers_by_subject_id(?)";
	    try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
	        preparedStatement.setInt(1, subjectId); 
	        try (ResultSet resultSet = preparedStatement.executeQuery()) {
	            Printer.printTable(resultSet);
	        }
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }
	}





	public boolean isCourseExist(int id) {
		return dbManager.isExist(id, Table_ID.course_id, Table.Courses);
	}

	public boolean isSubjectExist(int id) {
		return dbManager.isExist(id, Table_ID.subject_id, Table.Subjects);
	}

	public boolean isSubjectCourseMapExist(int subject_id, int course_id) {
		return dbManager.isMapExist(subject_id, course_id, Table_ID.subject_id, Table_ID.course_id,
				Table_Map.course_subject_map);
	}
	
	public boolean isTeacherSubejctMapExist(int subject_id, int teacher_id) {
		return dbManager.isMapExist(subject_id, teacher_id, Table_ID.subject_id, Table_ID.teacher_id,
				Table_Map.teacher_subject_map);
	}

	public boolean isTeacherSubjectCourseMapExist(int subjectId, int courseId) {
	    String query = "SELECT 1 FROM teacher_course_subject_map " +
	                   "WHERE course_id = ? AND subject_id = ? AND is_active = TRUE " +
	                   "LIMIT 1";
	    try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
	        preparedStatement.setInt(1, courseId);
	        preparedStatement.setInt(2, subjectId);

	        try (ResultSet resultSet = preparedStatement.executeQuery()) {
	            return resultSet.next();  // true if record exists
	        }
	    } catch (SQLException e) {
	        e.printStackTrace();
	        return false;
	    }
	}


}
