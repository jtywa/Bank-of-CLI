package com.justintywater.repository;

import com.justintywater.domain.Transaction;

public class UserRepositoryImpl implements UserRepository {
    public void addUser(String accountId, String pin){

    };
    public String login(String accountId, String pin){
        // search for row with username and password
        // if found, update sessionToken column with UUID
        // return that UUID
        return "1234";
    };
    public double checkBalance(String accountId, String pin){
        return 0.0; //placeholder
    };
    public void deposit(String accountId, String pin, double amount){
        
    };
    public void withdraw(String accountId, String pin, double amount){
        
    };
    public void transfer(String accountId, String pin, double amount, String recipientAccountId){
        
    };
    public Transaction[] getHistory(String accountId, String pin){
        return new Transaction[] {}; //placeholder
    };
}
