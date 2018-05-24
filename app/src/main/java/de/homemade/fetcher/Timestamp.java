package de.homemade.fetcher;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

/**
 * Created by tkallinich on 25.08.17.
 * general Timestamp creating by call.
 * UNIX Time = sec since beginn 1970 UTC
 */

public class Timestamp {

    String TAG = "FETCHER ";
    String CLASS = "TIME ST ";
    private Long TIME_STAMP;

    /**
     * generates a timestamp as soon at is called
     * (since epoch in millSec)
     *
     * @return timestamp as String
     */

    public Long createTimeStamp(){

        Long timestamp = System.currentTimeMillis()/1000;
        this.TIME_STAMP = timestamp;

        Log.i(TAG, CLASS + " TIME STAMP - createTimeStamp "+ timestamp);

        return timestamp;
    }

    /**
     * calculates the time without saving it via shared preferences
     *
     *
     * @return Long whatIsTheTime
     */
    public Long whatIsTheTime(){
        Long whatIsTheTime = System.currentTimeMillis()/1000;

        Log.i(TAG, CLASS + " WHAT IS THE TIME STAMP "+ whatIsTheTime);

        return whatIsTheTime;

    }

    /**
     * Saves a timestamp via shared prefences
     *
     * @param time Long
     * @param context
     */

    public void saveNewTimeStamp(Long time, Context context){
        // only this app has permission to see content of shared preferences file
        SharedPreferences sharedprefSave = context.getSharedPreferences("de.homemade.fetcher"
                                                                            ,context.MODE_PRIVATE);

        // editor is able to modify data in shared preferences
        SharedPreferences.Editor editor = sharedprefSave.edit();

        // Key: TIME_STAMP,  Value: time; set Key String in order to find it later on.
        editor.putLong("TIME_STAMP", time);

        // apply changed to shared preferences
        editor.apply();
        Log.i(TAG, CLASS + " WHAT IS THE TIME STAMP SAVED "+ time);

    }

    /**
     * calls TIME_STAMP from shared preferences
     *
     * @param context
     * @return Long time
     */

    public Long callLastTimeStamp(Context context){

        // only this app has permission to see content of shared preferences file
        SharedPreferences sharedprefCall = context.getSharedPreferences("de.homemade.fetcher"
                                                                            ,context.MODE_PRIVATE);

        // t0l is default return value if no data is found with "TIME_STAMP"
        Long time = sharedprefCall.getLong("TIME_STAMP", 0L);
        Log.i(TAG, CLASS + " LAST TIME STAMP: "+ time);

        return time;

    }

    /**
     * resets timestamp to zero
     *
     * @param context
     */

    public void resetTimeStamp(Context context){
        // only this app has permission to see content of shared preferences file
        SharedPreferences sharedprefSave = context.getSharedPreferences("de.homemade.fetcher"
                                                                            ,context.MODE_PRIVATE);

        // editor is able to modify data in shared preferences
        SharedPreferences.Editor editor = sharedprefSave.edit();

        // Key: TIME_STAMP,  Value: time; set Key String in order to find it later on.
        editor.putLong("TIME_STAMP", 0L);

        // apply changed to shared preferences
        editor.apply();
        Log.e(TAG, CLASS + " TIME STAMP RESETED");

    }

}
