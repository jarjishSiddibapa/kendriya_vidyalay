package com.aurionpro.manager.teacher;

import java.sql.*;

import com.aurionpro.database.Database;
import com.aurionpro.model.TeacherProfile;

public class TeacherProfileManager {
    private static Connection connection = Database.getConnection();

    public static void addTeacherProfile(TeacherProfile profile) {
        String insertQuery = "INSERT INTO teacher_profiles (city, email, alternate_number, blood_group, teacher_id) " +
                "VALUES (?, ?, ?, ?, ?) ON CONFLICT (teacher_id) DO NOTHING RETURNING profile_id";
        try (PreparedStatement statement = connection.prepareStatement(insertQuery)) {
            statement.setString(1, profile.getCity());
            statement.setString(2, profile.getEmail());
            statement.setString(3, profile.getAlternateNumber());
            statement.setString(4, profile.getBloodGroup());
            statement.setInt(5, profile.getTeacherId());
            ResultSet rs = statement.executeQuery();
            if (rs.next()) {
                int profileId = rs.getInt("profile_id");
                profile.setProfileId(profileId);
                System.out.println("Teacher profile added successfully! (ProfileId: " + profileId + ")");
            } else {
                System.out.println("Teacher profile NOT added. Duplicate email or profile for this teacher exists.");
                profile.setProfileId(0);
            }
        } catch (SQLException exception) {
            System.out.println("Error adding teacher profile: " + exception.getMessage());
            profile.setProfileId(0);
        }
    }

    public static TeacherProfile getTeacherProfileByTeacherId(int teacherId) {
        String selectQuery = "SELECT * FROM teacher_profiles WHERE teacher_id = ?";
        try (PreparedStatement statement = connection.prepareStatement(selectQuery)) {
            statement.setInt(1, teacherId);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                TeacherProfile profile = new TeacherProfile(
                        resultSet.getString("city"),
                        resultSet.getString("email"),
                        resultSet.getString("alternate_number"),
                        resultSet.getString("blood_group"),
                        resultSet.getInt("teacher_id")
                );
                profile.setProfileId(resultSet.getInt("profile_id"));
                return profile;
            }
        } catch (SQLException exception) {
            System.out.println("Error fetching teacher profile: " + exception.getMessage());
        }
        return null;
    }

    public static void updateTeacherProfile(TeacherProfile profile) {
        String updateQuery = "UPDATE teacher_profiles SET city=?, email=?, alternate_number=?, blood_group=? WHERE teacher_id=?";
        try (PreparedStatement statement = connection.prepareStatement(updateQuery)) {
            statement.setString(1, profile.getCity());
            statement.setString(2, profile.getEmail());
            statement.setString(3, profile.getAlternateNumber());
            statement.setString(4, profile.getBloodGroup());
            statement.setInt(5, profile.getTeacherId());
            int rowsAffected = statement.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Teacher profile updated successfully!");
            } else {
                System.out.println("No profile found to update for this teacher.");
            }
        } catch (SQLException exception) {
            if ("23505".equals(exception.getSQLState())) {
                System.out.println("Update failed: email already exists for another profile.");
            } else {
                System.out.println("Error updating teacher profile: " + exception.getMessage());
            }
        }
    }
}
