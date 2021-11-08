package com.home.crm.workbench.dao;

import com.home.crm.workbench.domain.Tran;
import com.home.crm.workbench.domain.TranHistory;

import java.util.List;

public interface TranHistoryDao {

    int save(TranHistory tranHistory);

    List<TranHistory> getByTranId(String id);
}
