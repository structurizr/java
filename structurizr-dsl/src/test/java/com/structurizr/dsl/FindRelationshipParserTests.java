package com.structurizr.dsl;

import com.structurizr.model.ModelItem;
import com.structurizr.model.Person;
import com.structurizr.model.Relationship;
import com.structurizr.model.SoftwareSystem;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class FindRelationshipParserTests extends AbstractTests {

    private final FindRelationshipParser parser = new FindRelationshipParser();

    @Test
    void test_parse_ThrowsAnException_WhenThereAreTooManyTokens() {
        try {
            parser.parse(context(), tokens("!relationship", "name", "tokens"));
            fail();
        } catch (Exception e) {
            assertEquals("Too many tokens, expected: !relationship <identifier|canonical name>", e.getMessage());
        }
    }

    @Test
    void test_parse_ThrowsAnException_WhenTheIdentifierOrCanonicalNameIsNotSpecified() {
        try {
            parser.parse(context(), tokens("!relationship"));
            fail();
        } catch (Exception e) {
            assertEquals("Expected: !relationship <identifier|canonical name>", e.getMessage());
        }
    }

    @Test
    void test_parse_ThrowsAnException_WhenTheReferencedRelationshipCannotBeFound() {
        try {
            parser.parse(context(), tokens("!relationship", "rel"));
            fail();
        } catch (Exception e) {
            assertEquals("A relationship identified by \"rel\" could not be found", e.getMessage());
        }
    }

    @Test
    void test_parse_FindsARelationshipByCanonicalName() {
        SoftwareSystem a = model.addSoftwareSystem("A");
        SoftwareSystem b = model.addSoftwareSystem("B");
        Relationship relationship = a.uses(b, "Description");

        ModelDslContext context = context();

        ModelItem modelItem = parser.parse(context, tokens("!relationship", "Relationship://SoftwareSystem://A -> SoftwareSystem://B (Description)"));
        assertSame(modelItem, relationship);
    }

    @Test
    void test_parse_FindsARelationshipByIdentifier() {
        SoftwareSystem a = model.addSoftwareSystem("A");
        SoftwareSystem b = model.addSoftwareSystem("B");
        Relationship relationship = a.uses(b, "Description");

        ModelDslContext context = context();
        IdentifiersRegister register = new IdentifiersRegister();
        register.register("rel", relationship);
        context.setIdentifierRegister(register);

        ModelItem modelItem = parser.parse(context, tokens("!relationship", "rel"));
        assertSame(modelItem, relationship);
    }

}