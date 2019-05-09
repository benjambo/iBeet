package com.example.ibeet;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class LoginActivity extends AppCompatActivity {
private EditText uname, pswd;
private Button login;
private TextView register;
private DatabaseSQL db;
private SharedPreferences prefs;
private long backPressedTime;
private Toast backToast;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        TimeCalculator.getInstance().updateDate(this);

        uname = findViewById(R.id.username);
        pswd = findViewById(R.id.password);
        login = findViewById(R.id.btn_login);

        prefs = getSharedPreferences("com.example.ibeet.DATES", MODE_PRIVATE);
        boolean firstStart = prefs.getBoolean("firstStart", true);

        if (!firstStart) {
            Intent profileActivity = new Intent(LoginActivity.this, ProfileActivity.class);
            startActivity(profileActivity);
        }

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = uname.getText().toString();
                String password = pswd.getText().toString();

                if (name.equals("admin") && password.equals("admin")) {
                    SharedPreferences.Editor edit = prefs.edit();
                    edit.putString("userKey", name);
                    edit.apply();
                    Intent mainActivity = new Intent(LoginActivity.this, MainActivity.class);
                    startActivity(mainActivity);
                    finish();
                }

                int id = checkUser(new User(name,password));
                if(id == -1) {
                    Toast.makeText(LoginActivity.this,"User Does Not Exist",Toast.LENGTH_SHORT).show();
                } else {
                    //set current user to prefs
                    SharedPreferences.Editor edit = prefs.edit();
                    edit.putString("userKey", name);
                    edit.apply();
                    Intent profileActivity = new Intent(LoginActivity.this, ProfileActivity.class);
                    startActivity(profileActivity);

                    finish();
                }
            }
        });

        db=new DatabaseSQL(LoginActivity.this);

        register = findViewById(R.id.register);

        String text = "Register here";

        SpannableString ss = new SpannableString(text);

        ClickableSpan clickableSpan = new ClickableSpan() {
            @Override
            public void onClick(@NonNull View widget) {
                Intent registerActivity = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(registerActivity);
            }
        };

        ss.setSpan(clickableSpan, 0, 13, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        register.setText(ss);
        register.setMovementMethod(LinkMovementMethod.getInstance());
    }

    public int checkUser(User user) {
        return db.checkUser(user);
    }

    @Override
    protected void onPause() {
        super.onPause();

        prefs = getSharedPreferences("com.example.ibeet.DATES", MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putBoolean("firstStart", false);
        editor.apply();
    }

    @Override
    public void onBackPressed() {

         /*Setting back button to not to respond on first click
         and on second click to exit the app on mainpage!!!*/
        if (backPressedTime + 2000 > System.currentTimeMillis()) {
            backToast.cancel();
            Intent exitApp = new Intent(Intent.ACTION_MAIN);
            exitApp.addCategory(Intent.CATEGORY_HOME);
            exitApp.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(exitApp);
            return;
        } else {
            backToast = Toast.makeText(LoginActivity.this, "Press back again to exit", Toast.LENGTH_SHORT);
            backToast.show();
        }
        backPressedTime = System.currentTimeMillis();
    }
}
