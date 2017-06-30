package com.shiva.moneybank;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.support.design.widget.NavigationView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;

public class CashActivity extends AppCompatActivity{

    private EditText getCash;
    private Spinner selectOption;
    private DatePicker datePicker;
    private Button confirm;
    private boolean isDeposit = true;
    private SQLiteDatabase database;
    private int day,month,year;
    private Toast successToast,insufficient;
    private ListView cashList;

    @Override
    public void onBackPressed() {
        startActivity(new Intent(this,MainActivity.class));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cash);
        getCash = (EditText)findViewById(R.id.cashInput);
        selectOption = (Spinner)findViewById(R.id.depositWithdraw);
        datePicker = (DatePicker)findViewById(R.id.datePickerCash);
        confirm = (Button)findViewById(R.id.cashConfirm);
        confirm.setEnabled(false);
        confirm.setOnClickListener(confirmListener);
        new CashActivityUpdater().execute();
        database = openOrCreateDatabase("bank.db",MODE_PRIVATE,null);
        cashList = (ListView)findViewById(R.id.cashList);
        LayoutInflater inflater = getLayoutInflater();
        View view = inflater.inflate(R.layout.success,(ViewGroup)findViewById(R.id.success));
        successToast = new Toast(getApplicationContext());
        successToast.setDuration(Toast.LENGTH_LONG);
        successToast.setView(view);
        successToast.setGravity(Gravity.CENTER|Gravity.FILL_HORIZONTAL|Gravity.BOTTOM,0,0);
        selectOption.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String result = parent.getItemAtPosition(position).toString();
                // If the result is deposit
                if(result.equals(getString(R.string.deposit)))
                {
                    isDeposit = true;
                }
                else
                {
                    isDeposit = false;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        ArrayList<String> list2 = new ArrayList<String>(); // used for displaying it in the listView
        Cursor cursor = database.rawQuery("select * from history where name = \"cash\";",null);
        while (cursor.moveToNext())
        {
            String state = cursor.getString(2).equals("deposit") ? getString(R.string.deposited):getString(R.string.withdrawed);
            String name = cursor.getString(1)+" is "+state+" on "+cursor.getString(3)+"."+cursor.getString(4)+"."+cursor.getString(5);
            list2.add(0,name);
        }
        ArrayAdapter<String> adapter1 = new ArrayAdapter<String>(CashActivity.this,android.R.layout.simple_list_item_1,list2);
        cashList.setAdapter(adapter1);

        // Insufficient funds message
        LayoutInflater inflater1 = getLayoutInflater();
        View view1 = inflater1.inflate(R.layout.warning,(ViewGroup)findViewById(R.id.insufficient));
        insufficient = new Toast(getApplicationContext());
        insufficient.setDuration(Toast.LENGTH_LONG);
        insufficient.setGravity(Gravity.CENTER|Gravity.FILL_HORIZONTAL|Gravity.BOTTOM,0,0);
        insufficient.setView(view1);
    }

    View.OnClickListener confirmListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            day = datePicker.getDayOfMonth();
            month = datePicker.getMonth()+1;
            year = datePicker.getYear();
            // Handling the deposit
            if(isDeposit)
            {
                             if(getCash.getText().toString().trim().equals(""))getCash.setError(getString(R.string.nonEmpty));
                else
                {
                    Cursor cursor = database.rawQuery("select * from cash",null);
                    if(cursor.moveToNext())
                    {
                        Double databaseCurrency = cursor.getDouble(1);
                        Double currentCash = Double.parseDouble(getCash.getText().toString().trim());
                        Database.cash = databaseCurrency+currentCash;
                        String query = "update cash set cash = "+Database.cash+" where id =0;";
                        String cash = "\"cash\"";
                        String deposit = "\"deposit\"";
                        String history = "insert into history values("+cash+","+currentCash+","+deposit+","+day+","+month+","+year+");";
                        database.execSQL(query);
                        database.execSQL(history);
                        successToast.show();
                    }
                    else
                    {
                        Double currentCash = Double.parseDouble(getCash.getText().toString().trim());
                        String query = "insert into cash values(0,"+currentCash+");";
                        String cash = "\"cash\"";
                        String deposit = "\""+getString(R.string.deposit)+"\"";
                        String history = "insert into history values("+cash+","+currentCash+","+deposit+","+day+","+month+","+year+");";
                        database.execSQL(query);
                        database.execSQL(history);
                        successToast.show();
                    }
                }
            }

            else
            {
                Double currentCash = Double.parseDouble(getCash.getText().toString().trim());
                Double mainCash = Database.cash;
                if(currentCash>mainCash)
                {
                    insufficient.show();
                }
                else
                {
                    Database.cash-=currentCash;
                    database.execSQL("update cash set cash = "+Database.cash+" where id = 0");
                    String cash = "\""+getString(R.string.cashsmall)+"\"";
                    String withdraw = "\""+getString(R.string.withdraw)+"\"";
                    String history = "insert into history values("+cash+","+currentCash+","+withdraw+","+day+","+month+","+year+");";
                    database.execSQL(history);
                    successToast.show();
                }
            }
        }
    };

    private class CashActivityUpdater extends AsyncTask<Void,Void,Void>
    {
        ArrayList<String> list;
        @Override
        protected Void doInBackground(Void... params) {
            list = new ArrayList<String>();
            list.add(getString(R.string.deposit));
            list.add(getString(R.string.withdraw));
            return null;
        }

        @Override
        protected void onPostExecute(Void v)
        {
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(CashActivity.this,android.R.layout.simple_spinner_item,list);
            selectOption.setAdapter(adapter);
            confirm.setEnabled(true);
        }
    }
}
