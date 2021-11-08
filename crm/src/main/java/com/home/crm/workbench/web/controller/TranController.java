package com.home.crm.workbench.web.controller;

import com.home.crm.exception.LoginException;
import com.home.crm.settings.domain.User;
import com.home.crm.utils.DateTimeUtil;
import com.home.crm.utils.PrintJson;
import com.home.crm.utils.ServiceFactory;
import com.home.crm.utils.UUIDUtil;
import com.home.crm.workbench.dao.TranHistoryDao;
import com.home.crm.workbench.domain.Tran;
import com.home.crm.workbench.domain.TranHistory;
import com.home.crm.workbench.service.TranService;
import com.home.crm.workbench.service.impl.TranServiceImpl;
import com.home.crm.workbench.vo.ViewObject;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.ws.spi.http.HttpContext;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class TranController extends HttpServlet {
    @Override
    protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        if ("/tran/add.do".equals(request.getServletPath())){
            add(request,response);
        }else if ("/tran/getCustomerName.do".equals(request.getServletPath())){
            getCustomerName(request,response);
        }else if ("/tran/tranList.do".equals(request.getServletPath())){
            tranList(request,response);
        }else if ("/tran/save.do".equals(request.getServletPath())){
            save(request,response);
        }else if ("/tran/detail.do".equals(request.getServletPath())){
            detail(request,response);
        }else if ("/tran/tranHistoryList.do".equals(request.getServletPath())){
            tranHistoryList(request,response);
        }else if ("/tran/tranPieChart.do".equals(request.getServletPath())){
            tranPieChart(request,response);
        }
    }

    private void tranPieChart(HttpServletRequest request, HttpServletResponse response) {
        TranService service=(TranService) ServiceFactory.getService(new TranServiceImpl());
        List<Map<String,Object>> maps=service.tranPieChart();
        PrintJson.printJsonObj(response,maps);
    }

    private void tranHistoryList(HttpServletRequest request, HttpServletResponse response) {
        TranService service = (TranService) ServiceFactory.getService(new TranServiceImpl());
        List<TranHistory> tranHistoryList=service.tranHistoryList(request.getParameter("id"));
        Map<String,String> pmap = (Map<String, String>) request.getServletContext().getAttribute("pmap");
        for (TranHistory tranHistory:tranHistoryList){
            tranHistory.setPossible(pmap.get(tranHistory.getStage()));
        }
        PrintJson.printJsonObj(response,tranHistoryList);
    }

    private void detail(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        TranService service= (TranService) ServiceFactory.getService(new TranServiceImpl());
        String id=request.getParameter("id");
        Tran tran=service.getById(id);
        request.setAttribute("tran",tran);
        //处理可能性的值
        ServletContext application=request.getServletContext();
        Map<String,String> pmap= (Map<String, String>) application.getAttribute("pmap");
        request.setAttribute("possible",pmap.get(tran.getStage()));
        request.getRequestDispatcher("/workbench/transaction/detail.jsp").forward(request,response);
    }

    private void save(HttpServletRequest request, HttpServletResponse response) throws IOException {
        TranService service = (TranService) ServiceFactory.getService(new TranServiceImpl());
        Tran tran = new Tran();
        tran.setId(UUIDUtil.getUUID());//id
        tran.setOwner(request.getParameter("transactionOwner"));//owner
        tran.setMoney(request.getParameter("amountOfMoney"));//money
        tran.setName(request.getParameter("transactionName"));//name
        tran.setExpectedDate(request.getParameter("transactionExpectedDate"));//expectedDate
        tran.setStage(request.getParameter("transactionStage"));//stage
        tran.setType(request.getParameter("transactionType"));//type
        tran.setSource(request.getParameter("clueSource"));//source
        tran.setActivityId(request.getParameter("activityName"));//activityId
        tran.setContactsId(request.getParameter("contactsName"));//contactsId
        tran.setCreateBy(((User)request.getSession().getAttribute("user")).getName());//createBy
        tran.setCreateTime(DateTimeUtil.getSysTime());//createTime
        tran.setDescription(request.getParameter("describe"));//description
        tran.setContactSummary(request.getParameter("contactSummary"));//contactSummary
        tran.setNextContactTime(request.getParameter("nextContactTime"));//nextContactTime
        String customerName =request.getParameter("customerName");
        boolean flg=service.save(tran,customerName);
        System.out.println(flg);
        if (flg){
            response.sendRedirect(request.getContextPath()+"/workbench/transaction/index.jsp");
        }
    }

    private void tranList(HttpServletRequest request, HttpServletResponse response) {
        TranService service = (TranService) ServiceFactory.getService(new TranServiceImpl());
        String pageNoStr=request.getParameter("pageNo");
        String pageSizeStr=request.getParameter("pageSize");

        int pageNo=Integer.parseInt(pageNoStr);
        int pageSize=Integer.parseInt(pageSizeStr);
        int skipCount=(pageNo-1)*pageSize;

        Map<String,Object> map=new HashMap<>();
        map.put("skipCount",skipCount);
        map.put("pageSize",pageSize);
        ViewObject<Tran> vo=service.tranList(map);
        PrintJson.printJsonObj(response,vo);
    }

    private void getCustomerName(HttpServletRequest request, HttpServletResponse response) {
        TranService service = (TranService) ServiceFactory.getService(new TranServiceImpl());
        List<String> stringList=service.getCustomerName(request.getParameter("name"));
        PrintJson.printJsonObj(response,stringList);
    }

    private void add(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        TranService service = (TranService) ServiceFactory.getService(new TranServiceImpl());
        List<User> userList=service.add();
        request.setAttribute("userList",userList);
        request.getRequestDispatcher("/workbench/transaction/save.jsp").forward(request,response);
    }
}
