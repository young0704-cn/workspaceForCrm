package com.home.crm.workbench.web.controller;

import com.home.crm.settings.domain.User;
import com.home.crm.settings.sevice.UserService;
import com.home.crm.settings.sevice.impl.UserServiceImpl;
import com.home.crm.utils.PrintJson;
import com.home.crm.utils.ServiceFactory;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

public class ClueController extends HttpServlet {
    @Override
    protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        if("/clue/getUserList.do".equals(request.getServletPath())){
            getUserList(request,response);
        }
    }

    private void getUserList(HttpServletRequest request, HttpServletResponse response) {
        UserService userService = (UserService) ServiceFactory.getService(new UserServiceImpl());
        List<User> userList=userService.getAll();
        PrintJson.printJsonObj(response,userList);
    }
}
