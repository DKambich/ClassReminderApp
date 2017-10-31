package edu.purdue.dkambich.classreminderapp.Activities;

import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.TimePicker;

import com.google.gson.Gson;

import java.util.Calendar;

import edu.purdue.dkambich.classreminderapp.Models.Course;
import edu.purdue.dkambich.classreminderapp.R;

public class InputActivity extends AppCompatActivity {

    //View Variables
    private AutoCompleteTextView name, location;
    private FloatingActionButton inputButton;
    private RelativeLayout inputLayout;
    private TextView startTime;

    //Realm Variables
    private final int RESULT_OK = 0, RESULT_RETURN = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_input);

        //Set click listener on the activity
        inputLayout = (RelativeLayout) findViewById(R.id.inputLayout);
        inputLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
            }
        });

        //Set keyboard input type for name input
        name = (AutoCompleteTextView) findViewById(R.id.courseInputView);
        name.setInputType(InputType.TYPE_CLASS_TEXT);

        //Create an autocomplete for the name input
        ArrayAdapter<String> nameAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line, getResources().getStringArray(R.array.course_abbr_array));
        name.setAdapter(nameAdapter);
        name.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if(!name.getText().toString().contains("-")){
                    name.setText(name.getText() + "-");
                    name.setInputType(InputType.TYPE_CLASS_NUMBER);
                    name.setSelection(name.getText().toString().length());
                }
            }
        });

        //Create a key listener to change keyboard types
        name.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int i, KeyEvent keyEvent) {
                if(keyEvent.getKeyCode() == KeyEvent.KEYCODE_DEL) {
                    if(!name.getText().toString().contains("-")) {
                        name.setInputType(InputType.TYPE_CLASS_TEXT);
                    }
                }
                return false;
            }
        });

        //Create a touch listener to change keyboard types
        name.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if(!name.getText().toString().contains("-") || name.getSelectionStart() < name.getText().toString().indexOf("-")) {
                    name.setInputType(InputType.TYPE_CLASS_TEXT);
                }
                else {
                    name.setInputType(InputType.TYPE_CLASS_NUMBER);
                }
                return false;
            }
        });


        location = (AutoCompleteTextView) findViewById(R.id.locationInputView);
        //Create an autocomplete list
        ArrayAdapter<String> locAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line, getResources().getStringArray(R.array.building_abbr_array));
        location.setAdapter(locAdapter);
        location.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                View newView = getCurrentFocus();
                if (newView != null) {
                    InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(newView.getWindowToken(), 0);
                }

            }
        });

        startTime = (TextView) findViewById(R.id.startTimeInputView);
        //Add a click listener to allow time input
        startTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar currentTime = Calendar.getInstance();
                int hour = currentTime.get(Calendar.HOUR_OF_DAY);
                int minute = currentTime.get(Calendar.MINUTE);
                TimePickerDialog mTimePicker;
                mTimePicker = new TimePickerDialog(InputActivity.this, R.style.DialogTheme, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                        String timeOfDay = " AM";
                        if(selectedHour > 12){
                            timeOfDay = " PM";
                            selectedHour -= 12;
                        }
                        if(selectedMinute < 10){
                            startTime.setText(selectedHour + ":0" + selectedMinute + timeOfDay);
                        }
                        else{
                            startTime.setText(selectedHour + ":" + selectedMinute + timeOfDay);
                        }


                    }
                }, hour, minute, false);
                //mTimePicker.setTitle("Select Time");
                mTimePicker.show();
            }
        });

        //Get input button and apply submit logic
        inputButton = (FloatingActionButton) findViewById(R.id.confirmCourseFAB);
        inputButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Create a course and add it to the CourseList
                int[] ID = {R.id.toggleMonday, R.id.toggleTuesday, R.id.toggleWednesday, R.id.toggleThursday, R.id.toggleFriday };
                Course newCourse = new Course(name.getText().toString(), location.getText().toString(), startTime.getText().toString());
                String days = "";
                for(int viewID: ID){
                    TextView day = (TextView) findViewById(viewID);
                    if(day.getBackground().getConstantState().equals(getDrawable(R.drawable.gold_circle_drawable).getConstantState())){
                        days += day.getText().toString();
                    }
                }
                newCourse.setDaysOfWeek(days);

                Intent activity = new Intent();
                activity.putExtra("course", new Gson().toJson(newCourse));
                setResult(RESULT_OK, activity);
                finish();
            }
        });
    }

    public void toggleDay(View v){
        TextView clickable = (TextView) v;
        if(clickable.getBackground().getConstantState().equals(getDrawable(R.drawable.gold_circle_drawable).getConstantState())){
            clickable.setBackground(getDrawable(R.drawable.white_circle_drawable));
            clickable.setTextColor(ContextCompat.getColor(getBaseContext(), R.color.textColorSecondary));
        }
        else {
            clickable.setBackground(getDrawable(R.drawable.gold_circle_drawable));
            clickable.setTextColor(Color.parseColor("#FAFAFA"));
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                setResult(RESULT_RETURN);
                finish();
                break;
        }
        return true;
    }

    @Override
    public void onBackPressed() {
        setResult(RESULT_RETURN);
        finish();
        super.onBackPressed();
    }
}