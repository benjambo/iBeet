/**************************************************************************************************
 *
 *                  FoodActivity
 *
 *  User Inputs:
 *          PlateWeigh (50 - 1000)
 *          Percentage of Vegetables to rest of plate (0 - 100)
 *          Percentage of meat to rest of the plate (0 - 100)
 *          Calculate button -> CaloriesCalculator.class
 *
 *  Receives:
 *          Calories his day : int
 *          Calories this week : int
 *          daily nutrition comparison (calories, carbs, protein, fats : int, int, int, int)
 *          weekly nutritiom comparison (calories, carbs, protein, fats : int, int, int, int)
 *
 **************************************************************************************************/

package com.example.ibeet;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Locale;


public class FoodActivity extends AppCompatActivity {

    private double weight, vegetablePercentage, meatPercentage;
    private SeekBar sbWeight, sbVege, sbMeat;
    private Button calculate;
    private TextView dayTxtV, weekTxtV;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food);

        //Set a ton of onClickListeners
        sbWeight = findViewById(R.id.sbWeight);
        sbVege = findViewById(R.id.sbVege);
        sbMeat = findViewById(R.id.sbMeat);
        calculate = findViewById(R.id.calculatePlateBtn);
        dayTxtV = findViewById(R.id.dayBreakdownTxt);
        weekTxtV = findViewById(R.id.weekBreakdownTxt);

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
                Toast.makeText(FoodActivity.this, "Weight set to: " +
                        (1000 * (weight/100) + 50) +
                                " grams", Toast.LENGTH_SHORT).show();
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

        /* this is currently broken

        StringBuilder dayText = new StringBuilder();
        StringBuilder weekText = new StringBuilder();
        for(int i=0; i<4; i++){
            dayText.append(String.format(Locale.getDefault() ,"%.1f",
                    CaloriesCalculator.getInstance().getDaysResults()[i]) +
                    " || ");
            weekText.append(String.format(Locale.getDefault(), "%.1f",
                    CaloriesCalculator.getInstance().getWeeksAverageResults()[i]) +
                    " || ");
        }
        dayTxtV.setText(("Calories || Carbs ||Protein ||Fats \n" + dayText));
        weekTxtV.setText(("Calories || Carbs ||Protein ||Fats \n" + weekText));
        */
    }
}
