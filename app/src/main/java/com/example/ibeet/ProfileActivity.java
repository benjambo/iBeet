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

        username = findViewById(R.id.userName);
        age = findViewById(R.id.ageDisplay);
        logout = findViewById(R.id.logoutBtn);

        prefs = getSharedPreferences("com.example.ibeet.DATES", MODE_PRIVATE);

        //GETTING USERS NAME AND AGE FROM SHARED PREFERENCES
        String user = prefs.getString("nameKey", "Login");
        String ages = prefs.getString("ageKey", "to see");

        username.setText(user);
        age.setText(ages);

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
    }
}
