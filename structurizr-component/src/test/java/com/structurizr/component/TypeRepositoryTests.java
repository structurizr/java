package com.structurizr.component;

import org.apache.bcel.classfile.ClassParser;
import org.apache.bcel.classfile.JavaClass;
import org.junit.jupiter.api.Test;

import java.io.File;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;

public class TypeRepositoryTests {

    @Test
    void add_MergesClassInformation() throws Exception {
        String fqn = "com.structurizr.component.TypeRepositoryTests";
        File classes = new File("build/classes/java/test");
        ClassParser parser = new ClassParser(new File(classes, "com/structurizr/component/TypeRepositoryTests.class").getAbsolutePath());
        JavaClass javaClass = parser.parse();

        Type classType = new Type(javaClass);
        Type sourceType = new Type(fqn);
        sourceType.setSource("source path");

        TypeRepository typeRepository = new TypeRepository();
        typeRepository.add(sourceType); // source first
        typeRepository.add(classType);

        assertSame(javaClass, typeRepository.getType(fqn).getJavaClass());
        assertEquals("source path", typeRepository.getType(fqn).getSource());
    }

    @Test
    void add_MergesSourceInformation() throws Exception {
        String fqn = "com.structurizr.component.TypeRepositoryTests";
        File classes = new File("build/classes/java/test");
        ClassParser parser = new ClassParser(new File(classes, "com/structurizr/component/TypeRepositoryTests.class").getAbsolutePath());
        JavaClass javaClass = parser.parse();

        Type classType = new Type(javaClass);
        Type sourceType = new Type(fqn);
        sourceType.setSource("source path");

        TypeRepository typeRepository = new TypeRepository();
        typeRepository.add(classType); // class first
        typeRepository.add(sourceType);

        assertSame(javaClass, typeRepository.getType(fqn).getJavaClass());
        assertEquals("source path", typeRepository.getType(fqn).getSource());
    }

}