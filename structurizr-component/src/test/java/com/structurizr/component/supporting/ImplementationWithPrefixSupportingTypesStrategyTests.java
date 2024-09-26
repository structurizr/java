package com.structurizr.component.supporting;

import com.structurizr.component.Type;
import com.structurizr.component.TypeRepository;
import org.apache.bcel.classfile.ClassParser;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

public class ImplementationWithPrefixSupportingTypesStrategyTests {

    private final File classes = new File("build/classes/java/test");

    @Test
    void findSupportingTypes() throws Exception {
        Type interfaceType = new Type(new ClassParser(new File(classes, "com/structurizr/component/supporting/implementation/ExampleRepository.class").getAbsolutePath()).parse());
        Type implementationTypeWithPrefix = new Type(new ClassParser(new File(classes, "com/structurizr/component/supporting/implementation/JdbcExampleRepository.class").getAbsolutePath()).parse());
        Type implementationTypeWithSuffix = new Type(new ClassParser(new File(classes, "com/structurizr/component/supporting/implementation/ExampleRepositoryImpl.class").getAbsolutePath()).parse());

        TypeRepository typeRepository = new TypeRepository();
        typeRepository.add(interfaceType);
        typeRepository.add(implementationTypeWithPrefix);
        typeRepository.add(implementationTypeWithSuffix);

        Set<Type> supportingTypes = new ImplementationWithPrefixSupportingTypesStrategy("Jdbc").findSupportingTypes(interfaceType, typeRepository);
        assertEquals(1, supportingTypes.size());
        assertTrue(supportingTypes.contains(implementationTypeWithPrefix));
    }

    @Test
    void findSupportingTypes_ThrowsAnException_WhenTheTypeIsNotAnInterface() throws Exception {
        try {
            Type type = new Type(new ClassParser(new File(classes, "com/structurizr/component/supporting/implementation/JdbcExampleRepository.class").getAbsolutePath()).parse());
            new ImplementationWithPrefixSupportingTypesStrategy("Impl").findSupportingTypes(type, null);
            fail();
        } catch (Exception e) {
            assertEquals("The type com.structurizr.component.supporting.implementation.JdbcExampleRepository is not an interface", e.getMessage());
        }
    }

}