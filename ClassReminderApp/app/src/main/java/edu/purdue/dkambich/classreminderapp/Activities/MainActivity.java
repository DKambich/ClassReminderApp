package edu.purdue.dkambich.classreminderapp.Activities;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import edu.purdue.dkambich.classreminderapp.Fragments.MapFragment;
import edu.purdue.dkambich.classreminderapp.Models.Alarm;
import edu.purdue.dkambich.classreminderapp.R;

public class MainActivity extends AppCompatActivity  {

    //View Variables
    private FloatingActionButton locationButton;
    private MapFragment map;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Receive mapFragment
        map = (MapFragment) getSupportFragmentManager().findFragmentById(R.id.mapFragment);

        //Receive locationButton and add click listeners
        locationButton = (FloatingActionButton) findViewById(R.id.targetLocationFAB);
        locationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                map.centerLocation();

            }
        });

        locationButton.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                map.resetView();
                return true;
            }
        });


    }

    //Navigation Methods

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //Inflate the main option menu
        getMenuInflater().inflate(R.menu.mainmenu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        //If the item selected is the course screen
        if(item.getItemId() == R.id.courseScreen) {
            //Create a new intent to switch to the CourseListActivity
            Intent courses = new Intent(MainActivity.this, CourseListActivity.class);
            //Start the activity using the intent
            startActivity(courses);
        }
        return true;
    }

}
