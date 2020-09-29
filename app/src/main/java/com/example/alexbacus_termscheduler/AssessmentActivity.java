package com.example.alexbacus_termscheduler;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.example.alexbacus_termscheduler.Entities.AssessmentEntity;
import com.example.alexbacus_termscheduler.Entities.TermEntity;
import com.example.alexbacus_termscheduler.ViewModel.AssessmentViewModel;
import com.example.alexbacus_termscheduler.ViewModel.TermViewModel;
import com.example.alexbacus_termscheduler.ui.AssessmentAdapter;
import com.example.alexbacus_termscheduler.ui.TermAdapter;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

public class AssessmentActivity extends AppCompatActivity {

    public static final int NEW_WORD_ACTIVITY_REQUEST_CODE = 1;
    private AssessmentViewModel mAssessmentViewModel;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_assessment);
        RecyclerView recyclerView = findViewById(R.id.recyclerview);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_baseline_arrow_back_24);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        final AssessmentAdapter adapter = new AssessmentAdapter(this);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        mAssessmentViewModel = new ViewModelProvider(this).get(AssessmentViewModel.class);
        mAssessmentViewModel.getAllAssessments().observe(this, new Observer<List<AssessmentEntity>>() {
            @Override
            public void onChanged(@Nullable final List<AssessmentEntity> assessments) {
                // Update the cached copy of the words in the adapter.
                List<AssessmentEntity> temp = new ArrayList<AssessmentEntity>();
                for(AssessmentEntity a: assessments) {
                    if (a.getBasicStatus() != BasicStatus.TRASHED.getValue()) {
                        temp.add(a);
                    }
                }
                adapter.setAssessments(temp);
            }
        });
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AssessmentActivity.this, AssessmentDetail.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                startActivityForResult(intent, NEW_WORD_ACTIVITY_REQUEST_CODE);
            }
        });
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}