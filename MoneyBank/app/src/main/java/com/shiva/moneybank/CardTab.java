package com.shiva.moneybank;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import static com.shiva.moneybank.R.id.cardTabListx;

/**
 * Created by Visweswaran on 28-06-2017.
 */

public class CardTab extends Fragment {
    private ListView listView;
    private List<Card> cardTabList = new ArrayList<Card>();
    private SQLiteDatabase database;
    private View view;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.cardtab,container,false);
        listView = (ListView)view.findViewById(cardTabListx);
        database = getContext().openOrCreateDatabase("bank.db", Context.MODE_PRIVATE,null);
        addItems();
        generate();
        return view;
    }

    private void addItems() {
        Cursor cur = database.rawQuery("select * from user;",null);
        cur.moveToFirst();
        String currency = cur.getString(1);
        Cursor cursor = database.rawQuery("select * from cardHistory;",null);
        while (cursor.moveToNext())
        {
            String amount = cursor.getString(1);
            String status = cursor.getString(3)+": "+cursor.getString(2)+" "+currency;
            String date = cursor.getString(4)+"."+cursor.getString(5)+"."+cursor.getString(6);
            cardTabList.add(0,new Card(amount,status,date));
        }
    }

    private void generate()
    {
        ArrayAdapter<Card> adapter = new CardView();
        listView.setAdapter(adapter);
    }

    private class CardView extends ArrayAdapter<Card>
    {
        CardView()
        {
            super(view.getContext(),R.layout.cardtab,cardTabList);
        }

        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            View view1 = convertView;
            if(view1==null)
            {
                view1 = getActivity().getLayoutInflater().inflate(R.layout.list1,parent,false);
            }
            Card currentCash = cardTabList.get(position);

            TextView cash = (TextView) view1.findViewById(R.id.NAME1);
            cash.setText(currentCash.getAmount());

            TextView status = (TextView) view1.findViewById(R.id.STATUS1);
            status.setText(currentCash.getStatus());

            TextView date = (TextView) view1.findViewById(R.id.DATE1);
            date.setText(currentCash.getDate());
            return view1;
            //return super.getView(position, convertView, parent);
        }
    }
}
