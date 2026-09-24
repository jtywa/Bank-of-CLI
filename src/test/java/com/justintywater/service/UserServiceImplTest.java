package com.justintywater.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.justintywater.persistence.UserDAO;

class UserServiceImplTest {

    private UserDAO mockRepo;
    private UserServiceImpl service;

    @BeforeEach
    void setUp() {
        mockRepo = mock(UserDAO.class);
        service = new UserServiceImpl(mockRepo);
    }

    @Test
    void withdraw_amountLessThanBalance_returnsNewBalance() {
        when(mockRepo.checkBalance("acc-123", "1234")).thenReturn(100.0);
        when(mockRepo.withdraw("acc-123", "1234", 40.0)).thenReturn(60.0);

        double newBalance = service.withdraw("acc-123", "1234", 40.0);

        assertEquals(60.0, newBalance, 0.0001);
        verify(mockRepo).withdraw("acc-123", "1234", 40.0);
    }

    @Test
    void withdraw_amountExceedsBalance_throwsIllegalArgumentException() {
        when(mockRepo.checkBalance("acc-123", "1234")).thenReturn(20.0);

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> service.withdraw("acc-123", "1234", 40.0));
        assertEquals("Requested withdrawal amount exceeds available funds.", ex.getMessage());
    }
}