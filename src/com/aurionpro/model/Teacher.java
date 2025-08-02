package com.aurionpro.model;

import java.sql.Date;
import java.sql.Timestamp;

public class Teacher {
    private int teacherId;              // Auto-assigned by DB
    private String name;
    private String mobileNumber;
    private Date dateOfBirth;
    private double salary;
    private boolean isActive;
    private Timestamp createdAt;        // Auto-assigned by DB
    private Timestamp updatedAt;        // Auto-assigned by DB
    private TeacherProfile profile;     // Optional relationship

    public Teacher() {
    }

    // Constructor for creating a new Teacher
    public Teacher(String name, String mobileNumber, Date dateOfBirth, double salary) {
        this.name = name;
        this.mobileNumber = mobileNumber;
        this.dateOfBirth = dateOfBirth;
        this.salary = salary;
    }

    // Constructor with all fields
    public Teacher(int teacherId, String name, String mobileNumber, Date dateOfBirth,
                   double salary, boolean isActive, Timestamp createdAt, Timestamp updatedAt) {
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
                ", dateOfBirth=" + dateOfBirth +
                ", salary=" + salary +
                ", isActive=" + isActive +
                ", createdAt=" + createdAt +
                ", updatedAt=" + updatedAt +
                ", profile=" + profile +
                '}';
    }
}
