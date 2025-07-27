package com.aurionpro.model;

public class TeacherSubjectMap {
	private int teacherId;
	private int subjectId;

	public TeacherSubjectMap(int teacherId, int subjectId) {
		super();
		this.teacherId = teacherId;
		this.subjectId = subjectId;
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
}
