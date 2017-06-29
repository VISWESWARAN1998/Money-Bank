package com.shiva.moneybank;

/**
 * Created by Visweswaran on 28-06-2017.
 */

public class Cash
{
    String status,amount,date;

    public Cash(String amount, String status, String date) {
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
