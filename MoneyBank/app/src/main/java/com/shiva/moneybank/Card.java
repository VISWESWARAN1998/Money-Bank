package com.shiva.moneybank;

/**
 * Created by Visweswaran on 28-06-2017.
 */

public class Card
{
    String status,amount,date;

    public Card(String amount, String status, String date) {
        this.status = status;
        this.amount = amount;
        this.date = date;
    }

    public String getStatus() {
        return status;
    }

    public String getAmount() {
        return amount;
    }

    public String getDate() {
        return date;
    }
}
