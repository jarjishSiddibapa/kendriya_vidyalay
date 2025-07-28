package com.aurionpro.controller;

import java.sql.Connection;
import java.util.InputMismatchException;
import java.util.Scanner;

import com.aurionpro.dao.CourseDao;
import com.aurionpro.database.DBManager;
import com.aurionpro.model.Course;
import com.aurionpro.model.Subject;

public class CourseController {
	    private CourseDao courseDao = null;
	   
	    private Scanner scanner;
	    public CourseController(Connection connection, Scanner scanner, DBManager dbManager) {
	        this.scanner = scanner;
	        courseDao = new CourseDao(connection, dbManager);
	    }
	public void start() {

		while (true) {
			try {
				System.out.println();

				System.out.println("1> Show All Courses");
				System.out.println("2> Add New Courses");
				System.out.println("3> Add New Subject");
				System.out.println("4> Add Subject to Course");
				System.out.println("5> Delete Course");
				System.out.println("6> View Subjects of Course");
				System.out.println("7> View Students of Course");
				System.out.println("8> Show All Subjects");
				System.out.println("9> Delete Subject");
				System.out.println("10> Delete Subject from Course");
				System.out.println("11> Assign/Update Subject Teacher for Course");
				System.out.println("12> UnAssign Subject Teacher for Course");
				System.out.println("13> Exit");

				int input =  scanner.nextInt();
				switch (input) {
				case 1: {
					showAllCourse();
					break;
				}
				case 2: {
					addNewCourse();
					break;
				}
				case 3: {
					addNewSubject();
					break;
				}
				case 4: {
					addSubjectToCourse();
					break;
				}
				case 5: {
					deleteCourse();
					break;
				}
				case 6: {
					showSubjectOfCourse();
					break;
				}
				case 7: {
					showStudentsOfCourse();
					break;
				}
				case 8: {
					showAllSubject();
					break;
				}
				case 9: {
					deleteSubject();
					break;
				}
				case 10: {
					deleteSubjectFromCourse();
					break;
				}
				case 11: {
					assignTeacherToCourseSubject();
					break;
				}
				case 12: {
					unassignTeacherFromCourseSubject();
					break;
				}
				case 13: {
					System.out.println("Exiting From Course Management ... ");
					return;
				}
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
	
	private void deleteSubject() {
		showAllSubject();
		try {
			System.out.print("Enter Subject ID: ");
			int id = scanner.nextInt();
			courseDao.deleteSubject(id);

		} catch (InputMismatchException e) {
			System.out.println("\nPlease enter numbers where required.");
			scanner.nextLine();
		} catch (Exception e) {
			System.out.println("\nSomething went wrong: " + e.getMessage());
			e.printStackTrace();
		}
	}
	private void showAllSubject() {
		courseDao.showAllSubject();
	}
	private void showStudentsOfCourse() {
		try {
			System.out.println();
			System.out.print("Enter Course ID: ");
			int courseId = scanner.nextInt();
			courseDao.showStudentsOfCourse(courseId);
		} catch (InputMismatchException e) {
			System.out.println("Invalid input format. Please enter numbers only.");
			scanner.next();
		} catch (Exception e) {
			System.out.println("An unexpected error occurred.");
			e.printStackTrace();
		}
	}
	private void showSubjectOfCourse() {
		try {
			System.out.println();
			System.out.print("Enter Course ID: ");
			int courseId = scanner.nextInt();
			courseDao.showSubjectOfCourse(courseId);
		} catch (InputMismatchException e) {
			System.out.println("Invalid input format. Please enter numbers only.");
			scanner.next();
		} catch (Exception e) {
			System.out.println("An unexpected error occurred.");
			e.printStackTrace();
		}
	}
	private void deleteCourse() {
		showAllCourse();
		try {
			System.out.print("Enter Course ID: ");
			int id = scanner.nextInt();
			System.out.println();
			courseDao.deleteCourse(id);

		} catch (InputMismatchException e) {
			System.out.println("\nPlease enter numbers where required.");
			scanner.nextLine();
		} catch (Exception e) {
			System.out.println("\nSomething went wrong: " + e.getMessage());
			e.printStackTrace();
		}
	}
	private void addSubjectToCourse() {
		try {
			System.out.println();
			System.out.print("Enter Course ID: ");
			int courseId = scanner.nextInt();

			if (!courseDao.isCourseExist(courseId)) {
				System.out.println("Course does not exist.");
				return;
			}

			while (true) {
				showAllSubject();
				System.out.println("Enter Subject ID to add to this course:");
				System.out.println("Enter 0 to exit.");
				int subjectId = scanner.nextInt();

				if (subjectId == 0) {
					return;
				}

				if (!courseDao.isSubjectExist(courseId)) {
					System.out.println("Subject does not exist.");
					continue;
				}

				if (courseDao.isSubjectCourseMapExist(subjectId, courseId)) {
					System.out.println("This subject is already assigned to this course.");
					continue;
				}

				courseDao.addSubjectToCourse(subjectId,courseId);
			}

		} catch (InputMismatchException e) {
			System.out.println("Invalid input format. Please enter numbers only.");
			scanner.next();
		} catch (Exception e) {
			System.out.println("An unexpected error occurred.");
			e.printStackTrace();
		}

	    	
	}
	
	private void deleteSubjectFromCourse() {
		try {
			System.out.println();
			System.out.print("Enter Course ID: ");
			int courseId = scanner.nextInt();

			if (!courseDao.isCourseExist(courseId)) {
				System.out.println("Course does not exist.");
				return;
			}

			while (true) {
				courseDao.showSubjectOfCourse(courseId);
				System.out.println();
				System.out.println("Enter Subject ID to Delete from this course:");
				System.out.println("Enter 0 to exit.");
				int subjectId = scanner.nextInt();

				if (subjectId == 0) {
					return;
				}

				if (!courseDao.isSubjectCourseMapExist(subjectId, courseId)) {
					System.out.println("This subject is not exist in course.");
					continue;
				}
				courseDao.deleteSubjectFromCourse(subjectId, courseId);
			}

		} catch (InputMismatchException e) {
			System.out.println("Invalid input format. Please enter numbers only.");
			scanner.next();
		} catch (Exception e) {
			System.out.println("An unexpected error occurred.");
			e.printStackTrace();
		}

	    	
	}

	private void addNewSubject() {
		try {
			System.out.print("Enter Subject Name: ");
			String subjectName = scanner.next().trim();
			
			Subject subject = new Subject(subjectName);
			courseDao.addNewSubject(subject);
		}
		catch (InputMismatchException e) {
			System.out.println("Invalid input format.");
		} catch (Exception e) {
			System.out.println("Unexpected error occurred.");
			e.printStackTrace();
		}
		
	}
	private void addNewCourse() {
		try {
			System.out.print("Enter Course Name: ");
			String courseName = scanner.next();

			System.out.print("Enter Course Fees: ");
			int courseFees = scanner.nextInt();

			System.out.print("Enter Course Description: ");
			String courseDescription = scanner.next();
			Course course = new Course(courseName,courseFees,courseDescription);
			courseDao.addNewCourse(course);
		}
		catch (InputMismatchException e) {
			System.out.println("Invalid input format.");
		} catch (Exception e) {
			System.out.println("Unexpected error occurred.");
			e.printStackTrace();
		}
		
	}
	
	
	
	private void assignTeacherToCourseSubject() {
		try {
			System.out.println();
			System.out.print("Enter Course ID: ");
			int courseId = scanner.nextInt();

			if (!courseDao.isCourseExist(courseId)) {
				System.out.println("Course does not exist.");
				return;
			}

			while (true) {
				courseDao.showSubjectAndTeacherOfCourse(courseId);
				System.out.println();
				System.out.println("Enter Subject ID to Update Teacher:");
				System.out.println("Enter 0 to exit.");
				int subjectId = scanner.nextInt();
				if (subjectId == 0) {
					return;
				}

				if (!courseDao.isSubjectCourseMapExist(subjectId, courseId)) {
					System.out.println("This subject is not exist in course.");
					continue;
				}
				
				if (courseDao.isTeacherSubjectCourseMapExist(subjectId, courseId)) {
				    System.out.println("Teacher already assigned to this subject, To Update 1st Unassign Teacher");
				    continue;
				}
				courseDao.showAllTeacherForSubject(subjectId);
				System.out.println("Enter Teacher ID to Add Teacher:");
				System.out.println("Enter 0 to exit.");
				int teacherId = scanner.nextInt();
				
				if (!courseDao.isTeacherSubejctMapExist(subjectId, teacherId)) {
					System.out.println("This Teacher is not exist in this Subject.");
					continue;
				}
				
			
				courseDao.assignTeacherToCourseSubject(teacherId,courseId,subjectId);
			}

		} catch (InputMismatchException e) {
			System.out.println("Invalid input format. Please enter numbers only.");
			scanner.next();
		} catch (Exception e) {
			System.out.println("An unexpected error occurred.");
			e.printStackTrace();
		}

	    	
	}
	
	
	private void unassignTeacherFromCourseSubject() {
		try {
			System.out.println();
			System.out.print("Enter Course ID: ");
			int courseId = scanner.nextInt();

			if (!courseDao.isCourseExist(courseId)) {
				System.out.println("Course does not exist.");
				return;
			}

			while (true) {
				courseDao.showSubjectAndTeacherOfCourse(courseId);
				System.out.println();
				System.out.println("Enter Subject ID to Update Teacher:");
				System.out.println("Enter 0 to exit.");
				int subjectId = scanner.nextInt();
				if (subjectId == 0) {
					return;
				}

				if (!courseDao.isSubjectCourseMapExist(subjectId, courseId)) {
					System.out.println("This subject is not exist in course.");
					continue;
				}
				courseDao.unassignTeacherFromCourseSubject(courseId, subjectId);
			}

		} catch (InputMismatchException e) {
			System.out.println("Invalid input format. Please enter numbers only.");
			scanner.next();
		} catch (Exception e) {
			System.out.println("An unexpected error occurred.");
			e.printStackTrace();
		}

	}
	private void showAllCourse() {
		courseDao.showAllCourse();
	}
	
	
	
}
