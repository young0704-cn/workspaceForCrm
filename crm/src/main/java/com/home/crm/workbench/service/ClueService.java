package com.home.crm.workbench.service;

import com.home.crm.workbench.domain.Activity;
import com.home.crm.workbench.domain.Clue;
import com.home.crm.workbench.domain.ClueActivityRelation;
import com.home.crm.workbench.domain.Tran;
import com.home.crm.workbench.vo.ViewObject;

import java.util.List;
import java.util.Map;

public interface ClueService {

    boolean save(Clue clue);

    ViewObject<Clue> pageList(Map<String, Object> map);

    Clue detail(String id);

    List<Activity> showActivityList(String clueId);

    boolean deleteCAR(String id);

    Boolean saveCAR(ClueActivityRelation car);

    boolean convert(Tran tran, String clueId, String createBy);
}
