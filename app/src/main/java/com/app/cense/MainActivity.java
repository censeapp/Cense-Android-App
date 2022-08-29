package com.app.cense;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

/*import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.admin.DevicePolicyManager;
import android.content.Context;
import android.content.Intent;*/
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import com.app.cense.ui.browser.WebviewFragment;
import com.app.cense.ui.done.DoneActivity;
import com.app.cense.ui.parent.ParentAuthActivity;
import com.app.cense.ui.parent.TimeoutSelectActivity;
import com.app.cense.ui.testActivity.TestActivity;

import org.json.JSONException;

import java.math.BigDecimal;
import java.util.Currency;
import java.util.HashSet;
import java.util.Objects;
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
    TimeTracker timeTracker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        System.out.println(this.getPackageName());
        App.getInstance().isStoragePermissionGranted(this);
        if (App.getInstance().isMyAppLauncherDefault()){
            System.out.println("App.getInstance().isMyAppLauncherDefault()");
            TestActivity.start(this);
        }
        System.out.println("purchases Checked");
        PurchasedChecker.startPurchasesChecked(this);

        timeTracker = new TimeTracker();
        timeTracker.classname = "main";


    }

    public void change(View view) {
       if(view.getId() == R.id.parentMainActivity) {
            ParentAuthActivity.start(this);
           //startValidation("https://www.google.com/", this);
       } else if(view.getId() == R.id.childMainActivity) {
           LockScreenActivity.start(this);
       }
    }

    WebView webView;

    @Override
    protected void onResume() {
        super.onResume();
        timeTracker.onResumeTime = System.currentTimeMillis();
    }

    @Override
    protected void onPause() {
        super.onPause();
        timeTracker.onPauseTime = System.currentTimeMillis();
        timeTracker.saveTime();
    }

    public static void start(Context context) {
        Intent intent = new Intent(context, MainActivity.class);
        context.startActivity(intent);
    }
}