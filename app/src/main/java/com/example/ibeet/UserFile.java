package com.example.ibeet;

import android.util.Log;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;

public class UserFile implements Serializable {

    private HashMap<String, ArrayList<double[]>> userNutritionList;
    private HashMap<String, User> userList;
    private User user;

    /**
     * this constructor is (atleast is meant to) run only the first time the application is launched
     */
    public UserFile(){
        userNutritionList = new HashMap<>();
        userList = new HashMap<>();
    }

    /**
     * Add a new user into the filesystem
     * @param user : User
     */
    public void addUser(User user){
        this.user = user;

        if(userList == null){
            userList = new HashMap<>();
            userNutritionList = new HashMap<>();
        }

        userList.put(user.getUserName(), user);
        initNewUserNutrition(user.getUserName());
    }

    /**
     * New users get their nutritional statistic lists that we initialize here
     * @param newUser : String
     */
    private void initNewUserNutrition(String newUser){
        ArrayList<double[]> emptyList = new ArrayList<>();
        for(int i=0;i<7;i++){
            emptyList.add(new double[]{0, 0, 0, 0});
        }
        userNutritionList.put(newUser, emptyList);
    }

    /**
     * After succesfull login, set that user as active user
     * @param username : String
     */
    public void enableUser(String username){
        if(userList.containsKey(username)){
            this.user = getUser(username);
        } else {
            Log.d("LLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLL"
                    , "enableUser: REQUESTING NON-EXISTANT USER");
        }
    }

    /**
     * Simply get access to a user, using username as identificator
     * @param username : String
     * @return user : User
     */
    public User getUser(String username){
        return userList.get(username);
    }

    public User getUser(){
        return userList.get(this.user.getUserName());
    }

    /**
     * Update latest nutritinal list to UserFile
     * @param nutList : ArrayList<double[]>
     */
    public void setUserNutritionList(ArrayList<double[]> nutList){
        userNutritionList.put(user.getUserName(), nutList);
    }

    public ArrayList<double[]> getUserNutrition(String username){
        return userNutritionList.get(username);
    }
}
