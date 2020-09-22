package com.example.alexbacus_termscheduler.DAO;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.example.alexbacus_termscheduler.Entities.TermEntity;

import java.util.List;

@Dao
public interface TermDAO {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(TermEntity term);

    @Query("DELETE FROM term_table")
    void deleteAllTerms();

    @Query("SELECT * FROM term_table ORDER BY termID ASC")
    LiveData<List<TermEntity>> getAllTerms();

//    @Query("SELECT * FROM part_table WHERE productID= :prodID ORDER BY partID ASC")
//    LiveData<List<PartEntity>> getAllAssociatedParts(int prodID);
}
