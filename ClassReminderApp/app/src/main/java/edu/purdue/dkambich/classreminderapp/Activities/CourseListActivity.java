package edu.purdue.dkambich.classreminderapp.Activities;

import android.app.AlarmManager;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;

import com.google.gson.Gson;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;

import edu.purdue.dkambich.classreminderapp.Adapters.CourseAdapter;
import edu.purdue.dkambich.classreminderapp.Models.Alarm;
import edu.purdue.dkambich.classreminderapp.Models.Course;
import edu.purdue.dkambich.classreminderapp.R;
import io.realm.Realm;
import io.realm.RealmConfiguration;

public class CourseListActivity extends AppCompatActivity {

    //Constants
    private final int COURSE_INPUT_REQUEST = 0;

    //List Variables
    private ArrayList<Course> listValues;
    private CourseAdapter adapter;
    private ListView courseList;

    //Realm Variables
    private Realm realm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course_list);

        //Initialize Realm
        Realm.init(this);
        realm = Realm.getDefaultInstance();

        //Receive a list from Realm
        listValues = new ArrayList<>(realm.where(Course.class).findAll());

        //Send listValues to a new adapter
        adapter = new CourseAdapter(this, R.layout.row_layout, listValues);

        //Set the adapter for our list view to our adapter
        courseList = (ListView) findViewById(R.id.courseListView);
        courseList.setAdapter(adapter);
    }

    //Course Methods

    public void addCourse(View view) {
        //Create an intent to switch to InputActivity
        Intent myIntent = new Intent(this, InputActivity.class);
        //Start the activity
        startActivityForResult(myIntent, COURSE_INPUT_REQUEST);
    }

    public void scheduleAlarm(Course course, int day) {
        //Receive an instance of calendar
        Calendar calendar = Calendar.getInstance();

        System.out.println(course.isMorningClass());
        if(course.isMorningClass()) {
            calendar.set(Calendar.AM_PM, Calendar.AM);
        }
        else {
            calendar.set(Calendar.AM_PM, Calendar.PM);
        }

        //Set the second of the calendar to the start of the minute
        calendar.set(Calendar.SECOND, 0);

        //Set the minute of the calendar as the course's start minute
        calendar.set(Calendar.MINUTE, course.getStartMinute());

        //Set the hour of the calendar as the course's start hour
        System.out.println("Course Hour: " + course.getStartHour());
        calendar.set(Calendar.HOUR_OF_DAY, course.getStartHour());

        //Set the day of the week of the calendar
        calendar.set(Calendar.DAY_OF_WEEK, day);

        System.out.println(calendar.getTime());

        //Get the alarm manager from the system
        AlarmManager manager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);

        //Create an intent to use the Alarm Class
        Intent myIntent = new Intent(this, Alarm.class);
        myIntent.putExtra("alarmCourse", new Gson().toJson(course));

        //Create a pending intent
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 0 , myIntent, PendingIntent.FLAG_ONE_SHOT);

        manager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), AlarmManager.INTERVAL_DAY * 7, pendingIntent);
    }

    //TODO: Put course info into intent for class reminder, use JSON implementation
    public void setCourseAlarm(Course course) {
        if(course.getDaysOfWeek().contains("MON")) {
            System.out.println("MON");
            scheduleAlarm(course, Calendar.MONDAY);
        }
        if(course.getDaysOfWeek().contains("TUE")) {
            System.out.println("TUE");
            scheduleAlarm(course, Calendar.TUESDAY);
        }
        if(course.getDaysOfWeek().contains("WED")) {
            System.out.println("WED");
            scheduleAlarm(course, Calendar.WEDNESDAY);
        }
        if(course.getDaysOfWeek().contains("THU")) {
            System.out.println("THU");
            scheduleAlarm(course, Calendar.THURSDAY);
        }
        if(course.getDaysOfWeek().contains("FRI")) {
            System.out.println("FRI");
            scheduleAlarm(course, Calendar.FRIDAY);
        }
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(getApplicationContext())
                .setContentTitle(course.getName())
                .setSmallIcon(R.drawable.gold_circle_drawable)
                .setContentText(String.format("You have %s at %s", course.getName(), course.getStartTime()));
        NotificationManager mNotifyMgr = (NotificationManager) getApplicationContext().getSystemService(getApplication().NOTIFICATION_SERVICE);
        mNotifyMgr.notify(001, mBuilder.build());

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
        //If the item selected is the map screen
        if(item.getItemId() == R.id.mapScreen) {
            //Create a new intent to switch to the MainActivity
            Intent courses = new Intent(CourseListActivity.this, MainActivity.class);
            //Start the activity using the intent
            startActivity(courses);
        }
        return true;
    }

    @Override
    protected void onActivityResult (int requestCode, int resultCode, Intent data) {
        //If a course was entered, extract it from the bundle
        if(resultCode == RESULT_OK) {
            //Retrieve the extras from the bundle
            Bundle extras = data.getExtras();
            //Retrieve the course object
            String jsonMyObject = extras.getString("course");
            //Get the course from the JSON
            Course myCourse = new Gson().fromJson(jsonMyObject, Course.class);

            //Add it to the list
            listValues.add(myCourse);
            //Send the new course to realm
            realm.beginTransaction();
            realm.copyToRealm(myCourse);
            realm.commitTransaction();
            //Update the course list adapter
            adapter.notifyDataSetChanged();

            //Notify the user of the changes
            Toast.makeText(getBaseContext(), "Course added successfully", Toast.LENGTH_SHORT).show();

            //Create an alarm for the course
            setCourseAlarm(myCourse);
        } //If the course input was canceled
        else if(resultCode == RESULT_CANCELED) {
            //Notify the user the course was canceled
            Toast.makeText(this, "Canceled Course Entry", Toast.LENGTH_LONG).show();
        }
    }

}
