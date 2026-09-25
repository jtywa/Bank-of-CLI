package com.justintywater.api;

import com.justintywater.persistence.UserDAO;
import com.justintywater.persistence.UserDAOImpl;
import com.justintywater.service.TransactionService;
import com.justintywater.service.TransactionServiceImpl;
import com.justintywater.persistence.TransactionDAO;
import com.justintywater.persistence.TransactionDAOImpl;
import com.justintywater.service.UserService;
import com.justintywater.service.UserServiceImpl;

public class Main {
    public static void main(String[] args) {
        UserDAO userRepo = new UserDAOImpl();
        TransactionDAO transactionRepo = new TransactionDAOImpl();
        UserService userService = new UserServiceImpl(userRepo);
        TransactionService transactionService = new TransactionServiceImpl(transactionRepo);
        new BankRepl(userService, transactionService).run();
    }
}