package com.app.cense;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;

import java.io.OutputStreamWriter;
import java.math.BigDecimal;
import java.net.HttpURLConnection;

import java.net.URL;
import java.nio.charset.StandardCharsets;

import java.util.Scanner;

import jakarta.xml.bind.DatatypeConverter;

public class Payment {
    public static void sendToken(MainActivity context, String token, BigDecimal sum, String description, String currency) {
        try {

            URL url = new URL("https://api.yookassa.ru/v3/payments");
            HttpURLConnection httpConn = (HttpURLConnection) url.openConnection();
            httpConn.setRequestMethod("POST");
            httpConn.setRequestProperty("Idempotence-Key", token);
            httpConn.setRequestProperty("Content-Type", "application/json");
            byte[] message = ("926158:test_XUxGgUzWzMDC9s5IszFm3mQz-pVqWVSh3dFULkAXD8U").getBytes(StandardCharsets.UTF_8);
            String basicAuth = DatatypeConverter.printBase64Binary(message);
            httpConn.setRequestProperty("Authorization", "Basic " + basicAuth);
            httpConn.setDoOutput(true);
            OutputStreamWriter writer = new OutputStreamWriter(httpConn.getOutputStream());
            writer.write("{\"payment_token\": \"" + token + "\",\"amount\": {\"value\": \"" + sum.toString() + "\",\"currency\": \"" + currency + "\"},\"capture\": true,\"description\": \"" + description + "\"}");
            writer.flush();
            writer.close();
            httpConn.getOutputStream().close();
            InputStream responseStream = httpConn.getResponseCode() / 100 == 2
                    ? httpConn.getInputStream()
                    : httpConn.getErrorStream();
            Scanner s = new Scanner(responseStream).useDelimiter("\\A");
            String response = s.hasNext() ? s.next() : "";
            System.out.println(response);
            System.out.println("response code " + httpConn.getResponseCode());

            responseParse(response, context);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void responseParse(String response, Activity context) throws JSONException {
        JSONObject jsonResponse = new JSONObject(response);
        context.runOnUiThread(() -> {
            try {
                if (!jsonResponse.isNull("status")) {
                    App.getInstance().getSharedPreferences().addTryPurchase(jsonResponse.getString("id"));//фиксируем попытку покупки
                    if (jsonResponse.get("status").equals("succeeded"))
                        Toast.makeText(context, "Успешно", Toast.LENGTH_SHORT).show();
                    if (jsonResponse.get("status").equals("canceled"))
                        Toast.makeText(context, "Ошибка, Недостаточно средств", Toast.LENGTH_SHORT).show();
                    if (jsonResponse.get("status").equals("pending")) {
                        Toast.makeText(context, "3DSecurity перенаправлен в браузер", Toast.LENGTH_SHORT).show();
                        confirmationParse(jsonResponse, context);//Запустить проверку на проведение 3DSecurity
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    private static void confirmationParse(JSONObject jsonResponse, Context context) {
        if (!jsonResponse.isNull("confirmation")) {
            try {
                JSONObject conf = jsonResponse.getJSONObject("confirmation");
                if (!conf.isNull("confirmation_url")) {
                    //открыть ссылку с подтверждением в браузере
                    startValidation(conf.getString("confirmation_url"), context);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private static void startValidation(String url, Context context) {
        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(String.valueOf(url)));
        context.startActivity(browserIntent);
    }

    public static void getPurchaseInfo(){
        try {
            URL url = new URL("https://api.yookassa.ru/v3/payments/2a622ee0-000f-5000-8000-17248602eddc");
            HttpURLConnection httpConn = (HttpURLConnection) url.openConnection();
            httpConn.setRequestMethod("GET");

            byte[] message = ("926158:test_XUxGgUzWzMDC9s5IszFm3mQz-pVqWVSh3dFULkAXD8U").getBytes(StandardCharsets.UTF_8);
            String basicAuth = DatatypeConverter.printBase64Binary(message);
            httpConn.setRequestProperty("Authorization", "Basic " + basicAuth);

            InputStream responseStream = httpConn.getResponseCode() / 100 == 2
                    ? httpConn.getInputStream()
                    : httpConn.getErrorStream();
            Scanner s = new Scanner(responseStream).useDelimiter("\\A");
            String response = s.hasNext() ? s.next() : "";
            System.out.println(response);
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
