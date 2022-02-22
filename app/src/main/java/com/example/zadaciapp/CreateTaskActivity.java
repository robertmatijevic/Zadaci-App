package com.example.zadaciapp;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import com.example.zadaciapp.bazapodataka.DatabaseHandler;
import com.example.zadaciapp.modeli.Task;

public class CreateTaskActivity extends AppCompatActivity {
    EditText titleEditText;
    EditText descriptionEditText;
    CheckBox checkBoxDone;
    EditText dateEditText;
    EditText timeEditText;

    Calendar myCalendar;
    DatePickerDialog.OnDateSetListener date;
    Button saveButton;
    Button cancelButton;
    DatabaseHandler db;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_task);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        myCalendar = Calendar.getInstance();

        titleEditText = (EditText) findViewById(R.id.edit_text_title);
        descriptionEditText = (EditText) findViewById(R.id.edit_text_description);
        checkBoxDone = (CheckBox) findViewById(R.id.check_done);
        dateEditText = (EditText) findViewById(R.id.edit_text_date);
        timeEditText = (EditText) findViewById(R.id.edit_text_Time);
        saveButton = (Button) findViewById(R.id.btn_save);
        cancelButton = (Button) findViewById(R.id.btn_cancel);


        db = new DatabaseHandler(getApplicationContext());
        date = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                // TODO Auto-generated method stub
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateDateLabel();
            }

        };

        Task taskForEdit = (Task) getIntent().getSerializableExtra("taskForEdit");
        if(taskForEdit == null){
            initializeCreate();
        } else{
            initializeEdit(taskForEdit);
        }

        dateEditText.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                DatePickerDialog mDatePicker = new DatePickerDialog(CreateTaskActivity.this, date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH));
                mDatePicker.setTitle("Odaberi datum");

                mDatePicker.show();
            }
        });

        timeEditText.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub

                int hour = myCalendar.get(Calendar.HOUR_OF_DAY);
                int minute = myCalendar.get(Calendar.MINUTE);
                TimePickerDialog mTimePicker;
                mTimePicker = new TimePickerDialog(CreateTaskActivity.this, new TimePickerDialog.OnTimeSetListener() {

                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                        myCalendar.set(Calendar.HOUR, selectedHour);
                        myCalendar.set(Calendar.MINUTE, selectedMinute);
                        updateTimeLabel();
                    }
                }, hour, minute, true);//Yes 24 hour time
                mTimePicker.setTitle("Odaberi vrijeme");
                mTimePicker.show();

            }
        });





        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CreateTaskActivity.this, MainActivity.class);
                setResult(RESULT_CANCELED);
                finish();
            }
        });



    }

    private void initializeEdit(final Task taskForEdit) {
        titleEditText.setText(taskForEdit.getName());
        checkBoxDone.setChecked(taskForEdit.isDone());
        descriptionEditText.setText(taskForEdit.getDescription());
        dateEditText.setText(taskForEdit.getDateString());
        timeEditText.setText(taskForEdit.getTimeString());
        myCalendar = taskForEdit.getCalendar();
        saveButton.setText("Uredi");

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                initializeTask(taskForEdit);

                db.updateTask(taskForEdit);

                Intent intent = new Intent(CreateTaskActivity.this, MainActivity.class);
                setResult(RESULT_OK);
                finish();
            }
        });

    }

    private void initializeTask(Task task){
        task.setName(titleEditText.getText().toString());
        task.setDone(checkBoxDone.isChecked());
        task.setDescription(descriptionEditText.getText().toString());
        task.setDate(myCalendar);
    }

    @Override
    public boolean onSupportNavigateUp() {
        setResult(RESULT_CANCELED);
        finish();
        return true;
    }

    private void initializeCreate() {
        saveButton.setText("Dodaj");

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Task task = new Task();
                initializeTask(task);

                db.addTask(task);

                Intent intent = new Intent(CreateTaskActivity.this, MainActivity.class);
                intent.putExtra("newTask", task);
                setResult(RESULT_OK);
                finish();

            }
        });
    }


    private void updateDateLabel() {

        SimpleDateFormat iso8601Format = new SimpleDateFormat(
                "dd. MM. yyyy.");
        dateEditText.setText(iso8601Format.format(myCalendar.getTime()));
    }

    private void updateTimeLabel(){
        SimpleDateFormat iso8601Format = new SimpleDateFormat(
                "HH:mm");
        timeEditText.setText( iso8601Format.format(myCalendar.getTime()));

    }

}
