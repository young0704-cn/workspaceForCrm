package com.home.crm.web.listener;

import com.home.crm.settings.dao.DicTypeDao;
import com.home.crm.settings.domain.DicValue;
import com.home.crm.settings.sevice.DicoService;
import com.home.crm.settings.sevice.impl.DicServiceImp;
import com.home.crm.utils.ServiceFactory;
import com.home.crm.utils.SqlSessionUtil;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class SystemListener implements ServletContextListener {
    @Override
    public void contextInitialized(ServletContextEvent servletContextEvent) {
        DicoService dicoService = (DicoService) ServiceFactory.getService(new DicServiceImp());
        System.out.println("服务器缓存数据字典start");
        //1、获取全局作用域对象
        ServletContext application=servletContextEvent.getServletContext();
        //2、数据字典
        Map<String, List<DicValue>> map=dicoService.getDVList();

        //3、给全局作用域对象中存入元素(数据字典)
        Set<Map.Entry<String,List<DicValue>>> entrySet=map.entrySet();
        for (Map.Entry<String,List<DicValue>> me:entrySet){
            application.setAttribute(me.getKey(),me.getValue());
            System.out.println(me.getKey()+"------------------------");
        }
        System.out.println("服务器缓存数据字典over");
    }

    @Override
    public void contextDestroyed(ServletContextEvent servletContextEvent) {
        System.out.println("监听到全局作用域对象销毁");
    }
}
