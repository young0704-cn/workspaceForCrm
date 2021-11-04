package com.home.crm.workbench.dao;

import com.home.crm.workbench.domain.Customer;

public interface CustomerDao {

    Customer getCustomer(String company);

    int save(Customer customer);
}
