package com.example.ibeet;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private Button food;
    private Button login;

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

        login = findViewById(R.id.loginBtn);
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent loginActivity = new Intent(MainActivity.this, LoginActivity.class);
                startActivity(loginActivity);
            }
        });
    }
    //blerb
    @Override
    protected void onPause() {
        super.onPause();
        TimeCalculator.getInstance().updateDate(this);
    }
}