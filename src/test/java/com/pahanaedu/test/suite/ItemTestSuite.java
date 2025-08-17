package com.pahanaedu.test.suite;

import com.pahanaedu.controller.ItemServletTest;
import com.pahanaedu.dao.ItemDAOTest;
import com.pahanaedu.model.ItemModelTest;
import com.pahanaedu.service.ItemServiceTest;
import com.pahanaedu.test.integration.ItemValidationTest;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(Suite.class)
@Suite.SuiteClasses({
        ItemModelTest.class,
        ItemDAOTest.class,
        ItemServiceTest.class,
        ItemServletTest.class,
        ItemValidationTest.class
})
public class ItemTestSuite {
    // Runs all item-related tests
}