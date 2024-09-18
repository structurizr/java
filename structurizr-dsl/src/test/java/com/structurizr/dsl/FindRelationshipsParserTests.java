package com.structurizr.dsl;

import com.structurizr.model.Relationship;
import com.structurizr.model.SoftwareSystem;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class FindRelationshipsParserTests extends AbstractTests {

    private final FindRelationshipsParser parser = new FindRelationshipsParser();

    @Test
    void test_parse_ThrowsAnException_WhenThereAreTooManyTokens() {
        try {
            parser.parse(context(), tokens("!relationships", "expression", "tokens"));
            fail();
        } catch (Exception e) {
            assertEquals("Too many tokens, expected: !relationships <expression>", e.getMessage());
        }
    }

    @Test
    void test_parse_ThrowsAnException_WhenThereAreNoRelationshipsFound() {
        try {
            parser.parse(context(), tokens("!relationships", "expression"));
            fail();
        } catch (Exception e) {
            assertEquals("No relationships found for expression \"expression\"", e.getMessage());
        }
    }

    @Test
    void test_parse_FindsRelationshipsByExpression() {
        SoftwareSystem a = model.addSoftwareSystem("A");
        SoftwareSystem b = model.addSoftwareSystem("B");
        SoftwareSystem c = model.addSoftwareSystem("C");

        Relationship relationship1 = a.uses(b, "Uses");
        Relationship relationship2 = b.uses(c, "Uses");
        Relationship relationship3 = a.uses(c, "Uses");

        ModelDslContext context = context();
        IdentifiersRegister register = new IdentifiersRegister();
        register.register("c", c);
        context.setIdentifierRegister(register);

        Set<Relationship> relationships = parser.parse(context, tokens("!relationships", "*->c"));
        assertTrue(relationships.contains(relationship2));
        assertTrue(relationships.contains(relationship3));
    }

}