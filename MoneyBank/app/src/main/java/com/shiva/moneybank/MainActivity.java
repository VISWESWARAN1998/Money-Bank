package com.shiva.moneybank;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;


import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private TextView currencyView,currenyNameView;
    private SQLiteDatabase database;
    protected DrawerLayout drawer;
    private ListView mainView;
    private List<Balance> balanceList = new ArrayList<Balance>();
    private ArrayAdapter<Balance> balanceArrayAdapter;
    String currencyName = "";

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Boolean preferred = getSharedPreferences("preference",MODE_PRIVATE).getBoolean("firstRun",true);
        if(preferred)
        {
            Intent intent = new Intent(MainActivity.this,Form.class);
            startActivity(intent);
        }
        getSharedPreferences("preference",MODE_PRIVATE).edit().putBoolean("firstRun",false).commit();
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Money Bank");
        setSupportActionBar(toolbar);

        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        database = openOrCreateDatabase("bank.db",MODE_PRIVATE,null);
        currencyView = (TextView) findViewById(R.id.currencyView);
        currenyNameView = (TextView)findViewById(R.id.currencyNameView);
        mainView = (ListView)findViewById(R.id.mainCardsList);
        new MoneyUpdate().execute();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
//            super.onBackPressed();
             this.finishAffinity();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            Intent intent = new Intent(MainActivity.this,Settings.class);
            startActivity(intent);
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_cash) {
            Intent intent = new Intent(MainActivity.this,CashActivity.class);
            startActivity(intent);
            MainActivity.this.finish();
        } else if (id == R.id.nav_cards) {
            Intent intent = new Intent(MainActivity.this,CardsActivity.class);
            startActivity(intent);
            MainActivity.this.finish();

        } else if (id == R.id.nav_history) {
            Intent intent = new Intent(MainActivity.this,HistoryActivity.class);
            startActivity(intent);
            MainActivity.this.finish();

        } else if (id == R.id.nav_manage) {
            Intent intent = new Intent(MainActivity.this,Settings.class);
            startActivity(intent);
            MainActivity.this.finish();

        }  else if (id == R.id.nav_about) {
            Intent intent = new Intent(MainActivity.this,License.class);
            startActivity(intent);
            MainActivity.this.finish();
        }
        else if(id==R.id.nav_events)
        {
            Intent intent = new Intent(MainActivity.this,Events.class);
            startActivity(intent);
            MainActivity.this.finish();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private class MoneyUpdate extends AsyncTask<Void,Void,Void>
    {
        String money;
        boolean toProcess = false;
        @Override
        protected Void doInBackground(Void... params) {
            database.execSQL("create table if not exists user (name text,currency text);");
            database.execSQL("create table if not exists salary (day int,salary double);");
            database.execSQL("create table if not exists bill (day int,salary double);");
            database.execSQL("create table if not exists cash(id int,cash double);");
            database.execSQL("create table if not exists cards(cardName text primary key not null,number text,balance double);");
            database.execSQL("create table if not exists history(name text,amount double,process text,day int,month int,year int);");
            database.execSQL("create table if not exists cardHistory(name text,cname text,amount double,process text,day int,month int,year int);");
            database.execSQL("create table if not exists events(day int,month int,year int,event text,amount double,action int);");
            database.execSQL("create table if not exists notifications(day int,month int,year int)");
            database.execSQL("create table if not exists alarm(setx int);");

            Cursor cursor = database.rawQuery("select * from cash",null);
            if(cursor.moveToNext())
            {
                toProcess = false;
            }
            else toProcess = true;

            return null;
        }

        protected void onPostExecute(Void v)
        {
//            Intent intent = new Intent(MainActivity.this,Checker.class);
//            startService(intent);
            if(toProcess)currencyView.setText(Database.cash.toString().trim());
            else
            {
                Cursor cursor = database.rawQuery("select * from cash",null);
                cursor.moveToFirst();
                Double currenctCurrency = cursor.getDouble(1);
                Database.cash = currenctCurrency;
                currencyView.setText(Database.cash.toString().trim());
            }
            try {
                Cursor cursor = database.rawQuery("select * from user;", null);
                if(cursor.moveToNext()) {
                    //cursor.moveToFirst();
                    String name = cursor.getString(1);
                    currenyNameView.setText(name);
                    currencyName = name;
                }
            }
            catch (android.database.sqlite.SQLiteException e){

            }
            new CardsDisplay().execute();

            Cursor cursor = database.rawQuery("select * from alarm;",null);
            if(cursor.getCount()==0)
            {
                setAlarm();
                database.execSQL("insert into alarm values(0);");
            }
        }
    }

    private class CardsDisplay extends AsyncTask<Void,Void,Void>
    {

        @Override
        protected Void doInBackground(Void... params) {
            Cursor cursor = database.rawQuery("select * from cards;",null);
            while (cursor.moveToNext())
            {
                String cardName = cursor.getString(0);
                String cardBalance = cursor.getString(2)+" "+currencyName;
                balanceList.add(new Balance(cardName,cardBalance));
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void v)
        {
            balanceArrayAdapter = new Adapter();
            mainView.setAdapter(balanceArrayAdapter);
        }
    }

    private class Adapter extends ArrayAdapter<Balance>
    {

        public Adapter() {
            super(MainActivity.this,R.layout.content_main,balanceList);
        }

        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            View view = convertView;
            if(view==null)view = getLayoutInflater().inflate(R.layout.mainlist,parent,false);
            Balance balance = balanceList.get(position);

            TextView name = (TextView)view.findViewById(R.id.mainCardView);
            name.setText(balance.getName());
            TextView bal = (TextView)view.findViewById(R.id.mainCardBalane);
            bal.setText(balance.getBalance());
//            return super.getView(position, convertView, parent);
            return view;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    public void setAlarm()
    {
        Calendar calendar = new GregorianCalendar();
        calendar.set(Calendar.HOUR_OF_DAY,24);
        calendar.set(Calendar.MINUTE,0);
        calendar.set(Calendar.SECOND,0);
        Intent intent = new Intent(MainActivity.this,Receiver.class);
        PendingIntent intent1 = PendingIntent.getBroadcast(MainActivity.this,0,intent,PendingIntent.FLAG_UPDATE_CURRENT);
        AlarmManager manager = (AlarmManager)MainActivity.this.getSystemService(MainActivity.this.ALARM_SERVICE);
        manager.setRepeating(AlarmManager.RTC_WAKEUP,calendar.getTimeInMillis(),AlarmManager.INTERVAL_DAY,intent1);
        //Toast.makeText(getApplicationContext(),"Alarm started",Toast.LENGTH_SHORT).show();
    }
}
