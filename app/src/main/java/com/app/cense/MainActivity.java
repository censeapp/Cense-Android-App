package com.app.cense;

import androidx.appcompat.app.AppCompatActivity;

/*import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.admin.DevicePolicyManager;
import android.content.Context;
import android.content.Intent;*/
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.app.cense.ui.done.DoneActivity;
import com.app.cense.ui.parent.ParentAuthActivity;
import com.app.cense.ui.testActivity.TestActivity;

import org.json.JSONException;

import java.math.BigDecimal;
import java.util.Currency;
import java.util.HashSet;
import java.util.Set;

import ru.yoomoney.sdk.kassa.payments.Checkout;
import ru.yoomoney.sdk.kassa.payments.TokenizationResult;
import ru.yoomoney.sdk.kassa.payments.checkoutParameters.Amount;
import ru.yoomoney.sdk.kassa.payments.checkoutParameters.GooglePayParameters;
import ru.yoomoney.sdk.kassa.payments.checkoutParameters.PaymentMethodType;
import ru.yoomoney.sdk.kassa.payments.checkoutParameters.PaymentParameters;
import ru.yoomoney.sdk.kassa.payments.checkoutParameters.SavePaymentMethod;
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
           //ParentAuthActivity.start(this);
           //buy1Tokenize();

           Runnable task = () -> {
               Payment.getPurchaseInfo();
           };
           Thread thread1 = new Thread(task);
           thread1.start();
           //startValidation("aihds");


           //DoneActivity.Companion.start(this);
          // AchieveUnlockActivity.start(this);
       } else if(view.getId() == R.id.childMainActivity) {
           LockScreenActivity.start(this);
       }
    }

    final int BUY_1_TOKENIZE = 1001; //код покупки для onActivityResult()
    final String BUY_1_NAME = "Товар 1"; //название товара
    final String BUY_1_DESCRIPTION = "Тестовый товар"; //описание
    final BigDecimal BUY_1_PRICE = new BigDecimal("249.90"); //цена
    final String BUY_1_CURRENCY = "RUB"; //валюта


    void buy1Tokenize() {
        Set<PaymentMethodType> paymentMethodTypes = new HashSet<>();
        paymentMethodTypes.add(PaymentMethodType.BANK_CARD);
        PaymentParameters paymentParameters = new PaymentParameters(
                new Amount(BUY_1_PRICE, Currency.getInstance(BUY_1_CURRENCY)),
                BUY_1_NAME,
                BUY_1_DESCRIPTION,
                "test_OTI2MTU4VRQVDrT5U5EN1LQ41-nu88Kkp2IVLzrPw_M", //Ключ для мобильного SDK
                "926158", //ShopId
                SavePaymentMethod.OFF,
                paymentMethodTypes
        );
        Intent intent = Checkout.createTokenizeIntent(this, paymentParameters);
        startActivityForResult(intent, BUY_1_TOKENIZE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == BUY_1_TOKENIZE) {
            switch (resultCode) {
                case RESULT_OK:
                    // successful tokenization
                    TokenizationResult result = Checkout.createTokenizationResult(data);
                    Runnable task = () -> {
                        Payment.sendToken(this, result.getPaymentToken(), BUY_1_PRICE, BUY_1_DESCRIPTION, BUY_1_CURRENCY);//отправка токена на платёжный сервер
                    };
                    Thread thread1 = new Thread(task);
                    thread1.start();
                    break;
                case RESULT_CANCELED:
                    // user canceled tokenization
                    Toast.makeText(this, "Отмена токенизации", Toast.LENGTH_SHORT).show();
                    break;
            }
        }
    }







}