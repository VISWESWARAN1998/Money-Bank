package com.shiva.moneybank.api;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.Calendar;

/**
 * Created by Visweswaran on 29-06-2017.
 */

public class CommonModel {

    static CommonModel _instance;

    public static CommonModel getInstance(Context context) {
        if (_instance == null) {
            _instance = new CommonModel(context);
        }
        return _instance;
    }

    Context mContext;
    Calendar calendar;
    public CommonModel(Context context) {
        mContext = context;
        initLoad();
        _instance = this;
    }

    void initLoad() {
        calendar = Calendar.getInstance();
    }

    public void checkNotification(SQLiteDatabase db) {
        int dateCurrent = getDay();
        int dateLastClosed = AppUtils.getLastDate(mContext);
        int dateLastSalary = 0;

        if (dateCurrent > dateLastClosed) {
            // check salary
            Cursor cursor = db.rawQuery("select day from salary limit 1", null);
            if (cursor.getCount() > 0) {
                dateLastSalary = cursor.getInt(0);
            }
            cursor.close();

            if (dateLastSalary <= dateCurrent) {
                // -- Has your salary recived?
            }
        }
    }

    public void saveLastCloseTime() {
        AppUtils.setLastDate(mContext, getDay());
    }

    public int getDay() {
        return calendar.get(Calendar.DAY_OF_MONTH);
    }

    public int getMonth() {
        return calendar.get(Calendar.MONTH)+1;
    }

    public int getYear() {
        return calendar.get(Calendar.YEAR);
    }
}
