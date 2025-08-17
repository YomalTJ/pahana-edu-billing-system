package com.pahanaedu.test.suite;

import com.pahanaedu.controller.BillItemServletTest;
import com.pahanaedu.controller.BillingServletTest;
import com.pahanaedu.dao.BillDAOTest;
import com.pahanaedu.dao.InvoiceDAOTest;
import com.pahanaedu.model.BillingModelTest;
import com.pahanaedu.service.BillingServiceTest;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(Suite.class)
@Suite.SuiteClasses({
        BillingModelTest.class,
        BillDAOTest.class,
        InvoiceDAOTest.class,
        BillingServiceTest.class,
        BillingServletTest.class,
        BillItemServletTest.class
})
public class BillingTestSuite {
    // Runs all billing-related tests
}