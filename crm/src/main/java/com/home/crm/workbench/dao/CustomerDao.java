package com.home.crm.workbench.dao;

import com.home.crm.workbench.domain.Customer;

import java.util.List;

public interface CustomerDao {

    Customer getCustomer(String company);

    int save(Customer customer);

    List<Customer> getCustomerName(String name);
}
