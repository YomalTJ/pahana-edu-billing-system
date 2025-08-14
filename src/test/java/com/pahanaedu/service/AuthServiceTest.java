package com.pahanaedu.service;

import com.pahanaedu.dao.UserDAO;
import com.pahanaedu.model.User;
import com.pahanaedu.test.data.TestDataProvider;
import org.junit.*;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class AuthServiceTest {

    @Mock
    private UserDAO mockUserDao;

    private AuthService authService;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        authService = new AuthService();
        // Use reflection to inject mock DAO
        try {
            java.lang.reflect.Field field = AuthService.class.getDeclaredField("userDao");
            field.setAccessible(true);
            field.set(authService, mockUserDao);
        } catch (Exception e) {
            fail("Failed to inject mock DAO: " + e.getMessage());
        }
    }

    @Test
    public void testAuthenticateValidUser() {
        // Arrange
        String username = TestDataProvider.ValidCredentials.ADMIN_USERNAME;
        String password = TestDataProvider.ValidCredentials.ADMIN_PASSWORD;
        User expectedUser = TestDataProvider.getValidUser();

        when(mockUserDao.authenticate(username, password)).thenReturn(expectedUser);

        // Act
        User result = authService.authenticate(username, password);

        // Assert
        assertNotNull("User should not be null for valid credentials", result);
        assertEquals("Username should match", expectedUser.getUsername(), result.getUsername());
        assertEquals("User ID should match", expectedUser.getId(), result.getId());
        assertEquals("Role should match", expectedUser.getRole(), result.getRole());

        verify(mockUserDao, times(1)).authenticate(username, password);
    }

    @Test
    public void testAuthenticateInvalidUser() {
        // Arrange
        String username = TestDataProvider.InvalidCredentials.INVALID_USERNAME;
        String password = TestDataProvider.InvalidCredentials.INVALID_PASSWORD;

        when(mockUserDao.authenticate(username, password)).thenReturn(null);

        // Act
        User result = authService.authenticate(username, password);

        // Assert
        assertNull("User should be null for invalid credentials", result);
        verify(mockUserDao, times(1)).authenticate(username, password);
    }

    @Test
    public void testAuthenticateEmptyCredentials() {
        // Arrange
        String username = TestDataProvider.InvalidCredentials.EMPTY_USERNAME;
        String password = TestDataProvider.InvalidCredentials.EMPTY_PASSWORD;

        when(mockUserDao.authenticate(username, password)).thenReturn(null);

        // Act
        User result = authService.authenticate(username, password);

        // Assert
        assertNull("User should be null for empty credentials", result);
        verify(mockUserDao, times(1)).authenticate(username, password);
    }

    @Test
    public void testAuthenticateNullCredentials() {
        // Arrange
        String username = TestDataProvider.InvalidCredentials.NULL_USERNAME;
        String password = TestDataProvider.InvalidCredentials.NULL_PASSWORD;

        when(mockUserDao.authenticate(username, password)).thenReturn(null);

        // Act
        User result = authService.authenticate(username, password);

        // Assert
        assertNull("User should be null for null credentials", result);
        verify(mockUserDao, times(1)).authenticate(username, password);
    }

    @Test
    public void testAuthenticateCustomerUser() {
        // Arrange
        String username = TestDataProvider.ValidCredentials.CUSTOMER_USERNAME;
        String password = TestDataProvider.ValidCredentials.CUSTOMER_PASSWORD;
        User expectedUser = TestDataProvider.getValidCustomerUser();

        when(mockUserDao.authenticate(username, password)).thenReturn(expectedUser);

        // Act
        User result = authService.authenticate(username, password);

        // Assert
        assertNotNull("Customer user should not be null", result);
        assertEquals("Role should be CUSTOMER", "CUSTOMER", result.getRole());
        verify(mockUserDao, times(1)).authenticate(username, password);
    }

    @Test
    public void testAuthenticateSQLInjectionAttempt() {
        // Arrange
        String username = TestDataProvider.InvalidCredentials.SQL_INJECTION_USERNAME;
        String password = TestDataProvider.InvalidCredentials.SQL_INJECTION_PASSWORD;

        when(mockUserDao.authenticate(username, password)).thenReturn(null);

        // Act
        User result = authService.authenticate(username, password);

        // Assert
        assertNull("SQL injection attempt should return null", result);
        verify(mockUserDao, times(1)).authenticate(username, password);
    }
}