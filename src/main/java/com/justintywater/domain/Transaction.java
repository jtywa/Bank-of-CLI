package com.justintywater.domain;

import java.sql.Timestamp;

public class Transaction {
    private String type;
    private double amount;
    private String recipient;
    private String sender;
    private Timestamp timestamp;

    public String getType() {
        return this.type;
    }

    public double getAmount() {
        return this.amount;
    }

    public String getRecipient() {
        return this.recipient;
    }

    public String getSender() {
        return this.sender;
    }

    public Timestamp getTimestamp() {
        return this.timestamp;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public void setRecipient(String recipient) {
        this.recipient = recipient;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public void setTimestamp(Timestamp timestamp) {
        this.timestamp = timestamp;
    }
}
