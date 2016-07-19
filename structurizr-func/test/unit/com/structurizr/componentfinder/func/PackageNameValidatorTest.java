package com.structurizr.componentfinder.func;

import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class PackageNameValidatorTest {
    @Test
    public void correctPackageName() {
        validateCorectPackageName("com");
        validateCorectPackageName("com.structurizr");
        validateCorectPackageName("com.structurizr.componentFinder");
        validateCorectPackageName("com.structurizr.componentFinder.func");
    }

    @Test
    public void inCorrectPackageName() {
        validateIncorrectPackageName(".");
        validateIncorrectPackageName("*");
        validateIncorrectPackageName(".*");
        validateIncorrectPackageName("com.");
        validateIncorrectPackageName(".com");
        validateIncorrectPackageName("com.*");
        validateIncorrectPackageName("com.**");
        validateIncorrectPackageName("com.9q");
        validateIncorrectPackageName("com..q");
    }


    private void validateCorectPackageName(String com) {
        assertThat(PackageNameValidator.INSTANCE.test(com)).isTrue();
    }

    private void validateIncorrectPackageName(String packageName) {
        assertThat(PackageNameValidator.INSTANCE.test(packageName)).isFalse();
    }

}