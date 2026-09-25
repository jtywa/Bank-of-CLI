package com.justintywater.service;

import org.slf4j.LoggerFactory;
import java.util.List;
import com.justintywater.domain.Transaction;
import com.justintywater.persistence.UserDAO;
import com.justintywater.persistence.TransactionDAO;

import java.sql.SQLException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TransactionServiceImpl implements TransactionService {
    private final TransactionDAO repo;
    private static final Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);

    public TransactionServiceImpl(TransactionDAO repo) {
        this.repo = repo;
    }

    @Override
    public void addTransaction(String accountId, String type, double amount, String recipient) {
        repo.addTransaction(accountId, type, amount, recipient);
    }

    @Override
    public List<Transaction> getHistory(String accountId, String pin) {
        List<Transaction> list = repo.getHistory(accountId, pin);
        if (list == null) {
            return null;
        }
        logger.info("User {} checked their transaction history.", accountId);
        return list;
    }
}
