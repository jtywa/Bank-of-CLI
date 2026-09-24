package com.justintywater.persistence;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.when;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;

class UserDAOImplTest {

    private UserDAOImpl userDAO;

    private Connection mockConnection;
    private PreparedStatement mockStatement;
    private ResultSet mockResultSet;
    private ConnectionFactory mockFactory;
    private MockedStatic<ConnectionFactory> mockedFactoryClass;

    @BeforeEach
    void setUp() throws SQLException {
        userDAO = new UserDAOImpl();

        mockConnection = Mockito.mock(Connection.class);
        mockStatement = Mockito.mock(PreparedStatement.class);
        mockResultSet = Mockito.mock(ResultSet.class);
        mockFactory = Mockito.mock(ConnectionFactory.class);

        mockedFactoryClass = mockStatic(ConnectionFactory.class);
        mockedFactoryClass.when(ConnectionFactory::getConnectionFactory).thenReturn(mockFactory);

        when(mockFactory.getConnection()).thenReturn(mockConnection);
        when(mockConnection.prepareStatement(anyString())).thenReturn(mockStatement);
        when(mockStatement.executeQuery()).thenReturn(mockResultSet);
    }

    @AfterEach
    void tearDown() {
        mockedFactoryClass.close();
    }

    @Test
    void login_validCredentials_returnsTrue() throws SQLException {
        when(mockResultSet.next()).thenReturn(true);

        boolean result = userDAO.login("acc-123", "1234");

        assertTrue(result);
    }

    @Test
    void login_invalidCredentials_returnsFalse() throws SQLException {
        when(mockResultSet.next()).thenReturn(false);

        boolean result = userDAO.login("acc-123", "wrong-pin");

        assertFalse(result);
    }
}