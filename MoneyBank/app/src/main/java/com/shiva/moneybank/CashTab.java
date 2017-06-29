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

/**
 * Created by Visweswaran on 28-06-2017.
 */

//        listView = (ListView)view.findViewById(R.id.cashTabList);
//                ArrayList<String> list = new ArrayList<>();
//                list.add("sample1");
//                list.add("sample2");
//                ArrayAdapter<String> adapter = new ArrayAdapter<String>(view.getContext(),android.R.layout.simple_list_item_1,list);
//                listView.setAdapter(adapter);

public class CashTab extends Fragment {
    private ListView listView;
    List<Cash> cashTabList = new ArrayList<Cash>();
    private SQLiteDatabase database;
    private View view;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.cashtab,container,false);
        listView = (ListView)view.findViewById(R.id.cashTabList);
        database = getContext().openOrCreateDatabase("bank.db", Context.MODE_PRIVATE,null);
        addItems();
        generate();
        return view;
    }

    private void addItems() {
        Cursor cur = database.rawQuery("select * from user;",null);
        cur.moveToFirst();
        String currency = cur.getString(1);
        Cursor cursor = database.rawQuery("select * from history where name = \"cash\";",null);
        while (cursor.moveToNext())
        {
            String amount = cursor.getString(1)+" "+currency;
            String status = cursor.getString(2);
            String date = cursor.getString(3)+"."+cursor.getString(4)+"."+cursor.getString(5);
            cashTabList.add(0,new Cash(amount,status,date));
        }
    }

    private void generate()
    {
        ArrayAdapter<Cash> adapter = new CashView();
        listView.setAdapter(adapter);
    }

    private class CashView extends ArrayAdapter<Cash>
    {
        CashView()
        {
            super(view.getContext(),R.layout.cashtab,cashTabList);
        }

        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            View view1 = convertView;
            if(view1==null)
            {
                view1 = getActivity().getLayoutInflater().inflate(R.layout.list,parent,false);
            }
            Cash currentCash = cashTabList.get(position);

            TextView cash = (TextView) view1.findViewById(R.id.NAME);
            cash.setText(currentCash.getAmount());

            TextView status = (TextView) view1.findViewById(R.id.STATUS);
            status.setText(currentCash.getStatus());

            TextView date = (TextView) view1.findViewById(R.id.DATE);
            date.setText(currentCash.getDate());
            return view1;
            //return super.getView(position, convertView, parent);
        }
    }
}
