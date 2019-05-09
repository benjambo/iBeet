/**
 *                              Filehandler
 *
 *  FileHandler singleton is a tool for reading and writing User info and statistic to decice memory
 *  using java.io
 *
 *  two methods:
 *      - readUserFile
 *          makes sure that UserFile is returned.
 *
 *      - writeUserFile
 *          writes what exists in current instance of UserFile myFile
 */

package com.example.ibeet;

import android.content.Context;
import android.util.Log;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
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
        myFile = null;
    }

    /**
     * Read userfile.
     *  if read = get
     *  if !read = readFromMemory
     *  if !exists = create
     * @param context : Context
     * @return
     */
    public UserFile readUserFile(Context context){
        if(myFile != null){
            return myFile;
        } else {
            try{
                FileInputStream fileIn = context.openFileInput("userFile.txt");
                ObjectInputStream in = new ObjectInputStream(fileIn);
                myFile = (UserFile)in.readObject();
                fileIn.close();
                in.close();
                return myFile;

            } catch (IOException | ClassNotFoundException a){
                Log.d("ELÄIN",
                        "readUserFile: FILE DOESN'T EXIST");
                myFile = new UserFile();
                return myFile;
            }
        }
    }

    /**
     * Upon terminjation, write file into memory
     * @param context : Context
     */
    public void writeUserFile(Context context){
        if(myFile == null){
            Log.d("ELÄIN",
                    "writeUserFile: myFile or nutColl is null");
        }

        try{
            FileOutputStream fileOut = context.openFileOutput("userFile.txt", Context.MODE_PRIVATE);
            ObjectOutputStream out = new ObjectOutputStream(fileOut);
            out.writeObject(myFile);
            out.close();
            fileOut.close();

        } catch(IOException a){
            Log.d("ELÄIN", "writeUserFile: WRITING FAILED");
        }
    }
}
