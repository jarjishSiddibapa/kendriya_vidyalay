package com.aurionpro.test;

import java.sql.Date;
import java.util.List;
import java.util.Scanner;

import com.aurionpro.manager.TeacherManager;
import com.aurionpro.manager.TeacherProfileManager;
import com.aurionpro.model.Teacher;
import com.aurionpro.model.TeacherProfile;
import com.aurionpro.util.ConsoleFormatter;
import com.aurionpro.util.ValidationHelper;

public class KendriyaVidyalayTestDriver {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        while (true) {
            System.out.println("\n========== Teacher Management Menu ==========");
            System.out.println("1. Add Teacher (with Profile)");
            System.out.println("2. List All Teachers");
            System.out.println("3. Find Teacher by ID");
            System.out.println("4. Update Teacher Name");
            System.out.println("5. Update Teacher Mobile Number");
            System.out.println("6. Update Teacher Date of Birth");
            System.out.println("7. Update Teacher Salary");
            System.out.println("8. Delete Teacher");
            System.out.println("9. Update Teacher Profile");
            System.out.println("10. View Teacher Profile");
            System.out.println("0. Exit");

            System.out.print("Choose an option: ");
            int option = getValidatedInteger(scanner);

            switch (option) {
                case 1: addTeacherWithProfile(scanner); break;
                case 2: listAllTeachers(); break;
                case 3: findTeacherById(scanner); break;
                case 4: updateTeacherName(scanner); break;
                case 5: updateTeacherMobileNumber(scanner); break;
                case 6: updateTeacherDOB(scanner); break;
                case 7: updateTeacherSalary(scanner); break;
                case 8: deleteTeacher(scanner); break;
                case 9: updateTeacherProfile(scanner); break;
                case 10: viewTeacherProfile(scanner); break;
                case 0:
                    System.out.println("Exiting. Goodbye!");
                    scanner.close();
                    System.exit(0);
                default:
                    ConsoleFormatter.printMessage("Invalid option. Try again.");
            }
        }
    }
    // ---------- Helper input scanner methods ----------

    private static int getValidatedInteger(Scanner scanner) {
        while (true) {
            String value = scanner.nextLine().trim();
            try { return Integer.parseInt(value); }
            catch (NumberFormatException e) {
                ConsoleFormatter.printMessage("Please enter a valid integer option:");
            }
        }
    }

    private static String getNonEmptyInput(Scanner scanner, String prompt) {
        while (true) {
            System.out.print(prompt);
            String input = scanner.nextLine().trim();
            if (ValidationHelper.isNonEmpty(input)) return input;
            ConsoleFormatter.printMessage("This field cannot be empty. Try again:");
        }
    }

    private static String getValidatedMobile(Scanner scanner) {
        while (true) {
            System.out.print("Mobile Number: ");
            String mobile = scanner.nextLine().trim();
            if (ValidationHelper.isValidMobileNumber(mobile)) return mobile;
            ConsoleFormatter.printMessage("Invalid mobile number. It should be 10 digits and start with 6-9. Try again:");
        }
    }

    private static Date getValidatedDate(Scanner scanner) {
        while (true) {
            System.out.print("Date of Birth (YYYY-MM-DD): ");
            String input = scanner.nextLine().trim();
            try { return Date.valueOf(input); }
            catch (IllegalArgumentException e) {
                ConsoleFormatter.printMessage("Invalid date format. Use YYYY-MM-DD.");
            }
        }
    }

    private static double getValidatedPositiveDouble(Scanner scanner, String prompt) {
        while (true) {
            System.out.print(prompt);
            String input = scanner.nextLine().trim();
            try {
                double value = Double.parseDouble(input);
                if (ValidationHelper.isPositiveSalary(value)) return value;
                else ConsoleFormatter.printMessage("Salary must be positive. Try again:");
            } catch (NumberFormatException e) {
                ConsoleFormatter.printMessage("Invalid number. Please enter a valid number:");
            }
        }
    }

    private static String getValidatedEmail(Scanner scanner) {
        while (true) {
            System.out.print("Email: ");
            String email = scanner.nextLine().trim();
            if (ValidationHelper.isValidEmail(email)) return email;
            ConsoleFormatter.printMessage("Invalid email format. Please try again:");
        }
    }

    private static String getValidatedBloodGroup(Scanner scanner) {
        while (true) {
            System.out.print("Blood Group (A+, A-, B+, B-, AB+, AB-, O+, O-): ");
            String bloodGroup = scanner.nextLine().trim().toUpperCase();
            if (ValidationHelper.isValidBloodGroup(bloodGroup)) return bloodGroup;
            ConsoleFormatter.printMessage("Invalid blood group! Try again:");
        }
    }

    // ---------- Main menu operations ----------

    private static void addTeacherWithProfile(Scanner scanner) {
        System.out.println("\n--- Add Teacher ---");
        String name = getNonEmptyInput(scanner, "Name: ");
        String mobile = getValidatedMobile(scanner);
        Date dob = getValidatedDate(scanner);
        double salary = getValidatedPositiveDouble(scanner, "Salary: ");

        Teacher teacher = new Teacher(name, mobile, dob, salary);
        TeacherManager.addTeacher(teacher);
        int teacherId = teacher.getTeacherId();
        if (teacherId == 0) {
            ConsoleFormatter.printMessage("Teacher could not be added - profile creation aborted.");
            return;
        }

        System.out.println("\n--- Add Profile for Teacher ---");
        String city = getNonEmptyInput(scanner, "City: ");
        String email = getValidatedEmail(scanner);
        System.out.print("Alternate Number: ");
        String alternate = scanner.nextLine().trim();
        String bloodGroup = getValidatedBloodGroup(scanner);

        TeacherProfile profile = new TeacherProfile(city, email, alternate, bloodGroup, teacherId);
        TeacherProfileManager.addTeacherProfile(profile);
        teacher.setProfile(profile);
    }

    private static void listAllTeachers() {
        List<Teacher> teachers = TeacherManager.getAllTeachers();
        ConsoleFormatter.printTeacherTable(teachers);
    }

    private static void findTeacherById(Scanner scanner) {
        System.out.print("Enter Teacher ID: ");
        int id = getValidatedInteger(scanner);
        Teacher teacher = TeacherManager.getTeacherById(id);
        if (teacher == null) {
            ConsoleFormatter.printMessage("No teacher found with ID: " + id);
            return;
        }
        TeacherProfile profile = TeacherProfileManager.getTeacherProfileByTeacherId(teacher.getTeacherId());
        teacher.setProfile(profile);
        ConsoleFormatter.printTeacherDetails(teacher);
    }

    private static void updateTeacherName(Scanner scanner) {
        System.out.print("Enter Teacher ID: ");
        int id = getValidatedInteger(scanner);
        String name = getNonEmptyInput(scanner, "New Name: ");
        TeacherManager.updateTeacherName(id, name);
    }

    private static void updateTeacherMobileNumber(Scanner scanner) {
        System.out.print("Enter Teacher ID: ");
        int id = getValidatedInteger(scanner);
        String mobile = getValidatedMobile(scanner);
        TeacherManager.updateTeacherMobileNumber(id, mobile);
    }

    private static void updateTeacherDOB(Scanner scanner) {
        System.out.print("Enter Teacher ID: ");
        int id = getValidatedInteger(scanner);
        Date dob = getValidatedDate(scanner);
        TeacherManager.updateTeacherDateOfBirth(id, dob);
    }

    private static void updateTeacherSalary(Scanner scanner) {
        System.out.print("Enter Teacher ID: ");
        int id = getValidatedInteger(scanner);
        double salary = getValidatedPositiveDouble(scanner, "New Salary: ");
        TeacherManager.updateTeacherSalary(id, salary);
    }

    private static void deleteTeacher(Scanner scanner) {
        System.out.print("Enter Teacher ID: ");
        int id = getValidatedInteger(scanner);
        TeacherManager.deleteTeacher(id);
    }

    private static void updateTeacherProfile(Scanner scanner) {
        System.out.print("Enter Teacher ID: ");
        int teacherId = getValidatedInteger(scanner);
        TeacherProfile profile = TeacherProfileManager.getTeacherProfileByTeacherId(teacherId);
        if (profile == null) {
            ConsoleFormatter.printMessage("Profile not found for this teacher.");
            return;
        }

        System.out.print("New City (leave blank for no change): ");
        String city = scanner.nextLine().trim();
        if (ValidationHelper.isNonEmpty(city)) profile.setCity(city);

        System.out.print("New Email (leave blank for no change): ");
        String email = scanner.nextLine().trim();
        if (ValidationHelper.isNonEmpty(email)) {
            if (ValidationHelper.isValidEmail(email)) profile.setEmail(email);
            else ConsoleFormatter.printMessage("Invalid email! Update skipped.");
        }

        System.out.print("New Alternate Number (leave blank for no change): ");
        String alternate = scanner.nextLine().trim();
        if (ValidationHelper.isNonEmpty(alternate)) profile.setAlternateNumber(alternate);

        System.out.print("New Blood Group (leave blank for no change): ");
        String blood = scanner.nextLine().trim().toUpperCase();
        if (ValidationHelper.isNonEmpty(blood)) {
            if (ValidationHelper.isValidBloodGroup(blood)) profile.setBloodGroup(blood);
            else ConsoleFormatter.printMessage("Invalid blood group! Update skipped.");
        }

        TeacherProfileManager.updateTeacherProfile(profile);
    }

    private static void viewTeacherProfile(Scanner scanner) {
        System.out.print("Enter Teacher ID: ");
        int teacherId = getValidatedInteger(scanner);
        TeacherProfile profile = TeacherProfileManager.getTeacherProfileByTeacherId(teacherId);
        ConsoleFormatter.printTeacherProfile(profile);
    }
}
