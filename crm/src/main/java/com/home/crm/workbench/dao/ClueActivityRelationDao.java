package com.home.crm.workbench.dao;

import com.home.crm.workbench.domain.ClueActivityRelation;

public interface ClueActivityRelationDao {

    int deleteCAR(String id);

    int saveCAR(ClueActivityRelation car);
}
