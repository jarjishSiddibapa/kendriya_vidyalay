package com.aurionpro.test;

import java.util.Scanner;

import com.aurionpro.controller.SchoolController;
import com.aurionpro.database.DBManager;
import com.aurionpro.database.Database;

public class KendriyaVidyalayTestDriver {

	public static void main(String[] args) {
		System.out.println("Welcome to Kendriya Vidyalay. ❤️");
		Database database = new Database();

		Scanner scanner = new Scanner(System.in);
		DBManager dbManager = new DBManager(database.getConnection());
		SchoolController schoolController = new SchoolController(database.getConnection(), scanner, dbManager);
		schoolController.start();
	}

}


