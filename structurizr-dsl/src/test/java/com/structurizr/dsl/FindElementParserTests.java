package com.structurizr.dsl;

import com.structurizr.model.ModelItem;
import com.structurizr.model.Person;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class FindElementParserTests extends AbstractTests {

    private final FindElementParser parser = new FindElementParser();

    @Test
    void test_parse_ThrowsAnException_WhenThereAreTooManyTokens() {
        try {
            parser.parse(context(), tokens("!element", "name", "tokens"));
            fail();
        } catch (Exception e) {
            assertEquals("Too many tokens, expected: !element <identifier|canonical name>", e.getMessage());
        }
    }

    @Test
    void test_parse_ThrowsAnException_WhenTheIdentifierOrCanonicalNameIsNotSpecified() {
        try {
            parser.parse(context(), tokens("!element"));
            fail();
        } catch (Exception e) {
            assertEquals("Expected: !element <identifier|canonical name>", e.getMessage());
        }
    }

    @Test
    void test_parse_ThrowsAnException_WhenTheReferencedElementCannotBeFound() {
        try {
            parser.parse(context(), tokens("!element", "Person://User"));
            fail();
        } catch (Exception e) {
            assertEquals("An element identified by \"Person://User\" could not be found", e.getMessage());
        }
    }

    @Test
    void test_parse_FindsAnElementByCanonicalName() {
        Person user = workspace.getModel().addPerson("User");
        ModelItem element = parser.parse(context(), tokens("!element", "Person://User"));

        assertSame(user, element);
    }

    @Test
    void test_parse_FindsAnElementByIdentifier() {
        Person user = workspace.getModel().addPerson("User");

        ModelDslContext context = context();
        IdentifiersRegister register = new IdentifiersRegister();
        register.register("user", user);
        context.setIdentifierRegister(register);

        ModelItem modelItem = parser.parse(context, tokens("!element", "user"));
        assertSame(modelItem, user);
    }

}