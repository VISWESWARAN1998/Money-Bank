package com.shiva.moneybank;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import static com.shiva.moneybank.R.layout.list1;


/**
 * Created by Visweswaran on 29-06-2017.
 */

public class EventsTab extends Fragment {
    private View view;
    private ListView listView;
    private SQLiteDatabase database;
    private List<EventDisplay> list;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.eventstab,container,false);
        //return super.onCreateView(inflater, container, savedInstanceState);
        listView = (ListView)view.findViewById(R.id.eventTabsList);
        database = getContext().openOrCreateDatabase("bank.db", Context.MODE_PRIVATE,null);
        list = new ArrayList<EventDisplay>();
        new AddItems().execute();
        return view;
    }

    private class AddItems extends AsyncTask<Void,Void,Void>
    {
        @Override
        protected Void doInBackground(Void... params) {
            Cursor cursorx = database.rawQuery("select * from user",null);
            cursorx.moveToFirst();
            //cursorx.close();
            String currency = cursorx.getString(1);
            Cursor cursor = database.rawQuery("select * from events;",null);
            while(cursor.moveToNext())
            {
                String date = cursor.getString(0)+"."+cursor.getString(1)+"."+cursor.getString(2);
                String name = cursor.getString(3);
                String amount = cursor.getString(4)+" "+currency;
                Integer action = cursor.getInt(5) == 0 ? R.drawable.event1 : R.drawable.event2;
                list.add(0,new EventDisplay(name,amount,date,action));
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            ArrayAdapter<EventDisplay> adapter = new Generate();
            listView.setAdapter(adapter);
        }
    }

    private class Generate extends ArrayAdapter<EventDisplay>
    {

        public Generate() {
            super(view.getContext(),R.layout.eventstab,list);
        }

        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            View view1 = convertView;
            if(view1==null) view1 = getActivity().getLayoutInflater().inflate(list1,parent,false);
            EventDisplay currentEvent = list.get(position);
            TextView name = (TextView)view1.findViewById(R.id.NAME1);
            name.setText(currentEvent.getName());
            TextView amount = (TextView)view1.findViewById(R.id.STATUS1);
            amount.setText(currentEvent.getAmount());
            TextView date = (TextView)view1.findViewById(R.id.DATE1);
            date.setText(currentEvent.getDate());
            ImageView image = (ImageView)view1.findViewById(R.id.IMAGE1);
            image.setImageResource(currentEvent.getId());
            return view1;
            //return super.getView(position, convertView, parent);
        }
    }

}
