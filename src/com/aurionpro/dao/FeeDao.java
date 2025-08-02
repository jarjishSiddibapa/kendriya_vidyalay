package com.aurionpro.dao;

import com.aurionpro.model.StudentFee;
import com.aurionpro.util.Printer;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class FeeDao {
	private Connection connection;

	public FeeDao(Connection connection) {
		this.connection = connection;
	}

	// View total paid fees (all students & courses)
	public double getTotalPaidFees() {
		String sql = "SELECT SUM(paid_fee) FROM student_fee_map WHERE is_active=TRUE";
		try (PreparedStatement stmt = connection.prepareStatement(sql); ResultSet rs = stmt.executeQuery()) {
			if (rs.next())
				return rs.getDouble(1);
		} catch (SQLException e) {
			Printer.printErrorMessage("Error fetching total paid fees: " + e.getMessage());
		}
		return 0;
	}

	// View total pending fees (all students & courses)
	public double getTotalPendingFees() {
		String sql = "SELECT SUM(pending_fee) FROM student_fee_map WHERE is_active=TRUE";
		try (PreparedStatement stmt = connection.prepareStatement(sql); ResultSet rs = stmt.executeQuery()) {
			if (rs.next())
				return rs.getDouble(1);
		} catch (SQLException e) {
			Printer.printErrorMessage("Error fetching total pending fees: " + e.getMessage());
		}
		return 0;
	}

	// View paid/pending fees for a given student (across all their courses)
	public void showFeesByStudent(int studentId) {
		String sql = "SELECT sf.course_id, c.course_name, sf.paid_fee, sf.pending_fee, (sf.paid_fee + sf.pending_fee) as total_fee "
				+ "FROM student_fee_map sf JOIN courses c ON sf.course_id = c.course_id WHERE sf.student_id = ? AND sf.is_active=TRUE";
		try (PreparedStatement stmt = connection.prepareStatement(sql)) {
			stmt.setInt(1, studentId);
			Printer.printTable(stmt.executeQuery());
		} catch (SQLException e) {
			Printer.printErrorMessage("Error fetching fees by student: " + e.getMessage());
		}
	}

	// View fees for a course (sum of all students)
	public void showFeesByCourse(int courseId) {
		String sql = "SELECT SUM(sf.paid_fee) as total_paid, SUM(sf.pending_fee) as total_pending, SUM(sf.paid_fee+sf.pending_fee) as total_fee "
				+ "FROM student_fee_map sf WHERE sf.course_id = ? AND sf.is_active=TRUE";
		try (PreparedStatement stmt = connection.prepareStatement(sql)) {
			stmt.setInt(1, courseId);
			Printer.printTable(stmt.executeQuery());
		} catch (SQLException e) {
			Printer.printErrorMessage("Error fetching fees by course: " + e.getMessage());
		}
	}

	// Update paid fee for a student-course (simulate payment)
	public boolean updateFeeOfCourse(int studentId, int courseId, double payAmount) {
		String getSql = "SELECT paid_fee, pending_fee FROM student_fee_map WHERE student_id=? AND course_id=? AND is_active=TRUE";
		String updateSql = "UPDATE student_fee_map SET paid_fee=?, pending_fee=?, updated_at=NOW() WHERE student_id=? AND course_id=? AND is_active=TRUE";
		try (PreparedStatement getStmt = connection.prepareStatement(getSql)) {
			getStmt.setInt(1, studentId);
			getStmt.setInt(2, courseId);
			ResultSet rs = getStmt.executeQuery();
			if (rs.next()) {
				double paid = rs.getDouble("paid_fee");
				double pending = rs.getDouble("pending_fee");
				if (payAmount > pending) {
					Printer.printErrorMessage("Paying more than pending fee is not allowed.");
					return false;
				}
				paid += payAmount;
				pending -= payAmount;
				try (PreparedStatement updStmt = connection.prepareStatement(updateSql)) {
					updStmt.setDouble(1, paid);
					updStmt.setDouble(2, pending);
					updStmt.setInt(3, studentId);
					updStmt.setInt(4, courseId);
					int updated = updStmt.executeUpdate();
					Printer.printSuccessMessage("Fee updated successfully! Paid: " + paid + ", Pending: " + pending);
					return updated == 1;
				}
			} else {
				Printer.printErrorMessage("Fee record not found for this student/course.");
			}
		} catch (SQLException e) {
			Printer.printErrorMessage("Error updating fee: " + e.getMessage());
		}
		return false;
	}

	// Calculate total earning (same as total paid)
	public double getTotalEarning() {
		return getTotalPaidFees();
	}
}
