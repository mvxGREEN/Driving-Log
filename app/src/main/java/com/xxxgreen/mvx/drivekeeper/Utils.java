package com.xxxgreen.mvx.drivekeeper;

import android.util.Log;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by MVX on 8/9/2017.
 */

public final class Utils {
    private static final String TAG = "Utils";

    public static long makeLong(String time) {
        int minutes = Integer.valueOf(time.substring(3));
        int hours = Integer.valueOf(time.substring(0,2));
        return ((hours * 60 * 60) + (minutes * 60)) * 1000;
    }

    public static long makeLong(int hours, int minutes) {
        return ((hours * 60 * 60) + (minutes * 60)) * 1000;
    }

    //convert time to formatted String
    public static String getTime(long time) {
        int tMinutes = (int) time / 60000;
        String hours = String.valueOf(tMinutes / 60);
        if (hours.length() < 2) {
            hours = "0" + hours;
        }
        String minutes = String.valueOf(tMinutes % 60);
        if (minutes.length() < 2) {
            minutes = "0" + minutes;
        }
        return hours + ":" + minutes;
    }

    //get current date from Calendar as String
    public static String getDate() throws ParseException {
        Calendar c = Calendar.getInstance();
        Date date = c.getTime();
        SimpleDateFormat fmtOut = new SimpleDateFormat("MM/dd/yyyy");
        return fmtOut.format(date);
    }
}
