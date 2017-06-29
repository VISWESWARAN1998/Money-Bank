package com.shiva.moneybank;

/**
 * Created by Visweswaran on 28-06-2017.
 */

public class Balance
{
    String name,balance;

    public Balance(String name, String balance) {
        this.name = name;
        this.balance = balance;
    }

    public String getName() {
        return name;
    }


    public String getBalance() {
        return balance;
    }
}
