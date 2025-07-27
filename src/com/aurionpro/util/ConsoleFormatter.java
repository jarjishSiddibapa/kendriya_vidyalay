package com.aurionpro.util;

import com.aurionpro.model.Teacher;
import com.aurionpro.model.TeacherProfile;
import java.util.List;

public class ConsoleFormatter {
    public static void printTeacherTable(List<Teacher> teachers) {
        if (teachers == null || teachers.isEmpty()) {
            System.out.println("No teachers found.");
            return;
        }
        System.out.printf("%-5s %-20s %-15s %-12s %-10s %-20s %-20s%n",
                "ID", "Name", "Mobile", "DOB", "Salary", "Created At", "Updated At");
        System.out.println("-----------------------------------------------------------------------------------------------");
        for (Teacher t : teachers) {
            System.out.printf("%-5d %-20s %-15s %-12s %-10.2f %-20s %-20s%n",
                    t.getTeacherId(), t.getName(), t.getMobileNumber(), t.getDob(),
                    t.getSalary(), t.getCreatedAt(), t.getUpdatedAt());
        }
    }

    public static void printTeacherDetails(Teacher teacher) {
        if (teacher == null) {
            System.out.println("No teacher found.");
            return;
        }
        System.out.println(teacher);
    }

    public static void printTeacherProfile(TeacherProfile profile) {
        if (profile == null) {
            System.out.println("Profile not found.");
            return;
        }
        System.out.println(profile);
    }

    public static void printMessage(String message) {
        System.out.println(message);
    }
}
