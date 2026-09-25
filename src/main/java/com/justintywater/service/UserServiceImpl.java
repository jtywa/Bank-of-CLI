package com.justintywater.service;

import com.justintywater.domain.Transaction;
import com.justintywater.domain.exception.AccountCreationException;
import com.justintywater.domain.exception.LoginException;
import com.justintywater.domain.exception.TransferException;
import com.justintywater.persistence.UserDAO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.justintywater.domain.exception.ConnectionException;

public class UserServiceImpl implements UserService {
    private final UserDAO repo;
    private final int MAX_ID_LENGTH = 12;
    private final int MIN_ID_LENGTH = 3;
    private final int PIN_LENGTH = 4;
    private static final Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);

    public UserServiceImpl(UserDAO repo) {
        this.repo = repo;
    }

    @Override
    public boolean login(String accountId, String pin) {
        try {
            boolean loggedIn = repo.login(accountId, pin);
            if (loggedIn) {
                logger.info("User {} logged in.", accountId);
                return true;
            } else
                logger.warn("User attempted to sign in with invalid credentials.");
            throw new LoginException("Invalid credentials.");
        } catch (ConnectionException e) {
            return false;
        }
    }

    @Override
    public void logout(String accountId, String pin) {
        logger.info("User {} logged out.", accountId);
        repo.logout(accountId, pin);
    }

    @Override
    public void addUser(String accountId, String pin) {
        // TODO: if user exists, return error
        StringBuffer errorString = new StringBuffer();
        if (!isValidLength(accountId)) {
            errorString.append("\n- Account ID must be between 3 and 12 characters");
            logger.warn("User attempted to create an account with invalid account ID.");
        }
        if (!accountId.matches("^[a-zA-Z0-9]+$")) {
            errorString.append("\n- Account ID must be alphanumeric");
            logger.warn("User attempted to create an account with invalid account ID.");
        }
        if (pin.length() != PIN_LENGTH) {
            errorString.append("\n- Pin must be four digits");
            logger.warn("User attempted to create an account with invalid pin.");
        }
        if (!pin.matches("\\d+")) {
            errorString.append("\n- Pin must be numeric");
            logger.warn("User attempted to create an account with invalid pin.");
        }
        if (errorString.length() > 0) {
            throw new AccountCreationException("Invalid Format" + errorString);
        } else {
            repo.addUser(accountId, pin);
            return;
        }
    }

    @Override
    public double checkBalance(String accountId, String pin) {
        // TODO: currently assumes successful lookup
        logger.info("User {} checked their account balance.", accountId);
        return repo.checkBalance(accountId, pin);
    };

    @Override
    public double deposit(String accountId, String pin, double amount) {
        if (amount > 0) {
            logger.info("User {} deposited ${} to their account.", accountId, amount);
            return repo.deposit(accountId, pin, amount);
        } else
            logger.warn("User {} attempted to deposit ${} to their account. Transaction cancelled.", accountId,
                    amount);
        throw new IllegalArgumentException("Deposit amount must be positive");
    };

    @Override
    public double withdraw(String accountId, String pin, double amount) {
        double balance = repo.checkBalance(accountId, pin);
        if (amount < balance) {
            logger.info("User {} withdrew ${} from their account.", accountId, amount);
            return repo.withdraw(accountId, pin, amount);
        } else
            logger.warn(
                    "User {} attempted to withdraw ${} from their account, but had insufficient funds. Transaction cancelled.",
                    accountId, amount);
        throw new IllegalArgumentException("Requested withdrawal amount exceeds available funds.");
    };

    @Override
    public void transfer(String accountId, String pin, double amount, String recipientAccountId) {
        double balance = repo.checkBalance(accountId, pin);
        if (!repo.userExists(recipientAccountId)) {
            logger.warn("User {} attempted to transfer ${} to an account which does not exist. Transaction cancelled.",
                    accountId, amount);
            throw new TransferException("The account you would like to transfer to does not exist.");
        }
        if (amount > balance) {
            logger.warn("User {} attempted to transfer ${}, but had insufficient funds. Transaction cancelled.",
                    accountId, amount);
            throw new TransferException("Insufficient funds for this transfer.");
        }
        logger.info("User {} transferred ${} to user {}",
                accountId, amount, recipientAccountId);
        repo.transfer(accountId, pin, amount, recipientAccountId);
    };

    // ---------------------
    // Helper Methods
    // ---------------------

    private boolean isValidLength(String acc) {
        int length = acc.length();
        return (length <= MAX_ID_LENGTH && length >= MIN_ID_LENGTH);
    }
}
