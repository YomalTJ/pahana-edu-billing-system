package com.pahanaedu.test.suite;

import com.pahanaedu.controller.LoginServletTest;
import com.pahanaedu.dao.UserDAOTest;
import com.pahanaedu.service.AuthServiceTest;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(Suite.class)
@Suite.SuiteClasses({
        AuthServiceTest.class,
        UserDAOTest.class,
        LoginServletTest.class
})
public class AuthTestSuite {
    // This class runs all authentication-related tests
    // No additional code needed - annotations handle the execution
}