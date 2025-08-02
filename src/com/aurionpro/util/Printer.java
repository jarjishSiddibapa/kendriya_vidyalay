package com.aurionpro.util;

import java.lang.reflect.Field;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.aurionpro.model.Teacher;

/**
 * Utility class for printing tabular data, entity info, menus, prompts, and
 * messages.
 */
public class Printer {

	/**
	 * Prints a SQL ResultSet as a formatted and aligned table.
	 */
	public static void printTable(ResultSet resultSet) throws SQLException {
		if (resultSet == null) {
			printInfoMessage("No data available (ResultSet is null).");
			return;
		}
		ResultSetMetaData metaData = resultSet.getMetaData();
		int columnCount = metaData.getColumnCount();

		// Determine column names and their widths
		String[] columnNames = new String[columnCount];
		int[] columnWidths = new int[columnCount];
		for (int columnIndex = 1; columnIndex <= columnCount; columnIndex++) {
			columnNames[columnIndex - 1] = metaData.getColumnLabel(columnIndex);
			columnWidths[columnIndex - 1] = columnNames[columnIndex - 1].length();
		}

		// Gather data and dynamically adjust column widths
		List<String[]> allRows = new ArrayList<>();
		while (resultSet.next()) {
			String[] rowData = new String[columnCount];
			for (int columnIndex = 1; columnIndex <= columnCount; columnIndex++) {
				String value = resultSet.getString(columnIndex);
				rowData[columnIndex - 1] = (value != null) ? value : "NULL";
				columnWidths[columnIndex - 1] = Math.max(columnWidths[columnIndex - 1],
						rowData[columnIndex - 1].length());
			}
			allRows.add(rowData);
		}

		// Print header
		StringBuilder header = new StringBuilder();
		for (int columnIndex = 0; columnIndex < columnCount; columnIndex++) {
			header.append(String.format("%-" + (columnWidths[columnIndex] + 2) + "s", columnNames[columnIndex]));
		}
		System.out.println(header);

		// Print separator line
		for (int width : columnWidths) {
			System.out.print("=".repeat(width + 2));
		}
		System.out.println();

		// Print data rows
		for (String[] rowData : allRows) {
			for (int columnIndex = 0; columnIndex < columnCount; columnIndex++) {
				System.out.print(String.format("%-" + (columnWidths[columnIndex] + 2) + "s", rowData[columnIndex]));
			}
			System.out.println();
		}
	}

	/**
	 * Prints a list of Java objects (beans/POJOs) as a formatted table using
	 * reflection.
	 */
	public static <T> void printTable(List<T> objectList) {
		if (objectList == null || objectList.isEmpty()) {
			printInfoMessage("No records found.");
			return;
		}

		Class<?> objectClass = objectList.get(0).getClass();
		Field[] fields = objectClass.getDeclaredFields();

		// Extract field names and initial column widths
		List<String> columnNames = new ArrayList<>();
		List<Integer> columnWidths = new ArrayList<>();
		for (Field field : fields) {
			field.setAccessible(true);
			String fieldName = field.getName();
			columnNames.add(fieldName);
			columnWidths.add(fieldName.length());
		}

		// Build data rows and adjust column widths for each value
		List<String[]> allRows = new ArrayList<>();
		for (T objectInstance : objectList) {
			String[] rowData = new String[fields.length];
			for (int fieldIndex = 0; fieldIndex < fields.length; fieldIndex++) {
				String valueAsString;
				try {
					Object value = fields[fieldIndex].get(objectInstance);
					valueAsString = (value != null) ? value.toString() : "NULL";
				} catch (Exception exception) {
					valueAsString = "ERROR";
				}
				rowData[fieldIndex] = valueAsString;
				columnWidths.set(fieldIndex, Math.max(columnWidths.get(fieldIndex), valueAsString.length()));
			}
			allRows.add(rowData);
		}

		// Print header
		StringBuilder header = new StringBuilder();
		for (int index = 0; index < columnNames.size(); index++) {
			header.append(String.format("%-" + (columnWidths.get(index) + 2) + "s", columnNames.get(index)));
		}
		System.out.println(header);

		// Print separator
		for (Integer width : columnWidths) {
			System.out.print("=".repeat(width + 2));
		}
		System.out.println();

		// Print each row
		for (String[] rowData : allRows) {
			for (int columnIndex = 0; columnIndex < rowData.length; columnIndex++) {
				System.out.print(String.format("%-" + (columnWidths.get(columnIndex) + 2) + "s", rowData[columnIndex]));
			}
			System.out.println();
		}
	}

	/**
	 * Prints a visually distinct section header.
	 */
	public static void printHeader(String msg) {
		System.out.println();
		System.out.println("========== " + msg + " ==========");
	}

	/**
	 * Prints a menu or list of options, optionally with a title.
	 */
	public static void printMenu(String title, List<String> options) {
		if (title != null && !title.isEmpty()) {
			printHeader(title);
		}
		for (String option : options) {
			System.out.println(option);
		}
	}

	/**
	 * Prints an error message.
	 */
	public static void printErrorMessage(String message) {
		System.out.println("[ERROR] " + message);
	}

	/**
	 * Prints an info message.
	 */
	public static void printInfoMessage(String message) {
		System.out.println("[INFO] " + message);
	}

	/**
	 * Prints a success message.
	 */
	public static void printSuccessMessage(String message) {
		System.out.println("[SUCCESS] " + message);
	}

	/**
	 * Prints a prompt for user input (with no newline at end).
	 */
	public static void printPrompt(String prompt) {
		System.out.print(prompt + " ");
	}

	/**
	 * Prints a summary of a single Teacher in a nice format.
	 */
	public static void printTeacher(Teacher t) {
		if (t == null) {
			printInfoMessage("No teacher data to display.");
			return;
		}
		System.out.println("------------------------------");
		System.out.println("ID          : " + t.getTeacherId());
		System.out.println("Name        : " + t.getName());
		System.out.println("Mobile      : " + t.getMobileNumber());

		System.out.println("DOB         : " + t.getDateOfBirth());

		System.out.println("Salary      : " + t.getSalary());
		System.out.println("Created At  : " + t.getCreatedAt());
		System.out.println("Updated At  : " + t.getUpdatedAt());
		System.out.println("------------------------------");
	}
}

