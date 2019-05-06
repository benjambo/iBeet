/***************************************************************************************************
 *
 *                                      FileHandler
 *
 *  This is a singleton, that manages a devices internal storage (side-by-side with SQLite DB)
 *
 **************************************************************************************************/
package com.example.ibeet;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

class FileHandler {
    private static final FileHandler ourInstance = new FileHandler();

    static FileHandler getInstance() {
        return ourInstance;
    }

    private UserFile myFile;

    private FileHandler() {

    }

    /**
     * Gain access to UserFile
     * With this you can get and set values in the userfile.
     * @param context : Context
     * @return : UserFile
     */
    public UserFile readUserFile(Context context) {

        //If userfile is already declared & pointed
        if(myFile != null){
            return myFile;
        }

        try {
            FileInputStream fis = context.openFileInput("userFile.txt");
            ObjectInputStream ois = new ObjectInputStream(fis);
            myFile = (UserFile) ois.readObject();
            fis.close();
            ois.close();
            return  myFile;
        } catch (IOException | ClassNotFoundException a) {
            Log.d("LLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLL", "readUserFile: ERROR READING USERFILE");
            myFile = createFile(context);
        }
        return myFile;
    }

    /**
     * upon activity destruction save info into user file. This is not used for editing but saving
     * data in the device!
     * @param context : Context
     */
    public void writeFile(Context context){
        try{
            FileOutputStream fos = context.openFileOutput("userFile.txt", Context.MODE_PRIVATE);
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(this.myFile);
            fos.close();
            oos.close();
        } catch (IOException a){ }
    }

    //create storage file for a fresh application
    private UserFile createFile(Context context){
        myFile = new UserFile();
        writeFile(context);
        return myFile;
    }
}
