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


import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import org.florescu.android.rangeseekbar.RangeSeekBar;

import java.sql.Time;


public class FoodActivity extends AppCompatActivity {

    private double weightValue = 0;
    private double plateFirstDivision = 0;
    private double plateSecondDivision = 0;

    //Statistics field widgets
    private TextView caloriesDayText;
    private TextView caloriesWeekText;
    private TextView calCompDayText;
    private TextView calCompWeekText;
    private TextView caloriesReco;
    private TextView dietReco;

    private Button setNewPlate;

    //input field widgets
    private RangeSeekBar<Double> rangeSeekBar;
    private SeekBar plateSize;

    private Button cancelButton;
    private Button inputButton;

    private ConstraintLayout statisticsLay;
    private ConstraintLayout inputLay;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food);

        //setup Singletons
        TimeCalculator.getInstance().updateDate(this);
        CaloriesCalculator.getInstance().setupUser(this);

        //Init statistics Widgets
        caloriesDayText = findViewById(R.id.caloriesNowText);
        caloriesWeekText = findViewById(R.id.caloriesWeekText);
        calCompDayText = findViewById(R.id.caloriesDayComp);
        calCompWeekText = findViewById(R.id.caloriesWeekComp);
        caloriesReco = findViewById(R.id.reco);
        dietReco = findViewById(R.id.dietReco);

        //Init Layouts
        statisticsLay = findViewById(R.id.statisticLayout);
        inputLay = findViewById(R.id.inputPlateLayout);
        inputLay.setVisibility(View.GONE);

        //Init input Widgets
        rangeSeekBar = findViewById(R.id.rangeseekbar);//new RangeSeekBar<Double>(this);

        rangeSeekBar.setRangeValues(0.0, 100.0);
        rangeSeekBar.setSelectedMaxValue(99.0);
        rangeSeekBar.setSelectedMinValue(1.0);

        plateSize = findViewById(R.id.sizeBar);

        //Handle (Button) buttons
        setNewPlate = findViewById(R.id.newPlate);
        setNewPlate.setOnClickListener(myListener);

        cancelButton = findViewById(R.id.cancelButton);
        cancelButton.setOnClickListener(myListener);

        inputButton = findViewById(R.id.updateButton);
        inputButton.setOnClickListener(myListener);

        updateViews();

        //size slider
        plateSize.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            int barValue = 0;
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                barValue = progress;
            }
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                weightValue = (double)barValue * 10 + 40;

                Toast.makeText(FoodActivity.this, "plate weight: " + (int) weightValue
                        + " grams",
                        Toast.LENGTH_SHORT).show();
            }
        });

        //division slider
        rangeSeekBar.setOnRangeSeekBarChangeListener(new RangeSeekBar.OnRangeSeekBarChangeListener<Double>() {
            @Override
            public void onRangeSeekBarValuesChanged(RangeSeekBar<?> bar, Double minValue, Double maxValue) {
                   plateFirstDivision = minValue;
                   plateSecondDivision = maxValue;

                   Toast.makeText(FoodActivity.this, "Vegetables: " + (int)plateFirstDivision +
                           "%  Meat: " + (int)(plateSecondDivision-plateFirstDivision) +
                           "%  Carbs: " + (100 - (int)plateSecondDivision) + "%",
                            Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        CaloriesCalculator.getInstance().writeIntoDB(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        CaloriesCalculator.getInstance().writeIntoDB(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        updateViews();
    }

    //(Button) Listeners
    private View.OnClickListener myListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.newPlate:
                    toggleLayouts();
                    break;
                case R.id.cancelButton:
                    toggleLayouts();
                    break;
                case R.id.updateButton:
                    double vegePercent = plateFirstDivision / 100;
                    double meatPercent = (plateSecondDivision - plateFirstDivision) / 100;
                    CaloriesCalculator.getInstance().setNewPlate(weightValue, vegePercent, meatPercent);
                    //String foo = CaloriesCalculator.getInstance().calculatePlate();
                    toggleLayouts();
                    break;
            }
            updateViews();
        }
    };

    /**
     * This activity switches between two viewgroups
     */
    private void toggleLayouts(){
        if(statisticsLay.getVisibility()==View.VISIBLE){
            statisticsLay.setVisibility(View.GONE);
            inputLay.setVisibility(View.VISIBLE);
        }else{
            inputLay.setVisibility(View.GONE);
            statisticsLay.setVisibility(View.VISIBLE);
        }
        Toast.makeText(FoodActivity.this, "DEBUG: " +
                "dayrotation: "+ TimeCalculator.getInstance().getDayRotation() +
                " and compday" + TimeCalculator.getInstance().getComparativeDay(),
                Toast.LENGTH_LONG).show();
    }

    /**
     * Keep textfields up-to-date
     */
    private void updateViews(){
        //set the simple stats
        String calToday = String.valueOf(
                Math.floor(CaloriesCalculator.getInstance().getDaysResults()[0]));
        calToday = getResources().getString(R.string.cal_day) + "\n" + calToday;
        caloriesDayText.setText(calToday);

        String calThisWeek = String.valueOf(
                Math.floor(CaloriesCalculator.getInstance().getWeeksAverageResults()[0]));
        calThisWeek = getResources().getString(R.string.cal_week) + "\n" + calThisWeek;
        caloriesWeekText.setText(calThisWeek);

        //set comparative stats
        calCompDayText.setText(getCompString(0));
        calCompWeekText.setText(getCompString(1));
    }

    /**
     * A funktion to determine and compile a textfield message String.
     * Separated from updateViews() for easier audition
     * @param i : int
     * @return message : String
     */
    private String getCompString(int i) {
        int foo; //foo is Recommended calories - hadCalories
        int bar; //bar is recommended calories

        //toggle between dayComp and weekComp
        if (i == 0) {

            foo = (int)CaloriesCalculator.getInstance().getDaysComparison();
            bar = CaloriesCalculator.getInstance().getCalorieNeed();
            String fooBar = "\n" + (bar - foo) + "/" + bar;

            if (Math.abs(foo) <= 250) {
                return getResources().getString(R.string.cal_day_comp_neutral) + fooBar;
            } else if(isBetween(foo, 250, 1000)){
                return getResources().getString(R.string.cal_day_comp_low)+ fooBar;
            } else if(isBetween(foo, 1000, Integer.MAX_VALUE)){
                return getResources().getString(R.string.cal_day_comp_verylow)+ fooBar;
            } else if(isBetween(foo, -1000, -250)){
                return getResources().getString(R.string.cal_day_comp_high)+ fooBar;
            } else if(isBetween(foo, Integer.MIN_VALUE, -1000)){
                return getResources().getString(R.string.cal_day_comp_veryhigh)+ fooBar;
            } else {
                Log.d("", "getCompString: getCompString OR " +
                        "CaloriesCalculator.--.get% %Comparison BROKE");
                return "";
            }

        } else {

            foo = (int)CaloriesCalculator.getInstance().getWeeksComparison();
            bar = CaloriesCalculator.getInstance().getCalorieNeed();

            String fooBar = "\n" + (bar - foo) + "/" + bar;

            if(TimeCalculator.getInstance().getTimeDiffInDays() == 0){
                return getResources().getString(R.string.cal_week_comp_exception);
            } else if (Math.abs(foo) <= 400) {
                return getResources().getString(R.string.cal_week_comp_neutral) + fooBar;
            } else if(isBetween(foo, 500, 1500)){
                return getResources().getString(R.string.cal_week_comp_low) + fooBar;
            } else if(isBetween(foo, 1500, Integer.MAX_VALUE)){
                return getResources().getString(R.string.cal_week_comp_verylow) + fooBar;
            } else if(isBetween(foo, -1500, -500)){
                return getResources().getString(R.string.cal_week_comp_high) + fooBar;
            } else if(isBetween(foo, Integer.MIN_VALUE, -1500)){
                return getResources().getString(R.string.cal_week_comp_veryhigh) + fooBar;
            } else {
                Log.d("", "getCompString: getCompString OR " +
                        "CaloriesCalculator.--.getWeeksAverageResults BROKE");
                return "";
            }
        }
    }

    /**
     * Helper utility to calculate if ( lesser <= X <= larger ) == true.
     * @param x : int
     * @param lesser : int
     * @param larger : int
     * @return is : boolean
     */
    private boolean isBetween(int x, int lesser, int larger){
        if(lesser <= x && x <= larger){
            return true;
        } else {
            return false;
        }
    }
}
