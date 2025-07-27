package com.aurionpro.database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.aurionpro.constant.Table;
import com.aurionpro.constant.Table_ID;

public class DBManager {
	private PreparedStatement prepareStatement;
	private Connection connection ;
	
	public DBManager(Connection connection) {
		this.connection = connection;
	}
	
	public boolean isExist(int id ,Table_ID table_id,Table table ) {
		try {
			prepareStatement = connection.prepareStatement("select 1 from "+table+" where " + table_id + "=?");
			prepareStatement.setInt(1, id);

			ResultSet result = prepareStatement.executeQuery();
			if (result.next()) {
				return true;
			}
			return false;
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
	}
	public void delete(int id ,Table_ID table_id,Table table ) {
		try {
				prepareStatement = connection.prepareStatement("delete from "+table+" where "+table_id+" = ?");
				prepareStatement.setInt(1, id);
				int update = prepareStatement.executeUpdate();
				System.out.println(update + " "+table+" Deleted  successfully");
		}catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public boolean isStringExist(String str, String column_name, Table table) {
		try {
			prepareStatement = connection.prepareStatement("select 1 from "+table+" where " + column_name + "=?");
			prepareStatement.setString(1, str);

			ResultSet result = prepareStatement.executeQuery();
			if (result.next()) {
				return true;
			}
			return false;
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
	}
}
