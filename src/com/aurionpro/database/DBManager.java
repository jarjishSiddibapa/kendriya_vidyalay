package com.aurionpro.database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.aurionpro.constant.Table;
import com.aurionpro.constant.Table_ID;
import com.aurionpro.constant.Table_Map;

public class DBManager {
	private PreparedStatement prepareStatement;
	private Connection connection ;
	
	public DBManager(Connection connection) {
		this.connection = connection;
	}
	public boolean isMapExist(int id1,int id2,Table_ID id1_name,Table_ID id2_name,Table_Map table_Map) {
		try {
			prepareStatement = connection.prepareStatement("select 1 from "+table_Map+" where " + id1_name + "=? and "+id2_name +"=? and  is_active = TRUE");
			prepareStatement.setInt(1, id1);
			prepareStatement.setInt(2, id2);
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
	
	public boolean isExist(int id ,Table_ID table_id,Table table ) {
		try {

			prepareStatement = connection.prepareStatement("select 1 from "+table+" where " + table_id + "=? and is_active = TRUE");
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
	public void delete(int id, Table_ID table_id, Table table) {
	    try {
	        String sql = "UPDATE " + table + " SET is_active = FALSE WHERE " + table_id + " = ?";
	        prepareStatement = connection.prepareStatement(sql);
	        prepareStatement.setInt(1, id);
	        int update = prepareStatement.executeUpdate();
	        
	        if (update > 0) {
	            System.out.println(update + " record(s) soft-deleted from " + table + " successfully.");
	        } else {
	            System.out.println("No matching record found in " + table + " with ID: " + id);
	        }

	    } catch (SQLException e) {
	        e.printStackTrace();
	    }
	}


	public boolean isStringExist(String str, String column_name, Table table) {
		try {
			prepareStatement = connection.prepareStatement("select 1 from "+table+" where " + column_name + "=? and  is_active = TRUE");
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
