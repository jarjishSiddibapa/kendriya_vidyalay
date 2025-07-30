package com.aurionpro.model;

public class StudentFee {
	private int studentId;
	private int courseId;
	private double paidFee;
	private double pendingFee;

	public StudentFee(int studentId, int courseId, double paidFee, double pendingFee) {
		this.studentId = studentId;
		this.courseId = courseId;
		this.paidFee = paidFee;
		this.pendingFee = pendingFee;
	}

	public int getStudentId() {
		return studentId;
	}

	public void setStudentId(int studentId) {
		this.studentId = studentId;
	}

	public int getCourseId() {
		return courseId;
	}

	public void setCourseId(int courseId) {
		this.courseId = courseId;
	}

	public double getPaidFee() {
		return paidFee;
	}

	public void setPaidFee(double paidFee) {
		this.paidFee = paidFee;
	}

	public double getPendingFee() {
		return pendingFee;
	}

	public void setPendingFee(double pendingFee) {
		this.pendingFee = pendingFee;
	}
}
