package com.justintywater.persistence;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import com.justintywater.domain.Transaction;
import com.justintywater.domain.exception.LoginException;
import com.justintywater.domain.exception.AccountCreationException;

public class UserDAOImpl implements UserDAO {
    private static final String INSERT_SQL = "INSERT INTO bank.accounts (accountId, pin) VALUES (?, ?)";
    private static final String EXISTS_SQL = "SELECT 1 FROM bank.accounts WHERE accountId = ?";
    private static final String LOGIN_SQL = "SELECT 1 FROM bank.accounts WHERE accountId = ? AND pin = ?";
    private static final String BALANCE_SQL = "SELECT balance FROM bank.accounts WHERE accountId = ? AND pin = ?";
    private static final String DEPOSIT_SQL = "UPDATE bank.accounts SET balance = balance + ? WHERE accountId = ? AND pin = ? RETURNING balance";

    public void addUser(String accountId, String pin) {
        try (Connection connection = ConnectionFactory.getConnectionFactory().getConnection()) {

            try (PreparedStatement checkStatement = connection.prepareStatement(EXISTS_SQL)) {
                checkStatement.setString(1, accountId);
                try (ResultSet rs = checkStatement.executeQuery()) {
                    if (rs.next()) {
                        throw new AccountCreationException(
                                "Account with ID " + accountId + " already exists. Account creation failed.");
                    }
                }
            }

            try (PreparedStatement insertStatement = connection.prepareStatement(INSERT_SQL)) {
                insertStatement.setString(1, accountId);
                insertStatement.setString(2, pin);
                insertStatement.executeUpdate();
            }

        } catch (SQLException e) {
            throw new AccountCreationException(e.getMessage());
        }
    }

    public boolean login(String accountId, String pin) {
        try (Connection connection = ConnectionFactory.getConnectionFactory().getConnection();
                PreparedStatement statement = connection.prepareStatement(LOGIN_SQL)) {
            statement.setString(1, accountId);
            statement.setString(2, pin);

            try (ResultSet rs = statement.executeQuery()) {
                return rs.next();
            }
        } catch (SQLException e) {
            throw new LoginException(e.getMessage());
        }
    };

    public void logout(String accountId, String pin) {
        // TODO: Remove logout from service and data layers
    };

    public double checkBalance(String accountId, String pin) {
        try (Connection connection = ConnectionFactory.getConnectionFactory().getConnection();
                PreparedStatement statement = connection.prepareStatement(BALANCE_SQL)) {
            statement.setString(1, accountId);
            statement.setString(2, pin);

            try (ResultSet rs = statement.executeQuery()) {
                if (rs.next()) {
                return rs.getDouble(1);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e.getMessage());
        }
        return 0.0; // TODO: change this
    };

    public double deposit(String accountId, String pin, double amount) {
        try (Connection connection = ConnectionFactory.getConnectionFactory().getConnection();
                PreparedStatement statement = connection.prepareStatement(DEPOSIT_SQL)) {
            statement.setDouble(1, amount);
            statement.setString(2, accountId);
            statement.setString(3, pin);

            try (ResultSet rs = statement.executeQuery()) {
                if (rs.next()) {
                return rs.getDouble(1);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e.getMessage());
        }
        return 0.0; // TODO: change this
    };

    public void withdraw(String accountId, String pin, double amount) {

    };

    public void transfer(String accountId, String pin, double amount, String recipientAccountId) {

    };

    public Transaction[] getHistory(String accountId, String pin) {
        return new Transaction[] {}; // placeholder
    };
}
