package com.app.cense.data.firebase;

import com.app.cense.App;
import com.app.cense.data.room.testResults.TestData;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

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
        }catch (Exception e){
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

        if (validLogin==null) shortName(login);

        mDatabase.child("-App User Statistic").child(validLogin).child(date).child("average time").setValue(averageTime);
        mDatabase.child("-App User Statistic").child(validLogin).child(date).child("right answers").setValue(rightAnswers);
    }

    public void writePowerOff(String login, String text){
        if (validLogin==null) shortName(login);
        String date = App.getDate("dd-MM-yyyy hh:mm:ss");
        System.out.println(validLogin+" login");


        mDatabase.child("-App User Statistic").child(validLogin).child(date+" power").setValue(text);
    }

}
