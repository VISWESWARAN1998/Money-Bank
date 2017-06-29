package com.shiva.moneybank;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
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

/**
 * Created by Visweswaran on 29-06-2017.
 */

public class CardSettings extends Fragment {
    private Spinner delSpinner;
    private Button delButton;
    private SQLiteDatabase database;
    private String cardName = "";
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.cardsettings,container,false);
        delSpinner = (Spinner) view.findViewById(R.id.delSpinner);
        delButton = (Button) view.findViewById(R.id.delButton);
        delButton.setOnClickListener(delListener);
        database = getContext().openOrCreateDatabase("bank.db",Context.MODE_PRIVATE,null);
        Cursor cursor = database.rawQuery("select * from cards;",null);
        ArrayList<String> list = new ArrayList<String>();
        while (cursor.moveToNext())
        {
            String name = cursor.getString(0);
            list.add(0,name);
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(),android.R.layout.simple_spinner_item,list);
        delSpinner.setAdapter(adapter);
        delSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                cardName = parent.getItemAtPosition(position).toString().trim();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                cardName = parent.getSelectedItem().toString();
            }
        });
        return view;
        //return super.onCreateView(inflater, container, savedInstanceState);
    }

    View.OnClickListener delListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            database.execSQL("delete from cards where cardName = \""+cardName+"\";");
            Toast.makeText(getContext(),"Delected: "+cardName,Toast.LENGTH_LONG).show();
            Cursor cursor = database.rawQuery("select * from cards;",null);
            ArrayList<String> list = new ArrayList<String>();
            while (cursor.moveToNext())
            {
                String name = cursor.getString(0);
                list.add(0,name);
            }
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(),android.R.layout.simple_spinner_item,list);
            delSpinner.setAdapter(adapter);
        }
    };
}
