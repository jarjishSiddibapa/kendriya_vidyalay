package com.aurionpro.manager.teacher;

import java.sql.Connection;
import java.sql.Date;
import java.util.Arrays;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;

import com.aurionpro.database.DBManager;
import com.aurionpro.model.Teacher;
import com.aurionpro.util.Printer;

/**
 * Handles interactive menu-based management for teachers.
 */
public class TeacherManagement {
    private final Connection connection;
    private final Scanner scanner;
    private final DBManager dbManager;

    public TeacherManagement(Connection connection, Scanner scanner, DBManager dbManager) {
        this.connection = connection;
        this.scanner = scanner;
        this.dbManager = dbManager;
        start(); // Automatically launch when instantiated
    }

    public void start() {
        while (true) {
            try {
                List<String> teacherMenu = Arrays.asList(
                        "1> Add Teacher",
                        "2> Show All Teachers",
                        "3> Show Teacher by ID",
                        "4> Update Teacher",
                        "5> Delete Teacher",
                        "6> Back"
                );
                Printer.printMenu("Teacher Management", teacherMenu);
                Printer.printPrompt("Enter your choice: ");
                int choice = scanner.nextInt();
                scanner.nextLine();

                switch (choice) {
                    case 1: addTeacher(); break;
                    case 2: showAllTeachers(); break;
                    case 3: showTeacherById(); break;
                    case 4: updateTeacherMenu(); break;
                    case 5: deleteTeacher(); break;
                    case 6:
                        Printer.printInfoMessage("Returning to Admin Menu...");
                        return;
                    default:
                        Printer.printErrorMessage("Invalid option. Please choose from the menu.");
                }
            } catch (InputMismatchException e) {
                Printer.printErrorMessage("Please enter a valid integer choice.");
                scanner.nextLine();
            } catch (Exception e) {
                Printer.printErrorMessage("Error: " + e.getMessage());
                e.printStackTrace();
            }
        }
    }

    private void addTeacher() {
        Printer.printHeader("Add New Teacher");
        try {
            Printer.printPrompt("Enter name: ");
            String name = scanner.nextLine();
            Printer.printPrompt("Enter mobile number: ");
            String mobile = scanner.nextLine();
            Printer.printPrompt("Enter date of birth (yyyy-mm-dd): ");
            String dobStr = scanner.nextLine();
            Printer.printPrompt("Enter salary: ");
            double salary = scanner.nextDouble();
            scanner.nextLine();

            Date dob = Date.valueOf(dobStr);
            Teacher t = new Teacher(name, mobile, dob, salary);
            TeacherManager.addTeacher(t);

            if (t.getTeacherId() > 0) {
                Printer.printSuccessMessage("Teacher added with ID: " + t.getTeacherId());
            } else {
                Printer.printErrorMessage("Failed to add teacher.");
            }
        } catch (IllegalArgumentException e) {
            Printer.printErrorMessage("Invalid date format. Please use yyyy-MM-dd.");
            scanner.nextLine();
        } catch (InputMismatchException e) {
            Printer.printErrorMessage("Invalid input. Salary must be a number.");
            scanner.nextLine();
        }
    }

    private void showAllTeachers() {
        Printer.printHeader("All Teachers");
        List<Teacher> teachers = TeacherManager.getAllTeachers();
        if (teachers.isEmpty()) {
            Printer.printInfoMessage("No teachers found.");
        } else {
            for (Teacher t : teachers) {
                Printer.printTeacher(t);
            }
        }
    }

    private void showTeacherById() {
        Printer.printPrompt("Enter teacher ID: ");
        try {
            int id = scanner.nextInt();
            scanner.nextLine();
            Teacher teacher = TeacherManager.getTeacherById(id);
            if (teacher != null) {
                Printer.printTeacher(teacher);
            } else {
                Printer.printInfoMessage("No teacher found with ID " + id + ".");
            }
        } catch (InputMismatchException e) {
            Printer.printErrorMessage("Invalid teacher ID.");
            scanner.nextLine();
        }
    }

    private void deleteTeacher() {
        Printer.printPrompt("Enter teacher ID to delete: ");
        try {
            int id = scanner.nextInt();
            scanner.nextLine();
            TeacherManager.deleteTeacher(id);
        } catch (InputMismatchException e) {
            Printer.printErrorMessage("Invalid teacher ID.");
            scanner.nextLine();
        }
    }

    private void updateTeacherMenu() {
        Printer.printHeader("Update Teacher");
        Printer.printPrompt("Enter teacher ID to update: ");
        try {
            int id = scanner.nextInt();
            scanner.nextLine();

            Teacher teacher = TeacherManager.getTeacherById(id);
            if (teacher == null) {
                Printer.printInfoMessage("No teacher found with ID " + id + ".");
                return;
            }

            List<String> updateOptions = Arrays.asList(
                    "1> Name",
                    "2> Mobile Number",
                    "3> Date of birth",
                    "4> Salary",
                    "5> Cancel"
            );
            Printer.printMenu("What do you want to update?", updateOptions);
            Printer.printPrompt("Enter your choice: ");
            int choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {
                case 1:
                    Printer.printPrompt("Enter new name: ");
                    String newName = scanner.nextLine();
                    TeacherManager.updateTeacherName(id, newName);
                    break;
                case 2:
                    Printer.printPrompt("Enter new mobile number: ");
                    String newMobile = scanner.nextLine();
                    TeacherManager.updateTeacherMobileNumber(id, newMobile);
                    break;
                case 3:
                    Printer.printPrompt("Enter new date of birth (yyyy-mm-dd): ");
                    String newDob = scanner.nextLine();
                    TeacherManager.updateTeacherDateOfBirth(id, Date.valueOf(newDob));
                    break;
                case 4:
                    Printer.printPrompt("Enter new salary: ");
                    double newSalary = scanner.nextDouble();
                    scanner.nextLine();
                    TeacherManager.updateTeacherSalary(id, newSalary);
                    break;
                case 5:
                    Printer.printInfoMessage("Update cancelled.");
                    break;
                default:
                    Printer.printErrorMessage("Invalid option.");
            }
        } catch (InputMismatchException e) {
            Printer.printErrorMessage("Invalid input.");
            scanner.nextLine();
        } catch (IllegalArgumentException e) {
            Printer.printErrorMessage("Invalid date format.");
        }
    }
}
