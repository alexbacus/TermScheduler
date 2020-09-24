package com.example.alexbacus_termscheduler.Database;

import android.app.Application;
import androidx.lifecycle.LiveData;
import java.util.List;

import com.example.alexbacus_termscheduler.DAO.AssessmentDAO;
import com.example.alexbacus_termscheduler.DAO.CourseDAO;
import com.example.alexbacus_termscheduler.DAO.TermDAO;
import com.example.alexbacus_termscheduler.Entities.AssessmentEntity;
import com.example.alexbacus_termscheduler.Entities.CourseEntity;
import com.example.alexbacus_termscheduler.Entities.TermEntity;

public class TermManagementRepository {
    private TermDAO mTermDao;
    private CourseDAO mCourseDao;
    private AssessmentDAO mAssessmentDao;
    private LiveData<List<TermEntity>> mAllTerms;
    private LiveData<List<CourseEntity>> mAllCourses;
    private LiveData<List<AssessmentEntity>> mAllAssessments;

    public TermManagementRepository(Application application) {
        TermManagementDatabase db = TermManagementDatabase.getDatabase(application);
        mTermDao = db.termDAO();
        mAllTerms = mTermDao.getAllTerms();
        mCourseDao = db.courseDAO();
        mAllCourses = mCourseDao.getAllCourses();
        mAssessmentDao = db.assessmentDAO();
        mAllAssessments = mAssessmentDao.getAllAssessments();
    }

    public LiveData<List<TermEntity>> getAllTerms() {
        return mAllTerms;
    }

    public LiveData<List<CourseEntity>> getAllCourses() { return mAllCourses; }

    public LiveData<List<AssessmentEntity>> getAllAssessments() { return mAllAssessments; }

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

    public void insert(AssessmentEntity assessment) {
        TermManagementDatabase.databaseWriteExecutor.execute(() -> {
            mAssessmentDao.insert(assessment);
        });
    }

    public void deleteAssessment(int id) {
        TermManagementDatabase.databaseWriteExecutor.execute(() -> {
            mAssessmentDao.deleteById(id);
        });
    }

    public void deleteCourse(int id) {
        TermManagementDatabase.databaseWriteExecutor.execute(() -> {
            mCourseDao.deleteById(id);
        });
    }

    public void deleteTerm(int id) {
        TermManagementDatabase.databaseWriteExecutor.execute(() -> {
            mTermDao.deleteById(id);
        });
    }
}

