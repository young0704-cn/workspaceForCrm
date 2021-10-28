package com.home.crm.workbench.dao;

import com.home.crm.workbench.domain.Activity;
import com.home.crm.workbench.vo.ViewObject;

import java.util.List;
import java.util.Map;

public interface ActivityDao {
    int insert(Activity activity);
    List<Activity> pageList(Map<String,Object> map);
    int getTotal(Map<String,Object> map);
    int delete(String[] ids);

    Activity getActivityById(String id);
    int update(Map<String, Object> map);

    Activity detail(String id);
}
