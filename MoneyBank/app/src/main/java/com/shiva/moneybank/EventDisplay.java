package com.shiva.moneybank;

/**
 * Created by Visweswaran on 29-06-2017.
 */

public class EventDisplay
{
    String name,amount,date;
    int id;

    public EventDisplay(String name, String amount, String date, int id) {
        this.name = name;
        this.amount = amount;
        this.date = date;
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public String getAmount() {
        return amount;
    }

    public String getDate() {
        return date;
    }

    public int getId() {
        return id;
    }
}
