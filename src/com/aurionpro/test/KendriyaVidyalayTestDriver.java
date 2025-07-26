package com.aurionpro.test;

import com.aurionpro.database.Database;

public class KendriyaVidyalayTestDriver {

	public static void main(String[] args) {
		System.out.println("Welcome to Kendriya Vidyalay. ❤️");
		Database database = new Database();
		database.connect();
	}

}
