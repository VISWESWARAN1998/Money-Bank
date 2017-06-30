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
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

import java.util.ArrayList;

public class AddACard extends AppCompatActivity {

    private String cardName,cardNumber;
    private Double balance;
    private EditText cardNameText,cardNumberText,cardBalanceText;
    private AdView mAdView;
   // private Spinner option;
    private SQLiteDatabase database;
    boolean isDepositSelected = true;
    private Toast failed,success;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_acard);
        mAdView = (AdView) findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);
        cardNameText = (EditText)findViewById(R.id.cardNameText);
        cardNumberText = (EditText)findViewById(R.id.cardNumberText);
        cardBalanceText = (EditText)findViewById(R.id.cardBalanceText);
        //option = (Spinner)findViewById(R.id.cardSpinner);
        database = openOrCreateDatabase("bank.db",MODE_PRIVATE,null);

//        ArrayList<String> list = new ArrayList<String>();
//        list.add(getString(R.string.deposit));
//        list.add(getString(R.string.withdraw));
//        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item,list);
//        option.setAdapter(adapter);
        LayoutInflater inflater = getLayoutInflater();
        View view = inflater.inflate(R.layout.failure,(ViewGroup)findViewById(R.id.failed));
        failed = new Toast(getApplicationContext());
        failed.setDuration(Toast.LENGTH_LONG);
        failed.setView(view);
        failed.setGravity(Gravity.BOTTOM|Gravity.CENTER|Gravity.FILL_HORIZONTAL,0,0);
//        option.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//            @Override
//            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//                isDepositSelected = parent.getItemAtPosition(position).toString().equals(getString(R.string.deposit)) ? true : false;
//            }
//
//            @Override
//            public void onNothingSelected(AdapterView<?> parent) {
//
//            }
//        });

        LayoutInflater inflater1 = getLayoutInflater();
        View view1 = inflater1.inflate(R.layout.success,(ViewGroup)findViewById(R.id.success));
        success = new Toast(getApplicationContext());
        success.setDuration(Toast.LENGTH_LONG);
        success.setView(view1);
        success.setGravity(Gravity.BOTTOM|Gravity.CENTER|Gravity.FILL_HORIZONTAL,0,0);
    }


    public void confirmListener(View view)
    {
        if(cardNumberText.getText().toString().equals(""))cardNumberText.setText(getString(R.string.na));
        if(cardNameText.getText().toString().trim().equals(""))cardNameText.setError(getString(R.string.nonEmpty));
        else if(cardBalanceText.getText().toString().trim().equals(""))cardBalanceText.setError(getString(R.string.nonEmpty));
        else
        {
            cardName = cardNameText.getText().toString().trim();
            cardNumber = cardNumberText.getText().toString().trim();
            balance = Double.parseDouble(cardBalanceText.getText().toString().trim());
            // If the option is deposit
            if(isDepositSelected)
            {
                cardNameText.setEnabled(false);
                new CardDeposit().execute(); // Execute our background process
            }
            else
            {

            }
        }
    }

    private class CardDeposit extends AsyncTask<Void,Void,Void>
    {
        boolean processFailed = false;
        String string(String s)
        {
            return "\""+s+"\"";
        }
        @Override
        protected Void doInBackground(Void... params) {
            String query = "insert into cards values("+string(cardName)+","+string(cardNumber)+","+balance+");";
            try
            {
                database.execSQL(query);
                success.show();
                processFailed = false;
            }
            catch (Exception e)
            {
                failed.show();
                processFailed = true;
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void v)
        {
            cardNameText.setEnabled(true);
            if(processFailed==true)Toast.makeText(AddACard.this,getString(R.string.invalid),Toast.LENGTH_LONG).show();
            else
            {
                Intent intent = new Intent(AddACard.this,CardsActivity.class);
                startActivity(intent);
            }
        }
    }

}
