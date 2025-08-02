package com.aurionpro.controller;

import com.aurionpro.dao.FeeDao;
import com.aurionpro.util.Printer;

import java.sql.Connection;
import java.util.InputMismatchException;
import java.util.Scanner;

public class FeeController {
	private final FeeDao feeDao;
	private final Scanner scanner;

	public FeeController(Connection connection, Scanner scanner) {
		this.feeDao = new FeeDao(connection);
		this.scanner = scanner;
	}

	public void start() {
		while (true) {
			System.out.println("\n==== Fees Management ====");
			System.out.println("1> View Total Paid Fees");
			System.out.println("2> View Total Pending Fees");
			System.out.println("3> View Fees By Student");
			System.out.println("4> View Fees By Course");
			System.out.println("5> Update Fees Of A Course");
			System.out.println("6> View Total Earning");
			System.out.println("7> Exit");

			System.out.print("Enter your choice: ");
			int choice;
			try {
				choice = Integer.parseInt(scanner.nextLine().trim());
			} catch (NumberFormatException e) {
				System.out.println("Invalid input.");
				continue;
			}
			switch (choice) {
			case 1:
				System.out.println("Total Paid Fees: " + feeDao.getTotalPaidFees());
				break;
			case 2:
				System.out.println("Total Pending Fees: " + feeDao.getTotalPendingFees());
				break;
			case 3:
				System.out.print("Enter student ID: ");
				int sid = Integer.parseInt(scanner.nextLine().trim());
				feeDao.showFeesByStudent(sid);
				break;
			case 4:
				System.out.print("Enter course ID: ");
				int cid = Integer.parseInt(scanner.nextLine().trim());
				feeDao.showFeesByCourse(cid);
				break;
			case 5:
				System.out.print("Enter student ID: ");
				int stuId = Integer.parseInt(scanner.nextLine().trim());
				System.out.print("Enter course ID: ");
				int courId = Integer.parseInt(scanner.nextLine().trim());
				System.out.print("Enter amount to pay: ");
				double pay = Double.parseDouble(scanner.nextLine().trim());
				feeDao.updateFeeOfCourse(stuId, courId, pay);
				break;
			case 6:
				System.out.println("Total earning: " + feeDao.getTotalEarning());
				break;
			case 7:
				return;
			default:
				System.out.println("Invalid choice.");
			}
		}
	}
}
