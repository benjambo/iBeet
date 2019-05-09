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

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;


import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import org.florescu.android.rangeseekbar.RangeSeekBar;


public class FoodActivity extends AppCompatActivity {

    private double weightValue = 0;
    private double plateFirstDivision = 0;
    private double plateSecondDivision = 0;

    //Statistics field widgets
    private TextView caloriesDayText;
    private TextView caloriesWeekText;
    private TextView calCompDayText;
    private TextView calCompWeekText;
    private TextView dietReco;

    private long backPressedTime;
    private Toast backToast;

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
        CaloriesCalculator.getInstance().initializeCalc(this);

        BottomNavigationView bottomNav = findViewById(R.id.bottomNavigationView);
        bottomNav.setOnNavigationItemSelectedListener(navListener);

        Menu menu = bottomNav.getMenu();
        MenuItem menuItem = menu.getItem(0);
        menuItem.setChecked(true);

        //Init statistics Widgets
        caloriesDayText = findViewById(R.id.caloriesNowText);
        caloriesWeekText = findViewById(R.id.caloriesWeekText);
        calCompDayText = findViewById(R.id.caloriesDayComp);
        calCompWeekText = findViewById(R.id.caloriesWeekComp);
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
        FileHandler.getInstance().writeUserFile(this);
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
                    CaloriesCalculator.getInstance().calculatePlate(FoodActivity.this);
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
        calCompDayText.setText(viewStringBuild(0));
        calCompWeekText.setText(viewStringBuild(1));

        //set recommendations
        dietReco.setText(viewStringBuild(2));
    }

    /**
     * A funktion to determine and compile a textfield message String.
     * Separated from updateViews() for easier audition
     * @param i : int
     * @return message : String
     */
    private String viewStringBuild(int i) {
        int foo; //foo is Recommended calories - hadCalories
        int bar; //bar is recommended calories

        //build day comparison
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
                Log.d("", "viewStringBuild: viewStringBuild OR " +
                        "CaloriesCalculator.--.get% %Comparison BROKE");
                return "";
            }

            //build week comparison
        } else if(i == 1){

            foo = (int)CaloriesCalculator.getInstance().getWeeksComparison();
            bar = CaloriesCalculator.getInstance().getCalorieNeed();

            String fooBar = "\n" + (bar - foo) + "/" + bar;

            if(TimeCalculator.getInstance().getTimeDiffInDays() < 7){
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
                Log.d("", "viewStringBuild: viewStringBuild OR " +
                        "CaloriesCalculator.--.getWeeksAverageResults BROKE");
                return "";
            }

            //build dietary recommendation
        } else if(i == 2){

            double[] today = CaloriesCalculator.getInstance().getDaysResults();
            double percentCarb = today[1] / (today[1] + today[2] + today[3]);
            double percentProt = today[2] / (today[1] + today[2] + today[3]);

            StringBuilder buildReco = new StringBuilder();

            if(today[0] == 0){
                return getResources().getString(R.string.dietreco_null);
            } else if(0.4 <= percentCarb && 0.6 >= percentCarb &&
                        0.2 <= percentProt && 0.3 >= percentCarb){
                return  getResources().getString(R.string.dietreco_neutral);
            } else {

                if(percentCarb>0.6){
                    buildReco.append(getResources().getString(R.string.dietreco_highcarb));
                } else if(percentProt > 0.3){
                    buildReco.append(getResources().getString(R.string.dietreco_highprot));
                } else {
                    buildReco.append(getResources().getString(R.string.dietreco_highfat));
                }

                if(percentCarb < 0.4){
                    buildReco.append(getResources().getString(R.string.dietreco_lowcarb));
                } else if(percentProt < 0.2){
                    buildReco.append(getResources().getString(R.string.dietreco_lowprot));
                } else {
                    buildReco.append(getResources().getString(R.string.dietreco_lowfat));
                }
                return buildReco.toString();
            }
        }
        return "";
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

    private BottomNavigationView.OnNavigationItemSelectedListener navListener =
            new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                    switch (menuItem.getItemId()) {

                        case R.id.nav_food:
                            break;

                        case R.id.nav_profile:
                            Intent profileActivity = new Intent(FoodActivity.this, ProfileActivity.class);
                            startActivity(profileActivity);
                            break;

                        case R.id.nav_map:
                            Intent mapsActivity = new Intent(FoodActivity.this, TrackerActivity.class);
                            startActivity(mapsActivity);
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
            backToast = Toast.makeText(FoodActivity.this, "Press back again to exit", Toast.LENGTH_SHORT);
            backToast.show();
        }
        backPressedTime = System.currentTimeMillis();
    }
}
