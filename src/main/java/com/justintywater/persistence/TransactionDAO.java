package com.justintywater.persistence;

import com.justintywater.domain.Transaction;
import java.util.List;

public interface TransactionDAO {

    void addTransaction(String accountId, String type, double amount, String recipient);

    List<Transaction> getHistory(String accountId, String pin);
}
