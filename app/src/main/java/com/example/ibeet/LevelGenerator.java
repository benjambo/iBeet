package com.example.ibeet;

import android.content.SharedPreferences;
import android.util.Log;

public class LevelGenerator {
    int currentLevel;
    int totalXp;
    int currentXp;
    int neededXp;
    MarkerHandler markerHandler = new MarkerHandler();

    public LevelGenerator(){
        neededXp = 150;
    }

    public void calculateNewLevel() {
        while (currentXp > neededXp){
            isEnoughXp();
            getXpNeeded();
            resetCurrentXp();
        }
    }

    public void isEnoughXp(){
        if (currentXp>0){
            currentLevel++;
        } else{}
    }

    public void getXpNeeded(){
        neededXp += 150;
        Log.d("XpNeeded", String.valueOf(neededXp));
    }

    public void resetCurrentXp(){
        currentXp = currentXp - neededXp;
        Log.d("currentXp", String.valueOf(currentXp));
    }

    public void setTotalXp(int totalDist){
        this.totalXp = totalDist;
        Log.d("totalXp", String.valueOf(totalXp));
        currentXp = totalXp;
    }

    public int getCurrentLevel(){
        return currentLevel;
    }
}//VERSION 1.0
