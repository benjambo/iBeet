package com.example.ibeet;


import androidx.annotation.NonNull;

import java.io.Serializable;

public class User implements Serializable {
    int id;
    String userName;
    String password;
    String name;
    int age;
    boolean isMale;

    //Needs two constructors to function right!
    //Constructor containing two needed parameters
    public User(String userName, String password)
    {
        this.userName = userName;
        this.password=password;
    }
    //Constructor containing 3 three parameters
    public User(int id, String usr, String psd)
    {
        this.id=id;
        this.userName = usr;
        this.password=psd;
    }

    //Constructor for whole package
    public User(String userName, String password, String name, String age, boolean isMale){
        this.userName = userName;
        this.password = password;
        this.name = name;
        this.age = Integer.parseInt(age);
        this.isMale = isMale;
    }

    @NonNull
    @Override
    public String toString() {
        return name;
    }

    //NAME
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    //AGE
    public int getAge() { return age; }
    public void setAge(int age) { this.age = age; }

    //GENDER
    public boolean isMale() { return isMale; }
    public void setMale(boolean male) { isMale = male; }

    //ID
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    //USERNAME
    public String getUserName() { return userName; }
    public void setUserName(String userName) { this.userName = userName; }

    //PASSWORD
    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
}
