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
        neededXp = 200;
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
        neededXp += currentLevel*200;
    }

    public void resetCurrentXp(){
        currentXp = currentXp - neededXp;
    }

    public void setTotalXp(int totalDist){
        this.totalXp = totalDist;
        currentXp = totalXp;
    }

    public int getCurrentLevel(){
        return currentLevel;
    }
}//VERSION 1.0
