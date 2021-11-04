package com.home.crm.workbench.dao;

import com.home.crm.workbench.domain.ClueRemark;

import java.util.List;

public interface ClueRemarkDao {

    List<ClueRemark> getCRD(String clueId);

    int delete(String clueId);
}
