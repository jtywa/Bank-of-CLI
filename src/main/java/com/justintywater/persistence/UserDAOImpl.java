package com.justintywater.persistence;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import com.justintywater.domain.Transaction;
import com.justintywater.domain.exception.LoginException;
import com.justintywater.domain.exception.TransferException;
import com.justintywater.domain.exception.AccountCreationException;
import com.justintywater.domain.exception.ConnectionException;

public class UserDAOImpl implements UserDAO {
    private static final String INSERT_SQL = "INSERT INTO bank.accounts (accountId, pin) VALUES (?, ?)";
    private static final String EXISTS_SQL = "SELECT 1 FROM bank.accounts WHERE accountId = ?";
    private static final String LOGIN_SQL = "SELECT 1 FROM bank.accounts WHERE accountId = ? AND pin = ?";
    private static final String BALANCE_SQL = "SELECT balance FROM bank.accounts WHERE accountId = ? AND pin = ?";
    private static final String DEPOSIT_SQL = "UPDATE bank.accounts SET balance = balance + ? WHERE accountId = ? AND pin = ? RETURNING balance";
    private static final String WITHDRAW_SQL = "UPDATE bank.accounts SET balance = balance - ? WHERE accountId = ? AND pin = ? RETURNING balance";
    private static final String CREDIT_SQL = "UPDATE bank.accounts SET balance = balance + ? WHERE accountId = ?";
    private static final String DEBIT_SQL = "UPDATE bank.accounts SET balance = balance - ? WHERE accountId = ? AND pin = ?";
    private static final String HISTORY_SQL = "SELECT t.transactionType, t.sender, t.recipient, t.amount FROM bank.transactions t INNER JOIN bank.accounts a ON (a.accountId = t.sender OR a.accountId = t.recipient) WHERE a.accountId = ? AND a.pin = ?";

    public boolean userExists(String accountId) {
        try (Connection connection = ConnectionFactory.getConnectionFactory().getConnection()) {

            try (PreparedStatement checkStatement = connection.prepareStatement(EXISTS_SQL)) {
                checkStatement.setString(1, accountId);
                try (ResultSet rs = checkStatement.executeQuery()) {
                    if (rs.next()) {
                        return true;
                    } else
                        return false;
                }
            }
        } catch (SQLException e) {
            throw new TransferException(e.getMessage());
        }
    }

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
            throw new ConnectionException("Could not connect to the database.");
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

    public double withdraw(String accountId, String pin, double amount) {
        try (Connection connection = ConnectionFactory.getConnectionFactory().getConnection();
                PreparedStatement statement = connection.prepareStatement(WITHDRAW_SQL)) {
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
        return 0.0; // remove
    };

    public void transfer(String accountId, String pin, double amount, String recipientAccountId) {
        try (Connection connection = ConnectionFactory.getConnectionFactory().getConnection();
            PreparedStatement debit = connection.prepareStatement(DEBIT_SQL);
            PreparedStatement credit = connection.prepareStatement(CREDIT_SQL);) 
        {
            connection.setAutoCommit(false);

            try {
                debit.setDouble(1, amount);
                debit.setString(2, accountId);
                debit.setString(3, pin);

                if (debit.executeUpdate() != 1){
                    throw new SQLException("Debit failed.");
                }

                credit.setDouble(1, amount);
                credit.setString(2, recipientAccountId);

                if (credit.executeUpdate() != 1){
                    throw new SQLException("Credit failed.");
                }

                connection.commit();
            } catch (SQLException e) {
                try {
                    connection.rollback();
                } catch (SQLException rollbackException) {
                    throw new TransferException("Rollback failed");
                }
                throw new TransferException(e.getMessage());
            }
        } catch (SQLException e){
            throw new TransferException("Failed to connect.");
        }
    };

    public Transaction[] getHistory(String accountId, String pin) {
        return new Transaction[] {}; // placeholder
    };
}
