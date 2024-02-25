package com.structurizr.dsl;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThatCode;

public class UniqueConstantsConfigurationTests {

    @Test
    void shouldFailOnConstantDuplication() {
        String dsl = """
                    workspace {
                        !constant NAME toto
                        !constant NAME titi
                    }
                """;

        StructurizrDslParser parser = new StructurizrDslParser(
                new StructurizrDslParserOptions(true)
        );

        assertThatCode(() -> parser.parse(dsl))
                .isInstanceOf(StructurizrDslParserException.class)
                .hasMessageStartingWith("A constant named NAME already exists");
    }
}
