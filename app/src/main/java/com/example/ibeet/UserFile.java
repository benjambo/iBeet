/**
 *                              UserFile
 *
 * This implementation is not the best.
 *
 * I would now, after immense studying, create this system of User, UserFile, FileHandler
 * in a very different way, but now there's not enough time to rewrite.
 * - panu
 *
 * This class keeps a list of users and a list of nutritional statistics. Both accessable with a
 * username : String.
 */

package com.example.ibeet;

import android.util.Log;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;

public class UserFile implements Serializable {

    //THESE TWO FEATURES ARE SEPARATED TO AVOID OVERLAP. NOT ENOUGH TIME TO OPTIMIZE
    private HashMap<String, User> userList;
    private HashMap<String, ArrayList<double[]>> nutMap;

    public UserFile(){
        nutMap = new HashMap<>();
        userList = new HashMap<>();
    }

    /**
     * set nutritional collection of a user
     * @param nutCollection : ArrayList<double[]>
     * @param username : String
     */
    public void setNutCollection(ArrayList<double[]> nutCollection, String username){
        if(nutMap.containsKey(username)){
            nutMap.remove(username);
            nutMap.put(username, nutCollection);
        } else {
            nutMap.put(username, nutCollection);
        }
    }

    /**
     * get nutritionalCollection of an user
     * @param username : String
     * @return nutCollection : ArrayList<double>
     */
    public ArrayList<double[]> getNutCollection(String username){
        if(nutMap.containsKey(username)) {
            return nutMap.get(username);
        } else {
            Log.d("CCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCC",
                    "getNutCollection: NO KEY: " + username + " FOUND!!");
            return null;
        }
    }

    /**
     * Add a new user to userfile
     * @param user : User
     */
    public void setNewUser(User user){
        if(userList.containsKey(user.getUserName())){
            userList.remove(user.getUserName());
        }

        userList.put(user.getUserName(), user);

        ArrayList<double[]> list = new ArrayList<>();
        for(int i=0;i<7;i++){
            list.add(new double[]{0, 0, 0, 0});
        }
        nutMap.put(user.getUserName(), list);
    }

    /**
     * get user from file
     * @param username : String
     * @return user : User
     */
    public User getUser(String username){
        return userList.get(username);
    }
}
