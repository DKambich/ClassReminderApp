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
import android.widget.Toast;

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

        //Get location to add in buildings array and for later references
        location = (AutoCompleteTextView) findViewById(R.id.locationInputView);
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

        //Get start time to add on a TimePickerDialog and later references
        startTime = (TextView) findViewById(R.id.startTimeInputView);
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

                    /*if(!name.getText().toString().contains("-")) {
                        String message = "Incorrect course name format";
                        Toast toast =Toast.makeText(getBaseContext(), message, Toast.LENGTH_LONG);
                        toast.show();
                        return;
                    }

                    String abbr = name.getText().toString().substring(0, name.getText().toString().indexOf("-"));
                    String[] courses = getResources().getStringArray(R.array.course_abbr_array);
                    boolean error = true;
                    for(String course: courses) {
                        if(course.equals(abbr)) {
                            error = false;
                        }
                    }
                    if(error){
                        String message = "Please enter a valid course ID abbreviation";
                        Toast toast = Toast.makeText(getBaseContext(), message, Toast.LENGTH_LONG);
                        toast.show();
                        return;
                    }

                    //Check for proper time
                    if(startTime.getText().toString().equals("HH:MM")) {
                        String message = "Please enter a valid start time";
                        Toast toast =Toast.makeText(getBaseContext(), message, Toast.LENGTH_LONG);
                        toast.show();
                        return;
                    }

                    //Check for proper location
                    String[] buildings = getResources().getStringArray(R.array.building_abbr_array);
                    error = true;
                    for(String building: buildings){
                        if(building.equals(location.getText().toString())){
                            error = false;
                        }
                    }
                    if(error){
                        String message = "Please enter a valid location";
                        Toast toast = Toast.makeText(getBaseContext(), message, Toast.LENGTH_LONG);
                        toast.show();
                        return;
                    }*/
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



                    //

                    String message = "Course added successfully";
                    Toast toast = Toast.makeText(getBaseContext(), message, Toast.LENGTH_SHORT);
                    toast.show();


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