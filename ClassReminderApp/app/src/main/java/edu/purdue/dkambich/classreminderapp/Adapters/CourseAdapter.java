package edu.purdue.dkambich.classreminderapp.Adapters;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

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

        TextView name = (TextView) convertView.findViewById(R.id.rowCourseView);
        TextView location = (TextView) convertView.findViewById(R.id.rowLocationView);
        TextView time = (TextView) convertView.findViewById(R.id.rowStartTimeView);
        name.setText(course.getName());
        location.setText(course.getLocation());
        time.setText(course.getStartTime());

        if(name.getText().toString().equals("") || location.getText().toString().equals("") || time.getText().toString().equals("")){
            ((TextView) convertView.findViewById(R.id.rowMonday)).setText("");
            ((TextView) convertView.findViewById(R.id.rowTuesday)).setText("");
            ((TextView) convertView.findViewById(R.id.rowWednesday)).setText("");
            ((TextView) convertView.findViewById(R.id.rowThursday)).setText("");
            ((TextView) convertView.findViewById(R.id.rowFriday)).setText("");

        }
        else {
            int[] ID = {R.id.rowMonday, R.id.rowTuesday, R.id.rowWednesday, R.id.rowThursday, R.id.rowFriday };
            for(int viewID: ID) {
                TextView currentView = (TextView) convertView.findViewById(viewID);
                currentView.setText(currentView.getText().toString());
                if(course.getDaysOfWeek().contains(currentView.getText().toString())) {
                    currentView.setBackground(getContext().getDrawable(R.drawable.gold_circle_drawable));
                    currentView.setTextColor(Color.parseColor("#FAFAFA"));
                }
                else {
                    currentView.setBackground(getContext().getDrawable(R.drawable.white_circle_drawable));
                }
            }
        }
        return convertView;
    }

}
