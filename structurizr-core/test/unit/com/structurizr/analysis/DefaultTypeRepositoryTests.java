package com.structurizr.analysis;

import org.junit.Test;

import java.util.HashSet;
import java.util.Set;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class DefaultTypeRepositoryTests {

    private DefaultTypeRepository typeRepository;

    @Test
    public void test_getAllTypes_ReturnsAnEmptySet_WhenNoTypesWereFound() {
        typeRepository = new DefaultTypeRepository("com.structurizr.analysis.foo", new HashSet<>());
        Set<Class<?>> types = typeRepository.getAllTypes();
        assertTrue(types.isEmpty());
    }

    @Test
    public void test_getAllTypes_ReturnsANonEmptySet_WhenTypesWereFound() {
        typeRepository = new DefaultTypeRepository("com.structurizr.analysis.defaultTypeRepository", new HashSet<>());
        Set<String> types = typeRepository.getAllTypes().stream().map(Class::getCanonicalName).collect(Collectors.toSet());
        assertEquals(3, types.size()); // the enum is ignored

        assertTrue(types.contains("com.structurizr.analysis.defaultTypeRepository.SomeAbstractClass"));
        assertTrue(types.contains("com.structurizr.analysis.defaultTypeRepository.SomeClass"));
        assertTrue(types.contains("com.structurizr.analysis.defaultTypeRepository.SomeInterface"));
    }

    @Test
    public void test_getAllTypes_ReturnsANonEmptySet_WhenTypesAreFoundAndExclusionsHaveBeenSpecified() {
        Set<Pattern> exclusions = new HashSet<>();
        exclusions.add(Pattern.compile(".*Abstract.*"));
        typeRepository = new DefaultTypeRepository("com.structurizr.analysis.defaultTypeRepository", exclusions);
        Set<String> types = typeRepository.getAllTypes().stream().map(Class::getCanonicalName).collect(Collectors.toSet());
        assertEquals(2, types.size());

        assertTrue(types.contains("com.structurizr.analysis.defaultTypeRepository.SomeClass"));
        assertTrue(types.contains("com.structurizr.analysis.defaultTypeRepository.SomeInterface"));
    }

    @Test
    public void test_findReferencedTypes_ReturnsASetOnlyContainingJavaLangObject_WhenThereAreNoTypesReferenced() throws Exception {
        typeRepository = new DefaultTypeRepository("com.structurizr.analysis.defaultTypeRepository", new HashSet<>());
        Set<String> types = typeRepository.findReferencedTypes("com.structurizr.analysis.defaultTypeRepository.SomeInterface").stream().map(Class::getCanonicalName).collect(Collectors.toSet());
        assertEquals(1, types.size());

        assertTrue(types.contains("java.lang.Object"));
    }

    @Test
    public void test_findReferencedTypes_ReturnsANonEmptySet_WhenThereAreTypesReferenced() throws Exception {
        typeRepository = new DefaultTypeRepository("com.structurizr.analysis.defaultTypeRepository", new HashSet<>());
        Set<String> types = typeRepository.findReferencedTypes("com.structurizr.analysis.defaultTypeRepository.SomeClass").stream().map(Class::getCanonicalName).collect(Collectors.toSet());
        assertEquals(3, types.size());

        assertTrue(types.contains("com.structurizr.analysis.defaultTypeRepository.SomeEnum"));
        assertTrue(types.contains("com.structurizr.analysis.defaultTypeRepository.SomeAbstractClass"));
        assertTrue(types.contains("com.structurizr.annotation.Component"));
    }

}