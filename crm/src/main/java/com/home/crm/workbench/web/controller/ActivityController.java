package com.home.crm.workbench.web.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.home.crm.exception.LoginException;
import com.home.crm.settings.domain.User;
import com.home.crm.settings.sevice.UserService;
import com.home.crm.settings.sevice.impl.UserServiceImpl;
import com.home.crm.utils.*;
import com.home.crm.workbench.domain.Activity;
import com.home.crm.workbench.domain.ActivityRemark;
import com.home.crm.workbench.service.ActivityService;
import com.home.crm.workbench.service.impl.ActivityServiceImpl;
import com.home.crm.workbench.vo.ViewObject;
import com.mysql.cj.util.TimeUtil;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ActivityController extends HttpServlet {
    @Override
    protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        if ("/activity/getUserList.do".equals(request.getServletPath())){
            getUserList(request, response);
        }else if ("/activity/save.do".equals(request.getServletPath())){
            save(request,response);
        }else if("/activity/pageList.do".equals(request.getServletPath())){
            pageList(request,response);
        }else if ("/activity/delete.do".equals(request.getServletPath())){
            delete(request,response);
        }else if ("/activity/edit.do".equals(request.getServletPath())){
            edit(request,response);
        }else if ("/activity/update.do".equals(request.getServletPath())){
            update(request,response);
        }else if ("/activity/detail.do".equals(request.getServletPath())){
            detail(request,response);
        }else if ("/activity/remarkList.do".equals(request.getServletPath())){
            remarkList(request,response);
        }else if ("/activity/deleteRemark.do".equals(request.getServletPath())){
            deleteRemark(request,response);
        }else if ("/activity/saveRemark.do".equals(request.getServletPath())){
           saveRemark(request,response);
        }else if ("/activity/updateRemark.do".equals(request.getServletPath())){
            updateRemark(request,response);
        }
    }

    private void updateRemark(HttpServletRequest request, HttpServletResponse response) {
        ActivityService actService = (ActivityService) ServiceFactory.getService(new ActivityServiceImpl());

        ActivityRemark remark=new ActivityRemark();

        User user =(User)request.getSession().getAttribute("user");
        remark.setId(request.getParameter("id"));
        remark.setEditFlag("1");
        remark.setEditBy(user.getName());
        remark.setEditTime(DateTimeUtil.getSysTime());
        remark.setNoteContent(request.getParameter("noteContent"));

        try {
            List<ActivityRemark> remarkList=actService.updateRemark(remark);
            Map<String,Object> map =new HashMap<>();
            map.put("success",true);
            map.put("ar",remarkList);
            PrintJson.printJsonObj(response,map);
        } catch (LoginException e) {
            Map<String,Object> map =new HashMap<>();
            map.put("success",false);
            map.put("msg",e.getMessage());
            PrintJson.printJsonObj(response,map);
        }
    }

    private void saveRemark(HttpServletRequest request, HttpServletResponse response) {
        ActivityService actService = (ActivityService) ServiceFactory.getService(new ActivityServiceImpl());
        ActivityRemark remark =new ActivityRemark();
        User user = (User) request.getSession().getAttribute("user");

        remark.setActivityId(request.getParameter("activityId"));
        remark.setNoteContent(request.getParameter("remark"));

        remark.setCreateBy(user.getName());
        remark.setCreateTime(DateTimeUtil.getSysTime());
        remark.setId(UUIDUtil.getUUID());
        remark.setEditFlag("0");

        try {
            List<ActivityRemark> ar=actService.saveRemark(remark);
            Map<String,Object> map = new HashMap<>();
            map.put("success",true);
            map.put("ar",ar);
            PrintJson.printJsonObj(response,map);
        } catch (LoginException e) {
            Map<String,Object> map = new HashMap<>();
            map.put("success",false);
            map.put("msg",e.getMessage());
            PrintJson.printJsonObj(response,map);
        }

    }

    private void deleteRemark(HttpServletRequest request, HttpServletResponse response) {
        ActivityService actService = (ActivityService) ServiceFactory.getService(new ActivityServiceImpl());
        try {
            actService.deleteRemark(request.getParameter("id"));
            PrintJson.printJsonFlag(response,true);
        } catch (LoginException e) {
            Map<String,Object> map =new HashMap<>();
            map.put("success",false);
            map.put("msg",e.getMessage());
            PrintJson.printJsonObj(response,map);
        }
    }

    private void remarkList(HttpServletRequest request, HttpServletResponse response) {
        ActivityService actService= (ActivityService) ServiceFactory.getService(new ActivityServiceImpl());
        String id =request.getParameter("activityId");
        List<ActivityRemark> actRemarks=actService.remarkList(id);
        PrintJson.printJsonObj(response,actRemarks);
    }

    private void detail(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        ActivityService actService= (ActivityService) ServiceFactory.getService(new ActivityServiceImpl());
        String id =request.getParameter("id");
        Activity activity=actService.detail(id);
        request.setAttribute("activity",activity);
        request.getRequestDispatcher("/workbench/activity/detail.jsp").forward(request,response);
    }

    private void update(HttpServletRequest request, HttpServletResponse response) {
        ActivityService actService= (ActivityService) ServiceFactory.getService(new ActivityServiceImpl());
        Map<String,Object> map=new HashMap<>();
        map.put("id",request.getParameter("id"));
        map.put("name",request.getParameter("name"));
        map.put("owner",request.getParameter("owner"));
        map.put("cost",request.getParameter("cost"));
        map.put("endDate",request.getParameter("endDate"));
        map.put("startDate",request.getParameter("startDate"));
        map.put("description",request.getParameter("description"));
        map.put("editTime",DateTimeUtil.getSysTime());
        map.put("editBy",((User)request.getSession().getAttribute("user")).getName());

        try {
            actService.update(map);
            PrintJson.printJsonFlag(response,true);
        }catch (LoginException e){

            Map<String,Object> map1=new HashMap<>();
            map1.put("success",false);
            map1.put("msg",e.getMessage());
            PrintJson.printJsonObj(response,map1);
        }
    }

    private void edit(HttpServletRequest request, HttpServletResponse response) {
        ActivityService actService = (ActivityService) ServiceFactory.getService(new ActivityServiceImpl());
        String id=request.getParameter("id");
        Map<String,Object> map=actService.edit(id);
        PrintJson.printJsonObj(response,map);
    }

    private void delete(HttpServletRequest request, HttpServletResponse response){
        System.out.println("市场信息删除");
        ActivityService actService= (ActivityService) ServiceFactory.getService(new ActivityServiceImpl());

        String []ids=request.getParameterValues("ids");

        try {
            actService.delete(ids);
            PrintJson.printJsonFlag(response,true);
        } catch (LoginException e) {
            Map<String,Object> map=new HashMap<>();
            map.put("success",false);
            map.put("msg",e.getMessage());
            PrintJson.printJsonObj(response,map);
        }
    }

    private void pageList(HttpServletRequest request, HttpServletResponse response) {
        System.out.println("市场活动查询");
        //条件查询+分页查询
        String pageNoStr=request.getParameter("pageNo");
        String pageSizeStr=request.getParameter("pageSize");
        //pageNo页码  pageSize每页展示的条数
        int pageNo=Integer.parseInt(pageNoStr);
        int pageSize=Integer.parseInt(pageSizeStr);
        //计算sql语句中，limit第一个参数(略过的记录条数)
        int skipCount=(pageNo-1)*pageSize;

        Map<String,Object> map=new HashMap<>();
        map.put("skipCount",skipCount);
        map.put("pageSize",pageSize);
        map.put("name",request.getParameter("name"));
        map.put("owner",request.getParameter("owner"));
        map.put("endDate",request.getParameter("endDate"));
        map.put("startDate",request.getParameter("startDate"));

        ActivityService actService= (ActivityService) ServiceFactory.getService(new ActivityServiceImpl());

        ViewObject<Activity> vo=actService.pageList(map);


        PrintJson.printJsonObj(response,vo);
    }

    private void save(HttpServletRequest request, HttpServletResponse response) {
        //添加市场信息
        Activity act=new Activity();
        String owner,name,startDate,endDate,cost,description,id,createTime,createBy;

        id= UUIDUtil.getUUID();
        owner=request.getParameter("owner");
        name=request.getParameter("name");
        startDate=request.getParameter("startDate");
        endDate=request.getParameter("endDate");
        cost=request.getParameter("cost");
        description=request.getParameter("description");
        //获取当前系统时间
        createTime= DateTimeUtil.getSysTime();
        //获取当前session中的用户
        createBy=((User)request.getSession().getAttribute("user")).getName();

        act.setId(id);
        act.setName(name);
        act.setOwner(owner);
        act.setStartDate(startDate);
        act.setEndDate(endDate);
        act.setCost(cost);
        act.setDescription(description);
        act.setCreateTime(createTime);
        act.setCreateBy(createBy);

        System.out.println("保存活动");

        ActivityService actService= (ActivityService) ServiceFactory.getService(new ActivityServiceImpl());

        boolean flag= false;

        try {
           flag = actService.insert(act);
            PrintJson.printJsonFlag(response,flag);
        } catch (LoginException e) {
            String wrong=e.getMessage();
            Map<String,Object> map=new HashMap<>();
            map.put("success",false);
            map.put("msg",wrong);
            PrintJson.printJsonObj(response,map);
        }
    }

    private void getUserList(HttpServletRequest request, HttpServletResponse response) {
        //查所有
        UserService userService= (UserService) ServiceFactory.getService(new UserServiceImpl());

        List<User>userList=userService.getAll();

        PrintJson.printJsonObj(response,userList);
    }

}
