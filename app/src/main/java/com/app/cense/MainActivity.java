package com.app.cense;

import androidx.appcompat.app.AppCompatActivity;

/*import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.admin.DevicePolicyManager;
import android.content.Context;
import android.content.Intent;*/
import android.os.Bundle;
import android.view.View;

import com.app.cense.ui.done.DoneActivity;
import com.app.cense.ui.parent.ParentAuthActivity;
import com.app.cense.ui.testActivity.TestActivity;
/*import android.view.Window;
import android.view.WindowManager;

import java.io.IOException;
import java.util.Date;
import java.util.concurrent.locks.Lock;*/

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        System.out.println(this.getPackageName());
        App.getInstance().isStoragePermissionGranted(this);
        if (App.getInstance().isMyAppLauncherDefault()){
            TestActivity.start(this);
        }
    }

    public void change(View view) {
       if(view.getId() == R.id.parentMainActivity) {
           ParentAuthActivity.start(this);
           //DoneActivity.Companion.start(this);
          // AchieveUnlockActivity.start(this);
       } else if(view.getId() == R.id.childMainActivity) {
           LockScreenActivity.start(this);
       }
    }



}