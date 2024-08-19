package com.structurizr.component.supporting;

import com.structurizr.component.Type;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class DefaultSupportingTypesStrategyTests {

    @Test
    void findSupportingTypes() {
        Type type = new Type("com.example.a.A");
        type.addDependency(new Type("com.example.a.AImpl"));

        Set<Type> supportingTypes = new DefaultSupportingTypesStrategy().findSupportingTypes(type, null);
        assertTrue(supportingTypes.isEmpty());
    }

}