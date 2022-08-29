package com.app.cense;

import android.app.Activity;
import android.widget.Toast;

import com.app.cense.data.SharedPreferences.AppPreferences;
import com.app.cense.data.appMetrica.Event;
import com.app.cense.ui.parent.TimeoutSelectActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashSet;

public class PurchasedChecker {
    public static void startPurchasesChecked(Activity context){
        AppPreferences preferences = App.getInstance().getSharedPreferences();

        HashSet<String> idList = preferences.getAllTryPurchaseIds();


        for (String i : idList){
            Runnable task = () -> {
                try {
                    JSONObject json = new JSONObject(Payment.getPurchaseInfo(i));

                    if (json.getString("status").equals("succeeded")){
                        preferences.savePurchaseStatusTrue(json.getString("description"));
                        System.out.println("куплено"+json);
                        System.out.println(App.getInstance().getSharedPreferences().getLogin());
                        App.getInstance().getFirebaseController().writeBuy(App.getInstance().getSharedPreferences().getLogin(), json.getString("description"));
                        App.getInstance().getSharedPreferences().deleteAllTryPurchase();
                        if(!App.purchaseTry.equals("")){
                            App.getInstance().getFirebaseController().writeBuy(App.getInstance().getSharedPreferences().getLogin(), App.purchaseTry);
                            App.getInstance().getSharedPreferences().savePurchaseStatusTrue(App.purchaseTry);
                            Event.succefulPurchase(App.getDate(App.DATE_FORMAT), App.purchaseTry);
                            context.runOnUiThread(new Runnable() {
                                public void run() {
                                    Toast.makeText(context, "Успешная покупка", Toast.LENGTH_SHORT).show();
                                    App.purchaseTry = "";
                                }
                            });
                        }
                        //App.getInstance().getSharedPreferences().deleteAllTryPurchase();
                    } else if (json.getString("status").equals("canceled")){
                        App.getInstance().getSharedPreferences().deleteAllTryPurchase();
                        if(!App.purchaseTry.equals("")){
                            context.runOnUiThread(new Runnable() {
                                public void run() {
                                    System.out.println("UI thread I am the UI thread");
                                    Toast.makeText(context, "Платёж не прошёл", Toast.LENGTH_SHORT).show();
                                    App.purchaseTry = "";
                                }
                            });
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            };
            Thread thread1 = new Thread(task);
            thread1.start();
        }


    }
}
