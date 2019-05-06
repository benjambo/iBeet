package com.example.ibeet;

public class User {
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

    public String getName() { return name; }

    public void setName(String name) { this.name = name; }

    public int getAge() { return age; }

    public void setAge(int age) { this.age = age; }

    public boolean isMale() { return isMale; }

    public void setMale(boolean male) { isMale = male; }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
