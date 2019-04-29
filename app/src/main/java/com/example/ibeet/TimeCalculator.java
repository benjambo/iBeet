/***************************************************************************************************
 *
 *                      TimeCalculator
 *
 *  Time calculation is critical
 *
 **************************************************************************************************/
package com.example.ibeet;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.Date;
import java.util.TimeZone;

class TimeCalculator {
    private static final TimeCalculator ourInstance = new TimeCalculator();

    private static String PREFS_DATES = "com.example.ibeet.DATES";

    static TimeCalculator getInstance() {
        return ourInstance;
    }

    private Context context;
    private SharedPreferences preferences_dates;
    private SharedPreferences.Editor editor_date;
    private Date date; private TimeZone tz;

    private TimeCalculator() {
    }

    public void updateDate(Context context){
        this.context = context;
        preferences_dates = context.getSharedPreferences(PREFS_DATES, Context.MODE_PRIVATE);
        editor_date = preferences_dates.edit();
        date = new Date(); tz = TimeZone.getDefault();

        //if FIRST_DATE is set, if not set it.
        //else set CURRENT_DATE.
        if(preferences_dates.contains("FIRST_DATE")){
            editor_date.putLong("CURRENT_DATE", (date.getTime() /*+ tz.getOffset(date.getTime())*/));

        } else {
            long hoursInMilli = 1000 * 60 * 60;
            long daysInMilli = hoursInMilli * 24;

            //our first date begins the last time clock was 00:00 in devices current timezone,
            //so we have to find out the difference of current time and last 00:--.
            long difference = ((date.getTime() + tz.getOffset(date.getTime())) % daysInMilli);
            long firstDate = (date.getTime() - difference);
            editor_date.putLong("FIRST_DATE", firstDate);
            editor_date.putLong("CURRENT_DATE", firstDate);
        }
        editor_date.apply();

    }

    /**
     * get rotation index. Rotation length is 7
     * @return rotationIndex : int
     */
    public int getDayRotation(){
        int days = getTimeDiffInDays();
        int rotationIndex;
        if(days != 0) {
            rotationIndex = days % 7;
        } else {
            rotationIndex = 0;
        }
        return rotationIndex;
    }

    /**
     * Get time difference in days, of first set date and current date.
     * @return days : int
     */
    public int getTimeDiffInDays(){

        //Get the time-difference between first launch and time now.
        long daysInMillis = 1000 * 60 * 60 * 24;    // = 24 hours in milliseconds
        long daysDifferenceInMillis = (preferences_dates.getLong("CURRENT_DATE", 0) -
                preferences_dates.getLong("FIRST_DATE",0));

        //following calculation should produce even number but one can never be too certain
        int days = Math.toIntExact((daysDifferenceInMillis - (daysDifferenceInMillis % daysInMillis))
                / daysInMillis);
        return days;
    }
}
