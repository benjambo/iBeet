package com.example.ibeet;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private Button food, login, mapButton, profile, addDay, logout;
    private long backPressedTime;
    private Toast backToast;
    private SharedPreferences prefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        TimeCalculator.getInstance().updateDate(this);

        //Placeholder means for moving to foodpage
        food = findViewById(R.id.foodBtn);
        food.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent foodActivity = new Intent(MainActivity.this, FoodActivity.class);
                startActivity(foodActivity);
            }
        });

        mapButton = findViewById(R.id.mapbtn);
        mapButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent mapActivity = new Intent(MainActivity.this, TrackerActivity.class);
                startActivity(mapActivity);
            }
        });

        login = findViewById(R.id.loginBtn);
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent loginActivity = new Intent(MainActivity.this, LoginActivity.class);
                startActivity(loginActivity);
            }
        });

        profile = findViewById(R.id.profileBtn);
        profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent profileActivity = new Intent(MainActivity.this, ProfileActivity.class);
                startActivity(profileActivity);
            }
        });

        //ON CLICK LOGS OUT OF PROFILEs
        logout = findViewById(R.id.logoutBtn);
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                prefs = getSharedPreferences("com.example.ibeet.DATES", MODE_PRIVATE);
                SharedPreferences.Editor editor = prefs.edit();
                editor.putBoolean("firstStart", false);
                editor.apply();

                Intent loginActivity = new Intent(MainActivity.this, LoginActivity.class);
                startActivity(loginActivity);
                finish();
            }
        });

        //DEBUG
        addDay = findViewById(R.id.addDay);
        addDay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TextView tv =  findViewById(R.id.guideFood);
                String cals = (String)tv.getText();
                int calsInt = Integer.parseInt(cals);

                SharedPreferences prefs = MainActivity.this.getSharedPreferences("com.example.ibeet.DATES", Context.MODE_PRIVATE);
                double[] daa = new double[]{(double)calsInt, 0, 0, 0};

                FileHandler.getInstance().readUserFile(MainActivity.this).getNutCollection(
                        prefs.getString("userKey", "")).set(
                                TimeCalculator.getInstance().getDayRotation(), daa
                );

            }
        });
    }

    @Override
    protected void onPause() {
        super.onPause();
        TimeCalculator.getInstance().updateDate(this);
    }

    @Override
    public void onBackPressed() {
        /**
         * Setting back button to not to respond on first click
         * and on second click to exit the app on mainpage!!!
         */
        if (backPressedTime + 2000 > System.currentTimeMillis()) {
            backToast.cancel();
            Intent exitApp = new Intent(Intent.ACTION_MAIN);
            exitApp.addCategory(Intent.CATEGORY_HOME);
            exitApp.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(exitApp);
            return;
        } else {
            backToast = Toast.makeText(MainActivity.this, "Press back again to exit", Toast.LENGTH_SHORT);
            backToast.show();
        }
        backPressedTime = System.currentTimeMillis();
    }
}