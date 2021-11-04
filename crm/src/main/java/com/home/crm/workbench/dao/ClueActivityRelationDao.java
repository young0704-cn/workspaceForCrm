package com.home.crm.workbench.dao;

import com.home.crm.workbench.domain.ClueActivityRelation;

import java.util.List;

public interface ClueActivityRelationDao {

    int deleteCAR(String id);

    int saveCAR(ClueActivityRelation car);

    List<ClueActivityRelation> getCAR(String clueId);

    int delete(String clueId);
}
