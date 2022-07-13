package com.app.cense.AlarmService;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.Build;
import android.os.IBinder;

import androidx.core.app.NotificationCompat;

import com.app.cense.LockScreenActivity;
import com.app.cense.R;

public class AlarmService extends Service {

    public static final String CHANNEL_ID = "ForegroundServiceChannel";

    public AlarmService() {onCreate();}

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        super.onStartCommand(intent, flags, startId);

        System.out.println("service started");

        createNotificationChannel();
        Intent notificationIntent = new Intent(this, LockScreenActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, notificationIntent, PendingIntent.FLAG_IMMUTABLE);
        Notification notification = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setContentTitle("STEP TO THE MONEY")
                .setContentText("input")
                .setSmallIcon(R.drawable.cat)
                .setContentIntent(pendingIntent)
                .build();
        startForeground(1, notification);

        return START_NOT_STICKY;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        System.out.println("service onCreate");
        AlarmClock alarmClock = new AlarmClock();
        alarmClock.setAlarm(LockScreenActivity.lockScreenActivity);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        System.out.println("service OnDestroy");
    }

    @Override
    public IBinder onBind(Intent arg0) {
        return null;
    }

    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "Foreground Service Channel";

            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
            channel.setDescription("description");

            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }
}
