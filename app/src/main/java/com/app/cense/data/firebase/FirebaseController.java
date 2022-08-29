package com.app.cense.data.firebase;

import android.util.Log;

import androidx.annotation.NonNull;

import com.app.cense.App;
import com.app.cense.data.room.testResults.TestData;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

public class FirebaseController {
    DatabaseReference mDatabase;
    String validLogin;
    public FirebaseController(){
        mDatabase = FirebaseDatabase.getInstance().getReference();
    }

    private void shortName(String login){
        try {
            int i = 0;
            i = login.indexOf("@");
            validLogin = login.substring(0, i);
            validLogin = validLogin.replace(".", "");
            validLogin = validLogin.replace("#", "");
            validLogin = validLogin.replace(",", "");
            validLogin = validLogin.replace("$", "");
            validLogin = validLogin.replace("[", "");
            validLogin = validLogin.replace("]", "");

        }catch (Exception e){
            System.out.println("АХТУНГ!!!");
            e.printStackTrace();
            validLogin = "unauthorized";
        }
    }

    public void insertAllStatistic(String login, List<TestData> data){
        if (validLogin==null) shortName(login);
        for (TestData i : data){
            writeStatistic(validLogin, i.date, i.averageTime, i.rightAnswers);
        }
    }

    public void writeStatistic(String login, String date, int averageTime, int rightAnswers){
        /*if (validLogin==null) shortName(login);
        mDatabase.child("-App User Statistic").child(validLogin).child(date).child("average time").setValue(averageTime);
        mDatabase.child("-App User Statistic").child(validLogin).child(date).child("right answers").setValue(rightAnswers);*/
    }

    public void writeBuy(String login, String buyName){
        if (validLogin==null) shortName(login);
        mDatabase.child("-App User Buys").child(validLogin).child(buyName).setValue(true);
    }
    public void readBuy(String login, String buyName){
        if (validLogin==null) shortName(login);
        System.out.println("ридбай");
        // Add all polls in ref as rows
        mDatabase.child("-App User Buys").child(validLogin).child(buyName).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {


                if (!task.isSuccessful()) {
                    Log.e("firebase", "Error getting data", task.getException());
                }
                else {
                    Log.d("firebase achieve", String.valueOf(task.getResult().getValue()));
                    if (Boolean.parseBoolean(String.valueOf(task.getResult().getValue()))){
                        App.getInstance().getSharedPreferences().savePurchaseStatusTrue(buyName);
                    }
                }
            }
        });
    }

    public void writePowerOff(String login, String text){
        if (validLogin==null) shortName(login);
        String date = App.getDate("dd-MM-yyyy hh:mm:ss");
        System.out.println(validLogin+" login");


        mDatabase.child("-App User Statistic").child(validLogin).child(date+" power").setValue(text);
    }

}
