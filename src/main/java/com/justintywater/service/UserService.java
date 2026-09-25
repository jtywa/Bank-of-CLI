package com.justintywater.service;

public interface UserService {
    boolean login(String accountId, String pin);

    void logout(String accountId, String pin);

    void addUser(String accountId, String pin);

    double checkBalance(String accountId, String pin);

    double deposit(String accountId, String pin, double amount);

    double withdraw(String accountId, String pin, double amount);

    void transfer(String accountId, String pin, double amount, String recipientAccountId);

}
