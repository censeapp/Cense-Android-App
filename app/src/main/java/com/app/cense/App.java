package com.app.cense;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Application;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.room.Room;

import com.app.cense.data.SharedPreferences.AppPreferences;
import com.app.cense.data.SharedPreferences.TimeTrakcingPreferences;
import com.app.cense.data.firebase.FirebaseController;
import com.app.cense.data.room.AppDatabase;
import com.app.cense.data.room.question.QuestionHelper;
import com.flurry.android.FlurryAgent;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.yandex.metrica.YandexMetrica;
import com.yandex.metrica.YandexMetricaConfig;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class App extends Application {
    public static App instance;
    final public static String DATE_FORMAT = "dd-MM-yyyy hh:mm:ss";
    private AppDatabase database;
    private AppPreferences appPreferences;
    private TimeTrakcingPreferences ttp;
    private static FirebaseController firebaseController;
    private boolean firstLaunch = false;
    public static String three_d_secure_url = "https://www.google.com/";
    public static String purchaseTry = "";

    public static boolean canLeave = false;
    @Override
    public void onCreate(){
        super.onCreate();
        instance = this;
        FirebaseApp.initializeApp(this);
        database = Room.databaseBuilder(this, AppDatabase.class, "database").build();
        firebaseController = new FirebaseController();
        appPreferences = new AppPreferences(this);
        ttp = new TimeTrakcingPreferences(this);

        new FlurryAgent.Builder()
                .withLogEnabled(true)
                .build(this, "3FR4HPZKT93R2GMGV99C");

        // Creating an extended library configuration.
        YandexMetricaConfig config = YandexMetricaConfig.newConfigBuilder("04efb25f-1e1e-4cad-bff5-b6b4811f43b5")
                .handleFirstActivationAsUpdate(!isFirstLaunch())
                .build();

        // Initializing the AppMetrica SDK.
        YandexMetrica.activate(getApplicationContext(), config);
        // Automatic tracking of user activity.
        YandexMetrica.enableActivityAutoTracking(this);

        TimeTracker.sendTime();
        App.getInstance().getSharedPreferences().AchievementSendedToMetrica("Yong Tesla");
    }
    public boolean isFirstLaunch(){
        QuestionHelper qh = new QuestionHelper();
        if(qh.getAllQuestions().size()==0){
            System.out.println("first start");
            firstLaunch = true;
            return true;
        }
        System.out.println("notFirstStart "+qh.getAllQuestions().size());
        firstLaunch = false;
        return false;
    }
    public FirebaseController getFirebaseController(){
        return firebaseController;
    }

    public static App getInstance() {
        return instance;
    }

    public AppDatabase getDatabase(){
        return database;
    }

    public TimeTrakcingPreferences getTtp() { return ttp; };

    @SuppressLint("MissingPermission")
    public static boolean hasConnection(final Context context)
    {
        ConnectivityManager cm = (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
        @SuppressLint("MissingPermission") NetworkInfo wifiInfo = cm.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        if (wifiInfo != null && wifiInfo.isConnected())
        {
            return true;
        }
        wifiInfo = cm.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
        if (wifiInfo != null && wifiInfo.isConnected())
        {
            return true;
        }
        wifiInfo = cm.getActiveNetworkInfo();
        if (wifiInfo != null && wifiInfo.isConnected())
        {
            return true;
        }
        return false;
    }



    public AppPreferences getSharedPreferences() {return appPreferences;}

    public  boolean isStoragePermissionGranted(Activity context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    == PackageManager.PERMISSION_GRANTED) {
                Log.v("TAG","Permission is granted");
                return true;
            } else {

                Log.v("TAG","Permission is revoked");
                ActivityCompat.requestPermissions(context, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
                return false;
            }
        }
        else { //permission is automatically granted on sdk<23 upon installation
            Log.v("TAG","Permission is granted");
            return true;
        }
    }

    public static String getDate(String format){
        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        return sdf.format(date);
    }

    public boolean isMyAppLauncherDefault() {

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
                System.out.println("1234"+activity.getPackageName());
                has = true;
            }
        }
        return has;
    }
}
