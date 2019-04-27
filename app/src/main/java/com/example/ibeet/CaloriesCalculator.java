package com.example.ibeet;

import java.util.ArrayList;

class CaloriesCalculator {
    private static final CaloriesCalculator ourInstance = new CaloriesCalculator();

    private ArrayList<CalorieNeed> calorieNeeds;
    private double plateWeight, vegetablePercentage, meatToCarbsPercentage;
    private double calories, carbGrams, fatGrams, proteinGrams;

    static CaloriesCalculator getInstance() {
        return ourInstance;
    }

    private CaloriesCalculator() {
        FoodListGenerator foodListGenerator = new FoodListGenerator();
        calorieNeeds = foodListGenerator.getList();

    }

    /**
     * Purge the old and set new plate values
     * @param newWeight plate weight
     * @param vegetablePercentage percentage of vegatble to the plate
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

        /**
         * This is still in debugging mode
         */
        return "Calories: " + calories + " || carbs: " + carbGrams + " || protein: " + proteinGrams +
                " || fat: " + fatGrams;
    }


}

/***************************************************************************************************
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
 *                      Calculator
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