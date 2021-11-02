package com.home.crm.workbench.dao;

import com.home.crm.workbench.domain.Clue;

import java.util.List;
import java.util.Map;

public interface ClueDao {


    int save(Clue clue);

    List<Clue> pageList(Map<String, Object> map);

    int getTotal(Map<String, Object> map);

    Clue getById(String id);
}
