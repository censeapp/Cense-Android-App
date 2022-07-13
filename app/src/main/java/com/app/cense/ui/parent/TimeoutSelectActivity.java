package com.app.cense.ui.parent;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.app.cense.App;
import com.app.cense.R;
import com.app.cense.data.SharedPreferences.AppPreferences;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.io.IOException;

public class    TimeoutSelectActivity extends AppCompatActivity {

    private CheckBox matchCb;
    private CheckBox scienceCb;
    private CheckBox engCb;
    private CheckBox randomCb;
    private EditText answersToUnlockTV;

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
        checkBox.setChecked(true);
    }

    public void changeCourse(View view){
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


    }
    
  

    public void changeTimeOut(View view) {
        EditText targetClass = findViewById(R.id.classTargetSelectActivity);
        EditText targetDifficulty = findViewById(R.id.difficultyTargetSelectActivity);
        if (!timeSpinner.equalsIgnoreCase("") &&
                !subjectSpinner.equalsIgnoreCase("") &&
                !targetClass.getText().toString().equalsIgnoreCase("") &&
                !targetDifficulty.getText().toString().equalsIgnoreCase("") && !answersToUnlockTV.getText().toString().equalsIgnoreCase("")) {
            try {
                Toast.makeText(this, "Apply", Toast.LENGTH_SHORT).show();
                System.out.println("Here");
                setProperty(TimeoutSelectActivity.targetClass, targetClass.getText().toString(), this);
                setProperty(TimeoutSelectActivity.targetDifficulty, targetDifficulty.getText().toString(), this);
                setProperty(TimeoutSelectActivity.targetCourse, selectedCourse, this);
                setProperty(TimeoutSelectActivity.targetTime, timeSpinner, this);
                App.getInstance().getSharedPreferences().saveAnswersToUnlock(Integer.parseInt(answersToUnlockTV.getText().toString()));
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