package com.home.crm.workbench.service.impl;

import com.home.crm.utils.SqlSessionUtil;
import com.home.crm.workbench.dao.ActivityDao;
import com.home.crm.workbench.dao.ClueActivityRelationDao;
import com.home.crm.workbench.dao.ClueDao;
import com.home.crm.workbench.domain.Activity;
import com.home.crm.workbench.domain.Clue;
import com.home.crm.workbench.domain.ClueActivityRelation;
import com.home.crm.workbench.service.ClueService;
import com.home.crm.workbench.vo.ViewObject;

import java.util.List;
import java.util.Map;

public class ClueServiceImpl implements ClueService {
    ClueDao clueDao= SqlSessionUtil.getSqlSession().getMapper(ClueDao.class);
    ActivityDao activityDao=SqlSessionUtil.getSqlSession().getMapper(ActivityDao.class);

    @Override
    public boolean save(Clue clue) {
        if (1==clueDao.save(clue)){
            return true;
        }
        return false;
    }

    @Override
    public ViewObject<Clue> pageList(Map<String, Object> map) {
        ViewObject<Clue> vo=new ViewObject<>();
        List<Clue> clueList=clueDao.pageList(map);

        int total=clueDao.getTotal(map);
        vo.setDataList(clueList);
        vo.setTotal(total);
        return vo;
    }

    @Override
    public  Clue  detail(String id) {
        Clue clue=clueDao.getById(id);

        return clue;
    }

    @Override
    public List<Activity> showActivityList(String clueId) {
        return activityDao.getActivityList(clueId);
    }

    @Override
    public boolean deleteCAR(String id) {
        ClueActivityRelationDao carDao=SqlSessionUtil.getSqlSession().getMapper(ClueActivityRelationDao.class);
        return 1 == carDao.deleteCAR(id);
    }

    @Override
    public Boolean saveCAR(ClueActivityRelation car) {
        ClueActivityRelationDao carDao = SqlSessionUtil.getSqlSession().getMapper(ClueActivityRelationDao.class);
        return 1==carDao.saveCAR(car);
    }
}
