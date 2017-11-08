package edu.purdue.dkambich.classreminderapp.Models;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

public class Alarm extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent)
    {
        Toast.makeText(context, "Alarm !!!!!!!!!!", Toast.LENGTH_LONG).show(); // For example
    }

//    public void setAlarm(Context context)
//    {
//        AlarmManager am =( AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
//        Intent i = new Intent("edu.purdue.dkambich.classreminderapp.Models.SET_ALARM");
//        PendingIntent pi = PendingIntent.getBroadcast(context, 0, i, 0);
//        am.setRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(), 1000 * 5, pi); // Millisec * Second * Minute
//        System.out.println("Set alarm");
//    }
//
//    public void cancelAlarm(Context context)
//    {
//        Intent intent = new Intent(context, Alarm.class);
//        PendingIntent sender = PendingIntent.getBroadcast(context, 0, intent, 0);
//        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
//        alarmManager.cancel(sender);
//    }
}