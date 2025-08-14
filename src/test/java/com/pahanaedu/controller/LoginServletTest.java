package com.pahanaedu.controller;

import com.pahanaedu.model.User;
import com.pahanaedu.service.AuthService;
import com.pahanaedu.test.data.TestDataProvider;
import org.junit.*;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

public class LoginServletTest {

    @Mock
    private HttpServletRequest mockRequest;

    @Mock
    private HttpServletResponse mockResponse;

    @Mock
    private HttpSession mockSession;

    @Mock
    private RequestDispatcher mockDispatcher;

    @Mock
    private AuthService mockAuthService;

    private LoginServlet loginServlet;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        loginServlet = new LoginServlet();

        // Inject mock AuthService using reflection
        try {
            java.lang.reflect.Field field = LoginServlet.class.getDeclaredField("authService");
            field.setAccessible(true);
            field.set(loginServlet, mockAuthService);
        } catch (Exception e) {
            fail("Failed to inject mock AuthService: " + e.getMessage());
        }
    }

    @Test
    public void testDoGetForwardsToLoginPage() throws ServletException, IOException {
        // Arrange
        when(mockRequest.getRequestDispatcher("/WEB-INF/views/login.jsp"))
                .thenReturn(mockDispatcher);

        // Act
        loginServlet.doGet(mockRequest, mockResponse);

        // Assert
        verify(mockRequest, times(1)).getRequestDispatcher("/WEB-INF/views/login.jsp");
        verify(mockDispatcher, times(1)).forward(mockRequest, mockResponse);
    }

    @Test
    public void testDoPostValidCredentialsRedirectsToDashboard() throws ServletException, IOException {
        // Arrange
        String username = TestDataProvider.ValidCredentials.ADMIN_USERNAME;
        String password = TestDataProvider.ValidCredentials.ADMIN_PASSWORD;
        User validUser = TestDataProvider.getValidUser();

        when(mockRequest.getParameter("username")).thenReturn(username);
        when(mockRequest.getParameter("password")).thenReturn(password);
        when(mockAuthService.authenticate(username, password)).thenReturn(validUser);
        when(mockRequest.getSession()).thenReturn(mockSession);

        // Act
        loginServlet.doPost(mockRequest, mockResponse);

        // Assert
        verify(mockAuthService, times(1)).authenticate(username, password);
        verify(mockSession, times(1)).setAttribute("user", validUser);
        verify(mockResponse, times(1)).sendRedirect("dashboard");
    }

    @Test
    public void testDoPostInvalidCredentialsShowsError() throws ServletException, IOException {
        // Arrange
        String username = TestDataProvider.InvalidCredentials.INVALID_USERNAME;
        String password = TestDataProvider.InvalidCredentials.INVALID_PASSWORD;

        when(mockRequest.getParameter("username")).thenReturn(username);
        when(mockRequest.getParameter("password")).thenReturn(password);
        when(mockAuthService.authenticate(username, password)).thenReturn(null);
        when(mockRequest.getRequestDispatcher("/WEB-INF/views/login.jsp"))
                .thenReturn(mockDispatcher);

        // Act
        loginServlet.doPost(mockRequest, mockResponse);

        // Assert
        verify(mockAuthService, times(1)).authenticate(username, password);
        verify(mockRequest, times(1)).setAttribute("error", "Invalid username or password");
        verify(mockDispatcher, times(1)).forward(mockRequest, mockResponse);
    }

    @Test
    public void testDoPostEmptyCredentialsShowsError() throws ServletException, IOException {
        // Arrange
        String username = TestDataProvider.InvalidCredentials.EMPTY_USERNAME;
        String password = TestDataProvider.InvalidCredentials.EMPTY_PASSWORD;

        when(mockRequest.getParameter("username")).thenReturn(username);
        when(mockRequest.getParameter("password")).thenReturn(password);
        when(mockAuthService.authenticate(username, password)).thenReturn(null);
        when(mockRequest.getRequestDispatcher("/WEB-INF/views/login.jsp"))
                .thenReturn(mockDispatcher);

        // Act
        loginServlet.doPost(mockRequest, mockResponse);

        // Assert
        verify(mockAuthService, times(1)).authenticate(username, password);
        verify(mockRequest, times(1)).setAttribute("error", "Invalid username or password");
    }

    @Test
    public void testDoPostNullCredentialsShowsError() throws ServletException, IOException {
        // Arrange
        when(mockRequest.getParameter("username")).thenReturn(null);
        when(mockRequest.getParameter("password")).thenReturn(null);
        when(mockAuthService.authenticate(null, null)).thenReturn(null);
        when(mockRequest.getRequestDispatcher("/WEB-INF/views/login.jsp"))
                .thenReturn(mockDispatcher);

        // Act
        loginServlet.doPost(mockRequest, mockResponse);

        // Assert
        verify(mockAuthService, times(1)).authenticate(null, null);
        verify(mockRequest, times(1)).setAttribute("error", "Invalid username or password");
    }
}