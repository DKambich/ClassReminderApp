package edu.purdue.dkambich.classreminderapp.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;

import com.google.gson.Gson;

import java.util.ArrayList;

import edu.purdue.dkambich.classreminderapp.Adapters.CourseAdapter;
import edu.purdue.dkambich.classreminderapp.Models.Course;
import edu.purdue.dkambich.classreminderapp.R;
import io.realm.Realm;

public class CourseListActivity extends AppCompatActivity {

    //Realm Variables
    private final int REQUESTCODE = 0;
    private Realm realm;

    //View Variables
    private ListView courseList;

    //Course List Variables
    private ArrayList<Course> listValues;
    private CourseAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course_list);

        //Initialize Realm
        Realm.init(this);
        realm = Realm.getDefaultInstance();

        //Recieve a list from Realm
        listValues = new ArrayList<Course>(realm.where(Course.class).findAll());

        //Send listValues to a new adapater
        adapter = new CourseAdapter(this, R.layout.row_layout, listValues);

        //Set the adapter for our list view to our adapter
        courseList = (ListView) findViewById(R.id.courseListView);
        courseList.setAdapter(adapter);
    }

    protected void onActivityResult (int requestCode, int resultCode, Intent data) {
        if(resultCode == 0) {
            String jsonMyObject = "";
            Bundle extras = data.getExtras();
            if (extras != null) {
                jsonMyObject = extras.getString("course");
            }
            if(!jsonMyObject.equals("")){
                //Get the course from the GSON
                Course myCourse = new Gson().fromJson(jsonMyObject, Course.class);
                //Add it to the list
                listValues.add(myCourse);
                //Send the new course to realm
                realm.beginTransaction();
                final Course managedCourse = realm.copyToRealm(myCourse);
                realm.commitTransaction();
                //Update the course list adapter
                adapter.notifyDataSetChanged();
                String message = "Course added successfully";
                Toast toast = Toast.makeText(getBaseContext(), message, Toast.LENGTH_SHORT);
                toast.show();
            }
        }
        else if(resultCode == 1) {
            Toast.makeText(this, "Cancelled Course Entry", Toast.LENGTH_LONG).show();
        }
        // Collect data from the intent and use it

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.mainmenu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == R.id.mapScreen) {
            Intent courses = new Intent(CourseListActivity.this, MainActivity.class);
            startActivity(courses);
        }
        return true;
    }

    public void addCourse(View view) {
        Intent myIntent = new Intent(CourseListActivity.this, InputActivity.class);
        startActivityForResult(myIntent, REQUESTCODE);
    }

}
