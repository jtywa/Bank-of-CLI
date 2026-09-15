package com.justintywater.domain;
import com.justintywater.domain.Transaction;

public class User {
    public User(int accountId, int pin) {
        this.accountId = accountId;
        this.pin = pin;
    }

    private int accountId;
    private int pin;
    private double balance;
    private Transaction[] history;
}
