package com.pahanaedu.test.suite;

import com.pahanaedu.controller.CustomerServletTest;
import com.pahanaedu.dao.CustomerDAOTest;
import com.pahanaedu.service.CustomerServiceTest;
import com.pahanaedu.model.CustomerModelTest;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(Suite.class)
@Suite.SuiteClasses({
        CustomerModelTest.class,
        CustomerDAOTest.class,
        CustomerServiceTest.class,
        CustomerServletTest.class
})
public class CustomerTestSuite {
    // This class runs all customer-related tests
    // No additional code needed - annotations handle the execution
}