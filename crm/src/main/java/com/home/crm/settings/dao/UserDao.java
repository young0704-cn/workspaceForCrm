package com.home.crm.settings.dao;

import com.home.crm.settings.domain.User;

import java.util.ArrayList;
import java.util.List;

public interface UserDao {
    User login(User user);
    List<User> getAll();
}
