package com.justintywater.service;
import com.justintywater.domain.Transaction;
import com.justintywater.domain.exception.LoginException;
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
    public String login(String accountId, String pin){
        String sessionToken = repo.login(accountId, pin);
        if (sessionToken != null) return sessionToken;
        else throw new LoginException("Invalid credentials.");
    };

    @Override 
    public void logout(String accountId, String session){
        
    }

    @Override
    public double checkBalance(String accountId, String session){
        return 0.0; //placeholder
    };

    @Override
    public void deposit(String accountId, String session, double amount){

    };

    @Override
    public void withdraw(String accountId, String session, double amount){

    };

    @Override
    public void transfer(String accountId, String session, double amount, String recipientAccountId){

    };

    @Override
    public Transaction[] getHistory(String accountId, String session){
        return new Transaction[] {}; //placeholder
    };
}
