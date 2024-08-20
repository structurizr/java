package com.structurizr.component.supporting;

import com.structurizr.component.Type;
import com.structurizr.component.TypeRepository;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class AllTypesInPackageSupportingTypesStrategyTests {

    @Test
    void findSupportingTypes() {
        Type type = new Type("com.example.a.package-info");

        Type dependency1 = new Type("com.example.a.AImpl");
        Type dependency2 = new Type("com.example.a.internal.AInternal");
        Type dependency3 = new Type("com.example.util.SomeUtils");
        type.addDependency(dependency1);
        type.addDependency(dependency2);
        type.addDependency(dependency3);

        TypeRepository typeRepository = new TypeRepository();
        typeRepository.add(dependency1);
        typeRepository.add(dependency2);
        typeRepository.add(dependency3);

        Set<Type> supportingTypes = new AllTypesInPackageSupportingTypesStrategy().findSupportingTypes(type, typeRepository);
        assertEquals(1, supportingTypes.size());
        assertTrue(supportingTypes.contains(dependency1));
    }

}