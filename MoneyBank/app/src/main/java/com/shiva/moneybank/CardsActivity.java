package com.shiva.moneybank;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.support.v7.app.AlertDialog;
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

import java.util.ArrayList;

public class CardsActivity extends AppCompatActivity {

    private EditText cashText;
    private Spinner option;
    private Button add,confirm;
    private DatePicker datePicker;
    private SQLiteDatabase database;
    private boolean isDeposit = true;
    private int day,month,year;
    private ArrayList<String> cardsList;
    private ArrayAdapter<String> cardsAdapter;
    private AlertDialog.Builder selectCard;
    private Toast toast,toast1,toast2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cards);
        // Create a success Toast
        LayoutInflater inflater = getLayoutInflater();
        View view = inflater.inflate(R.layout.success,(ViewGroup)findViewById(R.id.success));
        toast = new Toast(getApplicationContext());
        toast.setDuration(Toast.LENGTH_LONG);
        toast.setGravity(Gravity.CENTER|Gravity.BOTTOM|Gravity.FILL_HORIZONTAL,0,0);
        toast.setView(view);

        // Create failure toast
        LayoutInflater inflater1 = getLayoutInflater();
        View view1 = inflater.inflate(R.layout.failure,(ViewGroup)findViewById(R.id.failed));
        toast1 = new Toast(getApplicationContext());
        toast1.setDuration(Toast.LENGTH_LONG);
        toast1.setGravity(Gravity.CENTER|Gravity.BOTTOM|Gravity.FILL_HORIZONTAL,0,0);
        toast1.setView(view1);

        // Create insufficient toast
        LayoutInflater inflater2 = getLayoutInflater();
        View view2 = inflater.inflate(R.layout.warning,(ViewGroup)findViewById(R.id.insufficient));
        toast2 = new Toast(getApplicationContext());
        toast2.setDuration(Toast.LENGTH_LONG);
        toast2.setGravity(Gravity.CENTER|Gravity.BOTTOM|Gravity.FILL_HORIZONTAL,0,0);
        toast2.setView(view2);

        // assign proper ids from the View class components
        cashText = (EditText)findViewById(R.id.cardCash);
        option = (Spinner)findViewById(R.id.cardOption);
        confirm = (Button)findViewById(R.id.cardConfirmButton);
        add = (Button)findViewById(R.id.addCardButton);
        datePicker = (DatePicker)findViewById(R.id.datePicker);
        ArrayList<String> options = new ArrayList<String>();
        options.add(getString(R.string.deposit));
        options.add(getString(R.string.withdraw));
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item,options);
        option.setAdapter(adapter);
        confirm.setEnabled(false);
        database = openOrCreateDatabase("bank.db",MODE_PRIVATE,null);
        new CardsUpdater().execute();
        confirm.setOnClickListener(confirmListener);
        cardsList = new ArrayList<String>();
        option.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                isDeposit = parent.getItemAtPosition(position).toString().equals(getString(R.string.deposit)) ? true : false;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        Cursor cursor = database.rawQuery("select * from cards",null);
        while (cursor.moveToNext())
        {
            cardsList.add(cursor.getString(0));
        }
    }
    View.OnClickListener confirmListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            day = datePicker.getDayOfMonth();
            month = datePicker.getMonth()+1;
            year = datePicker.getYear();
            if(cashText.getText().toString().trim().equals(""))cashText.setError(getString(R.string.nonEmpty));
            else
            {
               // If it is deposit
                selectCard = new AlertDialog.Builder(CardsActivity.this);
                selectCard.setTitle(getString(R.string.select));
                selectCard.setIcon(R.drawable.hdpi);
                new ShowCards().execute();
            }
        }
    };

    private class CardsUpdater extends AsyncTask<Void,Void,Void>
    {
        boolean canEnable = false;
        @Override
        protected Void doInBackground(Void... params) {
            Cursor cursor = database.rawQuery("select * from cards",null);
            if(cursor.moveToNext()){
                canEnable = true;
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void v)
        {
            confirm.setEnabled(canEnable);
        }
    }
    // OnClickListener is replaced in the xml to avoid lengthy code
    public void newCard(View view)
    {
        Intent intent = new Intent(CardsActivity.this,AddACard.class);
        startActivity(intent);
    }

    private class ShowCards extends AsyncTask<Void,Void,Void>
    {

        @Override
        protected Void doInBackground(Void... params) {
            return null;
        }

        @Override
        protected void onPostExecute(Void v)
        {
            cardsAdapter = new ArrayAdapter<String>(CardsActivity.this,android.R.layout.select_dialog_singlechoice,cardsList);
            selectCard.setAdapter(cardsAdapter, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    String name = cardsAdapter.getItem(which);
                    String Name = "\""+name+"\"";
                    String query = "select * from cards where cardName = \""+name+"\";";
                    Cursor cursor = database.rawQuery(query,null);
                    cursor.moveToFirst();
                    Double currentBalance = cursor.getDouble(2);
                    Double newBalance = Double.parseDouble(cashText.getText().toString().trim());
                    // If it is a deposit
                    if(isDeposit)
                    {
                        Double updatedBalance = currentBalance+newBalance;
                        database.execSQL("update cards set balance = "+updatedBalance+" where cardName = \""+name+"\";");
                        String card = "\"card\"";
                        String deposit = "\"deposit\"";
                        String history = "insert into cardHistory values("+card+","+Name+","+newBalance+","+deposit+","+day+","+month+","+year+");";
                        database.execSQL(history);
                        toast.show();
                    }
                    else
                    {
                        //Toast.makeText(CardsActivity.this,"working",Toast.LENGTH_LONG).show();
                        if(newBalance>currentBalance){toast2.show();}
                        else
                        {
                            Double updatedBalance = currentBalance-newBalance;
                            database.execSQL("update cards set balance = "+updatedBalance+" where cardName = \""+name+"\";");
                            String card = "\"card\"";
                            String withdraw = "\"withdraw\"";
                            String history = "insert into cardHistory values("+card+","+Name+","+newBalance+","+withdraw+","+day+","+month+","+year+");";
                            database.execSQL(history);
                            toast.show();
                        }
                    }
                }
            });
            selectCard.setNegativeButton(getString(R.string.cancel), new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });
            selectCard.show();
        }
    }
}
