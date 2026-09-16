package com.justintywater.service;
import javax.naming.AuthenticationException;

import com.justintywater.domain.Transaction;
import com.justintywater.domain.exception.AccountCreationException;
import com.justintywater.domain.exception.LoginException;
import com.justintywater.repository.UserRepository;

public class UserServiceImpl implements UserService {
    private final UserRepository repo;

    public UserServiceImpl(UserRepository repo){
        this.repo = repo;
    }

    @Override
    public String login(String accountId, String pin){
        String sessionToken = repo.login(accountId, pin);
        if (sessionToken != null) return sessionToken;
        else throw new LoginException("Invalid credentials.");
    };

    @Override 
    public void logout(String accountId, String session){
        repo.logout(accountId, session);
    }

    @Override
    public void addUser(String accountId, String pin){
        //TODO: if user exists, return error
        StringBuffer errorString = new StringBuffer();
        if (accountId.length() > 12 || accountId.length() < 1){
            errorString.append("\n- Account ID must be between 1 and 12 characters");
        }
        if (!accountId.matches("^[a-zA-Z0-9]+$")){
            errorString.append("\n- Account ID must be alphanumeric");
        }
        if (pin.length() != 4){
            errorString.append("\n- Pin must be four digits");
        }
        if (!pin.matches("\\d+")){
            errorString.append("\n- Pin must be numeric");
        }
        if (errorString.length() > 0){
            throw new AccountCreationException("Invalid Format" + errorString);
        } else {
            repo.addUser(accountId, pin);
        }
    }

    @Override
    public double checkBalance(String accountId, String session){
        // TODO: currently assumes successful lookup
        return repo.checkBalance(accountId, session);
    };

    @Override
    public void deposit(String accountId, String session, double amount){
        if (amount > 0) 
            repo.deposit(accountId, session, amount);
        else throw new IllegalArgumentException("Deposit amount must be positive");
    };

    @Override
    public void withdraw(String accountId, String session, double amount){
        double balance = repo.checkBalance(accountId, session);
        if (amount < balance){
            repo.withdraw(accountId, session, amount);
        } else throw new IllegalArgumentException("Requested withdrawal amount exceeds available funds.");
    };

    @Override
    public void transfer(String accountId, String session, double amount, String recipientAccountId){

    };

    @Override
    public Transaction[] getHistory(String accountId, String session){
        return new Transaction[] {}; //placeholder
    };
}
