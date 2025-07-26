package com.aurionpro.model;

import java.util.Date;

public class Student {
	private int studentId;
	private String name;
	private String mobileNumber;
	private Date dob;

	public Student() {
	}

	public Student(int studentId, String name, String mobileNumber, Date dob) {
		this.studentId = studentId;
		this.name = name;
		this.mobileNumber = mobileNumber;
		this.dob = dob;
	}

	public int getStudentId() {
		return studentId;
	}

	public void setStudentId(int studentId) {
		this.studentId = studentId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getMobileNumber() {
		return mobileNumber;
	}

	public void setMobileNumber(String mobileNumber) {
		this.mobileNumber = mobileNumber;
	}

	public Date getDob() {
		return dob;
	}

	public void setDob(Date dob) {
		this.dob = dob;
	}
}
