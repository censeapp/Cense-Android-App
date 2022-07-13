package com.app.cense.ui.parent;

import androidx.annotation.MainThread;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.app.cense.R;
import com.app.cense.data.SharedPreferences.AppPreferences;
import com.firebase.ui.auth.AuthUI;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class ParentAuthActivity extends AppCompatActivity {
    private FirebaseAuth mFirebaseAuth;
    private FirebaseUser mFirebaseUser;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_parent_auth);
        mFirebaseAuth = FirebaseAuth.getInstance();
        mFirebaseUser = mFirebaseAuth.getCurrentUser();
        checkLogin();
    }

    private void checkLogin(){
        if (mFirebaseUser == null){
            showSignInScreen();
        } else {
            AppPreferences sp = new AppPreferences(this);
            sp.saveLogin(mFirebaseUser.getEmail());
            TimeoutSelectActivity.start(this);
            finish();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        System.out.println("result1");
        if (requestCode == RC_SIGN_IN) {
            handleSignInResponse(resultCode, data);
            System.out.println("RC_SIGN_IN");
            return;
        }


    }

    @MainThread
    private void handleSignInResponse(int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            System.out.println("RESULT_OK");
            TimeoutSelectActivity.start(this);
            finish();
        }

        if (resultCode == RESULT_CANCELED) {
            Toast.makeText(this, "Incorrect login or password", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    private static final String GOOGLE_TOS_URL = "https://www.google.com/policies/terms/";
    private static final int RC_SIGN_IN = 100;

    @SuppressWarnings("deprecation")
    private void showSignInScreen() {
        startActivityForResult(
                AuthUI.getInstance().createSignInIntentBuilder()
                        .setTosUrl(GOOGLE_TOS_URL)
                        .setIsSmartLockEnabled(false)
                        .build(),
                RC_SIGN_IN);
    }

    public static void start(Context context) {
        Intent intent = new Intent(context, ParentAuthActivity.class);
        context.startActivity(intent);
    }
    EditText etLogin;
    public void change(View view) {
        etLogin = findViewById(R.id.loginParentAuth);
        switch (view.getId()) {
            case R.id.authParentAuth:

                break;
            default:
        }
    }

}