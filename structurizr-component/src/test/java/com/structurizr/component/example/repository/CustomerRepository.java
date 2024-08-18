package com.structurizr.component.example.repository;

import com.structurizr.component.example.domain.Customer;

import java.util.List;

/**
 * Provides a way to access customer data.
 */
public interface CustomerRepository {

    List<Customer> getCustomers();

}