package com.structurizr.testapp.paperboy.model;

import java.util.Set;

public interface CustomerRepository {
    Set<Customer> getAll(String street);

    Set<Customer> getAll();
}
