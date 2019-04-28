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
 *          Nutritional values this day : int
 *          Nutritional values this week : int
 *          daily nutrition comparison (calories, carbs, protein, fats : int, int, int, int)
 *          weekly nutritiom comparison (calories, carbs, protein, fats : int, int, int, int)
 *
 **************************************************************************************************/

package com.example.ibeet;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import java.io.StringWriter;
import java.text.DecimalFormat;
import java.util.Locale;


public class FoodActivity extends AppCompatActivity {

    private double weight, vegetablePercentage, meatPercentage;
    private SeekBar sbWeight, sbVege, sbMeat;
    private Button btnCalculate;
    private TextView dayTxtV, weekTxtV;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food);

        //Set a ton of onClickListeners
        sbWeight = findViewById(R.id.sbWeight);
        sbVege = findViewById(R.id.sbVege);
        sbMeat = findViewById(R.id.sbMeat);
        btnCalculate = findViewById(R.id.calculatePlateBtn);

        //textviews
        dayTxtV = findViewById(R.id.dayBreakdownTxt);
        weekTxtV = findViewById(R.id.weekBreakdownTxt);

        //initialize textfields
        setText();

        //Give singleton context, so it can access sharedPreferences
        CaloriesCalculator.getInstance().setContext(this);

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

        btnCalculate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                double finalWeight = (1000 * (weight/100) + 50);
                double vgPercentage = (vegetablePercentage / 100);
                double mtcPercentage = (meatPercentage / 100);
                setText();

                /**
                 * This is still in debugging mode
                 */
                CaloriesCalculator.getInstance().setNewPlate(finalWeight, vgPercentage, mtcPercentage);
                Toast.makeText(FoodActivity.this,
                        CaloriesCalculator.getInstance().calculatePlate(), Toast.LENGTH_LONG).show();
            }
        });
    }

    /**
     * Update statistics in FoodActivity
     */
    private void setText(){

        DecimalFormat df = new DecimalFormat("#.#");
        if(CaloriesCalculator.getInstance().getDaysResults() != null){
            double[] dayStats = CaloriesCalculator.getInstance().getDaysResults();
            dayTxtV.setText(("Calories || Carbs ||Protein ||Fats \n" + df.format(dayStats[0]) +
                    df.format(dayStats[1]) + df.format(dayStats[2]) + df.format(dayStats[3])));
        }
        ///*
        if(CaloriesCalculator.getInstance().getWeeksAverageResults() != null){
            double[] weekStats = CaloriesCalculator.getInstance().getWeeksAverageResults();
            weekTxtV.setText(("Calories || Carbs ||Protein ||Fats \n" + df.format(weekStats[0]) +
                    df.format(weekStats[1]) + df.format(weekStats[2]) + df.format(weekStats[3])));
        } //

    }
}
