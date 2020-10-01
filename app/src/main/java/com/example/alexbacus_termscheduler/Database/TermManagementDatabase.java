package com.example.alexbacus_termscheduler.Database;

import android.content.Context;
import android.os.AsyncTask;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.example.alexbacus_termscheduler.DAO.AssessmentDAO;
import com.example.alexbacus_termscheduler.DAO.CourseDAO;
import com.example.alexbacus_termscheduler.DAO.TermDAO;
import com.example.alexbacus_termscheduler.Entities.AssessmentEntity;
import com.example.alexbacus_termscheduler.Entities.CourseEntity;
import com.example.alexbacus_termscheduler.Entities.TermEntity;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Database(entities = {TermEntity.class, CourseEntity.class, AssessmentEntity.class}, version = 13, exportSchema = false)

public abstract class TermManagementDatabase extends RoomDatabase {
    public abstract TermDAO termDAO();
    public abstract CourseDAO courseDAO();
    public abstract AssessmentDAO assessmentDAO();

    private static volatile TermManagementDatabase INSTANCE;
    private static final int NUMBER_OF_THREADS = 4;
    static final ExecutorService databaseWriteExecutor = Executors.newFixedThreadPool(NUMBER_OF_THREADS);

    static TermManagementDatabase getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (TermManagementDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            TermManagementDatabase.class, "term_management_database")
                            .fallbackToDestructiveMigration()
                            .addCallback(sRoomDatabaseCallback)
                            .build();
                }
            }
        }
        return INSTANCE;
    }

    private static RoomDatabase.Callback sRoomDatabaseCallback = new RoomDatabase.Callback() {
        @Override
        public void onOpen(@NonNull SupportSQLiteDatabase db) {
            super.onOpen(db);
        }
    };

}

