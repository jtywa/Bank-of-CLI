package com.justintywater.domain;

public class Transaction {
    private String type;
    private String amount;
    private String recipient;
    private String sender;

    public String getType(){
        return this.type;
    }

    public String getAmount(){
        return this.amount;
    }

    public String getRecipient(){
        return this.recipient;
    }

    public String getSender(){
        return this.sender;
    }
}
