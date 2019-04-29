/***************************************************************************************************
 *
 *                      CaloriesCalculator
 *
 *  This calculator is based on dietary programs from verywellfit.com and dietary guidelines by age
 *  from heath.gov. The calculator also applies knowledge of food properties from juxtable.com,
 *  fatsecret.com. Info on avrage meal sizes from apjcn.org.tw. Fast foods nutrition cheat sheet
 *  from nutritionsheet.com
 *
 *  https://www.verywellfit.com/daily-diet-composition-calculator-charts-carbs-protein-fat-3861072
 *  https://health.gov/dietaryguidelines/2015/guidelines/appendix-2/
 *
 *  http://juxtable.com/meat-protein-comparison/
 *  https://www.fatsecret.com/calories-nutrition/food/salad
 *
 *  http://apjcn.nhri.org.tw/server/info/books-phds/books/foodfacts/html/data/data1b.html
 *  http://www.nutritionsheet.com/facts/restaurants
 *
 ***************************************************************************************************/
/**************************************************************************************************'
 *
 *                          dietaryPrograms
 *
 *  There are three different diets:
 *                      carb/protein/fat
 *  - High protein diet (40% 30% 30%)
 *  - Vegetarian diet   (55% 15% 30%)
 *  - Balanced diet     (50% 20% 30%)
 *
 **************************************************************************************************/
/***************************************************************************************************
 *
 *                ArrayList<> calorieNeeds
 *
 * calorieNeeds accounts for person's age, gender and amount of daily activity. It has stored
 * accurate recommended intakes from Health.gov
 *
 *
 **************************************************************************************************/
/***************************************************************************************************
 *
 *                      CaloriesCalculator
 *
 *  Input from user is meal in amounts (grams and division of plate, or preset)
 *  Input from preferences (Age, activity, preferred diet)
 *
 *  1. Turn input food into protein, fat and carbs // also get total calories
 *  2. Compare calorie amount to recommended amount (calorieNeeds)
 *  3. Compare food breakdown to dietary program
 *  4. Output feedback to user
 *
 *  Calculator will store results for last 7 days for daily and weekly reviews
 *
 **************************************************************************************************/

package com.example.ibeet;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Date;
import java.util.TimeZone;

class CaloriesCalculator {
    private static final CaloriesCalculator ourInstance = new CaloriesCalculator();

    private ArrayList<CalorieNeed> calorieNeeds;
    private ArrayList<double[]> nutritionalCollection;

    private double plateWeight, vegetablePercentage, meatToCarbsPercentage;
    private double calories, carbGrams, fatGrams, proteinGrams;
    private int currentDay = 0;


    static CaloriesCalculator getInstance() {
        return ourInstance;
    }

    private CaloriesCalculator() {
        FoodListGenerator foodListGenerator = new FoodListGenerator();
        calorieNeeds = foodListGenerator.getList();
        nutritionalCollection = new ArrayList<>();
        nutritionalCollection.add(new double[]{0,0,0,0});
    }

    /**
     * Purge the old and set new plate values
     * @param newWeight plate weight
     * @param vegetablePercentage percentage of vegetable to the plate
     * @param meatPercentage percentage of meat from rest of plate
     */
    public void setNewPlate(double newWeight, double vegetablePercentage, double meatPercentage) {
        calories = carbGrams = fatGrams = proteinGrams = 0;
        this.plateWeight = newWeight;
        this.vegetablePercentage = vegetablePercentage;
        this.meatToCarbsPercentage = meatPercentage;
    }

    /**
     * Calculate Calories and carbs, protein and fat in grams.
      * @return String
     */
    public String calculatePlate() {
        double vegeWeight = plateWeight * vegetablePercentage;
        double meatWeight = (plateWeight - vegeWeight) * meatToCarbsPercentage;
        double carbWeight = (plateWeight - vegeWeight) - meatWeight;

        calories = ((0.2 * vegeWeight) + (4 * meatWeight) + (4 * carbWeight));
        carbGrams = ((0.2 * 0.6 * vegeWeight) + (0.3 * carbWeight));
        proteinGrams = ((0.2 * 0.3 * vegeWeight) + (0.25 * 0.9 * meatWeight));
        fatGrams = ((0.25 * 0.1 * meatWeight) + (0.05 * carbWeight));

        update(calories, carbGrams, proteinGrams, fatGrams);
        /**
         * This is still in debugging mode
         */
        return "Calories: " + calories + " || carbs: " + carbGrams + " || protein: " + proteinGrams +
                " || fat: " + fatGrams;
    }


    /**
     * Update a nutritional breakdown into app's storage. The method stacks and indexes data by day.
     * @param calories : double
     * @param carbGrams : double
     * @param proteinGrams : double
     * @param fatGrams : double
     */
    public void update(double calories, double carbGrams, double proteinGrams, double fatGrams){

        double[] newCollection = {calories, carbGrams, proteinGrams, fatGrams};

        int rotationIndex = TimeCalculator.getInstance().getDayRotation();

        //make sure we stack current days info, not overwrite it
        //when week passes, previous weekdays get overwritten.
        if(rotationIndex == currentDay){
            for(int i=0; i<4; i++){
                double newValue = newCollection[i] +
                        nutritionalCollection.get(rotationIndex)[i];
                newCollection[i] = newValue;
            }
            nutritionalCollection.set(rotationIndex, newCollection);
        } else if(rotationIndex>currentDay){
            nutritionalCollection.set(rotationIndex, newCollection);
            currentDay = rotationIndex;
        } else {
            Log.d("CALORIES_CALCULATOR.UPDATE", "ERRROR SETTING NUTRITIONAL_COLLECTION");
        }
    }

    /**
     *  Get days results: double calories, double carbGrams, double proteinGrams, double fatGrams
     * @return double[] nutritionalCollection(currentDay)
     */
    public double[] getDaysResults(){
        return nutritionalCollection.get(currentDay);
    }

    /**
     * Get average results of last seven days, including current one:
     *  double calories, double carbGrams, double proteinGrams, double fatGrams
     *  derp asd
     * @return double[] averageCollection
     */
    public double[] getWeeksAverageResults(){
        //Function cannot resolve average for last seven days if there haven't been seven days
        if(nutritionalCollection.size()!=7){
            return null;
        }
        double averageValue = 0;
        double[] averageCollection = new double[4];

        for(int i = 0; i<4; i++){
            for(int j = 0; j<7; j++){
                averageValue += nutritionalCollection.get(j)[i];
            }
            averageValue /= 7;
            averageCollection[i] = averageValue;
        }
        return averageCollection;
    }
}
