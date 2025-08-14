package com.pahanaedu.test.data;

import com.pahanaedu.model.*;

import java.util.Date;
import java.util.Arrays;
import java.util.List;

public class TestDataProvider {

    // Valid test users
    public static User getValidUser() {
        User user = new User();
        user.setId(1);
        user.setUsername("admin");
        user.setRole("ADMIN");
        user.setStatus("ACTIVE");
        return user;
    }

    public static User getValidCustomerUser() {
        User user = new User();
        user.setId(2);
        user.setUsername("customer1");
        user.setRole("CUSTOMER");
        user.setStatus("ACTIVE");
        return user;
    }

    // Test credentials
    public static class ValidCredentials {
        public static final String ADMIN_USERNAME = "admin";
        public static final String ADMIN_PASSWORD = "admin123";
        public static final String CUSTOMER_USERNAME = "customer1";
        public static final String CUSTOMER_PASSWORD = "customer123";
    }

    // Invalid test data
    public static class InvalidCredentials {
        public static final String INVALID_USERNAME = "wronguser";
        public static final String INVALID_PASSWORD = "wrongpass";
        public static final String EMPTY_USERNAME = "";
        public static final String EMPTY_PASSWORD = "";
        public static final String NULL_USERNAME = null;
        public static final String NULL_PASSWORD = null;

        // SQL Injection attempts
        public static final String SQL_INJECTION_USERNAME = "admin'; DROP TABLE users; --";
        public static final String SQL_INJECTION_PASSWORD = "' OR '1'='1";

        // XSS attempts
        public static final String XSS_USERNAME = "<script>alert('xss')</script>";
        public static final String XSS_PASSWORD = "<img src=x onerror=alert('xss')>";
    }

    // Edge cases
    public static class EdgeCases {
        public static final String VERY_LONG_USERNAME = "a".repeat(1000);
        public static final String VERY_LONG_PASSWORD = "b".repeat(1000);
        public static final String SPECIAL_CHARS_USERNAME = "user@#$%^&*()";
        public static final String UNICODE_USERNAME = "用户名";
    }

    // Customer Test Data
    public static Customer getValidCustomer() {
        Customer customer = new Customer();
        customer.setAccountNumber("ACC00001");
        customer.setName("John Doe");
        customer.setAddress("123 Main Street, Colombo 03");
        customer.setTelephone("0771234567");
        customer.setEmail("john.doe@email.com");
        customer.setRegistrationDate(new Date());
        customer.setCustomerType("REGULAR");
        customer.setStatus("ACTIVE");
        return customer;
    }

    public static Customer getValidPremiumCustomer() {
        Customer customer = new Customer();
        customer.setAccountNumber("ACC00002");
        customer.setName("Jane Smith");
        customer.setAddress("456 Galle Road, Colombo 04");
        customer.setTelephone("0757654321");
        customer.setEmail("jane.smith@email.com");
        customer.setRegistrationDate(new Date());
        customer.setCustomerType("PREMIUM");
        customer.setStatus("ACTIVE");
        return customer;
    }

    public static Customer getCustomerForUpdate() {
        Customer customer = getValidCustomer();
        customer.setName("John Doe Updated");
        customer.setAddress("789 Updated Street, Colombo 05");
        customer.setTelephone("0777777777");
        customer.setEmail("john.updated@email.com");
        customer.setCustomerType("PREMIUM");
        customer.setStatus("INACTIVE");
        return customer;
    }

    public static CustomerType getRegularCustomerType() {
        CustomerType type = new CustomerType();
        type.setTypeId(1);
        type.setTypeName("REGULAR");
        type.setDiscountPercentage(0.0);
        return type;
    }

    public static CustomerType getPremiumCustomerType() {
        CustomerType type = new CustomerType();
        type.setTypeId(2);
        type.setTypeName("PREMIUM");
        type.setDiscountPercentage(10.0);
        return type;
    }

    public static CustomerType getVipCustomerType() {
        CustomerType type = new CustomerType();
        type.setTypeId(3);
        type.setTypeName("VIP");
        type.setDiscountPercentage(20.0);
        return type;
    }

    public static List<CustomerType> getAllCustomerTypes() {
        return Arrays.asList(
                getRegularCustomerType(),
                getPremiumCustomerType(),
                getVipCustomerType()
        );
    }

    // Invalid Customer Data
    public static class InvalidCustomerData {
        public static Customer getCustomerWithNullAccountNumber() {
            Customer customer = getValidCustomer();
            customer.setAccountNumber(null);
            return customer;
        }

        public static Customer getCustomerWithEmptyName() {
            Customer customer = getValidCustomer();
            customer.setName("");
            return customer;
        }

        public static Customer getCustomerWithInvalidEmail() {
            Customer customer = getValidCustomer();
            customer.setEmail("invalid-email");
            return customer;
        }

        public static Customer getCustomerWithInvalidPhone() {
            Customer customer = getValidCustomer();
            customer.setTelephone("invalid-phone");
            return customer;
        }

        public static Customer getCustomerWithSQLInjection() {
            Customer customer = getValidCustomer();
            customer.setName("'; DROP TABLE customers; --");
            customer.setAddress("<script>alert('xss')</script>");
            return customer;
        }

        public static Customer getCustomerWithVeryLongData() {
            Customer customer = getValidCustomer();
            customer.setName("A".repeat(1000));
            customer.setAddress("B".repeat(2000));
            customer.setTelephone("1".repeat(100));
            customer.setEmail("c".repeat(500) + "@email.com");
            return customer;
        }
    }

    public static Item getValidItem() {
        Item item = new Item();
        item.setItemId("ITM00001");
        item.setName("Advanced Java Programming");
        item.setDescription("Comprehensive guide to Java programming");

        Category category = new Category();
        category.setCategoryId(1);
        category.setCategoryName("Books");
        item.setCategory(category);

        item.setUnitPrice(29.99);
        item.setStockQuantity(50);
        item.setReorderLevel(10);
        return item;
    }

    public static Item getValidBookItem() {
        Item item = new Item();
        item.setItemId("ITM00002");
        item.setName("Design Patterns");
        item.setDescription("Elements of Reusable Object-Oriented Software");

        Category category = new Category();
        category.setCategoryId(1);
        category.setCategoryName("Books");
        item.setCategory(category);

        item.setUnitPrice(49.99);
        item.setStockQuantity(30);
        item.setReorderLevel(5);
        return item;
    }

    public static Item getItemForUpdate() {
        Item item = getValidItem();
        item.setName("Advanced Java Programming - 2nd Edition");
        item.setDescription("Updated comprehensive guide to Java programming");
        item.setUnitPrice(39.99);
        item.setStockQuantity(60);
        item.setReorderLevel(15);
        return item;
    }

    public static List<Category> getAllCategories() {
        Category books = new Category();
        books.setCategoryId(1);
        books.setCategoryName("Books");

        Category stationery = new Category();
        stationery.setCategoryId(2);
        stationery.setCategoryName("Stationery");

        return Arrays.asList(books, stationery);
    }

    public static class InvalidItemData {
        public static Item getItemWithInvalidId() {
            Item item = getValidItem();
            item.setItemId("INVALID01");
            return item;
        }

        public static Item getItemWithNegativePrice() {
            Item item = getValidItem();
            item.setUnitPrice(-10.00);
            return item;
        }

        public static Item getItemWithNegativeQuantity() {
            Item item = getValidItem();
            item.setStockQuantity(-5);
            return item;
        }

        public static Item getItemWithNullName() {
            Item item = getValidItem();
            item.setName(null);
            return item;
        }

        public static Item getItemWithEmptyDescription() {
            Item item = getValidItem();
            item.setDescription("");
            return item;
        }

        public static Item getItemWithSQLInjection() {
            Item item = getValidItem();
            item.setName("'; DROP TABLE items; --");
            item.setDescription("<script>alert('xss')</script>");
            return item;
        }

        public static Item getItemWithVeryLongData() {
            Item item = getValidItem();
            item.setName("A".repeat(1000));
            item.setDescription("B".repeat(2000));
            return item;
        }
    }
}