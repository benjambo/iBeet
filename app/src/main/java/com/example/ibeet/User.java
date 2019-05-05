package com.example.ibeet;

public class User {
    int id;
    String name;
    String password;

    //Needs two constructors to function right!
    //Constructor containing two needed parameters
    public User(String name, String password)
    {
        this.name = name;
        this.password=password;
    }
    //Constructor containing all three parameters
    public User(int id, String usr, String psd)
    {
        this.id=id;
        this.name = usr;
        this.password=psd;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
