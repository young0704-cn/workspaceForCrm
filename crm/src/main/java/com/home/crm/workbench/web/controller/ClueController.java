package com.home.crm.workbench.web.controller;

import com.home.crm.exception.LoginException;
import com.home.crm.settings.domain.User;
import com.home.crm.settings.sevice.UserService;
import com.home.crm.settings.sevice.impl.UserServiceImpl;
import com.home.crm.utils.DateTimeUtil;
import com.home.crm.utils.PrintJson;
import com.home.crm.utils.ServiceFactory;
import com.home.crm.utils.UUIDUtil;
import com.home.crm.workbench.domain.Activity;
import com.home.crm.workbench.domain.Clue;
import com.home.crm.workbench.domain.ClueActivityRelation;
import com.home.crm.workbench.domain.Tran;
import com.home.crm.workbench.service.ActivityService;
import com.home.crm.workbench.service.ClueService;
import com.home.crm.workbench.service.impl.ActivityServiceImpl;
import com.home.crm.workbench.service.impl.ClueServiceImpl;
import com.home.crm.workbench.vo.ViewObject;
import org.omg.CORBA.StringHolder;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.*;

public class ClueController extends HttpServlet {
    @Override
    protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        if("/clue/getUserList.do".equals(request.getServletPath())){
            getUserList(request,response);
        }else if ("/club/save.do".equals(request.getServletPath())){
            save(request,response);
        }else if ("/club/pageList.do".equals(request.getServletPath())){
            pageList(request,response);
        } else if ("/clue/detail.do".equals(request.getServletPath())){
            detail(request,response);
        }else if ("/clue/showActivityList.do".equals(request.getServletPath())){
            showActivityList(request,response);
        }else if ("/clue/deleteCAR.do".equals(request.getServletPath())){
            deleteCAR(request,response);
        }else if ("/clue/activityListCAR.do".equals(request.getServletPath())){
            activityListCAR(request,response);
        }else if ("/clue/saveCAR.do".equals(request.getServletPath())){
            saveCAR(request,response);
        }else if ("/clue/activityList.do".equals(request.getServletPath())){
            activityList(request,response);
        }else if ("/clue/convert.do".equals(request.getServletPath())){
            convert(request,response);
        }
    }

    private void convert(HttpServletRequest request, HttpServletResponse response) throws IOException {
        ClueService service = (ClueService) ServiceFactory.getService(new ClueServiceImpl());
        String clueId=request.getParameter("clueId");
        String flg=request.getParameter("flg");
        String activityId,money,name,expectedDate,stage,id;
        String createBy=((User)request.getSession().getAttribute("user")).getName();
        boolean success=false;
        Tran tran=null;

        if ("1".equals(flg)){
            id=UUIDUtil.getUUID();
            activityId=request.getParameter("activityId");
            money=request.getParameter("money");
            name=request.getParameter("name");
            expectedDate=request.getParameter("expectedDate");
            stage=request.getParameter("stage");

            tran=new Tran();
            tran.setId(id);
            tran.setActivityId(activityId);
            tran.setMoney(money);
            tran.setName(name);
            tran.setExpectedDate(expectedDate);
            tran.setStage(stage);
            tran.setCreateBy(createBy);
        }
        success=service.convert(tran,clueId,createBy);
        if (success){
            response.sendRedirect(request.getContextPath()+"/workbench/clue/index.jsp");
        }
    }

    private void activityList(HttpServletRequest request, HttpServletResponse response) {
        ActivityService service = (ActivityService) ServiceFactory.getService(new ActivityServiceImpl());
        try {
            List<Activity> activityList=service.activityList(request.getParameter("aname"));
            Map<String,Object> map=new HashMap<>();
            map.put("activityList",activityList);
            map.put("success",true);
            PrintJson.printJsonObj(response,map);
        } catch (LoginException e) {
            Map<String,Object> map=new HashMap<>();
            map.put("msg",e.getMessage());
            map.put("success",false);
            PrintJson.printJsonObj(response,map);
        }

    }

    private void saveCAR(HttpServletRequest request, HttpServletResponse response) {
        ClueService service = (ClueService) ServiceFactory.getService(new ClueServiceImpl());
        boolean flag =true;
        String []ids= request.getParameter("ids").split(",");
        String clueId=request.getParameter("clueId");

        ClueActivityRelation car=new ClueActivityRelation();

        for (String id : ids) {
            car.setClueId(clueId);
            car.setId(UUIDUtil.getUUID());
            car.setActivityId(id);
            if (!service.saveCAR(car)) {
                flag = false;
            }
        }
        PrintJson.printJsonFlag(response,flag);
    }

    private void activityListCAR(HttpServletRequest request, HttpServletResponse response) {
        ActivityService service = (ActivityService) ServiceFactory.getService(new ActivityServiceImpl());
        Map<String,Object> map = new HashMap<>();
        map.put("aname",request.getParameter("aname"));
        map.put("clueId",request.getParameter("clueId"));
        try {
            List<Activity> acList=service.activityListCAR(map);
            Map<String,Object> map1 = new HashMap<>();
            map1.put("success",true);
            map1.put("acList",acList);
            PrintJson.printJsonObj(response,map1);
        } catch (LoginException e) {
            Map<String,Object> map1 = new HashMap<>();
            map1.put("success",false);
            map1.put("msg",e.getMessage());
            PrintJson.printJsonObj(response,map1);
        }
    }

    private void deleteCAR(HttpServletRequest request, HttpServletResponse response) {
        ClueService service = (ClueService) ServiceFactory.getService(new ClueServiceImpl());
        boolean flag=service.deleteCAR(request.getParameter("id"));
        PrintJson.printJsonFlag(response,flag);
    }

    private void showActivityList(HttpServletRequest request, HttpServletResponse response) {
        ClueService service= (ClueService) ServiceFactory.getService(new ClueServiceImpl());
        List<Activity> activityList=service.showActivityList(request.getParameter("clueId"));
        PrintJson.printJsonObj(response,activityList);
    }

    private void detail(HttpServletRequest request, HttpServletResponse response) {
        ClueService service= (ClueService) ServiceFactory.getService(new ClueServiceImpl());
        String id=request.getParameter("id");
        Clue clue=service.detail(id);
        request.setAttribute("clue",clue);
        try {
            request.getRequestDispatcher("/workbench/clue/detail.jsp").forward(request,response);
        } catch (ServletException | IOException e) {
            e.printStackTrace();
        }
    }

    private void pageList(HttpServletRequest request, HttpServletResponse response) {
        ClueService service= (ClueService) ServiceFactory.getService(new ClueServiceImpl());
        int pageNo= Integer.parseInt(request.getParameter("pageNo"));
        int pageSize= Integer.parseInt(request.getParameter("pageSize"));
        Integer skipCount=(pageNo-1)*pageSize;
        Map<String,Object> map=new HashMap<>();
        map.put("fullname",request.getParameter("fullname"));
        map.put("phone",request.getParameter("phone"));
        map.put("mphone",request.getParameter("mphone"));
        map.put("source",request.getParameter("source"));
        map.put("state",request.getParameter("state"));
        map.put("owner",request.getParameter("owner"));
        map.put("company",request.getParameter("company"));
        map.put("skipCount",skipCount);
        map.put("pageSize",pageSize);

        ViewObject<Clue> vo=service.pageList(map);


        PrintJson.printJsonObj(response,vo);
    }

    private void save(HttpServletRequest request, HttpServletResponse response) {
        ClueService service= (ClueService) ServiceFactory.getService(new ClueServiceImpl());
        Clue clue =new Clue();
        clue.setAddress(request.getParameter("address"));
        clue.setAppellation(request.getParameter("appellation"));
        clue.setWebsite(request.getParameter("website"));
        clue.setState(request.getParameter("state"));
        clue.setSource(request.getParameter("source"));
        clue.setPhone(request.getParameter("phone"));
        clue.setOwner(request.getParameter("owner"));
        clue.setNextContactTime(request.getParameter("nextContactTime"));
        clue.setMphone(request.getParameter("mphone"));
        clue.setJob(request.getParameter("job"));
        clue.setId(UUIDUtil.getUUID());
        clue.setFullname(request.getParameter("fullname"));
        clue.setEmail(request.getParameter("email"));
        clue.setDescription(request.getParameter("description"));
        clue.setCreateTime(DateTimeUtil.getSysTime());
        User user=(User)request.getSession().getAttribute("user");
        clue.setCreateBy(user.getName());
        clue.setContactSummary(request.getParameter("contactSummary"));
        clue.setCompany(request.getParameter("company"));

        if (service.save(clue)){
            PrintJson.printJsonFlag(response,true);
        }else {
            PrintJson.printJsonFlag(response,false);
        }
    }

    private void getUserList(HttpServletRequest request, HttpServletResponse response) {
        UserService userService = (UserService) ServiceFactory.getService(new UserServiceImpl());
        List<User> userList=userService.getAll();
        PrintJson.printJsonObj(response,userList);
    }
}
