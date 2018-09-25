package edu.purdue.dkambich.classreminderapp.Models;

import io.realm.RealmObject;

public class Course extends RealmObject {

    private String name, location, startTime, daysOfWeek, latitude, longitude;

    public Course(){
        this.name = "";
        this.location = "";
        this.startTime = "";
        this.daysOfWeek = "";
    }

    public Course(String name, String location, String startTime) {
        this.name = name;
        this.location = location;
        this.startTime = startTime;
    }

    //Name Methods

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    //Location Methods

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    //Time Methods

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public int getStartHour() {
        int hour = Integer.parseInt(startTime.substring(0, startTime.indexOf(":")));
        //if(hour == 12 && startTime.contains("AM")) {
        //    hour = 0;
        //}
        //else if(hour != 12 && startTime.contains("PM")) {
        //    hour += 12;
        //}

        return hour;
    }

    public int getStartMinute(){
        return Integer.parseInt(startTime.substring(startTime.indexOf(":") + 1, startTime.indexOf(" ")));
    }

    public boolean isMorningClass(){
        return startTime.contains("AM");
    }

    //Day Methods

    public String getDaysOfWeek(){
        return daysOfWeek;
    }

    public void setDaysOfWeek(String daysOfWeek){
        this.daysOfWeek = daysOfWeek;
    }

    //LatLng Methods

    public String getLatitude() {
        return latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLatLng(String latitude, String longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
    }


}