package com.pahanaedu.test.suite;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(Suite.class)
@Suite.SuiteClasses({
        AuthTestSuite.class,
        CustomerTestSuite.class
})
public class PahanaEduTestSuite {
    // This class runs all system tests
    // Add more test suites here as you develop other modules
    // Examples: ItemTestSuite.class, BillingTestSuite.class, ReportTestSuite.class
}