package com.structurizr.component.example.repository;

import com.structurizr.component.example.domain.Customer;

import java.util.ArrayList;
import java.util.List;

public class CustomerRepositoryImpl implements CustomerRepository {

    @Override
    public List<Customer> getCustomers() {
        return new ArrayList<>();
    }

}