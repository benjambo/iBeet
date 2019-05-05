package com.example.ibeet;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class ProfileActivity extends AppCompatActivity {
private TextView username, age;
private Button logout;
private SharedPreferences prefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        username = (TextView) findViewById(R.id.userName);
        age = (TextView) findViewById(R.id.ageDisplay);
        logout = (Button) findViewById(R.id.logoutBtn);

        prefs = getSharedPreferences("com.example.ibeet.DATES", MODE_PRIVATE);

        String user = prefs.getString("nameKey", "Login");
        String ages = prefs.getString("ageKey", "to see");

        username.setText(user);
        age.setText(ages);

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                prefs = getSharedPreferences("com.example.ibeet.DATES", MODE_PRIVATE);
                SharedPreferences.Editor editor = prefs.edit();
                editor.putBoolean("firstStart", false);
                editor.apply();

                Intent loginActivity = new Intent(ProfileActivity.this, LoginActivity.class);
                startActivity(loginActivity);
                finish();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();

        prefs = getSharedPreferences("com.example.ibeet.DATES", MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putBoolean("firstStart", false);
        editor.commit();
    }
}
