package com.aurionpro.model;

import java.sql.Date;
import java.sql.Timestamp;

public class Teacher {
    private int teacherId; // auto-assigned by DB
    private String name;
    private String mobileNumber;
    private Date dob;
    private double salary;
    private Timestamp createdAt; // auto-assigned by DB
    private Timestamp updatedAt; // auto-assigned by DB
    private TeacherProfile profile; // has a relationship!!!
	
	public Teacher() {
		super();
	}
	
    public Teacher(String name, String mobileNumber, Date dob, double salary) {
        this.name = name;
        this.mobileNumber = mobileNumber;
        this.dob = dob;
        this.salary = salary;
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

	public Date getDob() {
		return dob;
	}

	public void setDob(Date dob) {
		this.dob = dob;
	}

	public double getSalary() {
		return salary;
	}

	public void setSalary(double salary) {
		this.salary = salary;
	}

	public Timestamp getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(Timestamp createdAt) {
		this.createdAt = createdAt;
	}

	public Timestamp getUpdatedAt() {
		return updatedAt;
	}

	public void setUpdatedAt(Timestamp updatedAt) {
		this.updatedAt = updatedAt;
	}
	
	public TeacherProfile getProfile() {
		return profile;
	}
	
	public void setProfile(TeacherProfile profile) {
		this.profile = profile; 
	}
    
	@Override
    public String toString() {
        return "Teacher{" +
                "teacherId=" + teacherId +
                ", name='" + name + '\'' +
                ", mobileNumber='" + mobileNumber + '\'' +
                ", dob=" + dob +
                ", salary=" + salary +
                ", createdAt=" + createdAt +
                ", updatedAt=" + updatedAt +
                '}';
    }
}