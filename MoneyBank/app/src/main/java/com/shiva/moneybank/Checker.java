package com.shiva.moneybank;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.IBinder;
import android.support.annotation.IntDef;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.widget.Toast;

import java.util.Calendar;

/**
 * Created by Visweswaran on 29-06-2017.
 */

public class Checker extends Service {
    private SQLiteDatabase database;
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        //Toast.makeText(getApplicationContext(),"Service started",Toast.LENGTH_SHORT).show();
        int day,month,year;
        Calendar calendar = Calendar.getInstance();
        day = calendar.get(Calendar.DAY_OF_MONTH);
        month = calendar.get(Calendar.MONTH)+1;
        year = calendar.get(Calendar.YEAR);
        database = openOrCreateDatabase("bank.db",MODE_PRIVATE,null);
        // getting the currency name is important
        Cursor curreny = database.rawQuery("select * from user",null);
        curreny.moveToFirst();
        String currenyName = curreny.getString(1);

        // check whther the user has been notified on this day
        String query1 = String.format("select * from notifications where day=%d and month=%d and year=%d;"
                ,day, month,year);
        Cursor isNotified = database.rawQuery(query1,null);
        if(isNotified.getCount()==0) {

            //check whether there is a salary day
            Cursor salaryChecker = database.rawQuery("select * from salary where day = " + day + ";", null);
            while (salaryChecker.moveToNext()) {
                // Display the notification here
                NotificationCompat.Builder notification = new NotificationCompat.Builder(this);
                notification.setContentTitle(getString(R.string.salrynotify) + " " + salaryChecker.getString(1));
                notification.setSmallIcon(R.drawable.hdpi);
                notification.setContentText(getString(R.string.please));
                notification.setVisibility(NotificationCompat.VISIBILITY_PUBLIC);

                Intent intent1 = new Intent(this, MainActivity.class);
                PendingIntent intent2 = PendingIntent.getActivity(this, 0, intent1, PendingIntent.FLAG_UPDATE_CURRENT);
                notification.setContentIntent(intent2);

                NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
                manager.notify(123454, notification.build());
            }

            // Check whether it is a bill day or not
            Cursor billChecker = database.rawQuery("select * from bill where day = " + day + ";", null);
            while (billChecker.moveToNext()) {
                // Display the notification here
                NotificationCompat.Builder notification = new NotificationCompat.Builder(this);
                notification.setContentTitle(getString(R.string.salrynotify) + " " + billChecker.getString(1));
                notification.setSmallIcon(R.drawable.hdpi);
                notification.setContentText(getString(R.string.please));
                notification.setVisibility(NotificationCompat.VISIBILITY_PUBLIC);

                Intent intent1 = new Intent(this, MainActivity.class);
                PendingIntent intent2 = PendingIntent.getActivity(this, 0, intent1, PendingIntent.FLAG_UPDATE_CURRENT);
                notification.setContentIntent(intent2);

                NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
                manager.notify(123454, notification.build());
            }

            // check whether there is any event or not
            String query = String.format("select * from events where day=%d and month=%d and year=%d;"
                    , day, month, year);
            Cursor eventChecker = database.rawQuery(query, null);
            while (eventChecker.moveToNext()) {
                // Display the notification here
                NotificationCompat.Builder notification = new NotificationCompat.Builder(this);
                notification.setContentTitle(getString(R.string.events) + ": " + eventChecker.getString(4) + " " + currenyName);
                notification.setSmallIcon(R.drawable.hdpi);
                notification.setContentText(getString(R.string.please));
                notification.setVisibility(NotificationCompat.VISIBILITY_PUBLIC);

                Intent intent1 = new Intent(this, MainActivity.class);
                PendingIntent intent2 = PendingIntent.getActivity(this, 0, intent1, PendingIntent.FLAG_UPDATE_CURRENT);
                notification.setContentIntent(intent2);

                NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
                manager.notify(123454, notification.build());
                String add = String.format("insert into notifications values(%d,%d,%d);"
                        ,day, month,year);
                database.execSQL(add);
            }
        }
        return START_STICKY;
    }
}
