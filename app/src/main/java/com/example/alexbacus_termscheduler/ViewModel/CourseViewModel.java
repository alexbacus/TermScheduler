package com.example.alexbacus_termscheduler.ViewModel;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.alexbacus_termscheduler.Database.TermManagementRepository;
import com.example.alexbacus_termscheduler.Entities.CourseEntity;

import java.util.List;

public class CourseViewModel extends AndroidViewModel {
//    int termId;
    private TermManagementRepository mRepository;
    private LiveData<List<CourseEntity>> mAllCourses;
//    private LiveData<List<CourseEntity>> mAssociatedCourses;
//    public CourseViewModel(Application application, int termId) {
//        super(application);
//        mRepository = new TermManagementRepository(application);
//        mAssociatedCourses = mRepository.getAllAssociatedCourses(termId);
//
//    }
    public CourseViewModel(Application application){
        super(application);
        mRepository=new TermManagementRepository(application);
        mAllCourses=mRepository.getAllCourses();
//        mAssociatedCourses = mRepository.getAllAssociatedCourses(termId);
    }
    public LiveData<List<CourseEntity>> getAllCourses(){
        return mAllCourses;
    }
//    public LiveData<List<CourseEntity>> getAssociatedCourses (int termId){
//        return mRepository.getAllAssociatedCourses(termId);
//    }
    public void insert(CourseEntity courseEntity){
        mRepository.insert(courseEntity);
    }
    public int lastID(){
        return mAllCourses.getValue().size();
    }
}
