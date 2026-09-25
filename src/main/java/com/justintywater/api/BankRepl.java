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

    public static final String RESET = "\u001B[0m";
    public static final String GREEN = "\u001B[32m";
    public static final String RED = "\u001B[31m";
    public static final String BLUE = "\u001B[34m";
    public static final String CYAN = "\u001B[36m";

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
        System.out.println(RESET + "Commands:");
        System.out.println(
                "  login\n  logout\n  signup\n  balance\n  transfer\n  deposit\n  withdraw\n  history\n  exit");
    }

    private void login() {
        if (loggedIn()) {
            printError("Already logged in as " + user);
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
            printSuccess("Logged in as " + RESET + acc + GREEN + ".");

        } catch (LoginException e) {
            printError(e.getMessage());
        }
    }

    private void logout() {
        if (loggedIn()) {
            this.user = null;
            this.pin = null;
            printSuccess("Logged out.");
        } else {
            printError("Must be logged in to log out.");
        }
    }

    private void signup() {
        String acc = readString("Choose an account ID (alphanumeric, 1-12 characters): ");
        String pin = readString("Choose a pin (numeric, 4 digits): ");
        try {
            userService.addUser(acc, pin);
            printSuccess("Account created! Please log in using your new credentials.");
        } catch (AccountCreationException e) {
            printError(e.getMessage());
        }
    }

    private void balance() {
        if (!loggedIn()) {
            printError("Must be logged in to view balance.");
        } else {
            System.out.printf(RESET + "Balance: $%.2f%n", userService.checkBalance(user, pin));
        }
    }

    private void transfer() {
        if (!loggedIn()) {
            printError("Must be logged in to make a transfer.");
            return;
        }
        String recipient = readString("Enter account to transfer to: ");
        double amount = readDouble("Enter amount to transfer: ");
        try {
            userService.transfer(user, pin, amount, recipient);
            System.out.printf("%sSuccessfully transferred %s$%.2f%s to %s%s%s.%n%s", GREEN, RESET, amount, GREEN, RESET,
                    recipient, GREEN, RESET);
            transactionService.addTransaction(user, "Transfer", amount, recipient);
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
            System.out.printf("%sSuccessfully deposited %s$%.2f%s. New balance: %s$%.2f%s%n", GREEN, RESET, amount,
                    GREEN, RESET, newBalance, RESET);
            transactionService.addTransaction(null, "Deposit", amount, user);
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
            System.out.printf("%sSuccessfully deposited %s$%.2f.%s New balance: %s$%.2f%n", GREEN, RESET, amount,
                    GREEN, RESET, newBalance);
            transactionService.addTransaction(user, "Withdrawal", amount, null);
        } catch (IllegalArgumentException e) {
            printError(e.getMessage());
        }
    }

    private void history() {
        if (!loggedIn()) {
            printError("Must be logged in to view recent transactions.");
            return;
        }
        List<Transaction> list = transactionService.getHistory(user, pin);
        if (list == null) {
            System.out.println(RESET + "No transactions found.");
            return;
        }
        for (Transaction t : list) {
            System.out.print(RESET + t.getTimestamp() + " -- ");
            System.out.print(t.getType());
            System.out.printf(" of amount $%.2f", t.getAmount());
            if (t.getSender() != null)
                System.out.print(" from " + CYAN + t.getSender() + RESET);
            if (t.getRecipient() != null)
                System.out.print(" to " + CYAN + t.getRecipient() + RESET);

            System.out.println("");
        }
    }

    // ----------------------------
    // Helper Methods
    // ----------------------------

    private String readString(String prompt) {
        System.out.print(RESET + prompt + CYAN);
        return scanner.nextLine().trim();
    }

    private double readDouble(String prompt) {
        System.out.print(RESET + prompt + CYAN);
        return Double.parseDouble(scanner.nextLine().trim());
    }

    private void printError(String message) {
        System.out.println(RED + "Error: " + message + RESET);
    };

    private void printSuccess(String message) {
        System.out.println(GREEN + message + RESET);
    };

    private boolean loggedIn() {
        return (this.pin != null && this.user != null);
    }
}
