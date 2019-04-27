package com.example.ibeet;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.sql.Time;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

public class MainActivity extends AppCompatActivity {

    SharedPreferences preferences_dates;
    SharedPreferences.Editor editor_date;
    Date date; TimeZone tz;
    Button food;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        updateDate();   //Updating current date is important

        food = findViewById(R.id.foodBtn);
        food.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent foodActivity = new Intent(MainActivity.this, FoodActivity.class);
                startActivity(foodActivity);
            }
        });
    }

    @Override
    protected void onPause() {
        super.onPause();
        updateDate();
    }

    /**
     * Every time main activity is created, the date is updated. Either current date is overwritten
     * in the SharedPreferences or the first date is set on the morning of the first use of the
     * application.
     *
     * Useful
     * https://stackoverflow.com/questions/21285161/android-difference-between-two-dates
     */
    private void updateDate(){
        preferences_dates = getSharedPreferences("DATES", Activity.MODE_PRIVATE);
        editor_date = preferences_dates.edit();
        date = new Date(); tz = TimeZone.getDefault();

        if(preferences_dates.contains("FIRST_DATE")){
            editor_date.putLong("CURRENT_DATE", (date.getTime() /*+ tz.getOffset(date.getTime())*/));

        } else {
            long hoursInMilli = 1000 * 60 * 60;
            long daysInMilli = hoursInMilli * 24;

            //our first date begins the last time clock was 00:-- in devices current timezone,
            //so we have to find out the difference of current time and last 00:--.
            long difference = ((date.getTime() + tz.getOffset(date.getTime())) % daysInMilli);

            long firstDate = (date.getTime() - difference);
            editor_date.putLong("FIRST_DATE", firstDate);
        }
        editor_date.apply();

        /* //DEBUGGING
        String first_date = new SimpleDateFormat("yyyy.MM.dd HH:mm", Locale.getDefault()).format(
                new Date(preferences_dates.getLong("CURRENT_DATE", 0)));
        Toast.makeText(MainActivity.this, "TIME: " + first_date,
                Toast.LENGTH_LONG).show();
        */
    }
}