package com.justintywater.service;

import com.justintywater.domain.Transaction;
import com.justintywater.domain.exception.AccountCreationException;
import com.justintywater.domain.exception.LoginException;
import com.justintywater.domain.exception.TransferException;
import com.justintywater.persistence.UserDAO;

public class UserServiceImpl implements UserService {
    private final UserDAO repo;
    private final int MAX_ID_LENGTH = 12;
    private final int MIN_ID_LENGTH = 3;
    private final int PIN_LENGTH = 4;

    public UserServiceImpl(UserDAO repo) {
        this.repo = repo;
    }

    @Override
    public boolean login(String accountId, String pin) {
        boolean loggedIn = repo.login(accountId, pin);
        if (loggedIn)
            return true;
        else
            throw new LoginException("Invalid credentials.");
    };

    @Override
    public void logout(String accountId, String pin) {
        repo.logout(accountId, pin);
    }

    @Override
    public void addUser(String accountId, String pin) {
        // TODO: if user exists, return error
        StringBuffer errorString = new StringBuffer();
        if (!isValidLength(accountId)) {
            errorString.append("\n- Account ID must be between 3 and 12 characters");
        }
        if (!accountId.matches("^[a-zA-Z0-9]+$")) {
            errorString.append("\n- Account ID must be alphanumeric");
        }
        if (pin.length() != PIN_LENGTH) {
            errorString.append("\n- Pin must be four digits");
        }
        if (!pin.matches("\\d+")) {
            errorString.append("\n- Pin must be numeric");
        }
        if (errorString.length() > 0) {
            throw new AccountCreationException("Invalid Format" + errorString);
        } else {
            repo.addUser(accountId, pin);
        }
    }

    @Override
    public double checkBalance(String accountId, String pin) {
        // TODO: currently assumes successful lookup
        return repo.checkBalance(accountId, pin);
    };

    @Override
    public double deposit(String accountId, String pin, double amount) {
        if (amount > 0)
            return repo.deposit(accountId, pin, amount);
        else
            throw new IllegalArgumentException("Deposit amount must be positive");
    };

    @Override
    public double withdraw(String accountId, String pin, double amount) {
        double balance = repo.checkBalance(accountId, pin);
        if (amount < balance) {
            return repo.withdraw(accountId, pin, amount);
        } else
            throw new IllegalArgumentException("Requested withdrawal amount exceeds available funds.");
    };

    @Override
    public void transfer(String accountId, String pin, double amount, String recipientAccountId) {
        double balance = repo.checkBalance(accountId, pin);
        if (!repo.userExists(recipientAccountId)){
            throw new TransferException("The account you would like to transfer to does not exist.");
        }
        if (amount > balance){
            throw new TransferException("Insufficient funds for this transfer.");
        }
        repo.transfer(accountId, pin, amount, recipientAccountId);
    };

    @Override
    public Transaction[] getHistory(String accountId, String pin) {
        return new Transaction[] {}; // placeholder
    };

    // ---------------------
    // Helper Methods
    // ---------------------

    private boolean isValidLength(String acc) {
        int length = acc.length();
        return (length <= MAX_ID_LENGTH && length >= MIN_ID_LENGTH);
    }
}
