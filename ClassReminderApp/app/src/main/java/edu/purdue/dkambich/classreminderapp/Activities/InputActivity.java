package edu.purdue.dkambich.classreminderapp.Activities;

import android.app.TimePickerDialog;
import android.graphics.Color;
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
    EditText name;
    AutoCompleteTextView location;
    Button inputButton;
    TextView startTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_input);

        name = (EditText) findViewById(R.id.classNameInput);

        location = (AutoCompleteTextView) findViewById(R.id.autoCompleteLocation);
        final String[] buildings = getResources().getStringArray(R.array.building_abbr_array);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line, buildings);
        location.setAdapter(adapter);

        startTime = (TextView) findViewById(R.id.startTimeDisplay);
        startTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar mcurrentTime = Calendar.getInstance();
                int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
                int minute = mcurrentTime.get(Calendar.MINUTE);
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
                }, hour, minute, false);//Yes 24 hour time
                //mTimePicker.setTitle("Select Time");
                mTimePicker.show();
            }
        });

        inputButton = (Button) findViewById(R.id.inputButton);
        inputButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean error = true;
                for(String building: buildings){
                    if(building.equals(location.getText().toString())){
                        error = false;
                    }
                }
                String message;
                if(error){
                    message = "Error Entering Location";
                }
                else{
                    message = "Class Entered Successfully";
                }
                Toast toast = Toast.makeText(getBaseContext(), message, Toast.LENGTH_LONG);
                toast.show();
            }
        });
    }
}
