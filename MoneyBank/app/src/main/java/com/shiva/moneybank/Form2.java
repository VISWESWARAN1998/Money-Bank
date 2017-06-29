package com.shiva.moneybank;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;

public class Form2 extends AppCompatActivity {

    private EditText salary,bill;
    private Button salaryButton,billButton,complete;
    private Spinner salarySpinner,billSpinner;
    private ArrayAdapter<String> salaryAdapter,billAdapter;
    private SQLiteDatabase database;
    private Integer salaryDay = 1, billDay = 1;
    private Toast successToast;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form2);
        salary = (EditText) findViewById(R.id.selectSalaryAmount);
        salarySpinner = (Spinner) findViewById(R.id.selectSalaryDay);
        salaryButton = (Button) findViewById(R.id.salaryButton);
        bill = (EditText)findViewById(R.id.selectBillAmount);
        billSpinner = (Spinner)findViewById(R.id.selectBillDay);
        billButton = (Button)findViewById(R.id.billButton);
        complete = (Button)findViewById(R.id.completeButton);
        if(FormHandler.monthlySalary==false)salaryButton.setEnabled(false);
        if(FormHandler.monthlyBills==false)billButton.setEnabled(false);
        complete.setOnClickListener(completeListener);
        salarySpinner.setEnabled(false);
        billSpinner.setEnabled(false);
        new SpinnerUpdate().execute();
        database = openOrCreateDatabase("bank.db",MODE_PRIVATE,null);
        salaryButton.setOnClickListener(salaryListener);
        billButton.setOnClickListener(billListener);
        salarySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String value = parent.getItemAtPosition(position).toString().trim();
                salaryDay = Integer.parseInt(value);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        billSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String value = parent.getItemAtPosition(position).toString().trim();
                billDay = Integer.parseInt(value);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        // Display the success message using our custom toast
        LayoutInflater inflater = getLayoutInflater();
        View view = inflater.inflate(R.layout.success,(ViewGroup)findViewById(R.id.success));
        successToast = new Toast(getApplicationContext());
        successToast.setView(view);
        successToast.setDuration(Toast.LENGTH_LONG);
        successToast.setGravity(Gravity.BOTTOM|Gravity.FILL_HORIZONTAL|Gravity.CENTER,0,0);
    }

    // Assigning values to the spinner

    private class SpinnerUpdate extends AsyncTask<Void,Void,Void>
    {

        @Override
        protected Void doInBackground(Void... params) {
            ArrayList<String> days = new ArrayList<String>();
            for(int i=1;i<=31;i++)
            {
                days.add(""+i);
            }
            salaryAdapter = new ArrayAdapter<String>(Form2.this,android.R.layout.simple_spinner_item,days);
            billAdapter = new ArrayAdapter<String>(Form2.this,android.R.layout.simple_spinner_item,days);
            return null;
        }


        @Override
        protected void onPostExecute(Void v)
        {
            salarySpinner.setAdapter(salaryAdapter);
            billSpinner.setAdapter(billAdapter);
            salarySpinner.setEnabled(true);
            billSpinner.setEnabled(true);
        }
    }

    // Performing operations necessary for adding salary
    View.OnClickListener salaryListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if(salary.getText().toString().trim().equals(""))
            {
                salary.setError(getString(R.string.nonEmpty));
            }
            else {
                Double amount = Double.parseDouble(salary.getText().toString().trim());
                String query = "insert into salary values("+salaryDay+","+amount+");";
                database.execSQL(query);
                successToast.show();
            }
        }
    };


    // Performing opertaions necessary for adding bills

    View.OnClickListener billListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if(bill.getText().toString().trim().equals(""))
            {
                bill.setError(getString(R.string.nonEmpty));
            }
            else {
                Double amount = Double.parseDouble(bill.getText().toString().trim());
                String query = "insert into bill values("+billDay+","+amount+");";
                database.execSQL(query);
                successToast.show();
            }
        }
    };

    View.OnClickListener completeListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent = new Intent(Form2.this,MainActivity.class);
            startActivity(intent);
        }
    };
}
