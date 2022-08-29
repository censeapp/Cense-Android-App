package com.app.cense;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.View;
import android.widget.Toast;

import androidx.fragment.app.FragmentManager;

import com.app.cense.data.appMetrica.Event;
import com.app.cense.ui.browser.WebviewFragment;
import com.app.cense.ui.parent.TimeoutSelectActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;

import java.io.OutputStreamWriter;
import java.math.BigDecimal;
import java.net.HttpURLConnection;

import java.net.URL;
import java.nio.charset.StandardCharsets;

import java.sql.Time;
import java.util.Scanner;

import jakarta.xml.bind.DatatypeConverter;
import ru.yoomoney.sdk.kassa.payments.Checkout;
import ru.yoomoney.sdk.kassa.payments.checkoutParameters.PaymentMethodType;

public class Payment {
    public static void sendToken(TimeoutSelectActivity context, String token, BigDecimal sum, String description, String currency) {
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
            writer.flush();//отправить запрос на проведение операции с использованием ключа апи, id магазина, токена и данных об операции.
            writer.close();
            httpConn.getOutputStream().close();
            InputStream responseStream = httpConn.getResponseCode() / 100 == 2
                    ? httpConn.getInputStream()
                    : httpConn.getErrorStream();
            Scanner s = new Scanner(responseStream).useDelimiter("\\A");
            String response = s.hasNext() ? s.next() : "";
            System.out.println(response);
            System.out.println("response code " + httpConn.getResponseCode());
            responseParse(response, context);//обработать полученный ответ
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void responseParse(String response, TimeoutSelectActivity context) throws JSONException {
        JSONObject jsonResponse = new JSONObject(response);
        context.runOnUiThread(() -> {
            try {
                if (!jsonResponse.isNull("status")) {
                    App.getInstance().getSharedPreferences().addTryPurchase(jsonResponse.getString("id"));//фиксируем попытку покупки

                    if (jsonResponse.get("status").equals("succeeded")) {
                        Toast.makeText(context, "Успешно", Toast.LENGTH_SHORT).show();
                        System.out.println("услешно " + jsonResponse);
                        App.getInstance().getSharedPreferences().savePurchaseStatusTrue(jsonResponse.getString("description"));
                        context.progressBar.setVisibility(View.GONE);

                        Event.succefulPurchase(App.getDate(App.DATE_FORMAT), App.purchaseTry);
                        App.getInstance().getFirebaseController().writeBuy(App.getInstance().getSharedPreferences().getLogin(), App.purchaseTry);
                        App.getInstance().getSharedPreferences().savePurchaseStatusTrue(App.purchaseTry);
                        App.purchaseTry = "";
                    }

                    if (jsonResponse.get("status").equals("canceled"))
                        Toast.makeText(context, "Отмена", Toast.LENGTH_SHORT).show();
                    if (jsonResponse.get("status").equals("pending")) {
                        Toast.makeText(context, "3DSecurity", Toast.LENGTH_SHORT).show();
                        confirmationParse(jsonResponse, context);//Запустить проверку на проведение 3DSecurity
                    }
                } else {
                    Toast.makeText(context, "Ошибка платежа", Toast.LENGTH_SHORT).show();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    private static void confirmationParse(JSONObject jsonResponse, TimeoutSelectActivity context) {
        if (!jsonResponse.isNull("confirmation")) {
            try {
                JSONObject conf = jsonResponse.getJSONObject("confirmation");
                if (!conf.isNull("confirmation_url")) {

                    App.three_d_secure_url = conf.getString("confirmation_url");

                    context.startValidation(context);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
    static void start3DSecure(TimeoutSelectActivity context, String url) {
        Intent intent = Checkout.createConfirmationIntent(context, url, PaymentMethodType.BANK_CARD);
        context.startActivityForResult(intent, 1);
    }



    public static String getPurchaseInfo(String id){
        try {
            URL url = new URL("https://api.yookassa.ru/v3/payments/"+id);
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
            return response;
        }catch (Exception e){
            e.printStackTrace();
        }
        return "error";
    }
}
