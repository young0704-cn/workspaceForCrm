package com.home.crm.workbench.dao;

import com.home.crm.settings.domain.User;
import com.home.crm.workbench.domain.ActivityRemark;

import java.util.List;

public interface ActivityRemarkDao {
    int delete(String[] ids);
    int select(String[] ids);

    List<ActivityRemark> remarkList(String id);

    int deleteRemark(String id);

    int saveRemark(ActivityRemark remark);

    List<ActivityRemark> getAllId();

    int updateRemark(ActivityRemark remark);
}
