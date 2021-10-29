package com.home.crm.settings.sevice.impl;

import com.home.crm.settings.dao.DicTypeDao;
import com.home.crm.settings.dao.DicValueDao;
import com.home.crm.settings.domain.DicType;
import com.home.crm.settings.domain.DicValue;
import com.home.crm.settings.sevice.DicoService;
import com.home.crm.utils.SqlSessionUtil;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DicServiceImp implements DicoService {
    DicTypeDao typeDao = SqlSessionUtil.getSqlSession().getMapper(DicTypeDao.class);
    DicValueDao valueDao=SqlSessionUtil.getSqlSession().getMapper(DicValueDao.class);

    @Override
    public Map<String, List<DicValue>> getDVList() {

        Map<String, List<DicValue>> map = new HashMap<>();
        //获取所有字典类型
        List<DicType> dicTypeList=typeDao.getAll();

        for (DicType dt:dicTypeList){
            //遍历字典类型list集合获取code,然后查询  字典值  从表tbl_dic_value  通过typecode=code
            List<DicValue> dicValueList=valueDao.getValue(dt.getCode());
            //将获取到的字典值list集合,存入map并返回
            map.put(dt.getCode(),dicValueList);
        }

        return map;
    }
}
