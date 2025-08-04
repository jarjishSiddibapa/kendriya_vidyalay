package com.aurionpro.model;

import java.sql.Timestamp;

public class TeacherSubjectMap {
	private int teacherId;
	private int subjectId;
	private boolean isActive;
	private Timestamp createdAt;
	private Timestamp updatedAt;

	public TeacherSubjectMap(int teacherId, int subjectId, boolean isActive, Timestamp createdAt, Timestamp updatedAt) {
		this.teacherId = teacherId;
		this.subjectId = subjectId;
		this.isActive = isActive;
		this.createdAt = createdAt;
		this.updatedAt = updatedAt;
	}

	public TeacherSubjectMap(int teacherId, int subjectId) {
		this.teacherId = teacherId;
		this.subjectId = subjectId;
		this.isActive = true;
	}

	public int getTeacherId() {
		return teacherId;
	}

	public void setTeacherId(int teacherId) {
		this.teacherId = teacherId;
	}

	public int getSubjectId() {
		return subjectId;
	}

	public void setSubjectId(int subjectId) {
		this.subjectId = subjectId;
	}

	public boolean isActive() {
		return isActive;
	}

	public void setActive(boolean isActive) {
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
}
