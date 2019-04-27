package com.example.ibeet;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.Toast;


public class FoodActivity extends AppCompatActivity {

    private double weight, vegetablePercentage, meatPercentage;
    private SeekBar sbWeight, sbVege, sbMeat;
    private Button calculate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food);

        //Set a ton of onClickListeners
        sbWeight = findViewById(R.id.sbWeight);
        sbVege = findViewById(R.id.sbVege);
        sbMeat = findViewById(R.id.sbMeat);
        calculate = findViewById(R.id.calculatePlateBtn);

        sbWeight.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                weight = (double)progress;
            }
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                Toast.makeText(FoodActivity.this, "Weight set to: " + weight+
                                "%", Toast.LENGTH_SHORT).show();
            }
        });

        sbVege.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                vegetablePercentage = (double)progress;
            }
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                Toast.makeText(FoodActivity.this, "Vegetables ratio set to: " + vegetablePercentage +
                        "%", Toast.LENGTH_SHORT).show();
            }
        });

        sbMeat.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                meatPercentage = (double)progress;
            }
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                Toast.makeText(FoodActivity.this, "Meat to carbs ratio" +
                        " set to: " + meatPercentage + "%", Toast.LENGTH_SHORT).show();
            }
        });

        calculate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                double finalWeight = (1000 * (weight/100) + 50);
                double vgPercentage = (vegetablePercentage / 100);
                double mtcPercentage = (meatPercentage / 100);

                /**
                 * This is still in debugging mode
                 */
                CaloriesCalculator.getInstance().setNewPlate(finalWeight, vgPercentage, mtcPercentage);
                Toast.makeText(FoodActivity.this,
                        CaloriesCalculator.getInstance().calculatePlate(), Toast.LENGTH_LONG).show();
            }
        });
    }
}
