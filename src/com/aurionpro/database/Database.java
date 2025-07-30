package com.aurionpro.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import io.github.cdimascio.dotenv.Dotenv;

public class Database {

	private static Connection connection = null;

	public Database() {
		try {
			Dotenv dotenv = Dotenv.load();

			String url = dotenv.get("DB_URL");
			String user = dotenv.get("DB_USER");
			String password = dotenv.get("DB_PASSWORD");

			Class.forName("com.mysql.cj.jdbc.Driver");
			connection = DriverManager.getConnection(url, user, password);
			System.out.println("Connection successful!");
		} catch (ClassNotFoundException | SQLException e) {
			e.printStackTrace();
		}
	}

	public static Connection getConnection() {
		return connection;
	}
}
