package edu.purdue.dkambich.classreminderapp.Models;

import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;

import edu.purdue.dkambich.classreminderapp.R;

public class Alarm extends BroadcastReceiver {


    //Google Matrix API key: AIzaSyB9ztx38wbvEh32NAykUBmCrZNNpfy8iGk
    @Override
    public void onReceive(Context context, Intent intent) {
        String courseString = intent.getExtras().getString("alarmCourse");
        Course myCourse = new Gson().fromJson(courseString, Course.class);
        createNotification(context, myCourse);
        System.out.println("Got: " + myCourse.getName());
        Toast.makeText(context, myCourse.getName(), Toast.LENGTH_LONG).show(); // For example
        callGoogleMaps(context, myCourse);
    }

    public void callGoogleMaps(Context context, Course scheduledCourse) {
        RequestQueue queue = Volley.newRequestQueue(context);

        String url = "https://maps.googleapis.com/maps/api/distancematrix/json?units=imperial&origins=Washington,DC&destinations=New+York+City,NY&key=AIzaSyB9ztx38wbvEh32NAykUBmCrZNNpfy8iGk";
        //String url = "https://maps.googleapis.com/maps/api/distancematrix/json?";
        //url += "origins=";
        //url += user.latitude() + "," + user.longitude() + "|";
        //url += scheduledCourse.getLatitude() + "," + scheduledCourse.getLongitude();

        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                System.out.println(response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                System.err.println(error.getMessage());
            }
        });
        queue.add(stringRequest);
    }

    public void createNotification(Context context, Course course) {
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(context)
                .setContentTitle(course.getName())
                .setSmallIcon(R.drawable.gold_circle_drawable)
                .setContentText(String.format("You have %s at %s", course.getName(), course.getStartTime()));
        NotificationManager mNotifyMgr = (NotificationManager) context.getSystemService(context.NOTIFICATION_SERVICE);
        mNotifyMgr.notify(001, mBuilder.build());
    }
}