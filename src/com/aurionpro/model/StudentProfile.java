package com.aurionpro.model;

public class StudentProfile {
	private int profileId;
	private String city;
	private String email;
	private String guardianNumber;
	private String bloodGroup;
	private int studentId;

	public StudentProfile() {
	}

	public StudentProfile(String city, String email, String guardianNumber, String bloodGroup, int studentId) {
		this.city = city;
		this.email = email;
		this.guardianNumber = guardianNumber;
		this.bloodGroup = bloodGroup;
		this.studentId = studentId;
	}

	// Getters and Setters ...
	public int getProfileId() {
		return profileId;
	}

	public void setProfileId(int profileId) {
		this.profileId = profileId;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getGuardianNumber() {
		return guardianNumber;
	}

	public void setGuardianNumber(String guardianNumber) {
		this.guardianNumber = guardianNumber;
	}

	public String getBloodGroup() {
		return bloodGroup;
	}

	public void setBloodGroup(String bloodGroup) {
		this.bloodGroup = bloodGroup;
	}

	public int getStudentId() {
		return studentId;
	}

	public void setStudentId(int studentId) {
		this.studentId = studentId;
	}

	@Override
	public String toString() {
		return "StudentProfile{" + "profileId=" + profileId + ", city='" + city + '\'' + ", email='" + email + '\''
				+ ", guardianNumber='" + guardianNumber + '\'' + ", bloodGroup='" + bloodGroup + '\'' + ", studentId="
				+ studentId + '}';
	}
}
