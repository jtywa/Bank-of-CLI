package com.justintywater.api;
import com.justintywater.repository.UserRepository;
import com.justintywater.service.UserService;
import com.justintywater.repository.UserRepositoryImpl;
import com.justintywater.service.UserServiceImpl;

public class Main {
    public static void main(String[] args) {
        UserRepository repo = new UserRepositoryImpl();
        UserService service = new UserServiceImpl(repo);
        new BankRepl(service).run();
    }
}