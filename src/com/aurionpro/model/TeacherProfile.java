package com.aurionpro.model;

public class TeacherProfile {
	private int profileId;
	private String city;
	private String email;
	private String alternateNumber;
	private String bloodGroup;
	private int teacherId; 
	
	public TeacherProfile(String city, String email, String alternateNumber, String bloodGroup, int teacherId) {
        this.city = city;
        this.email = email;
        this.alternateNumber = alternateNumber;
        this.bloodGroup = bloodGroup;
        this.teacherId = teacherId;
    }

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

	public String getAlternateNumber() {
		return alternateNumber;
	}

	public void setAlternateNumber(String alternateNumber) {
		this.alternateNumber = alternateNumber;
	}

	public String getBloodGroup() {
		return bloodGroup;
	}

	public void setBloodGroup(String bloodGroup) {
		this.bloodGroup = bloodGroup;
	}

	public int getTeacherId() {
		return teacherId;
	}

	public void setTeacherId(int teacherId) {
		this.teacherId = teacherId;
	}
	
	@Override
	public String toString() {
	    return "TeacherProfile{" +
	            "profileId=" + profileId +
	            ", city='" + city + '\'' +
	            ", email='" + email + '\'' +
	            ", alternateNumber='" + alternateNumber + '\'' +
	            ", bloodGroup='" + bloodGroup + '\'' +
	            ", teacherId=" + teacherId +
	            '}';
	}
}
