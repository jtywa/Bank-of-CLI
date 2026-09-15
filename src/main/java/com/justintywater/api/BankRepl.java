package com.justintywater.api;
import java.util.Scanner;
import com.justintywater.service.UserService;

public class BankRepl {
    private final UserService service;
    private final Scanner scanner = new Scanner(System.in);

    public BankRepl(UserService service){
        this.service = service;
    }

    public void run(){
        System.out.println("----------------------");
        System.out.println("Welcome to Bank of CLI");
        System.out.println("----------------------");
        System.out.println("Type 'help' for list of commands");

        while (true){
            System.out.print(">");
            String command = scanner.nextLine().trim();

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

    private void handle(String command) {
        switch (command) {
            case "help" -> printHelp();
            default -> System.out.println("Unknown command");
        }
    }

    private void printHelp(){
        System.out.println("Commands:");
        System.out.println("  login\n  logout\n  signup\n  balance\n  transfer\n  deposit\n  withdraw\n  history\n  exit");
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
