package com.aurionpro.model;

<<<<<<< HEAD
import java.sql.Date;
import java.sql.Timestamp;
=======
import java.util.Date;
>>>>>>> a7da5ca398f61630a65a07f5a722169c6dfee4c6

public class Student {
	private int studentId;
	private String name;
	private String mobileNumber;
	private Date dob;
<<<<<<< HEAD
	private boolean isActive;
	private Timestamp createdAt;
	private Timestamp updatedAt;
	private StudentProfile profile;
=======
>>>>>>> a7da5ca398f61630a65a07f5a722169c6dfee4c6

	public Student() {
	}

<<<<<<< HEAD
	public Student(String name, String mobileNumber, Date dob) {
		this.name = name;
		this.mobileNumber = mobileNumber;
		this.dob = dob;
		this.isActive = true;
	}

	// Getters and Setters ...
=======
	public Student(int studentId, String name, String mobileNumber, Date dob) {
		this.studentId = studentId;
		this.name = name;
		this.mobileNumber = mobileNumber;
		this.dob = dob;
	}

>>>>>>> a7da5ca398f61630a65a07f5a722169c6dfee4c6
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
<<<<<<< HEAD

	public boolean isActive() {
		return isActive;
	}

	public void setActive(boolean active) {
		isActive = active;
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

	public StudentProfile getProfile() {
		return profile;
	}

	public void setProfile(StudentProfile profile) {
		this.profile = profile;
	}

	@Override
	public String toString() {
		return "Student{" + "studentId=" + studentId + ", name='" + name + '\'' + ", mobileNumber='" + mobileNumber
				+ '\'' + ", dob=" + dob + ", isActive=" + isActive + ", createdAt=" + createdAt + ", updatedAt="
				+ updatedAt + ", profile=" + profile + '}';
	}
=======
>>>>>>> a7da5ca398f61630a65a07f5a722169c6dfee4c6
}
