package edu.purdue.dkambich.classreminderapp.Activities;

import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;

import edu.purdue.dkambich.classreminderapp.Fragments.MapFragment;
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
                return false;
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.mainmenu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == R.id.courseScreen) {
            Intent courses = new Intent(MainActivity.this, CourseListActivity.class);
            startActivity(courses);
        }
        return true;
    }

}
