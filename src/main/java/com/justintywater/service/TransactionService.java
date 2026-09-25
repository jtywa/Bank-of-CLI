package com.justintywater.service;

import com.justintywater.domain.Transaction;
import java.util.List;

public interface TransactionService {
    void addTransaction(String accountId, String type, double amount, String recipient);

    List<Transaction> getHistory(String accountId, String pin);
}
