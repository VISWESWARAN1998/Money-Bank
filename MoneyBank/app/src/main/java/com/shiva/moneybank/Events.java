package com.shiva.moneybank;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

import java.util.ArrayList;

public class Events extends AppCompatActivity {

    private DatePicker picker;
    private Spinner spinner;
    private Button button;
    private EditText amount,name;
    private AdView mAdView;
    Toast toast;
    private SQLiteDatabase database;
    private int isDeposit = 0; //deposit =0 withdraw = false - SQLite database does not have boolean data-type so we use an int

    @Override
    public void onBackPressed() {
        startActivity(new Intent(this,MainActivity.class));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_events);
        mAdView = (AdView) findViewById(R.id.adView2);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);
        picker = (DatePicker)findViewById(R.id.eventDate);
        spinner = (Spinner)findViewById(R.id.eventSpinner);
        button = (Button)findViewById(R.id.addevent);
        amount = (EditText)findViewById(R.id.eventAmount);
        name = (EditText)findViewById(R.id.eventName);
        button.setOnClickListener(buttonListener);
        ArrayList<String> list = new ArrayList<String>();
        list.add(getString(R.string.deposit));
        list.add(getString(R.string.withdraw));
        LayoutInflater inflater = getLayoutInflater();
        View view = inflater.inflate(R.layout.success,(ViewGroup)findViewById(R.id.success));
        ArrayAdapter arrayAdapter = new ArrayAdapter(this,android.R.layout.simple_spinner_item,list);
        spinner.setAdapter(arrayAdapter);
        toast = new Toast(getApplicationContext());
        toast.setDuration(Toast.LENGTH_LONG);
        toast.setView(view);
        toast.setGravity(Gravity.CENTER|Gravity.BOTTOM|Gravity.FILL_HORIZONTAL,0,0);
        database = openOrCreateDatabase("bank.db",MODE_PRIVATE,null);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                isDeposit = parent.getItemAtPosition(position).toString().trim().equals(getString(R.string.deposit)) ? 0 : 1;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    View.OnClickListener buttonListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            int day,month,year;
            day = picker.getDayOfMonth();
            month = picker.getMonth()+1;
            year = picker.getYear();
            if(amount.getText().toString().trim().equals(""))amount.setError(getString(R.string.nonEmpty));
            else if(name.getText().toString().trim().equals(""))name.setError(getString(R.string.nonEmpty));
            else
            {
                String eventName = name.getText().toString();
                Double eventAmount = Double.parseDouble(amount.getText().toString().trim());
                //database.execSQL("create table if not exists events(day int,month int,year int,event text,amount double,action int);");
                String comma = ",";
                String query = "insert into events values("+day+comma+month+comma+year+comma+"\""+eventName+"\""+comma+eventAmount+comma+isDeposit+");";
                database.execSQL(query);
                toast.show();
            }
        }
    };
}
