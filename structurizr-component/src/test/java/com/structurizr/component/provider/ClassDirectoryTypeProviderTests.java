package com.structurizr.component.provider;

import com.structurizr.component.Type;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

public class ClassDirectoryTypeProviderTests {

    private static final File classes = new File("build/classes/java/test");

    @Test
    void construction_ThrowsAnException_WhenPassedANullDirectory() {
        assertThrowsExactly(IllegalArgumentException.class, () -> new ClassDirectoryTypeProvider(null));
    }

    @Test
    void construction_ThrowsAnException_WhenPassedAPathThatDoesNotExist() {
        assertThrowsExactly(IllegalArgumentException.class, () -> new ClassDirectoryTypeProvider(new File(classes, "com/example")));
    }

    @Test
    void construction_ThrowsAnException_WhenPassedAFile() {
        assertThrowsExactly(IllegalArgumentException.class, () -> new ClassDirectoryTypeProvider(new File(classes, "com/structurizr/component/provider/ClassDirectoryTypeProviderTests.class")));
    }

    @Test
    void getTypes() {
        TypeProvider typeProvider = new ClassDirectoryTypeProvider(classes);
        Set<Type> types = typeProvider.getTypes();

        assertTrue(types.size() > 0);
        assertNotNull(types.stream().filter(t -> t.getFullyQualifiedName().equals("com.structurizr.component.provider.ClassDirectoryTypeProviderTests")));
    }

}