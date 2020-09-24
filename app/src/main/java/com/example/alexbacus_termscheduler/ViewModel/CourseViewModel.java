package com.example.alexbacus_termscheduler.ViewModel;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.alexbacus_termscheduler.Database.TermManagementRepository;
import com.example.alexbacus_termscheduler.Entities.CourseEntity;

import java.util.List;

public class CourseViewModel extends AndroidViewModel {

    private TermManagementRepository mRepository;
    private LiveData<List<CourseEntity>> mAllCourses;

    public CourseViewModel(Application application){
        super(application);
        mRepository=new TermManagementRepository(application);
        mAllCourses=mRepository.getAllCourses();
    }
    public LiveData<List<CourseEntity>> getAllCourses(){
        return mAllCourses;
    }
    public void insert(CourseEntity courseEntity){
        mRepository.insert(courseEntity);
    }
    public void delete(int id) { mRepository.deleteCourse(id); }
    public int lastID(){
        return mAllCourses.getValue().size();
    }
}
