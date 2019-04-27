package com.example.ibeet;

public class CalorieNeed {

    private int age;
    private int mCalSedentary,mCalModerate, mcCalActive;
    private int fCalSedentary, fCalModerate, fCalActive;

    public CalorieNeed(int age, int mCalSedentary, int mCalModerate, int mCalActive,
                        int fCalSedentary, int fCalModerate, int fCalActive) {
        this.age = age;
        this.mCalSedentary = mCalSedentary;
        this.mCalModerate = mCalModerate;
        this.mcCalActive = mCalActive;
        this.fCalSedentary = fCalSedentary;
        this.fCalModerate = fCalModerate;
        this.fCalActive = fCalActive;
    }

    public int getCaloriesAmount(boolean isMale, int modeHowActive){
        if(isMale){
            switch (modeHowActive){
                case 1: return mCalSedentary;
                case 2: return mCalModerate;
                case 3: return mcCalActive;
                default: return mCalModerate;
            }
        } else{
            switch (modeHowActive){
                case 1: return fCalSedentary;
                case 2: return fCalModerate;
                case 3: return  fCalActive;
                default: return fCalModerate;
            }
        }
    }
}
