package com.example.alexbacus_termscheduler.Database;

import android.app.Application;
import androidx.lifecycle.LiveData;
import java.util.List;

import com.example.alexbacus_termscheduler.DAO.CourseDAO;
import com.example.alexbacus_termscheduler.DAO.TermDAO;
import com.example.alexbacus_termscheduler.Entities.CourseEntity;
import com.example.alexbacus_termscheduler.Entities.TermEntity;

public class TermManagementRepository {
    private TermDAO mTermDao;
    private CourseDAO mCourseDao;
    private LiveData<List<TermEntity>> mAllTerms;
    private LiveData<List<CourseEntity>> mAllCourses;
    private LiveData<List<CourseEntity>> mAssociatedCourses;
//    private int termId;

    public TermManagementRepository(Application application) {
        TermManagementDatabase db = TermManagementDatabase.getDatabase(application);
        mTermDao = db.termDAO();
        mAllTerms = mTermDao.getAllTerms();
        mCourseDao = db.courseDAO();
        mAllCourses = mCourseDao.getAllCourses();
//        mAssociatedCourses = mCourseDao.getAllAssociatedCourses(termId);
    }

    public LiveData<List<TermEntity>> getAllTerms() {
        return mAllTerms;
    }

    public LiveData<List<CourseEntity>> getAllCourses() { return mAllCourses; }

//    public LiveData<List<CourseEntity>> getAllAssociatedCourses(int termId) { return mAssociatedCourses; }

    public void insert(TermEntity term) {
        TermManagementDatabase.databaseWriteExecutor.execute(() -> {
            mTermDao.insert(term);
        });
    }

    public void insert(CourseEntity course) {
        TermManagementDatabase.databaseWriteExecutor.execute(() -> {
            mCourseDao.insert(course);
        });
    }
}

