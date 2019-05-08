package com.example.ibeet;

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

    //EVERY NEW LEVEL REQUIRES 150M MORE DISTANCE TRAVELLED
    public void getXpNeeded(){
        neededXp += 150;
    }
    //GET NEW CURRENT XP
    public void resetCurrentXp(){
        currentXp = currentXp - neededXp;
    }
    //SET TOTAL XP
    public void setTotalXp(int totalDist){
        this.totalXp = totalDist;
        currentXp = totalXp;
    }
    //RETURN CURRENT LEVEL
    public int getCurrentLevel(){
        return currentLevel;
    }
}//VERSION 1.0
