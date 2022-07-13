package com.app.cense.actionBoot;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import androidx.core.content.ContextCompat;

import com.app.cense.AlarmService.AlarmService;

public class BootCompletedReceiver extends BroadcastReceiver {
    public BootCompletedReceiver() {
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        Toast.makeText(context, "Boot сервис запущен", Toast.LENGTH_LONG).show();
        Intent serviceIntent = new Intent(context, AlarmService.class);
        ContextCompat.startForegroundService(context, serviceIntent);
    }
}