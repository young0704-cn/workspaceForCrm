package com.home.crm.settings.sevice;

import com.home.crm.exception.LoginException;
import com.home.crm.settings.domain.User;

import java.util.List;

public interface UserService {
    User login(String loginAct, String loginPwd, String ip) throws LoginException;
    List<User> getAll();
}
