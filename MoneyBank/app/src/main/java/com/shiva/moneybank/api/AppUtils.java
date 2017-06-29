package com.shiva.moneybank.api;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by Visweswaran on 29-06-2017.
 */

public class AppUtils {
    private final static String LAST_DATE = "LAST_DATE";

    public static void setLastDate(Context context, int date)
    {
        shared = context.getSharedPreferences("userInfo", 0);
        editor = shared.edit();
        editor.putInt(LAST_DATE, date).commit();
    }

    public static int getLastDate(Context context) {
        shared = context.getSharedPreferences("userInfo", 0);
        return shared.getInt(LAST_DATE, 0);
    }

    private static SharedPreferences shared;
    private static SharedPreferences.Editor editor;
}
