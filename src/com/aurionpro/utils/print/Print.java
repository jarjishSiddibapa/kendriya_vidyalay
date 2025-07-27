package com.aurionpro.utils.print;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class Print {

	public static void printTable(ResultSet resultSet) throws SQLException {
		ResultSetMetaData metaData = resultSet.getMetaData();
		int columnCount = metaData.getColumnCount();

		// 1. Prepare column widths
		int[] columnWidths = new int[columnCount];
		String[] columnNames = new String[columnCount];

		for (int i = 1; i <= columnCount; i++) {
			columnNames[i - 1] = metaData.getColumnLabel(i);
			columnWidths[i - 1] = columnNames[i - 1].length();
		}

		// 2. Find max width for each column
		List<String[]> rows = new ArrayList<>();
		while (resultSet.next()) {
			String[] row = new String[columnCount];
			for (int i = 1; i <= columnCount; i++) {
				String value = resultSet.getString(i);
				row[i - 1] = value != null ? value : "NULL";
				columnWidths[i - 1] = Math.max(columnWidths[i - 1], row[i - 1].length());
			}
			rows.add(row);
		}

		// 3. Print Header
		StringBuilder header = new StringBuilder();
		for (int i = 0; i < columnCount; i++) {
			header.append(String.format("%-" + (columnWidths[i] + 2) + "s", columnNames[i]));
		}
		System.out.println(header);

		// 4. Print separator
		for (int width : columnWidths) {
			System.out.print("=".repeat(width + 2));
		}
		System.out.println();

		// 5. Print Rows
		for (String[] row : rows) {
			for (int i = 0; i < columnCount; i++) {
				System.out.print(String.format("%-" + (columnWidths[i] + 2) + "s", row[i]));
			}
			System.out.println();
		}
	}

}
