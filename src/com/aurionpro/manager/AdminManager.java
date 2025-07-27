package com.aurionpro.manager;

import java.sql.Connection;
import java.util.Arrays;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;

import com.aurionpro.database.DBManager;
import com.aurionpro.manager.cource.CourseManagement;
import com.aurionpro.manager.teacher.TeacherManagement;
import com.aurionpro.util.Printer;

/**
 * Manages the administrative menu of the application. 
 * Handles navigation to different admin modules.
 */
public class AdminManager {
    private Connection connection;
    private Scanner scanner;
    private DBManager dbManager;

    /**
     * Constructs the AdminManager with required dependencies.
     */
    public AdminManager(Connection connection, Scanner scanner, DBManager dbManager) {
        this.connection = connection;
        this.scanner = scanner;
        this.dbManager = dbManager;
    }

    /**
     * Starts the interactive admin menu loop.
     */
    public void start() {
        while (true) {
            try {
                // Display admin menu using Printer utility
                List<String> adminMenuOptions = Arrays.asList(
                        "1> Student Management",
                        "2> Course Management",
                        "3> Teacher Management",
                        "6> Exit"
                );
                Printer.printMenu("Admin Menu", adminMenuOptions);
                Printer.printPrompt("Enter your choice:");

                int choice = scanner.nextInt();
                scanner.nextLine(); // consume leftover newline

                switch (choice) {
                    case 1:
                        Printer.printInfoMessage("Student Management not yet implemented.");
                        break;
                    case 2:
                        CourseManagement courseManagement = new CourseManagement(connection, scanner, dbManager);
                        courseManagement.start();
                        break;
                    case 3:
                        // Intended for TeacherManagement; currently placeholder
                        Printer.printInfoMessage("Teacher Management not yet implemented.");
                        TeacherManagement teacherManagement = new TeacherManagement(connection, scanner, dbManager);
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
