package com.justintywater.api;

import java.util.List;
import java.util.Scanner;
import com.justintywater.service.UserService;
import com.justintywater.domain.exception.LoginException;
import com.justintywater.domain.Transaction;
import com.justintywater.domain.exception.AccountCreationException;
import com.justintywater.domain.exception.TransferException;
import com.justintywater.service.TransactionService;

public class BankRepl {
    private final UserService userService;
    private final TransactionService transactionService;
    private final Scanner scanner = new Scanner(System.in);

    private String pin;
    private String user;

    public BankRepl(UserService userService, TransactionService transactionService) {
        this.userService = userService;
        this.transactionService = transactionService;
    }

    public void run() {
        System.out.println("----------------------");
        System.out.println("Welcome to Bank of CLI");
        System.out.println("----------------------");
        System.out.println("Type 'help' for list of commands");

        while (true) {
            String command = readString("> ");

            if (command.equals("exit")) {
                return;
            }

            try {
                handle(command);
            } catch (IllegalArgumentException e) {
                printError(e.getMessage());
            }
        }
    }

    private void handle(String command) {
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

    // -----------------------------
    // Command Handlers
    // -----------------------------

    private void printHelp() {
        System.out.println("Commands:");
        System.out.println(
                "  login\n  logout\n  signup\n  balance\n  transfer\n  deposit\n  withdraw\n  history\n  exit");
    }

    private void login() {
        if (loggedIn()) {
            System.out.println("Already logged in as " + user);
            return;
        }

        String acc = readString("Account ID: ");
        String pin = readString("Pin: ");

        try {
            boolean loggedIn = userService.login(acc, pin);
            if (loggedIn) {
                this.user = acc;
                this.pin = pin;
            }
            System.out.println("Logged in as " + acc + ".");

        } catch (LoginException e) {
            printError(e.getMessage());
        }
    }

    private void logout() {
        if (loggedIn()) {
            this.user = null;
            this.pin = null;
            System.out.println("Logged out.");
        } else {
            printError("Must be logged in to log out.");
        }
    }

    private void signup() {
        String acc = readString("Choose an account ID (alphanumeric, 1-12 characters): ");
        String pin = readString("Choose a pin (numeric, 4 digits): ");
        try {
            userService.addUser(acc, pin);
            System.out.println("Account created! Please log in using your new credentials.");
        } catch (AccountCreationException e) {
            printError(e.getMessage());
        }
    }

    private void balance() {
        if (!loggedIn()) {
            printError("Must be logged in to view balance.");
        } else {
            System.out.printf("Balance: $%.2f%n", userService.checkBalance(user, pin));
        }
    }

    private void transfer() {
        String recipient = readString("Enter account to transfer to: ");
        double amount = readDouble("Enter amount to transfer: ");
        try {
            userService.transfer(user, pin, amount, recipient);
            System.out.printf("Successfully transfered $%.2f to %s.%n", amount, recipient);
        } catch (TransferException e) {
            printError(e.getMessage());
        }
    }

    private void deposit() {
        if (!loggedIn()) {
            printError("Must be logged in to make a deposit.");
            return;
        }
        double amount = readDouble("Enter amount to deposit: ");
        try {
            double newBalance = userService.deposit(user, pin, amount);
            System.out.printf("Successfully deposited $%.2f. New balance: $%.2f%n", amount, newBalance);
        } catch (IllegalArgumentException e) {
            printError(e.getMessage());
        }
    }

    private void withdraw() {
        if (!loggedIn()) {
            printError("Must be logged in to make a withdrawal.");
            return;
        }
        double amount = readDouble("Enter amount to withdraw: ");
        try {
            double newBalance = userService.withdraw(user, pin, amount);
            System.out.printf("Successfully deposited $%.2f. New balance: $%.2f%n", amount, newBalance);
        } catch (IllegalArgumentException e) {
            printError(e.getMessage());
        }
    }

    private void history() {
        List<Transaction> list = transactionService.getHistory(user, pin);
        if (list == null) {
            System.out.println("No transactions found.");
            return;
        }
        for (Transaction t : list) {
            System.out.print(t.getType() + " of amount " + t.getAmount() + " ");
            if (t.getSender() != null)
                System.out.print("from " + t.getSender());
            if (t.getRecipient() != null)
                System.out.print("to " + t.getRecipient());
        }
    }

    // ----------------------------
    // Helper Methods
    // ----------------------------

    private String readString(String prompt) {
        System.out.print(prompt);
        return scanner.nextLine().trim();
    }

    private double readDouble(String prompt) {
        System.out.print(prompt);
        return Double.parseDouble(scanner.nextLine().trim());
    }

    private void printError(String message) {
        System.out.println("Error: " + message);
    };

    private boolean loggedIn() {
        return (this.pin != null && this.user != null);
    }
}
