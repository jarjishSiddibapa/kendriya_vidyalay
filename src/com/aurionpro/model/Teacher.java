package com.aurionpro.model;

import java.sql.Date;

public class Teacher {
	private int teacherId;
	private String name;
	private String mobileNumber;
	private Date dateOfBirth;
	private double salary;
	private boolean isActive;
	private Date createdAt;
	private Date updatedAt;

	public Teacher() {
	}

	public Teacher(int teacherId, String name, String mobileNumber, Date dateOfBirth, double salary, boolean isActive,
			Date createdAt, Date updatedAt) {
		this.teacherId = teacherId;
		this.name = name;
		this.mobileNumber = mobileNumber;
		this.dateOfBirth = dateOfBirth;
		this.salary = salary;
		this.isActive = isActive;
		this.createdAt = createdAt;
		this.updatedAt = updatedAt;
	}

	public int getTeacherId() {
		return teacherId;
	}

	public void setTeacherId(int teacherId) {
		this.teacherId = teacherId;
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

	public Date getDateOfBirth() {
		return dateOfBirth;
	}

	public void setDateOfBirth(Date dateOfBirth) {
		this.dateOfBirth = dateOfBirth;
	}

	public double getSalary() {
		return salary;
	}

	public void setSalary(double salary) {
		this.salary = salary;
	}

	public boolean getIsActive() {
		return isActive;
	}

	public void setIsActive(boolean isActive) {
		this.isActive = isActive;
	}

	public Date getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(Date createdAt) {
		this.createdAt = createdAt;
	}

	public Date getUpdatedAt() {
		return updatedAt;
	}

	public void setUpdatedAt(Date updatedAt) {
		this.updatedAt = updatedAt;
	}
}
