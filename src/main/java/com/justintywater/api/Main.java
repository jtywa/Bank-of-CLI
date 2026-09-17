package com.justintywater.api;
import com.justintywater.persistence.UserDAO;
import com.justintywater.persistence.UserDAOImpl;
import com.justintywater.service.UserService;
import com.justintywater.service.UserServiceImpl;

public class Main {
    public static void main(String[] args) {
        UserDAO repo = new UserDAOImpl();
        UserService service = new UserServiceImpl(repo);
        new BankRepl(service).run();
    }
}