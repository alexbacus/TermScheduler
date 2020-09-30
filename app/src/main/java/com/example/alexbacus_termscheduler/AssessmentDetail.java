package com.example.alexbacus_termscheduler;

import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
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
import com.example.alexbacus_termscheduler.ViewModel.AssessmentViewModel;
import com.example.alexbacus_termscheduler.ViewModel.CourseViewModel;
import com.example.alexbacus_termscheduler.ui.AssessmentAdapter;
import com.example.alexbacus_termscheduler.ui.CourseAdapter;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class AssessmentDetail extends AppCompatActivity  implements DatePickerDialog.OnDateSetListener, AdapterView.OnItemSelectedListener {
    private CourseViewModel mCourseViewModel;
    private AssessmentViewModel mAssessmentViewModel;
    private EditText mEditTitle;
    private EditText mEditDueDate;
    private String type;
    Button selectDueDateButton;
    FloatingActionButton deleteButton;
    private TextView editDate;
    private List<String> assessmentType = new ArrayList<>();
    private String alertDueDate = "";

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
        mEditDueDate = findViewById(R.id.due_date_edit_text);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_baseline_arrow_back_24);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        setUpDatePickers();
        setUpStatusSpinner();
        if (getIntent().getStringExtra("assessmentTitle") != null) {
            mEditTitle.setText(getIntent().getStringExtra("assessmentTitle"));
            mEditDueDate.setText(getIntent().getStringExtra("dueDate"));
            Spinner spinner = (Spinner) findViewById(R.id.TypeSpinner);
            spinner.setSelection(assessmentType.indexOf(getIntent().getStringExtra("type")));
        }
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent replyIntent = new Intent();
                String title = mEditTitle.getText().toString();
                String dueDate = mEditDueDate.getText().toString();
                replyIntent.putExtra("assessmentTitle", title);
                replyIntent.putExtra("dueDate", dueDate);
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
                AssessmentEntity assessment = new AssessmentEntity(id, title, type, dueDate, courseId, basicStatus);
                mAssessmentViewModel.insert(assessment);
                setResult(RESULT_OK, replyIntent);
                finish();
            }
        });
        deleteButton = (FloatingActionButton)findViewById(R.id.DeleteButton);
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
        selectDueDateButton = findViewById(R.id.select_due_date_button);

        selectDueDateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editDate = findViewById(R.id.due_date_edit_text);
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

        String currentDateString;
        String monthStr;
        String dayStr;
        if (month < 10) {
            monthStr = "0" + month;
        }
        else {
            monthStr = String.valueOf(month);
        }

        if (dayOfMonth < 10) {
            dayStr = "0" + dayOfMonth;
        }
        else {
            dayStr = String.valueOf(dayOfMonth);
        }

        currentDateString = year + "-" + monthStr + "-" + dayStr;
        editDate.setText(currentDateString);
    }

    private void scheduleNotification () {
        Intent notificationIntent = new Intent( this, NotificationPublisher.class );

        notificationIntent.putExtra("content", "Your assessment, " + getIntent().getStringExtra("assessmentTitle") + ", is due today!");
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 3, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        long futureInMillis = LocalDateTime.parse(alertDueDate).atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();

        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        assert alarmManager != null;
        alarmManager.setExact(AlarmManager.RTC_WAKEUP, futureInMillis, pendingIntent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        menu.findItem(R.id.add_alarm_end).setVisible(false);
        menu.findItem(R.id.share_notes_icon).setVisible(false);
        menu.findItem(R.id.add_alarm_start).setTitle("Add Due Date Alarm");
        if(getIntent().getIntExtra("assessmentID", -1) == -1) {
            menu.findItem(R.id.add_alert).setVisible(false);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.add_alarm_start) {
            alertDueDate = mEditDueDate.getText().toString() + "T" + LocalTime.now().toString();
            scheduleNotification() ;
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
