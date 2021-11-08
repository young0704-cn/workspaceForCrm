package com.home.crm.workbench.service;

import com.home.crm.exception.LoginException;
import com.home.crm.settings.domain.User;
import com.home.crm.workbench.domain.Tran;
import com.home.crm.workbench.domain.TranHistory;
import com.home.crm.workbench.vo.ViewObject;

import java.util.List;
import java.util.Map;

public interface TranService {
    List<User> add();

    List<String> getCustomerName(String name);

    ViewObject<Tran> tranList(Map<String, Object> map);

    boolean save(Tran tran, String customerName);

    Tran getById(String id);

    List<TranHistory> tranHistoryList(String id);

    List<Map<String, Object>> tranPieChart();
}
