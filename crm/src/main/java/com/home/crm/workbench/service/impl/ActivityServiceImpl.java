package com.home.crm.workbench.service.impl;

import com.home.crm.exception.LoginException;
import com.home.crm.settings.dao.UserDao;
import com.home.crm.settings.domain.User;
import com.home.crm.utils.SqlSessionUtil;
import com.home.crm.workbench.dao.ActivityDao;
import com.home.crm.workbench.dao.ActivityRemarkDao;
import com.home.crm.workbench.domain.Activity;
import com.home.crm.workbench.domain.ActivityRemark;
import com.home.crm.workbench.service.ActivityService;
import com.home.crm.workbench.vo.ViewObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ActivityServiceImpl implements ActivityService {
    private final ActivityDao activityDao = SqlSessionUtil.getSqlSession().getMapper(ActivityDao.class);
    private final ActivityRemarkDao actRDao = SqlSessionUtil.getSqlSession().getMapper(ActivityRemarkDao.class);
    private final UserDao userDao=SqlSessionUtil.getSqlSession().getMapper(UserDao.class);
    @Override
    public boolean insert(Activity activity) throws LoginException {
        int count =activityDao.insert(activity);
        if (count!=1){
            throw new LoginException("添加活动信息失败");
        }
        return true;
    }

    @Override

    public ViewObject<Activity> pageList(Map<String, Object> map) {
        ViewObject<Activity> vo=new ViewObject<>();

        List<Activity> actList=activityDao.pageList(map);
        int total=activityDao.getTotal(map);

        vo.setDataList(actList);
        vo.setTotal(total);
        return vo;
    }

    @Override
    public void delete(String[] ids) throws LoginException {
        /*
            删除活动信息前，要将其关联的备注进行删除操作
            为确保删除成功，1、先查询出需要删除的备注数量，2、再返回受到删除操作影响的备注数量。3、然后进行对比，一致则表示备注删除成功。不一致，删除失败需要回滚
        */
        //1、
        int count2=actRDao.select(ids);
        //2、
        int count1=actRDao.delete(ids);
        //3、
        if (count1!=count2){
            throw new LoginException("删除备注信息时,数据量不一致");
        }
        //删除市场活动信息
        int count3=activityDao.delete(ids);
        if (ids.length!=count3){
            throw new LoginException("删除市场活动信息时,数据量不一致");
        }
    }

    @Override
    public Map<String, Object> edit(String id) {
        List<User> uList=userDao.getAll();
        Activity activity=activityDao.getActivityById(id);
        Map<String,Object> map=new HashMap<>();
        map.put("uList",uList);
        map.put("activity",activity);
        return map;
    }

    @Override
    public Activity detail(String id) {
        Activity activity=activityDao.detail(id);
        return activity;
    }

    @Override
    public void update(Map<String, Object> map) throws LoginException {
        String startDate= (String) map.get("startDate");
        String endtDate= (String) map.get("endDate");
        if (startDate.compareTo(endtDate)>0){
            throw new LoginException("更新失败,请注意活动开始日期与结束日期");
        }
        int count =activityDao.update(map);
        if (count!=1){
            throw new LoginException("更新失败,未找到该活动信息");
        }
    }

    @Override
    public List<ActivityRemark> remarkList(String id) {
        List<ActivityRemark> aRList=actRDao.remarkList(id);
        return aRList;
    }

    @Override
    public void deleteRemark(String id) throws LoginException {
        if (actRDao.deleteRemark(id)!=1){
            throw new LoginException("删除失败,数据异常");
        }
    }

    @Override
    public  List<ActivityRemark> saveRemark(ActivityRemark remark) throws LoginException {
        if (actRDao.saveRemark(remark)!=1){
            throw new LoginException("备注保存失败");
        }
        return actRDao.getAllId();
    }

    @Override
    public List<ActivityRemark> updateRemark(ActivityRemark remark) throws LoginException {
        if (actRDao.updateRemark(remark)!=1){
            throw new LoginException("备注更新失败");
        }
        return actRDao.getAllId();
    }

    @Override
    public List<Activity> activityListCAR(Map<String, Object> map) throws LoginException {
        List<Activity>  activityList= activityDao.activityListCAR(map);
        if (activityList.size()==0){
            throw new LoginException("未找到相关活动");
        }
        return activityList;
    }

    @Override
    public List<Activity> activityList(String aname) throws LoginException {
            List<Activity> activityList=activityDao.activityList(aname);
        if (activityList.size()==0){
            throw new LoginException("未找到相关活动");
        }
        return activityList;
    }
}
