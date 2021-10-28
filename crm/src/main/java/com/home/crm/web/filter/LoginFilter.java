package com.home.crm.web.filter;

import com.home.crm.settings.domain.User;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

public class LoginFilter implements Filter {
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest req, ServletResponse resp, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request =(HttpServletRequest) req;
        HttpServletResponse response=(HttpServletResponse) resp;
        String path=request.getServletPath();
        //判断路径，如果是和登录业务相关的资源需要放行
        if ("/settings/user/login.do".equals(path)||"/login.jsp".equals(path)){
            filterChain.doFilter(request,response);
        }else {//其他路径需要拦截
            HttpSession session=request.getSession();
            User user= (User) session.getAttribute("user");

            if (user!=null){//如果user对象不为null，表示存在session对象。即从登录成功后访问
                //请求放行
                filterChain.doFilter(request,response);
            }else {//user对象为null，session对象为null。即非法访问
            /*
                重定向至登录页面

                重定向和请求转发路径的区别?
                    1、路径写法不同
                        重定向是传统绝对路径写法，需要加/项目名         /web/login.jsp
                        请求转发是种特殊绝对路径写法,不需要加/项目名     /login.jsp
                分析为什么只能用重定向?
                    2、页面跳转机制
                        重定向，页面会跳转至登录页并且浏览器地址栏会更改。
                        请求转发，地址栏中路径不会更改。
            */
                response.sendRedirect(request.getContextPath()+"/login.jsp");//request.getContextPath()获取当前请求对象的项目名称      "/web/login.jsp"
            }
        }


    }

    @Override
    public void destroy() {

    }
}
