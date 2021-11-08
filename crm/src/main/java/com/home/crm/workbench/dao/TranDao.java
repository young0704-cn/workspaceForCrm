package com.home.crm.workbench.dao;

import com.home.crm.workbench.domain.Tran;

import java.util.List;
import java.util.Map;

public interface TranDao {

    int save(Tran tran);

    int getTotal();

    List<Tran> tranList(Map<String, Object> map);

    Tran getById(String id);

    List<Map<String, Object>> tranPieChart();
}
