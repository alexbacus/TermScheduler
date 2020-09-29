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

@Database(entities = {TermEntity.class, CourseEntity.class, AssessmentEntity.class}, version = 10, exportSchema = false)

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

            // If you want to keep data through app restarts,
            // comment out the following code block

            databaseWriteExecutor.execute(() -> {
                TermDAO dao = INSTANCE.termDAO();
                dao.deleteAllTerms();

                TermEntity term = new TermEntity(1,"Hello", "2019-01-01", "2019-02-01", 1);
                dao.insert(term);
                term = new TermEntity(2, "World", "2020-01-01", "2020-02-01", 1);
                dao.insert(term);

                CourseDAO courseDao = INSTANCE.courseDAO();
                courseDao.deleteAllCourses();

                CourseEntity course = new CourseEntity(1, "Course 1", "2019-01-01", "2019-02-01", "In Progress", "Here are some notes", 1,
                        "John Smith", "johnsmith@wgu.edu", "1234567890", 1);
                courseDao.insert(course);
                course = new CourseEntity(2, "Course 2", "2020-01-01", "2020-02-01", "Completed", "Here are some notes", 2,
                        "Alex Jones", "alexjones@wgu.edu", "1234567890",1);
                courseDao.insert(course);

                AssessmentDAO assessmentDao = INSTANCE.assessmentDAO();
                assessmentDao.deleteAllAssessments();

                AssessmentEntity assessment = new AssessmentEntity(1, "Assessment 1", "Objective", "2019-01-01", "2019-02-01", 1, 1);
                assessmentDao.insert(assessment);
                assessment = new AssessmentEntity(2, "Assessment 2", "Performance", "2019-02-01", "2019-03-01", 2, 1);
                assessmentDao.insert(assessment);
            });
        }
    };

}

