package com.aurionpro.test;

import java.util.Scanner;

import com.aurionpro.database.DBManager;
import com.aurionpro.database.Database;
import com.aurionpro.manager.AdminManager;

public class KendriyaVidyalayTestDriver {

	public static void main(String[] args) {
		System.out.println("Welcome to Kendriya Vidyalay. ❤️");
		Database database = new Database();
		database.connect();
		
		Scanner scanner = new Scanner(System.in);
	    DBManager dbManager = new DBManager(database.getConnection());
		AdminManager adminManager = new AdminManager(database.getConnection(),scanner,dbManager);
		adminManager.start();
	}
}
