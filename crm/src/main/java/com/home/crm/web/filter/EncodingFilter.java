package com.home.crm.web.filter;

import javax.servlet.*;
import java.io.IOException;

public class EncodingFilter implements Filter {
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void destroy() {

    }

    @Override
    public void doFilter(ServletRequest req, ServletResponse resp, FilterChain filterChain) throws IOException, ServletException {
        //过滤post请求中文乱码
        req.setCharacterEncoding("UTF-8");
        //过滤响应流中文乱码
        resp.setContentType("text/html;charset=UTF-8");
        //请求放行
        filterChain.doFilter(req,resp);
    }
}
