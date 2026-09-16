package com.justintywater.service;
import com.justintywater.domain.Transaction;

public interface UserService {
    void addUser(String accountId, String pin);
    String login(String accountId, String pin);
    void logout(String accountId, String session);
    double checkBalance(String accountId, String session);
    void deposit(String accountId, String session, double amount);
    void withdraw(String accountId, String session, double amount);
    void transfer(String accountId, String session, double amount, String recipientAccountId);
    Transaction[] getHistory(String accountId, String session);
}
