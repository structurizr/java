package com.structurizr.component.example.controller;

import com.structurizr.component.example.repository.CustomerRepository;

/**
 * Allows users to view a list of customers.
 */
public class CustomerController {

    private CustomerRepository customerRepository;

    public CustomerController(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    void showCustomersPage() {
        customerRepository.getCustomers();
    }
}
