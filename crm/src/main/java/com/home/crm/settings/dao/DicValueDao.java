package com.home.crm.settings.dao;

import com.home.crm.settings.domain.DicValue;

import java.util.List;

public interface DicValueDao {
    List<DicValue> getValue(String code);
}
