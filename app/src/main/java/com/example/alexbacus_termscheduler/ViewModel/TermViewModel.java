package com.example.alexbacus_termscheduler.ViewModel;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.alexbacus_termscheduler.Database.TermManagementRepository;
import com.example.alexbacus_termscheduler.Entities.TermEntity;

import java.util.List;

public class TermViewModel extends AndroidViewModel {
        private TermManagementRepository mRepository;
        private LiveData<List<TermEntity>> mAllTerms;
        public TermViewModel(Application application){
            super(application);
            mRepository=new TermManagementRepository(application);
            mAllTerms=mRepository.getAllTerms();
        }
        public LiveData<List<TermEntity>> getAllTerms(){
            return mAllTerms;
        }
        public void insert(TermEntity termEntity){
            mRepository.insert(termEntity);
        }
        public int lastID(){
            return mAllTerms.getValue().size();
        }
}
