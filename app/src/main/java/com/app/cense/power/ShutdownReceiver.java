package com.app.cense.power;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.app.cense.App;
import com.app.cense.data.appMetrica.Event;

public class ShutdownReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d("shutdownReceiver", "shutdownReceiver " + intent.getAction());
        App.getInstance().getFirebaseController().writePowerOff(App.getInstance().getSharedPreferences().getLogin(), "OFF");
        Event.power("off");
    }
}
