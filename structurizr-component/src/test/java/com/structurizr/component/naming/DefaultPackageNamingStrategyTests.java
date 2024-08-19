package com.structurizr.component.naming;

import com.structurizr.component.Type;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class DefaultPackageNamingStrategyTests {

    @Test
    void nameOf() {
        assertEquals("Order Management", new DefaultPackageNamingStrategy().nameOf(new Type("com.example.orderManagement.package-info")));
    }

}