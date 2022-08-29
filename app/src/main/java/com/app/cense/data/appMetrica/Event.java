package com.app.cense.data.appMetrica;

import android.view.View;

import com.app.cense.App;

import com.app.cense.data.room.testAnswers.AnswerEntity;
import com.app.cense.data.room.testResults.TestData;
import com.yandex.metrica.YandexMetrica;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

public class Event {
    public static void allAnswers(List<AnswerEntity> list){
        for (AnswerEntity i : list){
            new Thread() {
                @Override
                public void run() {
                        try {
                            TimeUnit.MILLISECONDS.sleep(250);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    if (i.isRight){
                        trueAnswer(i);
                    } else {
                        falseAnswer(i);
                    }
                }
            }.start();
        }
    }


    public static void trueAnswer(AnswerEntity obj){
        String answer =  obj.option+", "+obj.resolutionTime+", "+obj.attempt+", "+obj.tag1+", "+obj.tag2+", "+obj.tag3+", "+obj.tag4+", "+obj.tag5+", "+obj.subject+", "+obj.difficult;
        YandexMetrica.reportEvent(Objects.requireNonNull(App.getInstance().getSharedPreferences().getLogin()), "{\"test results\": {\""+obj.date+"\":{\"right answers\":\""+answer+"\"}}}");
    }
    public static void falseAnswer(AnswerEntity obj){
        String answer = obj.option+", "+obj.tag1+", "+obj.tag2+", "+obj.tag3+", "+obj.tag4+", "+obj.tag5+", "+obj.subject+", "+obj.difficult;
        YandexMetrica.reportEvent(Objects.requireNonNull(App.getInstance().getSharedPreferences().getLogin()), "{\"test results\": {\""+obj.date+"\":{\"wrong answers\":\""+answer+"\"}}}");
    }

    public static void allAverage(List<TestData> testDataList){
        System.out.println("allAvarege "+testDataList+"  "+testDataList.size());
        for (TestData i : testDataList){
            new Thread() {
                @Override
                public void run() {
                    try {
                        TimeUnit.MILLISECONDS.sleep(250);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    averageValues(i.date, i.averageTime, i.rightAnswers);
                }
            }.start();
        }
    }

    public static void averageValues(String date, int averageTime, int percentage){
        String str = "allanswers average time "+averageTime+", percentage right "+percentage;
        YandexMetrica.reportEvent(App.getInstance().getSharedPreferences().getLogin(), "{\"test results\": {\""+date+"\":{\"average \":\""+str+"\"}}}");
    }

    public static void openPurchaseFragment(String date, String buyName){
        YandexMetrica.reportEvent(App.getInstance().getSharedPreferences().getLogin(), "{\"buys\": {\""+buyName+"\":\""+date+" opening a pop-up \"}}");
    }
    public static void succefulPurchase(String date, String buyName){
        YandexMetrica.reportEvent(App.getInstance().getSharedPreferences().getLogin(), "{\"buys\": {\""+buyName+"\":\""+date+" successful purchase \"}}");
    }

    public static void shareAchievement(String text){
        YandexMetrica.reportEvent(Objects.requireNonNull(App.getInstance().getSharedPreferences().getLogin()), "{\"achievements\": {\""+App.getDate(App.DATE_FORMAT)+" "+text+"\":\" shared \"}}");
    }

    public static void awardedAchievement(String text){
        YandexMetrica.reportEvent(App.getInstance().getSharedPreferences().getLogin(), "{\"achievements\": {\""+text+"\":\" awarded \"}}");
    }
    public static void power(String text){
        YandexMetrica.reportEvent(Objects.requireNonNull(App.getInstance().getSharedPreferences().getLogin()), "{\"power\": \""+App.getDate(App.DATE_FORMAT)+" "+text+"\"}");
    }



}
