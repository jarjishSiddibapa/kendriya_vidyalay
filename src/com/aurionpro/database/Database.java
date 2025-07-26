package com.aurionpro.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class Database {
	Connection connection;
	public void connect() {
		try {
			Class.forName("org.postgresql.Driver");
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		try {
			connection = DriverManager.getConnection("jdbc:postgresql://localhost:5433/kendriya_vidyalay_db", "postgres",
					"cheeseMuffins@007");
			System.out.println("Connection successful!");
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

}
