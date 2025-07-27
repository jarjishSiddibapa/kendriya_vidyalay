package com.aurionpro.manager;

import java.sql.Connection;
import java.util.InputMismatchException;
import java.util.Scanner;

import com.aurionpro.database.DBManager;
import com.aurionpro.manager.cource.CourseManagement;

public class AdminManager{
	private Connection connection;
	private Scanner scanner;
	private DBManager dbManager;
	public AdminManager(Connection connection, Scanner scanner, DBManager dbManager) {
		this.connection = connection;
		this.scanner = scanner;
	    this.dbManager =  dbManager;
	}
	
	public void start() {

		while (true) {
			try {
				System.out.println();
				
				System.out.println("1> Student Management");
				System.out.println("2> Courses Management");
				System.out.println("3> teacher Management");
				
				
				
				int input = scanner.nextInt();
				switch (input) {
				case 1: {
					
					break;
				}
				case 2: {
					CourseManagement courseManagement = new CourseManagement(connection,scanner,dbManager);
					courseManagement.start();
					break;
				}
				case 3: {
					CourseManagement courseManagement = new CourseManagement(connection,scanner,dbManager);
					courseManagement.start();
					break;
				}
				case 6: {
					System.out.println("Exiting... ");
					return ;				}
				default:
					throw new IllegalArgumentException("Unexpected value: " + input);
				}

			} catch (InputMismatchException e) {
				System.out.println("\nPlease enter numbers where required.");
				scanner.nextLine();
			} catch (Exception e) {
				System.out.println("\nSomething went wrong: " + e.getMessage());
				e.printStackTrace();
			}

		}
	}

}
