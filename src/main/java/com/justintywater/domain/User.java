package com.justintywater.domain;
import com.justintywater.domain.Transaction;

public class User {
    public User(int accountId, int pin, double balance, Transaction[] history) {
        this.accountId = accountId;
        this.pin = pin;
        this.balance = balance;
        this.history = history;
    }

    private int accountId;
    private int pin;
    private double balance;
    private Transaction[] history;
}
