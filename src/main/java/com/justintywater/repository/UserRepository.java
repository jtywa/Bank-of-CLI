package com.justintywater.repository;

import com.justintywater.domain.Transaction;

public interface UserRepository {
    void addUser(String accountId, String pin);
    void login(String accountId, String pin);
    double checkBalance(String accountId, String pin);
    void deposit(String accountId, String pin, double amount);
    void withdraw(String accountId, String pin, double amount);
    void transfer(String accountId, String pin, double amount, String recipientAccountId);
    Transaction[] getHistory(String accountId, String pin);
}
