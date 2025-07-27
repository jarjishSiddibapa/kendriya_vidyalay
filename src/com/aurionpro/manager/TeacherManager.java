package com.aurionpro.manager;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.aurionpro.constant.Table;
import com.aurionpro.constant.Table_ID;
import com.aurionpro.database.DBManager;
import com.aurionpro.database.Database;
import com.aurionpro.model.Teacher;
import com.aurionpro.util.Printer;

public class TeacherManager {
    private static final Connection connection = Database.getConnection();
    private static final DBManager dbManager = new DBManager(connection);

    public static void addTeacher(Teacher teacher) {
        String insertQuery = "INSERT INTO teachers (name, mobile_number, dob, salary) VALUES (?, ?, ?, ?) "
                + "ON CONFLICT (mobile_number) DO NOTHING RETURNING teacher_id";
        try (PreparedStatement ps = connection.prepareStatement(insertQuery)) {
            ps.setString(1, teacher.getName());
            ps.setString(2, teacher.getMobileNumber());
            ps.setDate(3, teacher.getDob());
            ps.setDouble(4, teacher.getSalary());

            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                teacher.setTeacherId(rs.getInt("teacher_id"));
                Printer.printSuccessMessage("Teacher added successfully!");
            } else {
                Printer.printErrorMessage("Teacher NOT added. Duplicate mobile number detected.");
                teacher.setTeacherId(0);
            }
        } catch (SQLException e) {
            System.out.println("Error while adding teacher: " + e.getMessage());
            teacher.setTeacherId(0);
        }
    }

    public static List<Teacher> getAllTeachers() {
        String query = "SELECT teacher_id, name, mobile_number, dob, salary, created_at, updated_at FROM teachers";
        List<Teacher> teachers = new ArrayList<>();
        try (PreparedStatement ps = connection.prepareStatement(query);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                teachers.add(mapResultSetToTeacher(rs));
            }
        } catch (SQLException e) {
            System.out.println("Error fetching teachers: " + e.getMessage());
        }
        return teachers;
    }

    public static Teacher getTeacherById(int teacherId) {
        if (!dbManager.isExist(teacherId, Table_ID.teacher_id, Table.Teachers)) {
            System.out.println("No teacher found with ID: " + teacherId);
            return null;
        }
        String query = "SELECT teacher_id, name, mobile_number, dob, salary, created_at, updated_at FROM teachers WHERE teacher_id = ?";
        try (PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setInt(1, teacherId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToTeacher(rs);
                }
            }
        } catch (SQLException e) {
            System.out.println("Error fetching teacher: " + e.getMessage());
        }
        return null;
    }

    public static void deleteTeacher(int teacherId) {
        if (!dbManager.isExist(teacherId, Table_ID.teacher_id, Table.Teachers)) {
            System.out.println("No teacher found with ID: " + teacherId);
            return;
        }
        dbManager.delete(teacherId, Table_ID.teacher_id, Table.Teachers);
    }

    // --- Helper for Update Operations ---
    private static void updateTeacherField(int teacherId, String field, Object value) {
        String query = "UPDATE teachers SET " + field + " = ?, updated_at = CURRENT_TIMESTAMP WHERE teacher_id = ?";
        try (PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setObject(1, value);
            ps.setInt(2, teacherId);
            int rows = ps.executeUpdate();
            if (rows > 0) {
                System.out.println("Teacher " + field + " updated successfully for ID: " + teacherId + ".");
            } else {
                System.out.println("No teacher found with ID: " + teacherId + ".");
            }
        } catch (SQLException e) {
            System.out.println("Error updating teacher " + field + ": " + e.getMessage());
        }
    }

    public static void updateTeacherName(int teacherId, String newName) {
        updateTeacherField(teacherId, "name", newName);
    }

    public static void updateTeacherSalary(int teacherId, double newSalary) {
        updateTeacherField(teacherId, "salary", newSalary);
    }

    public static void updateTeacherDateOfBirth(int teacherId, Date newDateOfBirth) {
        updateTeacherField(teacherId, "dob", newDateOfBirth);
    }

    public static void updateTeacherMobileNumber(int teacherId, String newMobileNumber) {
        // Use DBManager for unique constraint
        if (dbManager.isStringExist(newMobileNumber, "mobile_number", Table.Teachers)) {
            System.out.println("Update failed: mobile number already exists for another teacher.");
            return;
        }
        updateTeacherField(teacherId, "mobile_number", newMobileNumber);
    }

    // --- Util: Map ResultSet row to Teacher object ---
    private static Teacher mapResultSetToTeacher(ResultSet rs) throws SQLException {
        Teacher teacher = new Teacher();
        teacher.setTeacherId(rs.getInt("teacher_id"));
        teacher.setName(rs.getString("name"));
        teacher.setMobileNumber(rs.getString("mobile_number"));
        teacher.setDob(rs.getDate("dob"));
        teacher.setSalary(rs.getDouble("salary"));
        teacher.setCreatedAt(rs.getTimestamp("created_at"));
        teacher.setUpdatedAt(rs.getTimestamp("updated_at"));
        return teacher;
    }
}
