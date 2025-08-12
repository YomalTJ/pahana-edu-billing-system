package com.pahanaedu.service;

import com.pahanaedu.dao.UserDAO;
import com.pahanaedu.model.User;

public class AuthService {
    private UserDAO userDao = new UserDAO();

    public User authenticate(String username, String password) {
        return userDao.authenticate(username, password);
    }
}