package com.example.ibeet;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationMenu;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class ProfileActivity extends AppCompatActivity {
private TextView username, age, sessionDistance, sessionSpeed, allTimeDistance, lastSessionSpeed, levelViewer;
private Button logout;
private long backPressedTime;
private Toast backToast;
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

        BottomNavigationView bottomNav = findViewById(R.id.bottomNavigationView);
        bottomNav.setOnNavigationItemSelectedListener(navListener);

        Menu menu = bottomNav.getMenu();
        MenuItem menuItem = menu.getItem(1);
        menuItem.setChecked(true);

        prefs = getSharedPreferences("com.example.ibeet.DATES", MODE_PRIVATE);

        //GETTING USERS NAME AND AGE FROM SHARED PREFERENCES
        String user = prefs.getString("nameKey", "Login");
        String ages = prefs.getString("ageKey", "to see");

        username.setText(String.format("Name: %s", user));
        age.setText(String.format("Age: %s", ages));

        //USER LEVEL VERSION 1.0
        levelGenerator.setTotalXp((int) prefs.getFloat("allTimeDistanceTravelled", 0));
        Log.d("Kokonaismatka",String.valueOf(prefs.getFloat("allTimeDistanceTravelled", 0)));
        levelGenerator.calculateNewLevel();
        Log.d("Current level", String.valueOf(levelGenerator.getCurrentLevel()));

        //USERS TRACKER RECORDS AND LEVEL DISPLAYS HERE:
        float currSesDist = prefs.getFloat("sessionDistanceTravelled",0);
        float currSesSpeed = prefs.getFloat("thisSessionAverageSpeed", 0);
        float allDistance = prefs.getFloat("allTimeDistanceTravelled",0);
        float lastSesSpeed = prefs.getFloat("lastSessionAverageSpeed",0);
        int userLevel = levelGenerator.getCurrentLevel();

        sessionDistance.setText(String.valueOf(currSesDist));
        sessionSpeed.setText(String.valueOf(currSesSpeed));
        allTimeDistance.setText(String.valueOf(allDistance));
        lastSessionSpeed.setText(String.valueOf(lastSesSpeed));
        levelViewer.setText(String.valueOf(userLevel));

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

    //Navigation Setup
    private BottomNavigationView.OnNavigationItemSelectedListener navListener =
            new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                    switch (menuItem.getItemId()) {
                        case R.id.nav_food:
                            Intent foodActivity = new Intent(ProfileActivity.this, FoodActivity.class);
                            startActivity(foodActivity);
                            break;

                        case R.id.nav_profile:
                            break;

                        case R.id.nav_map:
                            Intent mapActivity = new Intent(ProfileActivity.this, TrackerActivity.class);
                            startActivity(mapActivity);
                            break;
                    }
                    return false;
                }
            };

    //Back press setup
    @Override
    public void onBackPressed() {
        if (backPressedTime + 2000 > System.currentTimeMillis()) {
            backToast.cancel();
            Intent exitApp = new Intent(Intent.ACTION_MAIN);
            exitApp.addCategory(Intent.CATEGORY_HOME);
            exitApp.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(exitApp);
            return;
        } else {
            backToast = Toast.makeText(ProfileActivity.this, "Press back again to exit", Toast.LENGTH_SHORT);
            backToast.show();
        }
        backPressedTime = System.currentTimeMillis();
    }

}
