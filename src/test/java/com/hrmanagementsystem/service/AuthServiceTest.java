package com.hrmanagementsystem.service;

import com.hrmanagementsystem.dao.interfaces.UserInterface;
import com.hrmanagementsystem.entity.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mindrot.jbcrypt.BCrypt;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

    @Mock
    private UserInterface userInterface;

    private AuthService authService;

    @BeforeEach
    void setUp() {
        authService = new AuthService(userInterface);
    }

    @Test
    void testAuthenticateSuccess() {
        String email = "test@example.com";
        String password = "password123";
        String hashedPassword = BCrypt.hashpw(password, BCrypt.gensalt());

        User mockUser = new User();
        mockUser.setEmail(email);
        mockUser.setPassword(hashedPassword);

        when(userInterface.findByEmail(email)).thenReturn(mockUser);

        User authenticatedUser = authService.authenticate(email, password);

        assertNotNull(authenticatedUser);
        assertEquals(email, authenticatedUser.getEmail());
        verify(userInterface).findByEmail(email);
    }

    @Test
    void testAuthenticateFailureWrongPassword() {
        String email = "test@example.com";
        String correctPassword = "password123";
        String wrongPassword = "wrongpassword";
        String hashedPassword = BCrypt.hashpw(correctPassword, BCrypt.gensalt());

        User mockUser = new User();
        mockUser.setEmail(email);
        mockUser.setPassword(hashedPassword);

        when(userInterface.findByEmail(email)).thenReturn(mockUser);

        User authenticatedUser = authService.authenticate(email, wrongPassword);

        assertNull(authenticatedUser);
        verify(userInterface).findByEmail(email);
    }

    @Test
    void testAuthenticateFailureUserNotFound() {
        String email = "nonexistent@example.com";
        String password = "password123";

        when(userInterface.findByEmail(email)).thenReturn(null);

        User authenticatedUser = authService.authenticate(email, password);

        assertNull(authenticatedUser);
        verify(userInterface).findByEmail(email);
    }
}