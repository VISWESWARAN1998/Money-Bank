package com.shiva.moneybank;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.*;
import android.support.annotation.IdRes;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.InputMismatchException;


public class Form extends AppCompatActivity {

    private EditText userName,currency;
    private Button confirm;
    private RadioGroup salary,bill;
    private SQLiteDatabase database;
    Toast successToast;
    Toast failedToast;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form);
        userName = (EditText)findViewById(R.id.name);
        currency = (EditText)findViewById(R.id.currency);
        confirm = (Button)findViewById(R.id.confirm);
        confirm.setOnClickListener(confirmListener);
        confirm.setEnabled(false);
        salary =  (RadioGroup)findViewById(R.id.formGroup1);
        bill = (RadioGroup)findViewById(R.id.formGroup2);
        database = openOrCreateDatabase("bank.db",MODE_PRIVATE,null);
        new Initialize().execute();
        LayoutInflater inflater = getLayoutInflater();
        View view = inflater.inflate(R.layout.success,(ViewGroup)findViewById(R.id.success));
        successToast = new Toast(getApplicationContext());
        successToast.setDuration(Toast.LENGTH_LONG);
        successToast.setView(view);
        successToast.setGravity(Gravity.CENTER|Gravity.BOTTOM|Gravity.FILL_HORIZONTAL,0,0);

        LayoutInflater inflater1 = getLayoutInflater();
        View view1 = inflater1.inflate(R.layout.failure,(ViewGroup)findViewById(R.id.failed));
        failedToast = new Toast(getApplicationContext());
        failedToast.setGravity(Gravity.CENTER|Gravity.BOTTOM|Gravity.FILL_HORIZONTAL,0,0);
        failedToast.setDuration(Toast.LENGTH_LONG);
        failedToast.setView(view1);

        salary.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
                if(checkedId == R.id.salaryYes)
                {
                    FormHandler.monthlySalary = true;
                }
                else
                {
                    FormHandler.monthlySalary = false;
                }
            }
        });

        bill.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
                if(checkedId == R.id.billYes)
                {
                    FormHandler.monthlyBills = true;
                }
                else
                {
                    FormHandler.monthlyBills = false;
                }
            }
        });
    }

    View.OnClickListener confirmListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            // Check if user name is proper
            try
            {
                if(userName.getText().toString().trim().equals(""))userName.setError(getString(R.string.nonEmpty));
                else if(currency.getText().toString().trim().equals(""))currency.setError(getString(R.string.nonEmpty));
                else if(salary.getCheckedRadioButtonId()==-1)
                {
                    failedToast.show();
                }
                else if(bill.getCheckedRadioButtonId()==-1)
                {
                    failedToast.show();
                }
                else
                {
                    Handler handler = new Handler();
                    String currentCurrency = handler.enclose(currency.getText().toString().trim());
                    String query = "insert into user values("+handler.enclose(userName.getText().toString().trim())+","+currentCurrency+");";
                    database.execSQL(query);
                    successToast.show();
                    Intent intent = new Intent(Form.this,Form2.class);
                    startActivity(intent);
                }
            }
            catch (InputMismatchException e)
            {

            }
        }
    };

    private class Initialize extends AsyncTask<Void,Void,Void>
    {

        @Override
        protected Void doInBackground(Void... params) {
            return null;
        }

        @Override
        protected void onPostExecute(Void v)
        {
            confirm.setEnabled(true);
            //Toast.makeText(Form.this,"Toast is working fine!",Toast.LENGTH_LONG).show();
        }
    }
}
