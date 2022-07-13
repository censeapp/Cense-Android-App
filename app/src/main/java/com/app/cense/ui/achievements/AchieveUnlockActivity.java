package com.app.cense.ui.achievements;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.FileProvider;

import android.Manifest;
import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;

import com.app.cense.App;
import com.app.cense.LockScreenActivity;
import com.app.cense.R;
import com.yandex.metrica.YandexMetrica;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;

public class AchieveUnlockActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_achieve_unlock);
        App.getInstance().isStoragePermissionGranted(this);
    }

    public static void start(Context context, String id) {
        Intent intent = new Intent(context, AchieveUnlockActivity.class);
        context.startActivity(intent);
    }


    @Override
    protected void onStop() {
        super.onStop();
        if (isNormal){
            finish();
        }else {
            if (!isMyAppLauncherDefault()) {
                Intent intent2 = new Intent(this, LockScreenActivity.class);
                intent2.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                this.startActivity(intent2);
            }
        }
    }
    boolean isNormal = false;
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        isNormal = true;
    }

    public void change(View view){
        if(view.getId()==R.id.share){
            achievementShare("I've discovered the secrets of electricity in Cense. Try it at cense.study!", R.drawable.young_tesla_share);
        } else
        if(view.getId()==R.id.close){
            isNormal = true;
            System.out.println("close");
            finish();
        }

    }

    private boolean isMyAppLauncherDefault() {
        final IntentFilter filter = new IntentFilter(Intent.ACTION_MAIN);
        filter.addCategory(Intent.CATEGORY_HOME);

        List<IntentFilter> filters = new ArrayList<>();
        filters.add(filter);

        List<ComponentName> activities = new ArrayList<>();
        final PackageManager packageManager = this.getPackageManager();

        packageManager.getPreferredActivities(filters, activities, null);

        boolean has = false;
        for (ComponentName activity : activities) {

            if ("com.app.cense".equals(activity.getPackageName())) {
                has = true;
            }
        }
        return has;
    }

    private void achievementShare(String text, int imageId){
        YandexMetrica.reportEvent("Achievement share", "young tesla");
        Bitmap bm = BitmapFactory.decodeResource( getResources(), imageId);
        String extStorageDirectory = Environment.getExternalStorageDirectory().toString();

        File file = new File(extStorageDirectory, "young_tesla.JPG");

        FileOutputStream outStream = null;
        try {
            outStream = new FileOutputStream(file);
            bm.compress(Bitmap.CompressFormat.PNG, 100, outStream);
            outStream.flush();
            outStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        Intent emailIntent = new Intent(android.content.Intent.ACTION_SEND);
        emailIntent.setType("application/image");
        emailIntent.putExtra(android.content.Intent.EXTRA_EMAIL, new String[]{"array"});
        emailIntent.putExtra(android.content.Intent.EXTRA_SUBJECT,"Achievement");
        emailIntent.putExtra(android.content.Intent.EXTRA_TEXT, text);

        File file1 = new File(String.valueOf(Uri.fromFile(file)));
        emailIntent.putExtra(Intent.EXTRA_STREAM, FileProvider.getUriForFile(this, "com.example.homefolder.example.provider",file));
        startActivity(Intent.createChooser(emailIntent, "Share"));
    }
}