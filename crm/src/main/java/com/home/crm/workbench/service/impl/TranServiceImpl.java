package com.home.crm.workbench.service.impl;

import com.home.crm.exception.LoginException;
import com.home.crm.settings.dao.UserDao;
import com.home.crm.settings.domain.User;
import com.home.crm.utils.SqlSessionUtil;
import com.home.crm.utils.UUIDUtil;
import com.home.crm.workbench.dao.CustomerDao;
import com.home.crm.workbench.dao.TranDao;
import com.home.crm.workbench.dao.TranHistoryDao;
import com.home.crm.workbench.domain.Customer;
import com.home.crm.workbench.domain.Tran;
import com.home.crm.workbench.domain.TranHistory;
import com.home.crm.workbench.service.TranService;
import com.home.crm.workbench.vo.ViewObject;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class TranServiceImpl implements TranService {
    UserDao userDao=SqlSessionUtil.getSqlSession().getMapper(UserDao.class);
    CustomerDao customerDao = SqlSessionUtil.getSqlSession().getMapper(CustomerDao.class);
    TranDao tranDao = SqlSessionUtil.getSqlSession().getMapper(TranDao.class);
    TranHistoryDao tranHistoryDao = SqlSessionUtil.getSqlSession().getMapper(TranHistoryDao.class);
    @Override
    public List<User> add() {
        return userDao.getAll();
    }

    @Override
    public List<String> getCustomerName(String name) {
        List<Customer> customerList=customerDao.getCustomerName(name);
        List<String> stringList=new LinkedList<>();
        for(Customer customer:customerList){
            stringList.add(customer.getName());
        }
        for (String string:stringList){
            System.out.println(string);
        }
        return stringList;
    }

    @Override
    public ViewObject<Tran> tranList(Map<String, Object> map){
        ViewObject<Tran> vo=new ViewObject<>();
        int total=tranDao.getTotal();
        vo.setDataList(tranDao.tranList(map));
        vo.setTotal(total);
        return vo;
    }

    @Override
    public boolean save(Tran tran, String customerName) {
        boolean flg = true;
        Customer customer=customerDao.getCustomer(customerName);
        if (customer==null){//根据未名字找到客户，创建新的客户
            customer=new Customer();
            customer.setId(UUIDUtil.getUUID());
            customer.setName(customerName);
            customer.setContactSummary(tran.getContactSummary());
            customer.setCreateTime(tran.getCreateTime());
            customer.setCreateBy(tran.getCreateBy());

            customerDao.save(customer);
        }
        tran.setCustomerId(customer.getId());

        if (1!=tranDao.save(tran)){
            flg=false;
        }
        TranHistory tranHistory=new TranHistory();
        tranHistory.setMoney(tran.getMoney());
        tranHistory.setCreateTime(tran.getCreateTime());
        tranHistory.setCreateBy(tran.getCreateBy());
        tranHistory.setTranId(tran.getId());
        tranHistory.setStage(tran.getStage());
        tranHistory.setExpectedDate(tran.getExpectedDate());
        tranHistory.setId(UUIDUtil.getUUID());

        if (1!=tranHistoryDao.save(tranHistory)){
            flg=false;
        }
        return flg;
    }

    @Override
    public Tran getById(String id) {
        return tranDao.getById(id);
    }

    @Override
    public List<TranHistory> tranHistoryList(String id) {
        return tranHistoryDao.getByTranId(id);
    }

    @Override
    public List<Map<String, Object>> tranPieChart() {
        return tranDao.tranPieChart();
    }
}
