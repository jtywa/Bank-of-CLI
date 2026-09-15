package com.justintywater.repository;

import com.justintywater.domain.Transaction;

public class UserRepositoryImpl implements UserRepository {
    public void addUser(String accountId, String pin){

    };
    public void login(String accountId, String pin){
        
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
