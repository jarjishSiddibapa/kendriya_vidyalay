package com.aurionpro.model;

public class Course {
	private String courseName;
	private int coursefees;
	private String courseDescription;
	

	public Course() {
		super();
	}

	public Course(String courseName, int coursefees, String courseDescription) {
		super();
		this.courseName = courseName;
		this.coursefees = coursefees;
		this.courseDescription = courseDescription;
	}

	public String getCourseName() {
		return courseName;
	}

	public void setCourseName(String courseName) {
		this.courseName = courseName;
	}

	public int getCoursefees() {
		return coursefees;
	}

	public void setCoursefees(int coursefees) {
		this.coursefees = coursefees;
	}

	public String getCourseDescription() {
		return courseDescription;
	}

	public void setCourseDescription(String courseDescription) {
		this.courseDescription = courseDescription;
	}

	
}
