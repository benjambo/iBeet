/**************************************************************************************************
 *
 *                  FoodActivity
 *
 *  UI
 *          The UI is two-staged. You start with statistic view and when you add plate,
 *          input view replaces statistics.
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
import androidx.constraintlayout.widget.ConstraintLayout;


import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;

import org.florescu.android.rangeseekbar.RangeSeekBar;


public class FoodActivity extends AppCompatActivity {


    //Statistics field widgets
    private TextView caloriesDayText;
    private TextView caloriesWeekText;
    private TextView caloriesReco;
    private TextView dietReco;

    private Button newPlate;

    //input field widgets
    private RangeSeekBar<Double> rangeSeekBar;
    private SeekBar plateSize;

    private ConstraintLayout statisticsLay;
    private ConstraintLayout inputLay;

    private Button cancelButton;
    private Button inputButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food);

        caloriesDayText = findViewById(R.id.caloriesNowText);
        caloriesWeekText = findViewById(R.id.caloriesWeekText);
        caloriesReco = findViewById(R.id.calorieReco);
        dietReco = findViewById(R.id.dietReco);

        newPlate = findViewById(R.id.newPlate);

        statisticsLay = findViewById(R.id.statisticLayout);
        inputLay = findViewById(R.id.inputPlateLayout);

        rangeSeekBar = new RangeSeekBar<Double>(this);
        rangeSeekBar.setRangeValues(0.0, 100.0);
        rangeSeekBar.setSelectedMaxValue(99.0);
        rangeSeekBar.setSelectedMinValue(1.0);

        plateSize = findViewById(R.id.sizeBar);
    }

    private void toggleLayots(){
        if(statisticsLay.getVisibility()==View.VISIBLE){
            statisticsLay.setVisibility(View.GONE);
            inputLay.setVisibility(View.VISIBLE);
        }else{
            inputLay.setVisibility(View.GONE);
            statisticsLay.setVisibility(View.VISIBLE);
        }
    }
}
