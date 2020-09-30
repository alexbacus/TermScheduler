package com.example.alexbacus_termscheduler.Entities;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.example.alexbacus_termscheduler.BasicStatus;

@Entity(tableName = "assessment_table")
public class AssessmentEntity {

    @PrimaryKey()
    private int assessmentID;

    private String assessmentTitle;
    private String type;
    private String dueDate;
    private int courseId;
    private int basicStatus;

    public AssessmentEntity(int assessmentID, String assessmentTitle, String type, String dueDate, int courseId, int basicStatus) {
        this.assessmentID = assessmentID;
        this.assessmentTitle = assessmentTitle;
        this.type = type;
        this.dueDate = dueDate;
        this.courseId = courseId;
        this.basicStatus = basicStatus;
    }

    public int getAssessmentID() { return assessmentID; }

    public void setAssessmentID(int assessmentID) { this.assessmentID = assessmentID; }

    public String getAssessmentTitle() { return assessmentTitle; }

    public void setAssessmentTitle(String assessmentTitle) { this.assessmentTitle = assessmentTitle; }

    public String getType() { return type; }

    public void setType(String type) { this.type = type; }

    public String getDueDate() { return dueDate; }

    public void setDueDate(String startDate) { this.dueDate = startDate; }

    public int getCourseId() { return courseId; }

    public void setCourseId(int courseId) { this.courseId = courseId; }

    public int getBasicStatus() { return basicStatus; }
}
