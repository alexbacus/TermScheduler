package com.example.alexbacus_termscheduler.DAO;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.example.alexbacus_termscheduler.Entities.CourseEntity;

import java.util.List;

@Dao
public interface CourseDAO {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(CourseEntity course);

    @Query("DELETE FROM course_table")
    void deleteAllCourses();

    @Query("SELECT * FROM course_table ORDER BY courseID ASC")
    LiveData<List<CourseEntity>> getAllCourses();

//    @Query("SELECT * FROM course_table WHERE termId = :termID")
//    LiveData<List<CourseEntity>> getAllAssociatedCourses(int termID);

//    @Query("SELECT * FROM part_table WHERE productID= :prodID ORDER BY partID ASC")
//    LiveData<List<PartEntity>> getAllAssociatedParts(int prodID);
}
