package com.structurizr.component.supporting;

import com.structurizr.component.Type;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class AllReferencedTypesSupportingTypesStrategyTests {

    @Test
    void findSupportingTypes() {
        Type type = new Type("com.example.a.A");

        Type dependency1 = new Type("com.example.a.AImpl");
        Type dependency2 = new Type("com.example.util.SomeUtils");
        type.addDependency(dependency1);
        type.addDependency(dependency2);

        Set<Type> supportingTypes = new AllReferencedTypesSupportingTypesStrategy().findSupportingTypes(type, null);
        assertEquals(2, supportingTypes.size());
        assertTrue(supportingTypes.contains(dependency1));
        assertTrue(supportingTypes.contains(dependency2));
    }

}