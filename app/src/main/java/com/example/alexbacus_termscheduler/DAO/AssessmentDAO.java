package com.example.alexbacus_termscheduler.DAO;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.example.alexbacus_termscheduler.Entities.AssessmentEntity;

import java.util.List;

@Dao
public interface AssessmentDAO {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(AssessmentEntity assessment);

    @Query("DELETE FROM assessment_table")
    void deleteAllAssessments();

    @Query("SELECT * FROM assessment_table ORDER BY assessmentID ASC")
    LiveData<List<AssessmentEntity>> getAllAssessments();

    @Query("UPDATE assessment_table SET basicStatus = 2 WHERE assessmentID == :id")
    void deleteById(int id);
}
