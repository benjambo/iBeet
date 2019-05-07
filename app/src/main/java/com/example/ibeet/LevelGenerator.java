package com.example.ibeet;

import android.content.SharedPreferences;

public class LevelGenerator {
    int currentLevel;
    int totalXp;
    int currentXp;
    int neededXp;
    MarkerHandler markerHandler = new MarkerHandler();

    public LevelGenerator(){
        totalXp = 0;
        currentLevel = 0;
        neededXp = 200;
        currentXp = totalXp - neededXp;
    }

    public void calculateNewLevel(){
        while (currentXp>neededXp){
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
        currentXp = totalXp - neededXp;
    }

    public void setTotalXp(int totalDist){
        this.totalXp = totalDist;
    }

    public int getCurrentLevel(){
        return currentLevel;
    }
}//VERSION 1.0
