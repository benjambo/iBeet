package com.example.ibeet;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

public class UserFile implements Serializable {

    private ArrayList<double[]> foo;
    private HashMap<String, ArrayList<double[]>> userLists;
    private String user;

    public UserFile(String user){
        this.user = user;
        userLists = new HashMap<>();
    }

    public void initNewUser(String newUser){
        ArrayList<double[]> emptyList = new ArrayList<>();
        for(int i=0;i<7;i++){
            emptyList.add(new double[]{0, 0, 0, 0});
        }
        userLists.put(newUser, emptyList);
    }

    public void setNutList(ArrayList<double[]> bar){
        foo = bar;
        if(userLists.containsKey(user)){
            userLists.remove(user);
            userLists.put(user, bar);
        } else {
            userLists.put(user, bar);
        }
    }

    public ArrayList<double[]> getNutList(){
        return userLists.get(user);
    }
}
