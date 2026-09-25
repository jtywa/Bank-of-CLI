package com.justintywater.persistence;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.List;
import java.util.ArrayList;

import com.justintywater.domain.Transaction;

public class TransactionDAOImpl implements TransactionDAO {
    private static final String ADD_TRANSACTION_SQL = "INSERT INTO bank.transactions (sender, type, amount, recipient, timestamp) VALUES (?, ?, ?, ?, ?)";
    private static final String HISTORY_SQL = """
            SELECT t.transactionType, t.sender, t.recipient, t.amount, t.timestamp \
            FROM bank.transactions t \
            INNER JOIN bank.accounts a \
            ON (a.accountId = t.sender OR a.accountId = t.recipient) \
            WHERE a.accountId = ? AND a.pin = ? \
            ORDER BY t.timestamp DESC \
            LIMIT 10 \
            """;

    @Override
    public void addTransaction(String accountId, String type, double amount, String recipient) {
        try (Connection connection = ConnectionFactory.getConnectionFactory().getConnection();
                PreparedStatement statement = connection.prepareStatement(ADD_TRANSACTION_SQL)) {
            Timestamp timestamp = new Timestamp(System.currentTimeMillis());

            statement.setString(1, accountId);
            statement.setString(2, type);
            statement.setDouble(3, amount);
            statement.setString(4, recipient);
            statement.setTimestamp(5, timestamp);

            statement.executeUpdate();

        } catch (SQLException e) {

        }
    }

    public List<Transaction> getHistory(String accountId, String pin) {
        try (Connection connection = ConnectionFactory.getConnectionFactory().getConnection();
                PreparedStatement statement = connection.prepareStatement(HISTORY_SQL)) {
            statement.setString(1, accountId);
            statement.setString(2, pin);

            List<Transaction> transactionList = new ArrayList<>();

            try (ResultSet rs = statement.executeQuery()) {
                while (rs.next()) {
                    Transaction t = new Transaction();
                    t.setType(rs.getString("transactionType"));
                    t.setSender(rs.getString("sender"));
                    t.setRecipient(rs.getString("recipient"));
                    t.setAmount(rs.getDouble("amount"));
                    t.setTimestamp(rs.getTimestamp("timestamp"));

                    transactionList.add(t);
                }
                return transactionList;
            } catch (SQLException e) {
                throw new SQLException("Failed to get transaction history.");
            }

        } catch (SQLException e) {
            return null;
        }
    }
}
