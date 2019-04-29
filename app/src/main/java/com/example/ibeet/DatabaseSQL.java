/**
 * Database help from http://coderzpassion.com/android-simple-login-example-using-sqlite/
 */
package com.example.ibeet;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


public class DatabaseSQL extends SQLiteOpenHelper {
    //Database Version
    private static final int Db_Version=1;

    //Database Name
    private static final String Db_Name="users";

    //Table name
    private static final String Table_Name="user";

    //Creating contacts Columns
    private static final String User_id="id";
    private static final String User_name="name";
    private static final String User_password="password";

    public DatabaseSQL(Context context)
    {
        super(context,Db_Name,null,Db_Version);
    }

    //Creating table
    @Override
    public void onCreate(SQLiteDatabase db) {
        // SQLite command to create table with required columns
        String Create_Table="CREATE TABLE " + Table_Name + "(" + User_id
                + " INTEGER PRIMARY KEY," + User_name + " TEXT," + User_password + " TEXT" + ")";
        db.execSQL(Create_Table);
    }

    //Upgrading the Database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        //Drop table if exists
        db.execSQL("DROP TABLE IF EXISTS " + Table_Name);
        //create the table again
        onCreate(db);
    }

    //Add new User by calling this method
    public void addUser(User usr)
    {
        //Getting db instance for writing the user
        SQLiteDatabase db=this.getWritableDatabase();
        ContentValues cv=new ContentValues();

        // cv.put(User_id,usr.getId());
        cv.put(User_name,usr.getName());
        cv.put(User_password,usr.getPassword());

        //Inserting row
        db.insert(Table_Name, null, cv);

        //Close the database to avoid any leak
        db.close();
    }
    public int checkUser(User us)
    {
        int id=-1;
        SQLiteDatabase db=this.getReadableDatabase();
        Cursor cursor=db.rawQuery("SELECT id FROM user WHERE name=? AND password=?",new String[]{us.getName(),us.getPassword()});
        if(cursor.getCount()>0) {
            cursor.moveToFirst();
            id=cursor.getInt(0);
            cursor.close();
        }
        return id;
    }
}
