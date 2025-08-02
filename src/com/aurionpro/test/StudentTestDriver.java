package com.aurionpro.test;

import com.aurionpro.manager.student.StudentManager;
import com.aurionpro.model.Student;
import com.aurionpro.model.StudentProfile;

import java.sql.Date;
import java.util.Scanner;
import java.util.List;

public class StudentTestDriver {

	public static void main(String[] args) {
		Scanner sc = new Scanner(System.in);
		StudentManager studentmanager = new StudentManager();

		try {
			while (true) {
				System.out.println("\n===== Student Operations Menu =====");
				System.out.println("1. Add Student (+ Profile)");
				System.out.println("2. List All Students");
				System.out.println("3. Search Student by ID or Mobile");
				System.out.println("4. Delete Student");
				System.out.println("5. Assign Course To Student");
				System.out.println("6. View Assigned Courses");
				System.out.println("0. Exit");
				System.out.print("Enter choice: ");
				int ch = sc.nextInt();
				sc.nextLine();

				if (ch == 0)
					break;

				switch (ch) {
				case 1:
					System.out.print("Enter Name: ");
					String name = sc.nextLine();
					System.out.print("Enter Mobile: ");
					String mob = sc.nextLine();
					System.out.print("Enter DOB (yyyy-mm-dd): ");
					String dobStr = sc.nextLine();
					System.out.print("Enter City: ");
					String city = sc.nextLine();
					System.out.print("Enter Email: ");
					String email = sc.nextLine();
					System.out.print("Enter Guardian Number: ");
					String guardian = sc.nextLine();
					System.out.print("Enter Blood Group: ");
					String bg = sc.nextLine();

					Student stu = new Student();
					stu.setName(name);
					stu.setMobileNumber(mob);
					stu.setDob(Date.valueOf(dobStr));

					StudentProfile sp = new StudentProfile();
					sp.setCity(city);
					sp.setEmail(email);
					sp.setGuardianNumber(guardian);
					sp.setBloodGroup(bg);

					boolean added = studentmanager.addStudent(stu, sp);
					System.out.println(added ? "Added Successfully" : "Error adding student");
					break;

				case 2:
					List<String> all = studentmanager.showAllStudents();
					if (all.isEmpty())
						System.out.println("No students found.");
					for (String info : all)
						System.out.println(info);
					break;

				case 3:
					System.out.print("Enter Student ID (or 0 if using Mobile): ");
					int sid = sc.nextInt();
					sc.nextLine();
					String mobile = "";
					if (sid == 0) {
						System.out.print("Enter Mobile Number: ");
						mobile = sc.nextLine();
					}
					System.out.println(studentmanager.searchStudent(sid, mobile));
					break;

				case 4:
					System.out.print("Enter Student ID to DELETE: ");
					int delid = sc.nextInt();
					boolean d = studentmanager.deleteStudent(delid);
					System.out.println(d ? "Deleted" : "Not Found");
					break;

				case 5:
					System.out.print("Enter Student ID: ");
					int aSid = sc.nextInt();
					System.out.print("Enter Course ID: ");
					int cid = sc.nextInt();
					boolean asg = studentmanager.assignCourse(aSid, cid);
					System.out.println(asg ? "Course Assigned" : "Assignment Failed (possible duplicate)");
					break;

				case 6:
					System.out.print("Enter Student ID: ");
					int viewid = sc.nextInt();
					List<String> courses = studentmanager.getCoursesByStudent(viewid);
					if (courses.isEmpty())
						System.out.println("No courses for student.");
					for (String cinfo : courses)
						System.out.println(cinfo);
					break;

				default:
					System.out.println("Invalid option.");
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			sc.close();
		}
		System.out.println("Bye.");
	}
}
