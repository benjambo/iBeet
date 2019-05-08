package com.example.ibeet;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.io.File;

public class ProfileActivity extends AppCompatActivity {
private TextView username, age, sessionDistance, sessionSpeed, allTimeDistance, lastSessionSpeed, levelViewer, levelProgressionViewer;
private ProgressBar levelProgression;
private Button logout;
private SharedPreferences prefs;
LevelGenerator levelGenerator = new LevelGenerator();

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
        levelViewer = findViewById(R.id.levelView);
        levelProgression = findViewById(R.id.progressBarLevel);
        levelProgressionViewer = findViewById(R.id.levelProgressionView);

        prefs = getSharedPreferences("com.example.ibeet.DATES", MODE_PRIVATE);

        //GETTING USERS NAME AND AGE FROM SHARED PREFERENCES
        String userFromPrefs = prefs.getString("userKey", "Login");

        User user = FileHandler.getInstance().readUserFile(this).getUser(userFromPrefs);
        String ages = String.valueOf(user.age);

        username.setText(String.format("Name: %s", user));
        age.setText(String.format("Age: %s", ages));

        //USER LEVEL, FIRST SET TOTAL XP EQUAL TO TOTAL DISTANCE TRAVELLED
        levelGenerator.setTotalXp((int) prefs.getFloat("allTimeDistanceTravelled", 0));
        levelGenerator.calculateNewLevel();
        levelProgression.setMax(levelGenerator.neededXp);
        levelProgression.setProgress(levelGenerator.currentXp);
        levelProgressionViewer.setText(levelGenerator.currentXp + "m/" + levelGenerator.neededXp + "m");

        //USERS TRACKER RECORDS AND LEVEL DISPLAYS HERE:
        float currSesDist = prefs.getFloat("sessionDistanceTravelled",0);
        float currSesSpeed = prefs.getFloat("thisSessionAverageSpeed", 0);
        float allDistance = prefs.getFloat("allTimeDistanceTravelled",0);
        float lastSesSpeed = prefs.getFloat("lastSessionAverageSpeed",0);
        int userLevel = levelGenerator.getCurrentLevel();

        sessionDistance.setText(String.valueOf(currSesDist) + "m");
        sessionSpeed.setText(String.valueOf(currSesSpeed));
        allTimeDistance.setText(String.valueOf(allDistance) + "m");
        lastSessionSpeed.setText(String.valueOf(lastSesSpeed));
        levelViewer.setText(String.valueOf(userLevel));

        //ON CLICK LOGS OUT OF PROFILE
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                prefs = getSharedPreferences("com.example.ibeet.DATES", MODE_PRIVATE);
                SharedPreferences.Editor editor = prefs.edit();
                editor.putBoolean("firstStart", false);
                editor.commit();

                Intent loginActivity = new Intent(ProfileActivity.this, LoginActivity.class);
                startActivity(loginActivity);
                finish();
            }
        });
    } //yeet //yeet
}
