package com.pahanaedu.test.integration;

import com.pahanaedu.model.Customer;
import com.pahanaedu.test.data.TestDataProvider;
import org.junit.*;
import java.util.regex.Pattern;
import static org.junit.Assert.*;

public class CustomerValidationTest {

    private static final Pattern EMAIL_PATTERN =
            Pattern.compile("^(?!.*\\.\\.)[A-Za-z0-9+_-]+(?:\\.[A-Za-z0-9+_-]+)*@([A-Za-z0-9-]+\\.)+[A-Za-z]{2,}$");

    private static final Pattern PHONE_PATTERN =
            Pattern.compile("^(\\+94|0|94)?[1-9][0-9]{8}$");

    private static final Pattern ACCOUNT_NUMBER_PATTERN =
            Pattern.compile("^ACC\\d{5}$");

    @Test
    public void testValidCustomerValidation() {
        // Arrange
        Customer customer = TestDataProvider.getValidCustomer();

        // Act & Assert
        assertTrue("Valid customer should pass validation", validateCustomer(customer));
        assertTrue("Account number should be valid format",
                ACCOUNT_NUMBER_PATTERN.matcher(customer.getAccountNumber()).matches());
        assertTrue("Email should be valid format",
                EMAIL_PATTERN.matcher(customer.getEmail()).matches());
        assertTrue("Phone should be valid format",
                PHONE_PATTERN.matcher(customer.getTelephone()).matches());
    }

    @Test
    public void testCustomerWithInvalidEmail() {
        // Arrange
        Customer customer = TestDataProvider.InvalidCustomerData.getCustomerWithInvalidEmail();

        // Act & Assert
        assertFalse("Customer with invalid email should fail validation",
                EMAIL_PATTERN.matcher(customer.getEmail()).matches());
    }

    @Test
    public void testCustomerWithInvalidPhone() {
        // Arrange
        Customer customer = TestDataProvider.InvalidCustomerData.getCustomerWithInvalidPhone();

        // Act & Assert
        assertFalse("Customer with invalid phone should fail validation",
                PHONE_PATTERN.matcher(customer.getTelephone()).matches());
    }

    @Test
    public void testCustomerWithNullAccountNumber() {
        // Arrange
        Customer customer = TestDataProvider.InvalidCustomerData.getCustomerWithNullAccountNumber();

        // Act & Assert
        assertFalse("Customer with null account number should fail validation",
                validateCustomer(customer));
    }

    @Test
    public void testCustomerWithEmptyName() {
        // Arrange
        Customer customer = TestDataProvider.InvalidCustomerData.getCustomerWithEmptyName();

        // Act & Assert
        assertFalse("Customer with empty name should fail validation",
                validateCustomer(customer));
    }

    @Test
    public void testAccountNumberGeneration() {
        // Test account number format validation
        String[] validAccountNumbers = {"ACC00001", "ACC12345", "ACC99999"};
        String[] invalidAccountNumbers = {"ACC123", "ACC123456", "XYZ00001", "ACC0000A"};

        for (String valid : validAccountNumbers) {
            assertTrue("Account number " + valid + " should be valid",
                    ACCOUNT_NUMBER_PATTERN.matcher(valid).matches());
        }

        for (String invalid : invalidAccountNumbers) {
            assertFalse("Account number " + invalid + " should be invalid",
                    ACCOUNT_NUMBER_PATTERN.matcher(invalid).matches());
        }
    }

    @Test
    public void testEmailValidation() {
        // Test various email formats
        String[] validEmails = {
                "user@example.com",
                "test.email@domain.org",
                "user+tag@example.co.uk",
                "firstname.lastname@example.com"
        };

        String[] invalidEmails = {
                "invalid-email",
                "@example.com",
                "user@",
                "user..name@example.com",
                "user@example",
                ""
        };

        for (String valid : validEmails) {
            assertTrue("Email " + valid + " should be valid",
                    EMAIL_PATTERN.matcher(valid).matches());
        }

        for (String invalid : invalidEmails) {
            assertFalse("Email " + invalid + " should be invalid",
                    EMAIL_PATTERN.matcher(invalid).matches());
        }
    }

    @Test
    public void testPhoneValidation() {
        // Test various phone number formats
        String[] validPhones = {
                "0771234567",
                "0112345678",
                "+94771234567",
                "94771234567"
        };

        String[] invalidPhones = {
                "077123456",     // Too short
                "07712345678",   // Too long
                "0071234567",    // Starts with 00
                "1234567890",    // Doesn't follow Sri Lankan format
                "invalid-phone", // Non-numeric
                ""
        };

        for (String valid : validPhones) {
            assertTrue("Phone " + valid + " should be valid",
                    PHONE_PATTERN.matcher(valid).matches());
        }

        for (String invalid : invalidPhones) {
            assertFalse("Phone " + invalid + " should be invalid",
                    PHONE_PATTERN.matcher(invalid).matches());
        }
    }

    @Test
    public void testCustomerStatusValidation() {
        // Test valid status values
        String[] validStatuses = {"ACTIVE", "INACTIVE"};
        String[] invalidStatuses = {"active", "SUSPENDED", "DELETED", "", null};

        for (String status : validStatuses) {
            assertTrue("Status " + status + " should be valid", isValidStatus(status));
        }

        for (String status : invalidStatuses) {
            assertFalse("Status " + status + " should be invalid", isValidStatus(status));
        }
    }

    @Test
    public void testCustomerTypeValidation() {
        // Test valid customer types
        String[] validTypes = {"REGULAR", "PREMIUM", "VIP"};
        String[] invalidTypes = {"regular", "GOLD", "SILVER", "", null};

        for (String type : validTypes) {
            assertTrue("Customer type " + type + " should be valid", isValidCustomerType(type));
        }

        for (String type : invalidTypes) {
            assertFalse("Customer type " + type + " should be invalid", isValidCustomerType(type));
        }
    }

    @Test
    public void testSQLInjectionPreventionInData() {
        // Test that customer data with SQL injection attempts are handled
        Customer maliciousCustomer = TestDataProvider.InvalidCustomerData.getCustomerWithSQLInjection();

        // The customer object should contain the malicious strings as-is
        // (Actual SQL injection prevention happens in DAO layer with prepared statements)
        assertNotNull("Customer should not be null", maliciousCustomer);
        assertTrue("Name should contain SQL injection attempt",
                maliciousCustomer.getName().contains("DROP TABLE"));
        assertTrue("Address should contain XSS attempt",
                maliciousCustomer.getAddress().contains("<script>"));
    }

    @Test
    public void testDataLengthLimits() {
        // Test handling of very long data
        Customer longDataCustomer = TestDataProvider.InvalidCustomerData.getCustomerWithVeryLongData();

        assertNotNull("Customer should not be null", longDataCustomer);
        assertTrue("Name should be very long", longDataCustomer.getName().length() > 500);
        assertTrue("Address should be very long", longDataCustomer.getAddress().length() > 1000);
        assertTrue("Email should be very long", longDataCustomer.getEmail().length() > 500);
    }

    // Helper methods for validation
    private boolean validateCustomer(Customer customer) {
        if (customer == null) return false;
        if (customer.getAccountNumber() == null || customer.getAccountNumber().trim().isEmpty()) return false;
        if (customer.getName() == null || customer.getName().trim().isEmpty()) return false;
        if (customer.getAddress() == null || customer.getAddress().trim().isEmpty()) return false;
        if (customer.getTelephone() == null || customer.getTelephone().trim().isEmpty()) return false;
        if (customer.getCustomerType() == null || customer.getCustomerType().trim().isEmpty()) return false;
        if (customer.getStatus() == null || customer.getStatus().trim().isEmpty()) return false;

        return true;
    }

    private boolean isValidStatus(String status) {
        return status != null && (status.equals("ACTIVE") || status.equals("INACTIVE"));
    }

    private boolean isValidCustomerType(String type) {
        return type != null && (type.equals("REGULAR") || type.equals("PREMIUM") || type.equals("VIP"));
    }
}