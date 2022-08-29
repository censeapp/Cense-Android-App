package com.app.cense.ui.testActivity;

import android.annotation.SuppressLint;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.FragmentManager;

import com.app.cense.App;
import com.app.cense.FakeLauncherActivity;
import com.app.cense.R;
import com.app.cense.TimeTracker;
import com.app.cense.data.SharedPreferences.AppPreferences;
import com.app.cense.data.appMetrica.Event;
import com.app.cense.data.firebase.FirebaseController;
import com.app.cense.data.room.question.QuestionHelper;
import com.app.cense.data.room.testAnswers.AnswerEntity;
import com.app.cense.data.room.testAnswers.AnswerHelper;
import com.app.cense.data.room.testResults.StatisticHelper;
import com.app.cense.data.room.testResults.TestData;
import com.app.cense.power.ShutdownReceiver;
import com.app.cense.ui.achievements.AchieveUnlockActivity;
import com.app.cense.ui.done.DoneActivity;
import com.app.cense.ui.hint.HintDialogFragment;
import com.app.cense.ui.parent.TimeoutSelectActivity;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class TestActivity extends AppCompatActivity implements OnProgressListener {
    TestActivityModel test = null;
    private static boolean isReady = true;
    TextView answer;
    TextView counter;
    ProgressBar bar;
    int counterInt = 1;
    DatabaseReference databaseReference;
    private List<QuestionModel> listData;
    public static boolean canAnswer = true;
    private final String QUESTION_KEY = "Question";
    public static int targetClass;
    public static String targetTag;
    public static String targetSubject;
    public static boolean isDone = false;
    FrameLayout loader;
    private ConstraintLayout testContainer;
    private ConstraintLayout hintContainer;
    private TextView hintLabel;
    private TextView hintText;
    private String startDate;


        int trueAnswers = 0;
        int falseAnswers = 0;
        List<Long> times = new ArrayList<>();

    private void saveCurrentTimeForStatistic(){
       times.add(System.currentTimeMillis());
    }

    private void saveStatistic(){
        StatisticHelper statisticHelper = new StatisticHelper();
        TestData testData = new TestData(startDate,times, trueAnswers, falseAnswers);
        statisticHelper.insertStatistic(testData);
        AnswerHelper answerHelper = new AnswerHelper();
        answerHelper.insertAll(answerEntities);
    }



    @Override
    public void onBackPressed() {
        System.out.println(isDone);
        if (App.canLeave) isDone = true;
        if (isDone) {
            getPackageManager().clearPackagePreferredActivities(getPackageName());
            Intent startMain = new Intent(Intent.ACTION_MAIN);
            startMain.addCategory(Intent.CATEGORY_HOME);
            startMain.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(startMain);
            isDone = false;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
        System.out.println("тестАктивити onCreate");
        testContainer = findViewById(R.id.testContainer);
        hintContainer = findViewById(R.id.hint_container);
        hintLabel = findViewById(R.id.hint_label);
        hintText = findViewById(R.id.hint_tv);
        startDate = App.getDate("dd-MM-yyyy hh:mm:ss");
        setTags();


        if (isDone) {
            isDone = false;
            /*bar = findViewById(R.id.progressBarTest);
            bar.setProgress(0);

            Intent intent2 = new Intent(this, MainActivity.class);
            intent2.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            this.startActivity(intent2);*/
        }
        hideLoader();
        initialize();

        if (App.hasConnection(this)){
            System.out.println("есть тырнет");
            getDataFromRemoteDB();
        } else {
            System.out.println("нема тырнету");
            getDataFromLocalDB();
        }
        saveCurrentTimeForStatistic();
        startChronometer();
    }

    private void hideLoader(){
        System.out.println("тестАктивити hideLoad");
        if (listData!=null){
            ProgressBar loader = findViewById(R.id.progressBar);
            loader.setVisibility(View.GONE);
            ConstraintLayout container = findViewById(R.id.testContainer);
            container.setVisibility(View.VISIBLE);
        }
    }

    private void showHintCL(String text, String label, int seconds){
        hintLabel.setText(label);
        hintText.setText(text);
        hintContainer.setVisibility(View.VISIBLE);
        testContainer.setVisibility(View.GONE);
        if (seconds>0){
            new Thread() {
                        @Override
                        public void run() {
                            canAnswer = false;
                            for (int i = 0; i < seconds; i++) {
                                try {
                                    TimeUnit.SECONDS.sleep(1);
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }
                            }
                            canAnswer = true;
                        }
                    }.start();
        }
    }
    public void hideHintCL(View view){
        if (canAnswer) {
            hintContainer.setVisibility(View.GONE);
            testContainer.setVisibility(View.VISIBLE);
        } else {
            Toast.makeText(this, "Read the tip first", Toast.LENGTH_SHORT).show();
        }
    }

    private void setTags(){
        AppPreferences sp = App.getInstance().getSharedPreferences();
        TestActivity.targetTag = sp.getProperty(TimeoutSelectActivity.targetDifficulty, "");
        TestActivity.targetClass = Integer.parseInt(sp.getProperty(TimeoutSelectActivity.targetClass, "5"));
        TestActivity.targetSubject = sp.getProperty(TimeoutSelectActivity.targetCourse, "English");
    }

    @SuppressLint("MissingPermission")
    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        System.out.println("тестАктивити onFocusChanged");
        if(!hasFocus) {
            App.getInstance().getFirebaseController().writePowerOff(App.getInstance().getSharedPreferences().getLogin(), "PROBABLY OFF");
            System.out.println("!hasFocus");
            Intent closeDialog = new Intent(Intent.ACTION_CLOSE_SYSTEM_DIALOGS);
            sendBroadcast(closeDialog);
        }
    }

    private void initialize() {
        System.out.println("тестАктивити initialize");
        counter = findViewById(R.id.textProgressTest);
        counter.setText("1/"+App.getInstance().getSharedPreferences().getAnswersToUnlock());
        bar = findViewById(R.id.progressBarTest);
        listData = new ArrayList<>();
        bar.setProgress(0);
        databaseReference = FirebaseDatabase.getInstance().getReference();
        //getDataFromRemoteDB();
    }

    public void onUpdateProgress(int progress) {
        System.out.println("тестАктивити onUpdateProgress");
        if (progress == 200) {
            loader.setVisibility(View.GONE);
        }
    }

    private void getDataFromRemoteDB() {
        System.out.println("тестАктивити getDataFromRemoteDB");
        ValueEventListener valueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (listData.size() > 0) {
                    listData.clear();
                }

                for (DataSnapshot dS : dataSnapshot.getChildren()) {
                    QuestionModel model = dS.getValue(QuestionModel.class);
                    assert model != null;
                    listData.add(model);
               //     System.out.println(listData.size());
                   // System.out.println(model.toString());
                }
                //System.out.println(targetClass + ", " + targetTag + ", " + targetSubject);
                if (test == null && listData.size() != 0) {
                    QuestionHelper questionHelper = new QuestionHelper();
                    questionHelper.rewriteTable(listData);
                    Log.e("testList", "if (test == null && listData.size() != 0)");
                                        test = new TestActivityModel(targetClass, targetTag, targetSubject, listData);
                    Log.e("testList", targetClass +" "+ targetTag + " " + targetSubject);
                    hideLoader();
                } else {
                    System.out.println("условие не выполнено "+ (test == null) + " " + (listData.size() != 0));
                }

                try {

                    assert test != null;
                    setView(test.getQuestion());
                } catch (IllegalStateException e) {
                    e.printStackTrace();
                    System.out.println("Cantr");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        };
        databaseReference.addValueEventListener(valueEventListener);
    }

    private void getDataFromLocalDB(){
        System.out.println("тестАктивити getDataFromLocalDB");
        QuestionHelper questionHelper = new QuestionHelper();
        listData = questionHelper.getAllQuestions();

        if (listData.size() == 0){
            Toast.makeText(this, "There is no connection to the Internet", Toast.LENGTH_SHORT).show();
            isDone = true;
            finish();
        }

        System.out.println("size "+listData.size());

        for (QuestionModel model: listData) {
            System.out.println("test in "+model.toString());
        }

        if (test == null && listData.size() != 0) {
            //counter.setText("1/" + listData.size());
            System.out.println("test == null && listData.size() != 0");
            isReady = true;
            test = new TestActivityModel(targetClass, targetTag, targetSubject, listData);
            hideLoader();
        } else {
            System.out.println("условие не выполнено "+ test == null+ " "+listData.size() );

        }

        try {
            setView(test.getQuestion());
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Cantr");
            Toast.makeText(this, "There is no connection to the Internet", Toast.LENGTH_SHORT).show();
            isDone = true;
            finish();
        }
    }

    ArrayList<AnswerEntity> answerEntities = new ArrayList<>();

    int attempts = 0;//количество попыток до правильного ответа
    public void answer(View view)  {

        System.out.println("answer");

        if (isReady) {
            System.out.println("isReady");
            QuestionModel question;
            try {
                test.getQuestion();
                question = test.getQuestion();
                if(view.getId() == R.id.helpButtonTest) {
                    System.out.println("helpButton");
                    /*Toast.makeText(this, question.getTip(), Toast.LENGTH_LONG).show();*/
                    //showHint(question.getTip());
                    showHintCL(question.getTip(), "Hint", 0);

                    return;
                }
            } catch (IllegalStateException e) {
                System.out.println("Herre");
                Intent startMain = new Intent(Intent.ACTION_MAIN);
                startMain.addCategory(Intent.CATEGORY_HOME);
                startMain.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(startMain);
                return;
            }
            if (canAnswer) {

                if(view.getId() == R.id.A) {
                    answer = findViewById(R.id.aAnswerTextTest);
                }
                if(view.getId() == R.id.B) {
                    answer = findViewById(R.id.bAnswerTextTest);
                }
                if(view.getId() == R.id.C) {
                    answer = findViewById(R.id.cAnswerTextTest);
                }
                if(view.getId() == R.id.D) {
                    answer = findViewById(R.id.dAnswerTextTest);
                }
                if (!test.answer(answer.getText().toString())) {
                    falseAnswers++;
                    saveCurrentTimeForStatistic();
                    System.out.println("неправильный ответ ");

                    attempts++;
                    AnswerEntity obj = new AnswerEntity(chronometer,
                            startDate,
                            false,
                            (String) view.getTag(),
                            attempts,
                            question.tag1,
                            question.tag2,
                            question.tag3,
                            question.tag4,
                            question.tag5,
                            question.subjectName,
                            question.getDifficult());
                    if (!App.hasConnection(this)) {
                        answerEntities.add(obj);
                    } else {
                        Event.falseAnswer(obj);
                    }
                    showHintCL(question.getTip(), "Wrong answer", 8);//заменить

                    App.getInstance().getSharedPreferences().updateTrueAnswers(0);
                    Toast.makeText(this, "this is the wrong answer", Toast.LENGTH_SHORT).show();

                    /*// int downgrade = test.downgrade();
                    if (downgrade != 0) {
                        bar.setProgress(0);
                        counterInt = 1;
                        counter.setText("1/" + test.getSize());
                        setView(test.getQuestion());
                    }*/


                } else {
                    trueAnswers++;
                    saveCurrentTimeForStatistic();
                    System.out.println("Правильный ответ "+chronometer);

                    AnswerEntity obj = new AnswerEntity(chronometer , startDate, true, (String) view.getTag(), attempts, question.tag1, question.tag2, question.tag3, question.tag4, question.tag5, question.subjectName, question.getDifficult());
                    if (!App.hasConnection(this)) {
                        answerEntities.add(obj);
                    }else {
                        Event.trueAnswer(obj);
                    }
                    chronometer = 0;

                    attempts = 0;
                    isAchievement();
                    counter.setText(counterInt + "/" + test.getSize());
                    bar.setProgress(bar.getProgress() + (100 / test.getSize()));
                    System.out.println("COUNTER");
                    if (counterInt >= test.getSize()) {
                        System.out.println("Done");
                        isDone = true;
                        DoneActivity.Companion.start(this);
                        System.out.println("finish");
                        return;
                    } else {
                        if (!(counterInt == counterInt+1)){
                            counterInt++;
                        }
                        counter.setText(counterInt + "/" + test.getSize());
                    }
                    try {
                        test.pop();
                    } catch (IndexOutOfBoundsException | IllegalStateException e) {
                        e.printStackTrace();
                        isDone = true;
                        PackageManager packageManager = getPackageManager();
                        ComponentName componentName = new ComponentName(this, FakeLauncherActivity.class);
                        packageManager.setComponentEnabledSetting(componentName, PackageManager.COMPONENT_ENABLED_STATE_ENABLED, PackageManager.DONT_KILL_APP);

                        Intent selector = new Intent(Intent.ACTION_MAIN);
                        selector.addCategory(Intent.CATEGORY_HOME);
                        selector.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(selector);

                        packageManager.setComponentEnabledSetting(componentName, PackageManager.COMPONENT_ENABLED_STATE_DEFAULT, PackageManager.DONT_KILL_APP);
                        return;
                    }
                    try {
                        test.getQuestion();
                        setView(test.getQuestion());
                    } catch (IllegalStateException e) {
                        PackageManager packageManager = getPackageManager();
                        ComponentName componentName = new ComponentName(this, FakeLauncherActivity.class);
                        packageManager.setComponentEnabledSetting(componentName, PackageManager.COMPONENT_ENABLED_STATE_ENABLED, PackageManager.DONT_KILL_APP);

                        Intent selector = new Intent(Intent.ACTION_MAIN);
                        selector.addCategory(Intent.CATEGORY_HOME);
                        selector.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(selector);

                        packageManager.setComponentEnabledSetting(componentName, PackageManager.COMPONENT_ENABLED_STATE_DEFAULT, PackageManager.DONT_KILL_APP);
                    }
                }

            } else {
                if (view.getId() == R.id.helpButtonTest) {
                    System.out.println("helpButton");
                    /*Toast.makeText(this, question.getTip(), Toast.LENGTH_LONG).show();*/
                    //showHint(question.getTip());
                    showHintCL(question.getTip(), "Hint", 0);
                }
            }
            if (isDone) {
                PackageManager packageManager = getPackageManager();
                ComponentName componentName = new ComponentName(this, FakeLauncherActivity.class);
                packageManager.setComponentEnabledSetting(componentName, PackageManager.COMPONENT_ENABLED_STATE_ENABLED, PackageManager.DONT_KILL_APP);

                Intent selector = new Intent(Intent.ACTION_MAIN);
                selector.addCategory(Intent.CATEGORY_HOME);
                selector.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(selector);

                packageManager.setComponentEnabledSetting(componentName, PackageManager.COMPONENT_ENABLED_STATE_DEFAULT, PackageManager.DONT_KILL_APP);

            }
        }
    }
    int chronometer = 0;
    private void startChronometer(){
        new Thread() {
            @Override
            public void run() {
                while (true){
                    try {
                        TimeUnit.SECONDS.sleep(1);
                        chronometer++;
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }.start();
    }

    private void showHint(String text){
        HintDialogFragment hint = new HintDialogFragment(text);
        FragmentManager manager = getSupportFragmentManager();
        hint.show(manager, "dialog");
    }

    public void isAchievement(){
        App.getInstance().getSharedPreferences().updateTrueAnswers(1);
        int countTrueAnswers = App.getInstance().getSharedPreferences().getCountTrueAnswers();
        System.out.println("правильных ответов подряд"+countTrueAnswers);
            if (countTrueAnswers == 5 && !App.getInstance().getSharedPreferences().isAchievementUnlocked("young tesla")) {
                AchieveUnlockActivity.start(this, "young tesla");
                System.out.println("achievement unlocked");
            }
    }

    @Override
    protected void onResume() {
        super.onResume();
        IntentFilter filter = new IntentFilter(Intent.ACTION_SHUTDOWN);
        ShutdownReceiver mReceiver = new ShutdownReceiver();
        registerReceiver(mReceiver, filter);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        System.out.println("тест активити onKeyDown");
        if (keyCode == KeyEvent.KEYCODE_MENU) {
            App.getInstance().getFirebaseController().writePowerOff(App.getInstance().getSharedPreferences().getLogin(), "PROBABLY OFF");
            Event.power("probably off");
            Intent intent2 = new Intent(this, TestActivity.class);
            intent2.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            this.startActivity(intent2);
        }
        return super.onKeyDown(keyCode, event);
    }

    void setView(QuestionModel model) {
        TextView subjName = findViewById(R.id.textThemeTest);
        subjName.setText(model.subjectName);

        System.out.println("setVie1w");
        TextView view = findViewById(R.id.aAnswerTextTest);
        List<Answer> answers = model.getRandomAnswers();

        System.out.println(model.toString());

        view.setText(answers.get(0).getAnswerBody());

        view = findViewById(R.id.bAnswerTextTest);
        view.setText(answers.get(1).getAnswerBody());

        view = findViewById(R.id.cAnswerTextTest);
        view.setText(answers.get(2).getAnswerBody());

        view = findViewById(R.id.dAnswerTextTest);
        view.setText(answers.get(3).getAnswerBody());

        view = findViewById(R.id.questionBodyTest);
        view.setText(model.getQuestion());
    }

    public static void start(Context context) {
        System.out.println("start");
        Intent intent = new Intent(context, TestActivity.class);
        context.startActivity(intent);
    }

    private void sendStatistic(){
        if (App.hasConnection(this)){
            AppPreferences sp = App.getInstance().getSharedPreferences();
            FirebaseController fc = App.getInstance().getFirebaseController();
            StatisticHelper helper = new StatisticHelper();
            fc.insertAllStatistic(sp.getLogin(), helper.getAllStatistic());
            Event.allAverage(helper.getAllStatistic());
            helper.deleteAll();

            AnswerHelper answerHelper = new AnswerHelper();

            Event.allAnswers(answerHelper.getAll());
            answerHelper.deleteAll();
        }
    }

    @Override
    protected void onStop() {
        System.out.println(Arrays.toString(Thread.currentThread().getStackTrace()));
        System.out.println("тест активити onStop");
        super.onStop();
        if (isDone) {
            saveStatistic();
            sendStatistic();

            /*getPackageManager().clearPackagePreferredActivities(getPackageName());
            Intent startMain = new Intent(Intent.ACTION_MAIN);
            startMain.addCategory(Intent.CATEGORY_HOME);
            startMain.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(startMain);*/

            finish();
            return;
        }
       //System.out.println(isMyAppLauncherDefault());
        if (!App.getInstance().isMyAppLauncherDefault()) {
            Intent intent2 = new Intent(this, TestActivity.class);
            intent2.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            System.out.println("AS");
            this.startActivity(intent2);
        } else {

        }
    }



}