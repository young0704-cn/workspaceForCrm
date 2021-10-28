package com.home.crm.settings.sevice.impl;

import com.home.crm.exception.LoginException;
import com.home.crm.settings.dao.UserDao;
import com.home.crm.settings.domain.User;
import com.home.crm.settings.sevice.UserService;
import com.home.crm.utils.DateTimeUtil;
import com.home.crm.utils.ServiceFactory;
import com.home.crm.utils.SqlSessionUtil;

import java.util.ArrayList;
import java.util.List;

public class UserServiceImpl implements UserService {
    private final UserDao userDao = SqlSessionUtil.getSqlSession().getMapper(UserDao.class);

    public  List<User> getAll(){
        List<User> userList;
       userList = userDao.getAll();
        return userList;
    }

    @Override
    public User login(String loginAct, String loginPwd, String ip) throws LoginException {
        User users=new User(){};
        users.setLoginAct(loginAct);
        users.setLoginPwd(loginPwd);
        users.setAllowIps(ip);

        User user=userDao.login(users);

        if (user==null){
            throw new LoginException("账号密码不正确");
        }
        //如果程序可以执行到此处，说明账号密码正确  接着验证失效时间expireTime
        String expireTime=user.getExpireTime();
        String currentTime= DateTimeUtil.getSysTime();

        if(currentTime.compareTo(expireTime)>0){
            throw new LoginException("账号已失效");
        }
        //如果执行到此处，账号、密码、时间都正确   接着判断锁定状态
        if ("0".equals(user.getLockState())){
            throw new LoginException("账号处于冻结状态");
        }
        //上面都正确，判断ip
        String allowIps=user.getAllowIps();
        if(!allowIps.contains(ip)){
            throw new LoginException("ip"+ip+"无权访问");
        }

        return user;
    }
}
