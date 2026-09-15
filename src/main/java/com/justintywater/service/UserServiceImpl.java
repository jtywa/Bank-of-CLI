package com.justintywater.service;
import com.justintywater.domain.Transaction;
import com.justintywater.repository.UserRepository;

public class UserServiceImpl implements UserService {
    private final UserRepository repo;

    public UserServiceImpl(UserRepository repo){
        this.repo = repo;
    }

    @Override
    public void addUser(String accountId, String pin){

    };

    @Override
    public void login(String accountId, String pin){

    };

    @Override
    public double checkBalance(String accountId, String pin){
        return 0.0; //placeholder
    };

    @Override
    public void deposit(String accountId, String pin, double amount){

    };

    @Override
    public void withdraw(String accountId, String pin, double amount){

    };

    @Override
    public void transfer(String accountId, String pin, double amount, String recipientAccountId){

    };

    @Override
    public Transaction[] getHistory(String accountId, String pin){
        return new Transaction[] {}; //placeholder
    };
}
