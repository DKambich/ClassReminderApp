package edu.purdue.dkambich.classreminderapp.Activities;

import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.places.ui.PlaceAutocomplete;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.google.android.gms.maps.model.LatLng;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

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
                //TESTING GEOCODER
                Geocoder geocoder = new Geocoder(getApplicationContext(), Locale.getDefault());
                try {
                    List<Address> addresses = geocoder.getFromLocationName("Lawson Computer Science Building, West Lafayette", 1);
                    Address obj = addresses.get(0);
                    map.addMarker(new LatLng(obj.getLatitude(), obj.getLongitude()), obj.getFeatureName());
                    String add = obj.getAddressLine(0);
                    add = add + "\n" + obj.getCountryName();
                    add = add + "\n" + obj.getCountryCode();
                    add = add + "\n" + obj.getAdminArea();
                    add = add + "\n" + obj.getPostalCode();
                    add = add + "\n" + obj.getSubAdminArea();
                    add = add + "\n" + obj.getLocality();
                    add = add + "\n" + obj.getSubThoroughfare();

                    System.out.println(add);
                    Toast.makeText(getApplicationContext(), add, Toast.LENGTH_SHORT).show();
                } catch (IOException e) {
                    e.printStackTrace();
                    Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                } catch (ArrayIndexOutOfBoundsException e) {
                    e.printStackTrace();
                    Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                }
                //TESTING GEOCODER
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.mainmenu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        //Check or mennu option and start a new intent
        if(item.getItemId() == R.id.courseScreen) {
            Intent courses = new Intent(MainActivity.this, CourseListActivity.class);
            startActivity(courses);
        }
        return true;
    }

}
