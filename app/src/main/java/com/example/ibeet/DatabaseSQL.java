/**
 * Database help from http://coderzpassion.com/android-simple-login-example-using-sqlite/
 */
package com.example.ibeet;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;


public class DatabaseSQL extends SQLiteOpenHelper {
    //Database Version
    private static final int Db_Version=1;

    //Database Name
    private static final String Db_Name="users";

    //Table name
    private static final String Table_Name="user";

    private static final String stats_table ="Food_stats_table";

    //Creating contacts Columns
    private static final String User_id="id";
    private static final String User_name="name";
    private static final String User_password="password";

    //Store all nutritional data from current day
    //Store calories from last seven days
    private static final String Col_1 = "prime_key_name";
    private static final String Col_2 ="first";
    private static final String Col_3 ="second";
    private static final String Col_4 ="third";
    private static final String Col_5 ="fourth";
    private static final String Col_6 ="calsDayOne";
    private static final String Col_7 ="calsDayTwo";
    private static final String Col_8 ="calsDayThree";
    private static final String Col_9 ="calsDayFour";
    private static final String Col_10 ="calsDayfive";
    private static final String Col_11 ="clasDaySix";
    private static final String Col_12 ="calDaySeven";

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

        //db.execSQL("DROP TABLE IF EXISTS " + stats_table);
        db.execSQL("CREATE TABLE " + stats_table + "( " +
                Col_1 + " TEXT PRIMARY KEY, " +
                Col_2 + " DOUBLE, " +
                Col_3 + " DOUBLE, " +
                Col_4 + " DOUBLE, " +
                Col_5 + " DOUBLE, " +
                Col_6 + " DOUBLE, " +
                Col_7 + " DOUBLE, " +
                Col_8 + " DOUBLE, " +
                Col_9 + " DOUBLE, " +
                Col_10 + " DOUBLE, " +
                Col_11 + " DOUBLE, " +
                Col_12 + " DOUBLE " +
                " );"
        );
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

    /**
     * Update foodstats to SQL
     * Firstdays (calories, carbs, protein, fats )
     * + weeks calories (1st, 2nd, 3rd, 4th, 5th, 6th) day
     * @param user : String
     * @param weeksResult : Arraylist<double> we will only every list member's first value
     * @param daysResult : double[]
     */
    public void setFoodStatsTable(String user, ArrayList<double[]> weeksResult, double[] daysResult){
        SQLiteDatabase db = getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(Col_1, user);
        //days values
        cv.put(Col_2, daysResult[0]);
        cv.put(Col_3, daysResult[1]);
        cv.put(Col_4, daysResult[2]);
        cv.put(Col_5, daysResult[3]);

        //weeks values
        cv.put(Col_6, weeksResult.get(0)[0]);
        cv.put(Col_7, weeksResult.get(1)[0]);
        cv.put(Col_8, weeksResult.get(2)[0]);
        cv.put(Col_9, weeksResult.get(3)[0]);
        cv.put(Col_10, weeksResult.get(4)[0]);
        cv.put(Col_11, weeksResult.get(5)[0]);
        cv.put(Col_12, weeksResult.get(6)[0]);

        long resultant = db.update(stats_table, cv, "prime_key_name=?", new String[]{user});
        Log.d("AAAAAAAAAAAAAAAAAAAAAAAAAAAAAA", "setFoodStatsTable: " + resultant);
        Log.d("CCCCCCCCCCCCCCCCCCCCCC", "setFoodStatsTable: " +
                weeksResult.get(0)[0] + " " );
        /*
        Cursor cursor = null;
         cursor = db.rawQuery(
                 "SELECT id FROM " + stats_table +
                         " WHERE id = " +
                         user, null
         );
         if(cursor == null){
             cursor.
         }
         cursor.close();
         */
    }

    /**
     * Fetch stats to put up in the app
     * @param user : String
     * @return compilation : double[] (see the composition from method above)
     */
    public double[] getFoodStatsTable(String user){
        double[] stats = {0, 0, 0, 0, 0 ,0 ,0 ,0, 0 ,0 ,0};
        SQLiteDatabase db = getReadableDatabase();

        /*
        Cursor cursor = db.rawQuery("SELECT * FROM " + stats_table + " WHERE Col_1 = " + user, null);
        cursor.moveToFirst();
        for(int i=1;i<12;i++){
            stats[i-1] = cursor.getDouble(i);
        }
        cursor.close();
        */

        Cursor cursor = db.query(stats_table,
                null,
                null,
                null,
                null,
                null,
                null,
                null
                );

        /*
        Cursor cursor = db.rawQuery("SELECT * FROM " + stats_table
                + " WHERE " + Col_1 + " = " + user, null);
        */

        if(cursor != null){
            int i = 0;
            while (cursor.moveToNext()){
                if(i==0){

                }else {
                    stats[i-1] = cursor.getDouble(i);
                }
                i++;
            }
        }
        cursor.close();
        Log.d("AAAAAAAAAAAAAAAAAAAAAAAAAAAA", "aaaaaaaa" +
                stats[0] + "  " +
                stats[1] + "  " +
                stats[2] + "  " +
                stats[3] + "  " +
                stats[4] + "  " +
                stats[5] + "  " +
                stats[6] + "  " +
                stats[7] + "  " +
                stats[8] + "  " +
                stats[9] + "  "
        );
        return stats;
    }
}
