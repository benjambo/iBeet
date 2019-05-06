package com.example.ibeet;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class ProfileActivity extends AppCompatActivity {
private TextView username, age, sessionDistance, sessionSpeed, allTimeDistance, lastSessionSpeed;
private Button logout;
private SharedPreferences prefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        username = findViewById(R.id.userName);
        age = findViewById(R.id.ageDisplay);
        logout = findViewById(R.id.logoutBtn);

        sessionDistance = findViewById(R.id.thisSessionDistanceView);
        sessionSpeed = findViewById(R.id.thisSessionAvSpeedView);
        allTimeDistance = findViewById(R.id.allTimeDistanceView);
        lastSessionSpeed = findViewById(R.id.lastSessionAvSpeedView);

        prefs = getSharedPreferences("com.example.ibeet.DATES", MODE_PRIVATE);

        //GETTING USERS NAME AND AGE FROM SHARED PREFERENCES
        String user = prefs.getString("nameKey", "Login");
        String ages = prefs.getString("ageKey", "to see");

        username.setText(user);
        age.setText(ages);

        //USERS TRACKER RECORDS DISPLAYED HERE:
        float currSesDist = prefs.getFloat("sessionDistanceTravelled",0);
        float currSesSpeed = prefs.getFloat("thisSessionAverageSpeed", 0);
        float allDistance = prefs.getFloat("allTimeDistanceTravelled",0);
        float lastSesSpeed = prefs.getFloat("lastSessionAverageSpeed",0);

        sessionDistance.setText(String.valueOf(currSesDist));
        sessionSpeed.setText(String.valueOf(currSesSpeed));
        allTimeDistance.setText(String.valueOf(allDistance));
        lastSessionSpeed.setText(String.valueOf(lastSesSpeed));
        //ON CLICK LOGS OUT OF PROFILE
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
}
