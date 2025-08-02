package com.aurionpro.controller;

import com.aurionpro.util.Printer;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DashboardController {
	private final Connection connection;

	public DashboardController(Connection connection) {
		this.connection = connection;
	}

	public void showDashboard() {
		// The following query aggregates all info required in the dashboard.
//		String sql = "SELECT " + "ROW_NUMBER() OVER() as `Sr. No.`, " + "s.student_id AS StudentID, "
//				+ "s.name AS Name, " + "c.course_name AS Course, " + "sf.paid_fee AS `Paid Fee`, "
//				+ "sf.pending_fee AS `Pending Fee`, " + "(sf.paid_fee + sf.pending_fee) AS `Total Fee`, "
//				+ "GROUP_CONCAT(DISTINCT subj.subject_name) AS Subjects, "
//				+ "GROUP_CONCAT(DISTINCT t.name) AS Teachers " + "FROM student_fee_map sf "
//				+ "JOIN students s ON sf.student_id = s.student_id " + "JOIN courses c ON sf.course_id = c.course_id "
//				+ "LEFT JOIN course_subject_map csm ON csm.course_id = c.course_id AND csm.is_active=TRUE "
//				+ "LEFT JOIN subjects subj ON csm.subject_id = subj.subject_id AND subj.is_active=TRUE "
//				+ "LEFT JOIN teacher_course_subject_map tcsm ON tcsm.course_id = c.course_id AND tcsm.subject_id = subj.subject_id AND tcsm.is_active=TRUE "
//				+ "LEFT JOIN teachers t ON tcsm.teacher_id = t.teacher_id AND t.is_active=TRUE "
//				+ "WHERE sf.is_active=TRUE AND s.is_active=TRUE AND c.is_active=TRUE "
//				+ "GROUP BY s.student_id, s.name, c.course_name, sf.paid_fee, sf.pending_fee;";
		
		
		String sql = ""
			    + "SELECT "
			    + "  ROW_NUMBER() OVER (ORDER BY s.student_id, c.course_name) AS sr_no, "
			    + "  s.student_id AS student_id, "
			    + "  s.name AS name, "
			    + "  c.course_name AS course, "
			    + "  sf.paid_fee AS paid_fee, "
			    + "  sf.pending_fee AS pending_fee, "
			    + "  (sf.paid_fee + sf.pending_fee) AS total_fee, "
			    + "  STRING_AGG(DISTINCT subj.subject_name, ', ') FILTER (WHERE subj.subject_name IS NOT NULL) AS subjects, "
			    + "  STRING_AGG(DISTINCT t.name, ', ') FILTER (WHERE t.name IS NOT NULL) AS teachers "
			    + "FROM student_fee_map sf "
			    + "JOIN students s ON sf.student_id = s.student_id "
			    + "JOIN courses c ON sf.course_id = c.course_id "
			    + "LEFT JOIN course_subject_map csm ON csm.course_id = c.course_id AND csm.is_active = TRUE "
			    + "LEFT JOIN subjects subj ON csm.subject_id = subj.subject_id AND subj.is_active = TRUE "
			    + "LEFT JOIN teacher_course_subject_map tcsm "
			    + "    ON tcsm.course_id = c.course_id "
			    + "   AND tcsm.subject_id = subj.subject_id "
			    + "   AND tcsm.is_active = TRUE "
			    + "LEFT JOIN teachers t ON tcsm.teacher_id = t.teacher_id AND t.is_active = TRUE "
			    + "WHERE sf.is_active = TRUE AND s.is_active = TRUE AND c.is_active = TRUE "
			    + "GROUP BY s.student_id, s.name, c.course_name, sf.paid_fee, sf.pending_fee "
			    + "ORDER BY s.student_id, c.course_name;";


		try (PreparedStatement stmt = connection.prepareStatement(sql)) {
			ResultSet rs = stmt.executeQuery();
			Printer.printTable(rs);
		} catch (SQLException e) {
			Printer.printErrorMessage("Error printing dashboard: " + e.getMessage());
		}
		

	}
}
