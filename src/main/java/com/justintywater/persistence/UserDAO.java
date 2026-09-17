package com.justintywater.persistence;

import com.justintywater.domain.Transaction;

public interface UserDAO {
    void addUser(String accountId, String pin);

    boolean login(String accountId, String pin);

    void logout(String accountId, String sessionToken);

    double checkBalance(String accountId, String pin);

    void deposit(String accountId, String pin, double amount);

    void withdraw(String accountId, String pin, double amount);

    void transfer(String accountId, String pin, double amount, String recipientAccountId);

    Transaction[] getHistory(String accountId, String pin);
}
