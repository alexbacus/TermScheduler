package com.example.alexbacus_termscheduler;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.alexbacus_termscheduler.Entities.AssessmentEntity;
import com.example.alexbacus_termscheduler.Entities.CourseEntity;
import com.example.alexbacus_termscheduler.ViewModel.AssessmentViewModel;
import com.example.alexbacus_termscheduler.ViewModel.CourseViewModel;
import com.example.alexbacus_termscheduler.ViewModel.TermViewModel;
import com.example.alexbacus_termscheduler.ui.AssessmentAdapter;
import com.example.alexbacus_termscheduler.ui.CourseAdapter;
import com.example.alexbacus_termscheduler.ui.TermAdapter;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class AssessmentDetail extends AppCompatActivity  implements DatePickerDialog.OnDateSetListener, AdapterView.OnItemSelectedListener {
    private CourseViewModel mCourseViewModel;
    private AssessmentViewModel mAssessmentViewModel;
    private EditText mEditTitle;
    private EditText mEditStartDate;
    private EditText mEditEndDate;
    private String type;
    Button selectStartDateButton;
    Button selectEndDateButton;
    Button deleteButton;
    private TextView editDate;
    private List<String> assessmentType = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        final CourseAdapter adapter = new CourseAdapter(this);
        final AssessmentAdapter assessmentAdapter = new AssessmentAdapter(this);
        mCourseViewModel = new ViewModelProvider(this).get(CourseViewModel.class);
        mAssessmentViewModel = new ViewModelProvider(this).get(AssessmentViewModel.class);
        mAssessmentViewModel.getAllAssessments().observe(this, assessmentAdapter::setAssessments);
        // Update the cached copy of the words in the adapter.
        mCourseViewModel.getAllCourses().observe(this, adapter::setCourses);
        setContentView(R.layout.activity_assessment_detail);
        mEditTitle = findViewById(R.id.InputAssessmentTitle);
        mEditStartDate = findViewById(R.id.start_date_edit_text);
        mEditEndDate = findViewById(R.id.end_date_edit_text);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_baseline_arrow_back_24);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        setUpDatePickers();
        setUpStatusSpinner();
        if (getIntent().getStringExtra("assessmentTitle") != null) {
            mEditTitle.setText(getIntent().getStringExtra("assessmentTitle"));
            mEditStartDate.setText(getIntent().getStringExtra("startDate"));
            mEditEndDate.setText(getIntent().getStringExtra("endDate"));
            Spinner spinner = (Spinner) findViewById(R.id.TypeSpinner);
            spinner.setSelection(assessmentType.indexOf(getIntent().getStringExtra("type")));
        }
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent replyIntent = new Intent();
                String title = mEditTitle.getText().toString();
                String startDate = mEditStartDate.getText().toString();
                String endDate = mEditEndDate.getText().toString();
                replyIntent.putExtra("assessmentTitle", title);
                replyIntent.putExtra("startDate", startDate);
                replyIntent.putExtra("endDate", endDate);
                replyIntent.putExtra("type", type);
                int id=getIntent().getIntExtra("assessmentID",-1);
                int basicStatus = BasicStatus.ACTIVE.getValue();
                if (id == -1) {
                    id = (mAssessmentViewModel.lastID()) + 1;
                    basicStatus = getIntent().getIntExtra("basicStatus", 1);
                }
                replyIntent.putExtra("basicStatus", basicStatus);
                int courseId = getIntent().getIntExtra("courseId", 0);
                replyIntent.putExtra("courseId", id);
                AssessmentEntity assessment = new AssessmentEntity(id, title, type, startDate, endDate, courseId, basicStatus);
                mAssessmentViewModel.insert(assessment);
                setResult(RESULT_OK, replyIntent);
                finish();
            }
        });
        deleteButton = (Button)findViewById(R.id.DeleteButton);
        if (getIntent().getIntExtra("assessmentID", -1) == -1) {
            deleteButton.setVisibility(View.INVISIBLE);
        }
        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mAssessmentViewModel.delete(getIntent().getIntExtra("assessmentID", -1));
                finish();
            }
        });
    }

    private void setUpStatusSpinner() {
        Spinner spinner = (Spinner) findViewById(R.id.TypeSpinner);
        spinner.setOnItemSelectedListener(this);

        assessmentType.add("Performance");
        assessmentType.add("Objective");

        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, assessmentType);

        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // attaching data adapter to spinner
        spinner.setAdapter(dataAdapter);

        // set default value
        spinner.setSelection(assessmentType.indexOf("Performance"));
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        if (parent.getId() == R.id.TypeSpinner) {
            type = parent.getItemAtPosition(position).toString();
        }
    }

    public void onNothingSelected(AdapterView<?> arg0) {
        // TODO Auto-generated method stub
    }

    private void setUpDatePickers(){
        selectStartDateButton = findViewById(R.id.select_start_date_button);
        selectEndDateButton = findViewById(R.id.select_end_date_button);

        selectStartDateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editDate = findViewById(R.id.start_date_edit_text);
                DialogFragment datePicker = new DatePickerFragment();
                datePicker.show(getSupportFragmentManager(), "date picker");
            }
        });

        selectEndDateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editDate = findViewById(R.id.end_date_edit_text);
                DialogFragment datePicker = new DatePickerFragment();
                datePicker.show(getSupportFragmentManager(), "date picker");
            }
        });

    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {

        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, year);
        calendar.set(Calendar.MONTH, month = month +1);
        calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
        String currentDateString = year + "-" + month + "-" + dayOfMonth;
        editDate.setText(currentDateString);
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
