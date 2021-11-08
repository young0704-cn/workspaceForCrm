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
import javax.servlet.http.HttpSession;
import javax.xml.ws.spi.http.HttpContext;
import java.util.*;

public class SystemListener implements ServletContextListener {
    @Override
    public void contextInitialized(ServletContextEvent servletContextEvent) {
        System.out.println("监听到全局作用域对象创建");
        DicoService dicoService = (DicoService) ServiceFactory.getService(new DicServiceImp());
        //1、获取全局作用域对象
        ServletContext application=servletContextEvent.getServletContext();
        //2、数据字典
        Map<String, List<DicValue>> map=dicoService.getDVList();

        //3、给全局作用域对象中存入元素(数据字典)
        Set<Map.Entry<String,List<DicValue>>> entrySet=map.entrySet();
        for (Map.Entry<String,List<DicValue>> me:entrySet){
            application.setAttribute(me.getKey(),me.getValue());
        }
//---------------------------------------------阶段与可能性------------------------------------------------
        //获取Stage2Possibility.properties的资源包,将properties的key,value遍历存入Map集合。最后存入全局作用域对象ServletContext
        ResourceBundle rb=ResourceBundle.getBundle("Stage2Possibility");
        Enumeration<String> e=rb.getKeys();
        Map<String,String> pmap=new HashMap<>();
        while(e.hasMoreElements()){
            String key=e.nextElement();
            String value=rb.getString(key);
            pmap.put(key,value);
        }
        application.setAttribute("pmap",pmap);
    }

    @Override
    public void contextDestroyed(ServletContextEvent servletContextEvent) {
        System.out.println("监听到全局作用域对象销毁");
    }
}
