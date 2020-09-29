package com.example.alexbacus_termscheduler.Entities;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "course_table")
public class CourseEntity {

    @PrimaryKey()
    private int courseID;

    private String courseTitle;
    private String startDate;
    private String endDate;
    private String status;
    private String notes;
    private int termId;
    private String mentorName;
    private String mentorEmail;
    private String mentorPhone;
    private int basicStatus;

    public CourseEntity(int courseID, String courseTitle, String startDate, String endDate,
                        String status, String notes, int termId,
                        String mentorName, String mentorEmail, String mentorPhone,
                        int basicStatus) {
        this.courseID = courseID;
        this.courseTitle = courseTitle;
        this.startDate = startDate;
        this.endDate = endDate;
        this.status = status;
        this.notes = notes;
        this.termId = termId;
        this.mentorName = mentorName;
        this.mentorEmail = mentorEmail;
        this.mentorPhone = mentorPhone;
        this.basicStatus = basicStatus;
    }

    public int getCourseID() {
        return courseID;
    }

    public void setCourseID(int courseID) {
        this.courseID = courseID;
    }

    public String getCourseTitle() {
        return courseTitle;
    }

    public void setCourseTitle(String courseTitle) {
        this.courseTitle = courseTitle;
    }

    public String getStartDate() { return startDate; }

    public void setStartDate(String startDate) { this.startDate = startDate; }

    public String getEndDate() { return endDate; }

    public void setEndDate(String endDate) { this.endDate = endDate; }

    public String getStatus() { return status; }

    public void setStatus(String status) { this.status = status; }

    public String getNotes() { return notes; }

    public void setNotes(String notes) { this.notes = notes; }

    public int getTermId() { return termId; }

    public void setTermId(int termId) { this.termId = termId; }

    public int getBasicStatus() { return basicStatus; }

    public String getMentorName() { return mentorName; }

    public void setMentorName(String mentorName) { this.mentorName = mentorName; }

    public String getMentorEmail() { return mentorEmail; }

    public void setMentorEmail(String mentorEmail) { this.mentorEmail = mentorEmail; }

    public String getMentorPhone() { return mentorPhone; }

    public void setMentorPhone(String mentorPhone) { this.mentorPhone = mentorPhone; }
}
