package com.app.cense;

//import android.annotation.SuppressLint;
import android.app.ActivityManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
//import android.util.Log;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
//import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.app.cense.AlarmService.AlarmService;
import com.app.cense.data.SharedPreferences.AppPreferences;
import com.app.cense.power.ShutdownReceiver;
import com.app.cense.ui.parent.TimeoutSelectActivity;
import com.app.cense.ui.testActivity.TestActivity;
//import com.google.android.gms.common.logging.Logger;

import java.util.concurrent.TimeUnit;

//import static android.content.ContentValues.TAG;

public class LockScreenActivity extends AppCompatActivity {

    protected AlarmService alarmService;

    public static LockScreenActivity lockScreenActivity;
    AppPreferences sp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        lockScreenActivity = this;
        setContentView(R.layout.activity_lock_screen);
        sp = new AppPreferences(this);


        Log.e("Тэги", TestActivity.targetTag+" "+TestActivity.targetClass+" "+TestActivity.targetSubject);
        //TODO: запустить тут сервис
        //AlarmClock alarmClock = new AlarmClock();
        //alarmClock.setAlarm(this, Integer.parseInt(getProperty(TimeoutSelectActivity.targetTime, "1")));

        if (isMyServiceRunning(AlarmService.class)){
            System.out.println("сервис продолжает работу");
        } else {
            Intent serviceIntent = new Intent(this, AlarmService.class);
            ContextCompat.startForegroundService(this, serviceIntent);
            System.out.println("сервис запущен");
        }
    }

    private boolean isMyServiceRunning(Class<?> serviceClass) {
        ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_MENU) {
            App.getInstance().getFirebaseController().writePowerOff(App.getInstance().getSharedPreferences().getLogin(), "PROBABLY OFF");
            Intent intent2 = new Intent(this, FakeLauncherActivity.class);
            intent2.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            this.startActivity(intent2);
        }
        return super.onKeyDown(keyCode, event);
    }



    public String getProperty(String key, String defValue) {
       return sp.getProperty(key, defValue);
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (!App.getInstance().isMyAppLauncherDefault() && !isUrgently) {
            Intent intent2 = new Intent(this, LockScreenActivity.class);
            intent2.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            this.startActivity(intent2);
        }/* else {

        }*/
    }

    @Override
    protected void onResume() {
        super.onResume();
        IntentFilter filter = new IntentFilter(Intent.ACTION_SHUTDOWN);
        ShutdownReceiver mReceiver = new ShutdownReceiver();
        registerReceiver(mReceiver, filter);
        isUrgently = false;
    }

    public void change(View view) {
        if (view.getId() == R.id.callButtonLockScreen){
            Intent intent = new Intent(Intent.ACTION_DIAL);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            isUrgently = true;
            openUrgently();
        }
        if (view.getId() == R.id.messageButtonLockScreen){
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setData(Uri.parse("sms:"));
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            isUrgently = true;
            openUrgently();
        }

        if (view.getId() == R.id.learnMoreButtonLockScreen) {
            System.out.println();
            if (App.getInstance().isMyAppLauncherDefault()) {
                TestActivity.start(this);
            } else {
                PackageManager packageManager = getPackageManager();
                ComponentName componentName = new ComponentName(this, FakeLauncherActivity.class);
                packageManager.setComponentEnabledSetting(componentName, PackageManager.COMPONENT_ENABLED_STATE_ENABLED, PackageManager.DONT_KILL_APP);

                if (android.os.Build.MANUFACTURER.equals("HUAWEI")) {
                    System.out.println("THIS IS HUAWEI!!!");
                    Intent selector = new Intent("com.android.settings.PREFERRED_SETTINGS");
                    //selector.addCategory(Intent.CATEGORY_HOME);
                    selector.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(selector);
                } else {
                    System.out.println("this is no Huawei");
                    Intent selector = new Intent(Intent.ACTION_MAIN);
                    selector.addCategory(Intent.CATEGORY_HOME);
                    selector.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(selector);
                    packageManager.setComponentEnabledSetting(componentName, PackageManager.COMPONENT_ENABLED_STATE_DEFAULT, PackageManager.DONT_KILL_APP);
                    }
                try {
                    int i = 0;
                    while (i<15) {
                        Thread.sleep(2000);
                        if (App.getInstance().isMyAppLauncherDefault()) {
                            TestActivity.start(this);
                            return;
                        }
                        i++;
                        System.out.println("time = "+i);
                    }
                } catch (InterruptedException e){
                }
            }
            System.out.println("Here");
        }
    }

    boolean isUrgently = false;
    private void openUrgently() {
        Thread thread = new Thread() {
            @Override
            public void run() {
                try {
                    TimeUnit.SECONDS.sleep(30);
                    System.out.println("время вышло");
                    if (isUrgently) {
                        System.out.println("возвращаем");
                        Intent intent2 = new Intent(LockScreenActivity.this, LockScreenActivity.class);
                        intent2.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent2);
                        isUrgently = false;
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        };
        thread.start();
    }

    @Override
    public void onBackPressed() {
        System.out.println("back");
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        System.out.println("onWindowFocusChanged");
        super.onWindowFocusChanged(hasFocus);
        if(!hasFocus) {
            App.getInstance().getFirebaseController().writePowerOff(App.getInstance().getSharedPreferences().getLogin(), "PROBABLY OFF");
            Intent closeDialog = new Intent(Intent.ACTION_CLOSE_SYSTEM_DIALOGS);
            sendBroadcast(closeDialog);
        }
    }

    public static void start(Context context) {
        System.out.println("start");
        Intent intent = new Intent(context, LockScreenActivity.class);
        context.startActivity(intent);
    }
}