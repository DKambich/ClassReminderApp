package edu.purdue.dkambich.classreminderapp.Activities;

import android.app.TimePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.sql.Time;
import java.util.Calendar;

import edu.purdue.dkambich.classreminderapp.R;

public class InputActivity extends AppCompatActivity {

    AutoCompleteTextView location;
    FloatingActionButton inputButton;
    EditText name;
    TextView startTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_input);
        //


        //Get course name for later references
        name = (EditText) findViewById(R.id.classNameInput);

        //Get location to add in buildings array and for later references
        location = (AutoCompleteTextView) findViewById(R.id.autoCompleteLocation);
        final String[] buildings = getResources().getStringArray(R.array.building_abbr_array);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line, buildings);
        location.setAdapter(adapter);

        //Get start time to add on a TimePickerDialog and later references
        startTime = (TextView) findViewById(R.id.startTimeDisplay);
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
        inputButton = (FloatingActionButton) findViewById(R.id.confirmCourse);
        inputButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    //Check for proper time
                    if(startTime.getText().toString().equals("HH:MM")) {
                        String message = "Please enter a valid start time";
                        Toast toast =Toast.makeText(getBaseContext(), message, Toast.LENGTH_LONG);
                        toast.show();
                        return;
                    }

                    //Check for proper location
                    boolean error = true;
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
                    }
                    //Create a course and add it to the CourseList



                    //
                    //Switch to CourseListActivity
                    Intent myIntent = new Intent(InputActivity.this, CourseListActivity.class);
                    startActivity(myIntent);
            }
        });
    }
}
