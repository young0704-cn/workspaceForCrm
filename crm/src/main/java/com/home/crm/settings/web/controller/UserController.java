package com.home.crm.settings.web.controller;

import com.home.crm.settings.domain.User;
import com.home.crm.settings.sevice.UserService;
import com.home.crm.settings.sevice.impl.UserServiceImpl;
import com.home.crm.utils.MD5Util;
import com.home.crm.utils.PrintJson;
import com.home.crm.utils.ServiceFactory;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class UserController extends HttpServlet {
    @Override
    protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        if ("/settings/user/login.do".equals(request.getServletPath())){
            login(request, response);
        }else if ("/xxx".equals(request.getServletPath())){

        }
    }

    //登录验证方法
    private void login(HttpServletRequest request,HttpServletResponse response){
        //获取参数
        String loginAct=request.getParameter("loginAct");
        String loginPwd=request.getParameter("loginPwd");
        System.out.println(loginAct);
        System.out.println("明文="+loginPwd);
        //获取ip
        String ip=request.getRemoteAddr();
        //明文密码转为密文，然后在数据库进行对比
        loginPwd=MD5Util.getMD5(loginPwd);
        System.out.println("密文="+loginPwd);

        //获取代理接口对象
        UserService userService = (UserService) ServiceFactory.getService(new UserServiceImpl());
        HttpSession session=request.getSession();

        //利用try,catch判断Service业务层执行情况
            //PrintJson封装jackson,会将信息(PrintWrite对象.print)输出到ajax   success:function (result_data)      result_data参数
        try {
            User user=userService.login(loginAct,loginPwd,ip);
            session.setAttribute("user",user);
            //执行到此，登录成功             如果没有抛出异常,代表Service业务层执行成功。即登录成功
            PrintJson.printJsonFlag(response,true);
        }catch (Exception e){
            String wrong=e.getMessage();
            //执行到此，登录失败             如果抛出异常,代表Service业务层执行失败。即登录失败
            Map<String,Object> map=new HashMap<>();
            map.put("success",false);
            map.put("msg",wrong);
            PrintJson.printJsonObj(response,map);
        }
    }

    void b(){}
}
