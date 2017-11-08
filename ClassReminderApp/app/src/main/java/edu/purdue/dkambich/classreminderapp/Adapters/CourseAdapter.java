package edu.purdue.dkambich.classreminderapp.Adapters;

import android.content.Context;
import android.graphics.Color;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import edu.purdue.dkambich.classreminderapp.Models.Course;
import edu.purdue.dkambich.classreminderapp.R;

public class CourseAdapter extends ArrayAdapter<Course> {

    ArrayList<Course> courses;

    public CourseAdapter(Context context, int resource, ArrayList<Course> data) {
        super(context, resource, data);
        courses = data;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        Course course = getItem(position);

        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.row_layout, parent, false);
        }

        //Get the course name view
        TextView name = (TextView) convertView.findViewById(R.id.rowCourseView);
        //Get the course location view
        TextView location = (TextView) convertView.findViewById(R.id.rowLocationView);
        //Get the course time view
        TextView time = (TextView) convertView.findViewById(R.id.rowStartTimeView);

        //Set the course name view text
        name.setText(course.getName());
        //Set the course location view text
        location.setText(course.getLocation());
        //Set the course time view text
        time.setText(course.getStartTime());

        //Create a list of ID's to go through the days of the week
        int[] ID = { R.id.rowMonday, R.id.rowTuesday, R.id.rowWednesday, R.id.rowThursday, R.id.rowFriday };
        TextView currentDay;
        //Loop through each id in ID
        for (int viewID : ID) {
            //Set the current day view to the current id
            currentDay = (TextView) convertView.findViewById(viewID);
            //If the course contains the current day
            if (course.getDaysOfWeek().contains(currentDay.getText().toString())) {
                //Set the current day as selected
                currentDay.setBackground(getContext().getDrawable(R.drawable.gold_circle_drawable));
                currentDay.setTextColor(Color.parseColor("#FAFAFA"));
            } //Otherwise,
            else {
                //Unselect the current day
                currentDay.setBackground(getContext().getDrawable(R.drawable.white_circle_drawable));
                currentDay.setTextColor(ContextCompat.getColor(getContext(), R.color.textColorPrimary));
            }
        }
        return convertView;
    }

}
