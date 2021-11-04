package com.home.crm.workbench.service;

import com.home.crm.exception.LoginException;
import com.home.crm.settings.domain.User;
import com.home.crm.workbench.domain.Activity;
import com.home.crm.workbench.domain.ActivityRemark;
import com.home.crm.workbench.vo.ViewObject;

import java.util.List;
import java.util.Map;

public interface ActivityService {

    boolean insert(Activity activity) throws LoginException;
    ViewObject<Activity> pageList(Map<String,Object> map);
    void delete(String[] ids) throws LoginException;
    Map<String,Object> edit(String id);
    Activity detail(String id);
    void update(Map<String, Object> map) throws LoginException;

    List<ActivityRemark> remarkList(String id);

    void deleteRemark(String id) throws LoginException;

    List<ActivityRemark> saveRemark(ActivityRemark remark) throws LoginException;

    List<ActivityRemark> updateRemark(ActivityRemark remark) throws LoginException;

    List<Activity> activityListCAR(Map<String, Object> map) throws LoginException;

    List<Activity> activityList(String aname) throws LoginException;
}
