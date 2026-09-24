package com.justintywater.api;
 
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
 
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
 
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
 
import com.justintywater.service.UserService;

class BankReplTest {
 
    private UserService mockService;
    private ByteArrayOutputStream outContent;
 
    private InputStream originalIn;
    private PrintStream originalOut;
 
    @BeforeEach
    void setUp() {
        mockService = mock(UserService.class);
        outContent = new ByteArrayOutputStream();
 
        originalIn = System.in;
        originalOut = System.out;
 
        System.setOut(new PrintStream(outContent, true, StandardCharsets.UTF_8));
    }
 
    @AfterEach
    void tearDown() {
        System.setIn(originalIn);
        System.setOut(originalOut);
    }
 
    @Test
    void balance_whenLoggedIn_printsFormattedBalance() {

        when(mockService.login("acc-123", "1234")).thenReturn(true);
        when(mockService.checkBalance("acc-123", "1234")).thenReturn(250.5);
 
        String simulatedInput = String.join("\n",
                "login",
                "acc-123",
                "1234",
                "balance",
                "exit") + "\n";
        System.setIn(new ByteArrayInputStream(simulatedInput.getBytes(StandardCharsets.UTF_8)));
 
        BankRepl repl = new BankRepl(mockService);
        repl.run();
 
        String output = outContent.toString(StandardCharsets.UTF_8);
        assertTrue(output.contains("Balance: $250.50"),
                "Expected formatted balance to be printed, but got:\n" + output);
    }
 
    @Test
    void balance_whenNotLoggedIn_printsError() {
        String simulatedInput = String.join("\n",
                "balance",
                "exit") + "\n";
        System.setIn(new ByteArrayInputStream(simulatedInput.getBytes(StandardCharsets.UTF_8)));
 
        BankRepl repl = new BankRepl(mockService);
        repl.run();
 
        String output = outContent.toString(StandardCharsets.UTF_8);
        assertTrue(output.contains("Error: Must be logged in to view balance."),
                "Expected login-required error to be printed, but got:\n" + output);
    }
}
