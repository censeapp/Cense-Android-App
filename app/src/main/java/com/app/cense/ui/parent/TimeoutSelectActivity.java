package com.app.cense.ui.parent;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewTreeObserver;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;

import com.app.cense.App;
import com.app.cense.MainActivity;
import com.app.cense.Payment;
import com.app.cense.PurchasedChecker;
import com.app.cense.R;
import com.app.cense.TimeTracker;
import com.app.cense.data.SharedPreferences.AppPreferences;
import com.app.cense.data.appMetrica.Event;
import com.app.cense.ui.browser.WebviewFragment;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.io.IOException;
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

public class    TimeoutSelectActivity extends AppCompatActivity {

    private CheckBox matchCb;
    private CheckBox scienceCb;
    private CheckBox engCb;
    private CheckBox bioCb;
    private CheckBox physCb;
    private CheckBox randomCb;
    private EditText answersToUnlockTV;
    WebView webView;
    public ProgressBar progressBar;

    public static final String targetClass = "target.class";
    public static final String targetDifficulty = "target.difficulty";
    public static final String targetCourse = "target.course";
    public static final String targetTime = "target.time";

    private String selectedCourse = "Math";

    String[] data = {"10m", "20m", "30m", "40m", "50m", "60m"};
    String[] courses = {"Math", "Science", "English", "Random"};
    /** Called when the activity is first created. */
  static  public String timeSpinner = "";
    static public String subjectSpinner = "";
    private TimeTracker timeTracker;

    public void clickHandler(View view) {
    }

    public void setProperty(String key, String value, Context context) throws IOException {
        SharedPreferences sharedPreferences = this.getSharedPreferences("preferences", MODE_PRIVATE);
        SharedPreferences.Editor ed = sharedPreferences.edit();
        ed.putString(key, value);
        ed.apply();
    }

    private void checkBox(CheckBox checkBox){
        matchCb.setChecked(false);
        scienceCb.setChecked(false);
        engCb.setChecked(false);
        randomCb.setChecked(false);
        physCb.setChecked(false);
        bioCb.setChecked(false);
        checkBox.setChecked(true);
    }



    final int BUY_1_TOKENIZE = 1001; //код покупки для onActivityResult()
    final String BUY_1_NAME = "Юный биомеханик"; //название товара
    final public static String BUY_1_DESCRIPTION = "Открытие курсов физики и биологии"; //описание
    final BigDecimal BUY_1_PRICE = new BigDecimal("249.90"); //цена
    final String BUY_1_CURRENCY = "RUB"; //валюта


    void buy1Tokenize() {
        Event.openPurchaseFragment(App.getDate(App.DATE_FORMAT), BUY_1_DESCRIPTION);
        App.purchaseTry = BUY_1_DESCRIPTION;
        Set<PaymentMethodType> paymentMethodTypes = new HashSet<PaymentMethodType>(){
            {
                add(PaymentMethodType.SBERBANK); // выбранный способ оплаты - SberPay
                //add(PaymentMethodType.YOO_MONEY); // выбранный способ оплаты - ЮMoney
                add(PaymentMethodType.BANK_CARD); // выбранный способ оплаты - Банковская карта
            }};
        PaymentParameters paymentParameters = new PaymentParameters(
                new Amount(BUY_1_PRICE, Currency.getInstance(BUY_1_CURRENCY)),
                BUY_1_NAME,
                BUY_1_DESCRIPTION,
                "test_OTI2MTU4VRQVDrT5U5EN1LQ41-nu88Kkp2IVLzrPw_M", //Ключ для мобильного SDK
                "926158", //ShopId
                SavePaymentMethod.OFF,
                paymentMethodTypes,
                null,
                null,
                "+79041234567",
                new GooglePayParameters(),
                "924574"
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
                    progressBar.setVisibility(View.VISIBLE);
                    TokenizationResult result = Checkout.createTokenizationResult(data);
                    Runnable task = () -> {
                        Payment.sendToken(this, result.getPaymentToken(), BUY_1_PRICE, BUY_1_DESCRIPTION, BUY_1_CURRENCY);//отправка токена на платёжный сервер
                    };
                    Thread thread1 = new Thread(task);
                    thread1.start();
                    try {
                        thread1.join();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    break;
                case RESULT_CANCELED:
                    progressBar.setVisibility(View.GONE);
                    // user canceled tokenization
                    App.purchaseTry = "";
                    Toast.makeText(this, "Покупка не завершена", Toast.LENGTH_SHORT).show();
                    break;
            }
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        App.purchaseTry = "";
    }

    public void hideWebview(View view){
        webView.setVisibility(View.GONE);
    }
    public void setOnTouch(){
        onTouch(findViewById(R.id.answers_to_unlock));
        onTouch(findViewById(R.id.difficultyTargetSelectActivity));
        onTouch(findViewById(R.id.classTargetSelectActivity));

    }

    public void onTouch(View view){
        view.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                webView.setVisibility(View.GONE);
                return false;
            }
        });
    }

    @SuppressLint("SetJavaScriptEnabled")
    public void startValidation(TimeoutSelectActivity context) {
        webView.setVisibility(View.VISIBLE);
        WebSettings webSettings = webView.getSettings();
        webView.setWebViewClient(new WebViewClient() {
            public boolean shouldOverrideUrlLoading(WebView view, String url){
                if (url.contains("success")){
                    webView.setVisibility(View.GONE);
                    Toast.makeText(context, "Платёж обрабатывается", Toast.LENGTH_LONG).show();
                    PurchasedChecker.startPurchasesChecked(context);
                }
                System.out.println("перенаправлен "+url);
                view.loadUrl(url);
                return false;
            }
            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                progressBar.setVisibility(View.GONE);
            }
        });
        webSettings.setJavaScriptEnabled(true);
        webView.loadUrl(App.three_d_secure_url);
    }

    @Override
    protected void onPause() {
        super.onPause();
        timeTracker.onPauseTime = System.currentTimeMillis();
        timeTracker.saveTime();
    }

    @Override
    protected void onResume() {
        super.onResume();
        PurchasedChecker.startPurchasesChecked(this);//сунуть в onresume
        App.getInstance().getFirebaseController().readBuy(App.getInstance().getSharedPreferences().getLogin(), TimeoutSelectActivity.BUY_1_DESCRIPTION);
        timeTracker.onResumeTime = System.currentTimeMillis();
    }

    public void changeCourse(View view){
        webView.setVisibility(View.GONE);
        if (view.getId()==R.id.phys_cb){
            if (!App.getInstance().getSharedPreferences().getPurchaseStatus(BUY_1_DESCRIPTION)) {
                if (!App.purchaseTry.equals("")){//если юзер случайно закрыл webview
                    webView.setVisibility(View.VISIBLE);
                } else {
                    buy1Tokenize();
                }
                physCb.setChecked(false);
            }else{
                System.out.println("товар был приобретён ранее");
                checkBox(physCb);
                selectedCourse = "Physics";
            }
        }
        if (view.getId()==R.id.bio_cb){

            if (!App.getInstance().getSharedPreferences().getPurchaseStatus(BUY_1_DESCRIPTION)) {
                if (!App.purchaseTry.equals("")){//если юзер случайно закрыл webview
                    webView.setVisibility(View.VISIBLE);
                } else {
                    buy1Tokenize();
                }
                bioCb.setChecked(false);
            }else{
                System.out.println("товар был приобретён ранее");
                checkBox(bioCb);
                selectedCourse = "Biology";
            }
        }

        if (view.getId()==R.id.math_cb){
            checkBox(matchCb);
            selectedCourse = "Math";
        }
        if (view.getId()==R.id.science_cb){
            checkBox(scienceCb);
            selectedCourse = "Science";
        }
        if (view.getId()==R.id.eng_cb){
            checkBox(engCb);
            selectedCourse = "English";
        }
        if (view.getId()==R.id.randomcb){
            checkBox(randomCb);
            selectedCourse = "Random";
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timeout_select);

        matchCb = findViewById(R.id.math_cb);
        scienceCb = findViewById(R.id.science_cb);
        engCb = findViewById(R.id.eng_cb);
        randomCb = findViewById(R.id.randomcb);
        bioCb = findViewById(R.id.bio_cb);
        physCb = findViewById(R.id.phys_cb);
        webView = findViewById(R.id.three_d_secure);
        progressBar = findViewById(R.id.progressBar3);

        answersToUnlockTV = findViewById(R.id.answers_to_unlock);

        ArrayAdapter<String> adapterCourse = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, courses);
        adapterCourse.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        Spinner spinnerCourse = findViewById(R.id.CourseSpinner);
        spinnerCourse.setAdapter(adapterCourse);
        spinnerCourse.setPrompt("Course");
        spinnerCourse.setSelection(1);
        spinnerCourse.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                subjectSpinner = spinnerCourse.getSelectedItem().toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, data);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        Spinner spinner = findViewById(R.id.timeoutSpinnerTimeoutSelect);
        spinner.setAdapter(adapter);
        // заголовок
        spinner.setPrompt("Title");
        // выделяем элемент
        spinner.setSelection(2);
        // устанавливаем обработчик нажатия
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long id) {
                // показываем позиция нажатого элемента
                TimeoutSelectActivity.timeSpinner =
                        spinner.getSelectedItem().toString().replaceAll("m", "");
            }
            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
            }
        });
        TimeoutSelectActivity.timeSpinner =
                spinner.getSelectedItem().toString().replaceAll("m", "");
        subjectSpinner = spinnerCourse.getSelectedItem().toString();
        FirebaseAuth auth = FirebaseAuth.getInstance();
        FirebaseUser user = auth.getCurrentUser();
        AppPreferences sp = new AppPreferences(this);
        assert user != null;
        sp.saveLogin(user.getEmail());
        setOnTouch();

        timeTracker = new TimeTracker();
        timeTracker.classname = "select";
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        MainActivity.start(this);
        finish();
    }

    public void changeTimeOut(View view) {
        webView.setVisibility(View.GONE);
        EditText targetClass = findViewById(R.id.classTargetSelectActivity);
        EditText targetDifficulty = findViewById(R.id.difficultyTargetSelectActivity);
        if (!timeSpinner.equalsIgnoreCase("") &&
                !subjectSpinner.equalsIgnoreCase("") &&
                !targetClass.getText().toString().equalsIgnoreCase("") &&
                !targetDifficulty.getText().toString().equalsIgnoreCase("") && !answersToUnlockTV.getText().toString().equalsIgnoreCase("")) {
            try {
            if (Integer.parseInt(answersToUnlockTV.getText().toString())<7) {

                    Toast.makeText(this, "Apply", Toast.LENGTH_SHORT).show();
                    System.out.println("Here");
                    setProperty(TimeoutSelectActivity.targetClass, targetClass.getText().toString(), this);
                    setProperty(TimeoutSelectActivity.targetDifficulty, targetDifficulty.getText().toString(), this);
                    setProperty(TimeoutSelectActivity.targetCourse, selectedCourse, this);
                    setProperty(TimeoutSelectActivity.targetTime, timeSpinner, this);
                    App.getInstance().getSharedPreferences().saveAnswersToUnlock(Integer.parseInt(answersToUnlockTV.getText().toString()));
                } else {
                    Toast.makeText(this, "слишком большое количество вопросов для разблокировки", Toast.LENGTH_SHORT).show();
                }
                } catch (IOException e) {
                    Toast.makeText(this, "error", Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                }

        } else {
            Toast.makeText(this, "fill in all fields", Toast.LENGTH_SHORT).show();
        }
    }



    public static void start(Context context) {
        Intent intent = new Intent(context, TimeoutSelectActivity.class);
        context.startActivity(intent);
    }
}