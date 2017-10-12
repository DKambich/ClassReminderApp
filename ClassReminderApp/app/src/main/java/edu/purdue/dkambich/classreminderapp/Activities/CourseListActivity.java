package edu.purdue.dkambich.classreminderapp.Activities;

import android.app.ListActivity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import com.google.gson.Gson;

import edu.purdue.dkambich.classreminderapp.Adapters.CourseAdapter;
import edu.purdue.dkambich.classreminderapp.Models.Course;
import edu.purdue.dkambich.classreminderapp.R;
import io.realm.Realm;

public class CourseListActivity extends ListActivity {

    private FloatingActionButton addCourse;
    private final int REQUESTCODE = 0;
    private Realm realm;

    public ArrayList<Course> listValues;
    CourseAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course_list);

        Realm.init(this);
        realm = Realm.getDefaultInstance();

        listValues = new ArrayList<Course>(realm.where(Course.class).findAll());
//        listValues.add(new Course("CS-18000", "PHYS", "2:30 PM"));
//        listValues.add(new Course("MA-16100", "WALC", "3:00 PM"));
//        listValues.add(new Course("CS-19300", "FRNY", "11:30 AM"));
//        listValues.add(new Course("ENGL-10600", "BRNG", "5:20 PM"));
//        listValues.add(new Course("CS-19100", "HAAS", "9:30 AM"));


        adapter = new CourseAdapter(this, R.layout.row_layout, listValues);

        setListAdapter(adapter);

        addCourse = (FloatingActionButton) findViewById(R.id.addCourse);
        addCourse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent myIntent = new Intent(CourseListActivity.this, InputActivity.class);
                startActivityForResult(myIntent, REQUESTCODE);
            }
        });

    }

    protected void onActivityResult (int requestCode, int resultCode, Intent data) {
        // Collect data from the intent and use it
        String jsonMyObject = "";
        Bundle extras = data.getExtras();
        if (extras != null) {
            jsonMyObject = extras.getString("course");
        }
        Course myCourse = new Gson().fromJson(jsonMyObject, Course.class);
        listValues.add(myCourse);
        realm.beginTransaction();
        final Course managedCourse = realm.copyToRealm(myCourse);
        realm.commitTransaction();

        adapter.notifyDataSetChanged();
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        Course course = listValues.get(position);
        Toast toast = Toast.makeText(getBaseContext(), "Selected: " + course.getName(), Toast.LENGTH_LONG);
        toast.show();
        super.onListItemClick(l, v, position, id);

    }

}
