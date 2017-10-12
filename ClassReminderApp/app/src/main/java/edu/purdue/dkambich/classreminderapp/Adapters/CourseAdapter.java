package edu.purdue.dkambich.classreminderapp.Adapters;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import edu.purdue.dkambich.classreminderapp.Activities.CourseListActivity;
import edu.purdue.dkambich.classreminderapp.Models.Course;
import edu.purdue.dkambich.classreminderapp.R;

/**
 * Created by Daniel on 10/6/2017.
 */

public class CourseAdapter extends ArrayAdapter<Course> {

    ArrayList<Course> courses;

    public CourseAdapter(Context context, int resource, ArrayList<Course> data) {
        super(context, resource , data);
        courses = data;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        Course course = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.row_layout, parent, false);
        }

        TextView name = (TextView) convertView.findViewById(R.id.courseNameView);
        TextView location = (TextView) convertView.findViewById(R.id.courseLocationView);
        TextView time = (TextView) convertView.findViewById(R.id.courseTimeView);
        name.setText(course.getName());
        location.setText(course.getLocation());
        time.setText(course.getStartTime());

        if(name.getText().toString().equals("") || location.getText().toString().equals("") || time.getText().toString().equals("")){
            ((TextView) convertView.findViewById(R.id.monday)).setText("");
            ((TextView) convertView.findViewById(R.id.tuesday)).setText("");
            ((TextView) convertView.findViewById(R.id.wednesday)).setText("");
            ((TextView) convertView.findViewById(R.id.thursday)).setText("");
            ((TextView) convertView.findViewById(R.id.friday)).setText("");

        }
        else {
            ((TextView) convertView.findViewById(R.id.monday)).setText("MON");
            ((TextView) convertView.findViewById(R.id.tuesday)).setText("TUE");
            ((TextView) convertView.findViewById(R.id.wednesday)).setText("WED");
            ((TextView) convertView.findViewById(R.id.thursday)).setText("THU");
            ((TextView) convertView.findViewById(R.id.friday)).setText("FRI");
        }


        return convertView;
    }



}
