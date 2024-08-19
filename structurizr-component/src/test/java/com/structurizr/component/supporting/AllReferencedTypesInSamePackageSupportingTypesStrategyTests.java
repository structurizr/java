package com.structurizr.component.supporting;

import com.structurizr.component.Type;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class AllReferencedTypesInSamePackageSupportingTypesStrategyTests {

    @Test
    void findSupportingTypes() {
        Type type = new Type("com.example.a.A");

        Type dependency1 = new Type("com.example.a.AImpl");
        Type dependency2 = new Type("com.example.util.SomeUtils");
        type.addDependency(dependency1);
        type.addDependency(dependency2);

        type.addDependency(new Type("com.example.util.SomeUtils"));

        Set<Type> supportingTypes = new AllReferencedTypesInSamePackageSupportingTypesStrategy().findSupportingTypes(type);
        assertEquals(1, supportingTypes.size());
        assertTrue(supportingTypes.contains(dependency1));
    }

}