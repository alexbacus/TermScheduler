package com.example.alexbacus_termscheduler;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.alexbacus_termscheduler.Entities.CourseEntity;
import com.example.alexbacus_termscheduler.Entities.TermEntity;
import com.example.alexbacus_termscheduler.ViewModel.CourseViewModel;
import com.example.alexbacus_termscheduler.ViewModel.TermViewModel;
import com.example.alexbacus_termscheduler.ui.CourseAdapter;
import com.example.alexbacus_termscheduler.ui.TermAdapter;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class TermDetail extends AppCompatActivity implements DatePickerDialog.OnDateSetListener {
    public static final int NEW_WORD_ACTIVITY_REQUEST_CODE = 1;
    public static final String EXTRA_REPLY = "com.decusatis.tester.wordlistsql.REPLY";
    private TermViewModel mTermViewModel;
    private CourseViewModel mCourseViewModel;
    private EditText mEditName;
    private EditText mEditStartDate;
    private EditText mEditEndDate;
    Button selectStartDateButton;
    Button selectEndDateButton;
    private TextView editDate;
    private Button addCourseButton;
    private FloatingActionButton deleteButton;
    private List<CourseEntity> associatedCourses = new ArrayList<CourseEntity>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_term_detail);
        final TermAdapter adapter = new TermAdapter(this);
        final CourseAdapter courseAdapter = new CourseAdapter(this);
        RecyclerView recyclerView = findViewById(R.id.recyclerview);
        mTermViewModel = new ViewModelProvider(this).get(TermViewModel.class);
        mCourseViewModel = new ViewModelProvider(this).get(CourseViewModel.class);
        recyclerView.setAdapter(courseAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        mTermViewModel.getAllTerms().observe(this, new Observer<List<TermEntity>>() {
            @Override
            public void onChanged(@Nullable final List<TermEntity> terms) {
                // Update the cached copy of the words in the adapter.
                adapter.setTerms(terms);
            }
        });
        mCourseViewModel.getAllCourses().observe(this, new Observer<List<CourseEntity>>() {
            @Override
            public void onChanged(@Nullable final List<CourseEntity> courses) {
                associatedCourses.clear();
                int termID = getIntent().getIntExtra("termID", -1);
                if (termID != -1) {
                    for(CourseEntity c: courses) {
                        if (c.getTermId() == termID && !(associatedCourses.contains(c))) {
                            if (c.getBasicStatus() != BasicStatus.TRASHED.getValue()){
                                associatedCourses.add(c);
                            }
                        }
                    }
                }
                if (!(associatedCourses.isEmpty())) {
                    courseAdapter.setCourses(associatedCourses);
                }
            }
        });

        deleteButton = (FloatingActionButton) findViewById(R.id.DeleteButton);
        if (getIntent().getIntExtra("termID", -1) == -1) {
            deleteButton.setVisibility(View.INVISIBLE);
        }
        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!associatedCourses.isEmpty()) {
                    Toast.makeText(
                            getApplicationContext(),
                            "Term could not be deleted because it has associated courses.",
                            Toast.LENGTH_LONG).show();
                    return;
                }
                else {
                    mTermViewModel.delete(getIntent().getIntExtra("termID", -1));
                    finish();
                }
            }
        });

        addCourseButton = (Button) findViewById(R.id.AddCourseButton);
        if (getIntent().getIntExtra("termID", -1) != -1) {
            addCourseButton.setVisibility(View.VISIBLE);
        }
        addCourseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int courseId = mCourseViewModel.lastID() + 1;
                String courseTitle = "Course " + courseId;
                String startDate = mEditStartDate.getText().toString();
                String endDate = mEditEndDate.getText().toString();
                String status = "In Progress";
                String notes = "";
                int termId = getIntent().getIntExtra("termID", -1);
                int basicStatus = BasicStatus.ACTIVE.getValue();

                CourseEntity course = new CourseEntity(courseId, courseTitle, startDate, endDate, status, notes, termId, basicStatus);
                mCourseViewModel.insert(course);
            }
        });
        mEditName = findViewById(R.id.InputTermName);
        mEditStartDate = findViewById(R.id.start_date_edit_text);
        mEditEndDate = findViewById(R.id.end_date_edit_text);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_baseline_arrow_back_24);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        setUpDatePickers();
        if (getIntent().getStringExtra("termName") != null) {
            mEditName.setText(getIntent().getStringExtra("termName"));
            mEditStartDate.setText(getIntent().getStringExtra("startDate"));
            mEditEndDate.setText(getIntent().getStringExtra("endDate"));
        }

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent replyIntent = new Intent();
                String name = mEditName.getText().toString();
                String startDate = mEditStartDate.getText().toString();
                String endDate = mEditEndDate.getText().toString();
                replyIntent.putExtra("termName", name);
                replyIntent.putExtra("startDate", startDate);
                replyIntent.putExtra("endDate", endDate);
                int id=getIntent().getIntExtra("termID",-1);
                int basicStatus = BasicStatus.ACTIVE.getValue();
                if (id == -1) {
                    id = (mTermViewModel.lastID()) + 1;
                }
                replyIntent.putExtra("termID", id);
                TermEntity term = new TermEntity(id, name, startDate, endDate, basicStatus);
                mTermViewModel.insert(term);
                setResult(RESULT_OK, replyIntent);
                finish();
            }
        });
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

    @Override
    protected void onRestart() {
        super.onRestart();
        finish();
        overridePendingTransition(0, 0);
        startActivity(getIntent());
        overridePendingTransition(0, 0);
    }
}