package com.home.crm.workbench.service.impl;

import com.home.crm.utils.DateTimeUtil;
import com.home.crm.utils.SqlSessionUtil;
import com.home.crm.utils.UUIDUtil;
import com.home.crm.workbench.dao.*;
import com.home.crm.workbench.domain.*;
import com.home.crm.workbench.service.ClueService;
import com.home.crm.workbench.vo.ViewObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ClueServiceImpl implements ClueService {
    ClueDao clueDao= SqlSessionUtil.getSqlSession().getMapper(ClueDao.class);
    ActivityDao activityDao=SqlSessionUtil.getSqlSession().getMapper(ActivityDao.class);

    @Override
    public boolean save(Clue clue) {
        return 1 == clueDao.save(clue);
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

        return clueDao.getById(id);
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

    @Override
    public boolean convert(Tran tran, String clueId, String createBy) {
        CustomerDao customerDao=SqlSessionUtil.getSqlSession().getMapper(CustomerDao.class);
        boolean flg=true;
        String createTime= DateTimeUtil.getSysTime();
//1、获取clue线索相关信息
        Clue clue=clueDao.getById(clueId);
        String company=clue.getCompany();
//2、通过company字段去Customer表中查看是否存在该客户,完成客户表的创建
        Customer customer=customerDao.getCustomer(company);
        if (customer==null){
            customer=new Customer();
            String id = UUIDUtil.getUUID();
            customer.setWebsite(clue.getWebsite());
            customer.setPhone(clue.getPhone());
            customer.setOwner(clue.getOwner());
            customer.setNextContactTime(clue.getNextContactTime());
            customer.setName(company);
            customer.setId(id);
            customer.setDescription(clue.getDescription());
            customer.setCreateBy(createBy);
            customer.setCreateTime(createTime);
            customer.setContactSummary(clue.getContactSummary());
            customer.setAddress(clue.getAddress());

            if(1!=customerDao.save(customer)){
                flg = false;
            }
        }
//3、同一个客户，可以存在多个联系人。所以直接添加就行，不需要判断。创建联系人
        ContactsDao contactsDao = SqlSessionUtil.getSqlSession().getMapper(ContactsDao.class);
        Contacts contacts=new Contacts();
        contacts.setAddress(clue.getAddress());
        contacts.setSource(clue.getSource());
        contacts.setOwner(clue.getOwner());
        contacts.setNextContactTime(clue.getNextContactTime());
        contacts.setMphone(clue.getMphone());
        contacts.setJob(clue.getJob());
        contacts.setId(UUIDUtil.getUUID());
        contacts.setFullname(clue.getFullname());
        contacts.setEmail(clue.getEmail());
        contacts.setDescription(clue.getDescription());
        contacts.setCustomerId(customer.getId());
        contacts.setCreateTime(createTime);
        contacts.setCreateBy(createBy);
        contacts.setAppellation(clue.getAppellation());
        contacts.setContactSummary(clue.getContactSummary());

        if (1!=contactsDao.save(contacts)){
            flg = false;
        }
//4、处理clue_remark信息，将备注信息存入联系人备注,客户备注
        ClueRemarkDao clueRemarkDao = SqlSessionUtil.getSqlSession().getMapper(ClueRemarkDao.class);
        ContactsRemarkDao contactsRemarkDao =SqlSessionUtil.getSqlSession().getMapper(ContactsRemarkDao.class);
        CustomerRemarkDao customerRemarkDao = SqlSessionUtil.getSqlSession().getMapper(CustomerRemarkDao.class);

        List<ClueRemark> clueRemarkList=clueRemarkDao.getCRD(clueId);

        if (0!=clueRemarkList.size()){
            for (ClueRemark clueRemark:clueRemarkList){
                ContactsRemark contactsRemark=new ContactsRemark();
                contactsRemark.setContactsId(contacts.getId());
                contactsRemark.setCreateBy(createBy);
                contactsRemark.setCreateTime(createTime);
                contactsRemark.setNoteContent(clueRemark.getNoteContent());
                contactsRemark.setId(UUIDUtil.getUUID());
                contactsRemark.setEditFlag("0");

                CustomerRemark customerRemark=new CustomerRemark();
                customerRemark.setCustomerId(customer.getId());
                customerRemark.setCreateBy(createBy);
                customerRemark.setCreateTime(createTime);
                customerRemark.setNoteContent(clueRemark.getNoteContent());
                customerRemark.setId(UUIDUtil.getUUID());
                customerRemark.setEditFlag("0");
                if (1!=contactsRemarkDao.save(contactsRemark)){
                    flg = false;
                }
                if (1!=customerRemarkDao.save(customerRemark)){
                    flg = false;
                }
            }
        }
//5、将线索市场关系表转换为联系人市场关系表
        ClueActivityRelationDao clueActivityRelationDao = SqlSessionUtil.getSqlSession().getMapper(ClueActivityRelationDao.class);
        ContactsActivityRelationDao contactsActivityRelationDao = SqlSessionUtil.getSqlSession().getMapper(ContactsActivityRelationDao.class);

         List<ClueActivityRelation> clueActivityRelationList= clueActivityRelationDao.getCAR(clueId);
         if (0!=clueActivityRelationList.size()){
             for (ClueActivityRelation clueActivityRelation:clueActivityRelationList){
                 ContactsActivityRelation contactsActivityRelation=new ContactsActivityRelation();
                 contactsActivityRelation.setActivityId(clueActivityRelation.getActivityId());
                 contactsActivityRelation.setId(UUIDUtil.getUUID());
                 contactsActivityRelation.setContactsId(contacts.getId());

                 if (1!=contactsActivityRelationDao.save(contactsActivityRelation)){
                     flg = false;
                 }
             }
         }
 //6、如果有创建交易需求，创建一条交易
        if (tran!=null){
//           tran中已经封装的属性 id,createBy,activityId,money,name,expectedDate,stage  封装其他属性
            tran.setOwner(clue.getOwner());
            tran.setCustomerId(customer.getId());
            tran.setSource(clue.getSource());
            tran.setContactsId(contacts.getId());
            tran.setCreateTime(createTime);
            tran.setDescription(clue.getDescription());
            tran.setContactSummary(clue.getContactSummary());
            tran.setNextContactTime(clue.getNextContactTime());

            TranDao tranDao = SqlSessionUtil.getSqlSession().getMapper(TranDao.class);
            if (tranDao.save(tran)!=1){
                flg = false;
            }
            //7、如果创建交易，那么还需要创建交易历史
            TranHistoryDao tranHistoryDao = SqlSessionUtil.getSqlSession().getMapper(TranHistoryDao.class);
            TranHistory tranHistory =new TranHistory();

            tranHistory.setCreateTime(createTime);
            tranHistory.setCreateBy(createBy);
            tranHistory.setId(UUIDUtil.getUUID());
            tranHistory.setTranId(tran.getId());
            tranHistory.setExpectedDate(tran.getExpectedDate());
            tranHistory.setStage(tran.getStage());
            tranHistory.setMoney(tran.getMoney());

            if (tranHistoryDao.save(tranHistory)!=1){
                flg = false;
            }
        }
//8、删除线索备注
        if (clueRemarkDao.delete(clueId)!=1){
            flg = false;
        }
//9、删除市场活动和线索关系表
        if (clueActivityRelationDao.delete(clueId)!=1){
            flg = false;
        }
//10、删除线索
        if (clueDao.delete(clueId)!=1){
            flg = false;
        }
        return flg;
    }
}
