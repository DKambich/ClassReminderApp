package edu.purdue.dkambich.classreminderapp.Models;

/**
 * Created by Daniel on 9/26/2017.
 */

public class Course {

    private String name, location, startTime;

    public Course(String name, String location, String startTime) {
        this.name = name;
        this.location = location;
        this.startTime = startTime;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public int getStartHour(){
        return Integer.parseInt(startTime.substring(0, startTime.indexOf(":"))) + (startTime.contains("PM") ? 12 : 0);
    }

    public int getStartMinute(){
        return Integer.parseInt(startTime.substring(startTime.indexOf(":") + 1, startTime.indexOf(" ")));
    }

}
