package edu.purdue.dkambich.classreminderapp.Activities;

import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.TextView;
import android.widget.TimePicker;

import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocomplete;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.google.gson.Gson;

import java.util.Calendar;

import edu.purdue.dkambich.classreminderapp.Models.Course;
import edu.purdue.dkambich.classreminderapp.R;

public class InputActivity extends AppCompatActivity {

    //View Variables
    private AutoCompleteTextView name;
    private TextView startTime, location;

    //Google Places Constants
    private final int PLACE_PICKER_REQUEST = 0, PLACE_AUTOCOMPLETE_REQUEST = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_input);

        //Set keyboard input type for name input
        name = (AutoCompleteTextView) findViewById(R.id.courseInputView);
        name.setInputType(InputType.TYPE_CLASS_TEXT);

        //Create an autocomplete for the name input
        ArrayAdapter<String> nameAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line, getResources().getStringArray(R.array.course_abbr_array));
        name.setAdapter(nameAdapter);

        startTime = (TextView) findViewById(R.id.startTimeInputView);

        location = (TextView) findViewById(R.id.locationInputView);
    }

    //General Input Methods

    public void closeKeyboard(View view) {
        //Get the input method manager
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        //Hide the keyboard
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    //TODO: Set flags for what count as a legitimate course
    public void confirmCourse(View view) {
        //Create a new course using the inputted name, location, and start time
        Course newCourse = new Course(name.getText().toString(), location.getText().toString(), startTime.getText().toString());
        //Create an array of ID's to loop through for days
        int[] ID = { R.id.toggleMonday, R.id.toggleTuesday, R.id.toggleWednesday, R.id.toggleThursday, R.id.toggleFriday };
        //Create a string to hold what days are selected
        StringBuilder days = new StringBuilder();
        //Loop through each id
        TextView day;
        for(int viewID: ID) {
            day = (TextView) findViewById(viewID);
            //If the day is selected, append the day to the string
            if(day.getBackground().getConstantState().equals(getDrawable(R.drawable.gold_circle_drawable).getConstantState())){
                days.append(day.getText().toString());
            }
        }
        //Set the days of the week for the new course
        newCourse.setDaysOfWeek(days.toString());

        //Create an intent to send the course information
        Intent activity = new Intent();
        //Put the course in a Json format
        activity.putExtra("course", new Gson().toJson(newCourse));
        //Set the return result as ok and finish
        setResult(RESULT_OK, activity);
        finish();
    }

    //Location Input Methods

    public void showLocationPlacePicker(View view) {
        //Create a Place Picker Builder
        PlacePicker.IntentBuilder placePickerBuilder = new PlacePicker.IntentBuilder();
        try {
            //Start a new activity using the Place Picker Builder
            startActivityForResult(placePickerBuilder.build(this), PLACE_PICKER_REQUEST);
        }
        catch (GooglePlayServicesNotAvailableException e) {
            e.printStackTrace();
        }
        catch (GooglePlayServicesRepairableException e) {
            e.printStackTrace();
        }
    }

    public void showLocationAutoComplete(View view) {
        //Create a Place Autocomplete Builder
        PlaceAutocomplete.IntentBuilder builder = new PlaceAutocomplete.IntentBuilder(PlaceAutocomplete.MODE_OVERLAY);
        try {
            //Start a new 'activity' using the Place Autocomplete Builder
            startActivityForResult(builder.build(this), PLACE_AUTOCOMPLETE_REQUEST);
        }
        catch (GooglePlayServicesRepairableException e) {
            e.printStackTrace();
        }
        catch (GooglePlayServicesNotAvailableException e) {
            e.printStackTrace();
        }
    }

    //Time Input Methods

    public void showTimeDialog(View view) {
        //Get an instance of the calendar
        Calendar currentTime = Calendar.getInstance();
        //Retrieve the current hour and minute
        int hour = currentTime.get(Calendar.HOUR_OF_DAY), minute = currentTime.get(Calendar.MINUTE);
        //Create a new Timepicker, defaulted to the current hour and minute in a 12 hour format
        TimePickerDialog mTimePicker = new TimePickerDialog(InputActivity.this, R.style.DialogTheme, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                String timeOfDay = " AM";
                //If the hour is in the second half of the day, readjust the hour to  work for the afternoon
                if(selectedHour > 12) {
                    timeOfDay = " PM";
                    selectedHour -= 12;
                }
                //Properly build the minute part of the string if there are less than 10 minutes
                String chosenTime = selectedHour + ":";
                if(selectedMinute < 10) {
                    chosenTime += "0" + selectedMinute + timeOfDay;
                    startTime.setText(chosenTime);
                }
                else {
                    chosenTime += selectedMinute + timeOfDay;
                    startTime.setText(chosenTime);
                }
            }
        }, hour, minute, false);
        //Show the created dialog
        mTimePicker.show();
    }

    public void toggleDay(View view){
        TextView dayClicked = (TextView) view;
        //If the day is already selected, unselected it
        if(dayClicked.getBackground().getConstantState().equals(getDrawable(R.drawable.gold_circle_drawable).getConstantState())){
            dayClicked.setBackground(getDrawable(R.drawable.white_circle_drawable));
            dayClicked.setTextColor(ContextCompat.getColor(getBaseContext(), R.color.textColorSecondary));
        }
        else { //Otherwise select the day
            dayClicked.setBackground(getDrawable(R.drawable.gold_circle_drawable));
            dayClicked.setTextColor(Color.parseColor("#FAFAFA"));
        }
    }

    //Navigation Methods

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            //If the home button is clicked
            case android.R.id.home:
                //Set the return result as canceled and finish the activity
                setResult(RESULT_CANCELED);
                finish();
                break;
        }
        return true;
    }

    @Override
    public void onBackPressed() {
        //Set the return result as canceled and finish
        setResult(RESULT_CANCELED);
        finish();
        super.onBackPressed();
    }

    //TODO: Add in LatLng holder in Course to be set here
    //TODO: Account for LatLng outside of Purdue range
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        //If the result is from the place picker and a location is chosen
        if (requestCode == PLACE_PICKER_REQUEST && resultCode == RESULT_OK) {
            //Get the chosen place
            Place place = PlacePicker.getPlace(this, data);
            //If the place is a custom location, adjust accordingly
            if(place.getName().toString().contains("°")) {
                location.setText(R.string.customLocationText);
            }
            else {
                //Otherwise, set location to the places name
                location.setText(place.getName());
            }
        } //Otherwise if the the result is from the place autocomplete and a location is chosen
        else if (requestCode == PLACE_AUTOCOMPLETE_REQUEST && resultCode == RESULT_OK) {
            //Get the chosen place
            Place place = PlaceAutocomplete.getPlace(this, data);
            //Set location to the places name
            location.setText(place.getName());
        }
    }

}