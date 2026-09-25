package com.justintywater.persistence;

public interface UserDAO {

    boolean userExists(String accountId);

    void addUser(String accountId, String pin);

    boolean login(String accountId, String pin);

    void logout(String accountId, String pin);

    double checkBalance(String accountId, String pin);

    double deposit(String accountId, String pin, double amount);

    double withdraw(String accountId, String pin, double amount);

    void transfer(String accountId, String pin, double amount, String recipientAccountId);

}
