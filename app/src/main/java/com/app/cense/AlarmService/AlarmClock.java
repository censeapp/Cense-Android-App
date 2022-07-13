package com.app.cense.AlarmService;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import com.app.cense.LockScreenActivity;
import com.app.cense.ui.parent.TimeoutSelectActivity;
import com.app.cense.data.SharedPreferences.AppPreferences;

import java.util.Calendar;

public class AlarmClock extends BroadcastReceiver
{
    @Override
    public void onReceive(Context context, Intent intent) {
        setAlarm(context);
        String message = "It's time to test ----";
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
        Intent intent2 = new Intent(context, LockScreenActivity.class);
        intent2.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent2);

    }

    public void setAlarm(Context context){
        Log.d("Carbon","Alrm SET !!");
        // get a Calendar object with current time
        Calendar cal = Calendar.getInstance();
        // add 30 seconds to the calendar object
        AppPreferences sp = new AppPreferences(context);
        cal.add(Calendar.MINUTE, Integer.parseInt(sp.getProperty(TimeoutSelectActivity.targetTime, "10")));
        Intent intent = new Intent(context, AlarmClock.class);
        PendingIntent sender = PendingIntent.getBroadcast(context, 192837, intent, 0) ;
        // Get the AlarmManager service
        AlarmManager am = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        am.set(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis(), sender);

    }
}