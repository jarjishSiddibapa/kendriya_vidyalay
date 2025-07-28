package com.aurionpro.controller;

import java.sql.Connection;
import java.util.Arrays;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;

import com.aurionpro.dao.CourseDao;
import com.aurionpro.database.DBManager;
import com.aurionpro.util.Printer;

public class SchoolController {
	    private Connection connection;
	    private Scanner scanner;
	    private DBManager dbManager;
	    public SchoolController(Connection connection, Scanner scanner, DBManager dbManager) {
	        this.connection = connection;
	        this.scanner = scanner;
	        this.dbManager = dbManager;
	    }
	    
	    public void start() {
	        while (true) {
	            try {
	                List<String> adminMenuOptions = Arrays.asList(
	                        "1> Student Management",
	                        "2> Course Management",
	                        "3> Teacher Management",
	                        "6> Exit"
	                );
	                Printer.printMenu("Admin Menu", adminMenuOptions);
	                Printer.printPrompt("Enter your choice:");

	                int choice = scanner.nextInt();
	                scanner.nextLine();

	                switch (choice) {
	                    case 1:
	                        Printer.printInfoMessage("Student Management not yet implemented.");
	                        break;
	                    case 2:
	                    	CourseController courseController = new CourseController(connection, scanner, dbManager);
	                    	courseController.start();
	                        break;
	                    case 3:
	                        Printer.printInfoMessage("Teacher Management not yet implemented.");
	                        break;
	                    case 6:
	                        Printer.printSuccessMessage("Exiting...");
	                        return;
	                    default:
	                        Printer.printErrorMessage("Unexpected value: " + choice + ". Please choose a valid option.");
	                }
	            } catch (InputMismatchException e) {
	                Printer.printErrorMessage("Please enter numbers where required.");
	                scanner.nextLine(); // clear invalid input
	            } catch (Exception e) {
	                Printer.printErrorMessage("Something went wrong: " + e.getMessage());
	                e.printStackTrace();
	            }
	        }
	    }
}
