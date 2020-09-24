package com.example.alexbacus_termscheduler.ViewModel;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.alexbacus_termscheduler.Database.TermManagementRepository;
import com.example.alexbacus_termscheduler.Entities.AssessmentEntity;

import java.util.List;

public class AssessmentViewModel extends AndroidViewModel {
    private TermManagementRepository mRepository;
    private LiveData<List<AssessmentEntity>> mAllAssessments;
    public AssessmentViewModel(Application application){
        super(application);
        mRepository=new TermManagementRepository(application);
        mAllAssessments=mRepository.getAllAssessments();
    }
    public LiveData<List<AssessmentEntity>> getAllAssessments(){
        return mAllAssessments;
    }
    public void insert(AssessmentEntity assessmentEntity){
        mRepository.insert(assessmentEntity);
    }
    public void delete(int id) { mRepository.deleteAssessment(id); }
    public int lastID(){
        return mAllAssessments.getValue().size();
    }
}
