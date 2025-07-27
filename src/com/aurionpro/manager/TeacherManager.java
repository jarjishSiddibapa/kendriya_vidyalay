package com.aurionpro.manager;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.aurionpro.database.Database;
import com.aurionpro.model.Teacher;

public class TeacherManager {
    private static Connection connection = Database.getConnection();

    public static void addTeacher(Teacher teacher) {
        String insertQuery = "INSERT INTO teachers (name, mobile_number, dob, salary) VALUES (?, ?, ?, ?) "
                           + "ON CONFLICT (mobile_number) DO NOTHING RETURNING teacher_id";
        try (PreparedStatement insertStatement = connection.prepareStatement(insertQuery)) {
            insertStatement.setString(1, teacher.getName());
            insertStatement.setString(2, teacher.getMobileNumber());
            insertStatement.setDate(3, teacher.getDob());
            insertStatement.setDouble(4, teacher.getSalary());

            ResultSet rs = insertStatement.executeQuery();
            if (rs.next()) {
                int id = rs.getInt("teacher_id");
                teacher.setTeacherId(id);
                System.out.println("Teacher added successfully!");
            } else {
                System.out.println("Teacher NOT added. Duplicate mobile number detected.");
                teacher.setTeacherId(0);
            }
        } catch (SQLException exception) {
            System.out.println("Error while adding teacher: " + exception.getMessage());
            teacher.setTeacherId(0);
        }
    }



    public static List<Teacher> getAllTeachers() {
        List<Teacher> teachers = new ArrayList<>();
        String selectQuery = "SELECT teacher_id, name, mobile_number, dob, salary, created_at, updated_at FROM teachers";

        try (PreparedStatement selectStatement = connection.prepareStatement(selectQuery);
             ResultSet resultSet = selectStatement.executeQuery()) {

            while (resultSet.next()) {
                Teacher teacher = new Teacher();
                teacher.setTeacherId(resultSet.getInt("teacher_id"));
                teacher.setName(resultSet.getString("name"));
                teacher.setMobileNumber(resultSet.getString("mobile_number"));
                teacher.setDob(resultSet.getDate("dob"));
                teacher.setSalary(resultSet.getDouble("salary"));
                teacher.setCreatedAt(resultSet.getTimestamp("created_at"));
                teacher.setUpdatedAt(resultSet.getTimestamp("updated_at"));
                teachers.add(teacher);
            }
        } catch (SQLException exception) {
            System.out.println("Error fetching teachers: " + exception.getMessage());
        }

        return teachers;
    }

    public static Teacher getTeacherById(int teacherId) {
        String selectQuery = "SELECT teacher_id, name, mobile_number, dob, salary, created_at, updated_at FROM teachers WHERE teacher_id = ?";
        try (PreparedStatement statement = connection.prepareStatement(selectQuery)) {
            statement.setInt(1, teacherId);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    Teacher teacher = new Teacher();
                    teacher.setTeacherId(resultSet.getInt("teacher_id"));
                    teacher.setName(resultSet.getString("name"));
                    teacher.setMobileNumber(resultSet.getString("mobile_number"));
                    teacher.setDob(resultSet.getDate("dob"));
                    teacher.setSalary(resultSet.getDouble("salary"));
                    teacher.setCreatedAt(resultSet.getTimestamp("created_at"));
                    teacher.setUpdatedAt(resultSet.getTimestamp("updated_at"));
                    return teacher;
                }
            }
        } catch (SQLException exception) {
            System.out.println("Error fetching teacher: " + exception.getMessage());
        }
        return null; // Teacher not found
    }

    public static void deleteTeacher(int teacherId) {
        String deleteQuery = "DELETE FROM teachers WHERE teacher_id = ?";
        try (PreparedStatement deleteStatement = connection.prepareStatement(deleteQuery)) {
            deleteStatement.setInt(1, teacherId);
            int rowsAffected = deleteStatement.executeUpdate();

            if (rowsAffected > 0) {
                System.out.println("Teacher deleted successfully (ID: " + teacherId + ").");
                return;
            }
            System.out.println("No teacher found with ID: " + teacherId + ".");
        } catch (SQLException exception) {
            System.out.println("Error deleting teacher: " + exception.getMessage());
        }
    }
    
    public static void updateTeacherName(int teacherId, String newName) {
        String updateQuery = "UPDATE teachers SET name = ?, updated_at = CURRENT_TIMESTAMP WHERE teacher_id = ?";
        try (PreparedStatement updateStatement = connection.prepareStatement(updateQuery)) {
            updateStatement.setString(1, newName);
            updateStatement.setInt(2, teacherId);
            int rowsAffected = updateStatement.executeUpdate();

            if (rowsAffected > 0) {
                System.out.println("Teacher name updated successfully for ID: " + teacherId + ".");
            } else {
                System.out.println("No teacher found with ID: " + teacherId + ".");
            }
        } catch (SQLException exception) {
            System.out.println("Error updating teacher name: " + exception.getMessage());
        }
    }

    public static void updateTeacherMobileNumber(int teacherId, String newMobileNumber) {
        String updateQuery = "UPDATE teachers SET mobile_number = ?, updated_at = CURRENT_TIMESTAMP WHERE teacher_id = ?";
        try (PreparedStatement updateStatement = connection.prepareStatement(updateQuery)) {
            updateStatement.setString(1, newMobileNumber);
            updateStatement.setInt(2, teacherId);
            int rowsAffected = updateStatement.executeUpdate();

            if (rowsAffected > 0) {
                System.out.println("Teacher mobile number updated successfully for ID: " + teacherId + ".");
            } else {
                System.out.println("No teacher found with ID: " + teacherId + ", or mobile number already exists for another teacher.");
            }
        } catch (SQLException exception) {
            // Unique violation code for PostgreSQL
            if ("23505".equals(exception.getSQLState())) {
                System.out.println("Update failed: mobile number already exists for another teacher.");
            } else {
                System.out.println("Error updating teacher mobile number: " + exception.getMessage());
            }
        }
    }

    
    public static void updateTeacherDateOfBirth(int teacherId, java.sql.Date newDateOfBirth) {
        String updateQuery = "UPDATE teachers SET dob = ?, updated_at = CURRENT_TIMESTAMP WHERE teacher_id = ?";
        try (PreparedStatement updateStatement = connection.prepareStatement(updateQuery)) {
            updateStatement.setDate(1, newDateOfBirth);
            updateStatement.setInt(2, teacherId);
            int rowsAffected = updateStatement.executeUpdate();

            if (rowsAffected > 0) {
                System.out.println("Teacher date of birth updated successfully for ID: " + teacherId + ".");
            } else {
                System.out.println("No teacher found with ID: " + teacherId + ".");
            }
        } catch (SQLException exception) {
            System.out.println("Error updating teacher date of birth: " + exception.getMessage());
        }
    }

    public static void updateTeacherSalary(int teacherId, double newSalary) {
        String updateQuery = "UPDATE teachers SET salary = ?, updated_at = CURRENT_TIMESTAMP WHERE teacher_id = ?";
        try (PreparedStatement updateStatement = connection.prepareStatement(updateQuery)) {
            updateStatement.setDouble(1, newSalary);
            updateStatement.setInt(2, teacherId);
            int rowsAffected = updateStatement.executeUpdate();

            if (rowsAffected > 0) {
                System.out.println("Teacher salary updated successfully for ID: " + teacherId + ".");
            } else {
                System.out.println("No teacher found with ID: " + teacherId + ".");
            }
        } catch (SQLException exception) {
            System.out.println("Error updating teacher salary: " + exception.getMessage());
        }
    }

}
