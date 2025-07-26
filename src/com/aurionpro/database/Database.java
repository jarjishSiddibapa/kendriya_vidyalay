package com.aurionpro.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class Database {

	private Connection connection = null;

	public void connect() {
		try {
			Class.forName("org.postgresql.Driver");

			connection = DriverManager.getConnection("jdbc:postgresql://localhost:5432/kendriya_vidyalay_db", "postgres",
					"root");
			System.out.println("Connection successful!");

		} catch (ClassNotFoundException | SQLException e) {
			e.printStackTrace();
		}
	}

	
	
	public Connection getConnection() {
		return connection;
	}
}
