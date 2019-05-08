package com.example.ibeet;

import android.util.Log;

public class LevelGenerator {
    int currentLevel;
    int totalXp;
    int currentXp;
    int neededXp;

    public LevelGenerator(){
        neededXp = 150;
    }

    public void calculateNewLevel() {
        //CHECK IF CURRENT XP IS ENOUGH TO LEVEL UP, RESET XP NEEDED FOR NEW LEVEL, TAKE NEEDED XP FROM CURRENT XP AND RETURN NEW CURRENT XP
        while (currentXp > neededXp){
            currentLevel++;
            getXpNeeded();
            resetCurrentXp();
        }
    }
    //CHECK IF THERE'S ENOUGH XP TO LEVEL UP
    public void isEnoughXp(){
            currentLevel++;
    }
    //EVERY NEW LEVEL REQUIRES 150M MORE DISTANCE TRAVELLED
    public void getXpNeeded(){
        neededXp += 150;
        Log.d("XpNeeded", String.valueOf(neededXp));
    }
    //GET NEW CURRENT XP
    public void resetCurrentXp(){
        currentXp = currentXp - neededXp;
        Log.d("currentXp", String.valueOf(currentXp));
    }
    //SET TOTAL XP
    public void setTotalXp(int totalDist){
        this.totalXp = totalDist;
        Log.d("totalXp", String.valueOf(totalXp));
        currentXp = totalXp;
    }
    //RETURN CURRENT LEVEL
    public int getCurrentLevel(){
        return currentLevel;
    }
}//VERSION 1.0
