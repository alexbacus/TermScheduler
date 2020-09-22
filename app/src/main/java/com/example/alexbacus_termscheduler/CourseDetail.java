package com.example.alexbacus_termscheduler;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;

import com.example.alexbacus_termscheduler.Entities.CourseEntity;
import com.example.alexbacus_termscheduler.Entities.TermEntity;
import com.example.alexbacus_termscheduler.ViewModel.CourseViewModel;
import com.example.alexbacus_termscheduler.ViewModel.TermViewModel;
import com.example.alexbacus_termscheduler.ui.CourseAdapter;
import com.example.alexbacus_termscheduler.ui.TermAdapter;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class CourseDetail extends AppCompatActivity implements DatePickerDialog.OnDateSetListener, AdapterView.OnItemSelectedListener {

    private CourseViewModel mCourseViewModel;
    private TermViewModel mTermViewModel;
    private EditText mEditTitle;
    private EditText mEditStartDate;
    private EditText mEditEndDate;
    private String status;
    private EditText mEditNotes;
    Button selectStartDateButton;
    Button selectEndDateButton;
    private TextView editDate;
    private List<String> courseStatus = new ArrayList<>();
    private TermEntity selectedTerm;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        final CourseAdapter adapter = new CourseAdapter(this);
        final TermAdapter termAdapter = new TermAdapter(this);
        mCourseViewModel = new ViewModelProvider(this).get(CourseViewModel.class);
        mTermViewModel = new ViewModelProvider(this).get(TermViewModel.class);
        mTermViewModel.getAllTerms().observe(this, terms -> termAdapter.setTerms(terms));
        mCourseViewModel.getAllCourses().observe(this, courses -> {
            // Update the cached copy of the words in the adapter.
            adapter.setCourses(courses);
        });
        setContentView(R.layout.activity_course_detail);
        mEditTitle = findViewById(R.id.InputCourseTitle);
        mEditStartDate = findViewById(R.id.start_date_edit_text);
        mEditEndDate = findViewById(R.id.end_date_edit_text);
        mEditNotes = findViewById(R.id.InputNotes);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_baseline_arrow_back_24);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        setUpDatePickers();
        setUpStatusSpinner();
        if (getIntent().getStringExtra("courseTitle") != null) {
            mEditTitle.setText(getIntent().getStringExtra("courseTitle"));
            mEditStartDate.setText(getIntent().getStringExtra("startDate"));
            mEditEndDate.setText(getIntent().getStringExtra("endDate"));
            Spinner spinner = (Spinner) findViewById(R.id.StatusSpinner);
            spinner.setSelection(courseStatus.indexOf(getIntent().getStringExtra("status")));
            mEditNotes.setText(getIntent().getStringExtra("notes"));
        }
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent replyIntent = new Intent();
                String title = mEditTitle.getText().toString();
                String startDate = mEditStartDate.getText().toString();
                String endDate = mEditEndDate.getText().toString();
                String notes = mEditNotes.getText().toString();
                replyIntent.putExtra("courseTitle", title);
                replyIntent.putExtra("startDate", startDate);
                replyIntent.putExtra("endDate", endDate);
                replyIntent.putExtra("status", status);
                replyIntent.putExtra("notes", notes);
                int id=getIntent().getIntExtra("courseID",-1);
                if (id == -1) {
                    id = (mCourseViewModel.lastID()) + 1;
                }
                replyIntent.putExtra("courseID", id);
                CourseEntity course = new CourseEntity(id, title, startDate, endDate, status, notes, selectedTerm.getTermID());
                mCourseViewModel.insert(course);
                setResult(RESULT_OK, replyIntent);
                finish();
            }
        });
    }

    private void setUpStatusSpinner() {
        Spinner spinner = (Spinner) findViewById(R.id.StatusSpinner);
        spinner.setOnItemSelectedListener(this);

        courseStatus.add("In Progress");
        courseStatus.add("Completed");
        courseStatus.add("Dropped");
        courseStatus.add("Plan to Take");

        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, courseStatus);

        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // attaching data adapter to spinner
        spinner.setAdapter(dataAdapter);

        // set default value
        spinner.setSelection(courseStatus.indexOf("In Progress"));

        spinner = findViewById(R.id.TermSpinner);
        final ArrayAdapter<TermEntity> adapter2 = new ArrayAdapter<TermEntity>(this,
                android.R.layout.simple_spinner_item);
        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter2);
        spinner.setOnItemSelectedListener(this);

        mTermViewModel.getAllTerms().observe(this, new Observer<List<TermEntity>>() {
            @Override
            public void onChanged(List<TermEntity> termEntities) {
                adapter2.addAll(termEntities);
            }
        });
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        if (parent.getId() == R.id.StatusSpinner) {
            status = parent.getItemAtPosition(position).toString();
        }
        else if (parent.getId() == R.id.TermSpinner) {
            selectedTerm = (TermEntity)parent.getItemAtPosition(position);
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