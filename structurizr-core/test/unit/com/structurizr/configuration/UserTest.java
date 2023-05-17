package com.structurizr.configuration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.exc.MismatchedInputException;
import com.fasterxml.jackson.databind.exc.ValueInstantiationException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertThrows;

class UserTest {

    @Test
    public void testNullsNotAllowed() {
        ObjectMapper mapper = new ObjectMapper();
        assertThrows(
                ValueInstantiationException.class,
                () -> mapper.readValue("{\"username\": \"test\", \"role\": null}", User.class)
        );

        assertThrows(
                ValueInstantiationException.class,
                () -> mapper.readValue("{\"username\": null, \"role\": \"ReadWrite\"}", User.class)
        );

        assertThrows(
                MismatchedInputException.class,
                () -> mapper.readValue("{\"role\": \"ReadWrite\"}", User.class)
        );

        assertThrows(
                MismatchedInputException.class,
                () -> mapper.readValue("{\"username\": \"test\"}", User.class)
        );
    }

}
