package com.justintywater.api;
import java.util.Scanner;
import com.justintywater.service.UserService;
import com.justintywater.domain.exception.LoginException;
import com.justintywater.domain.exception.AccountCreationException;

public class BankRepl {
    private final UserService service;
    private final Scanner scanner = new Scanner(System.in);

    private String sessionToken;
    private String user;

    public BankRepl(UserService service){
        this.service = service;
    }

    public void run(){
        System.out.println("----------------------");
        System.out.println("Welcome to Bank of CLI");
        System.out.println("----------------------");
        System.out.println("Type 'help' for list of commands");

        while (true){
            String command = readString("> ");

            if (command.equals("exit")) {
                return;
            }
            
            try {
                handle(command);
            } catch (IllegalArgumentException e){
                System.out.println("Error: " + e.getMessage());
            }
        }
    }

    private void handle(String command){
        switch (command) {
            case "help" -> printHelp();
            case "login" -> login();
            case "logout" -> logout();
            case "signup" -> signup();
            case "balance" -> balance();
            case "transfer" -> transfer();
            case "deposit" -> deposit();
            case "withdraw" -> withdraw();
            case "history" -> history();
            default -> throw new IllegalArgumentException("Invalid command.");
        }
    }

    private void printHelp(){
        System.out.println("Commands:");
        System.out.println("  login\n  logout\n  signup\n  balance\n  transfer\n  deposit\n  withdraw\n  history\n  exit");
    }

    private void login(){ // TODO: generate actual session token from repo
        String acc = readString("Account ID: ");
        String pin = readString("Pin: ");

        try {
            this.sessionToken = service.login(acc, pin);
            this.user = acc;
            System.out.println("Logged in as " + acc + ".");

        } catch (LoginException e){
            System.out.println("Error: " + e.getMessage());
        }
    }

    private void logout(){
        if (this.sessionToken != null){
            service.logout(user, sessionToken);
        } else {
            System.out.println("You cannot log out because you are not logged in.");
        }
    }

    private void signup(){
        String acc = readString("Choose an account ID (alphanumeric, 1-12 characters): ");
        String pin = readString("Choose a pin (numeric, 4 digits): ");
        try {
            service.signup(acc, pin);
            System.out.println("Account created! Please log in using your new credentials.");
        } catch (AccountCreationException e){
            System.out.println("Error: " + e.getMessage());
        }
    }

    private void balance(){

    }

    private void transfer(){

    }

    private void deposit(){

    }

    private void withdraw(){

    }

    private void history(){

    }

    private String readString(String prompt) {
        System.out.print(prompt);
        return scanner.nextLine().trim();
    }

    private int readInt(String prompt) {
        System.out.print(prompt);
        return Integer.parseInt(scanner.nextLine().trim());
    }

    private double readDouble(String prompt) {
        System.out.print(prompt);
        return Double.parseDouble(scanner.nextLine().trim());
    }
}
