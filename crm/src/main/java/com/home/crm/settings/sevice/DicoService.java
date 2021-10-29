package com.home.crm.settings.sevice;

import com.home.crm.settings.domain.DicValue;

import java.util.List;
import java.util.Map;

public interface DicoService {
    Map<String, List<DicValue>> getDVList();
}
