package com.structurizr.dsl;

import com.structurizr.model.Component;
import com.structurizr.model.Container;
import com.structurizr.model.Element;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class FindElementsParserTests extends AbstractTests {

    private final FindElementsParser parser = new FindElementsParser();

    @Test
    void test_parse_ThrowsAnException_WhenThereAreTooManyTokens() {
        try {
            parser.parse(context(), tokens("!elements", "expression", "tokens"));
            fail();
        } catch (Exception e) {
            assertEquals("Too many tokens, expected: !elements <expression>", e.getMessage());
        }
    }

    @Test
    void test_parse_ThrowsAnException_WhenThereAreNoElementsFound() {
        try {
            parser.parse(context(), tokens("!elements", "expression"));
            fail();
        } catch (Exception e) {
            assertEquals("No elements found for expression \"expression\"", e.getMessage());
        }
    }

    @Test
    void test_parse_FindsElementsByExpression() {
        Container application = model.addSoftwareSystem("Software System").addContainer("Application");
        Component componentA = application.addComponent("A");
        Component componentB = application.addComponent("B");
        Component componentC = application.addComponent("C");

        ContainerDslContext context = new ContainerDslContext(application);
        context.setWorkspace(workspace);
        IdentifiersRegister register = new IdentifiersRegister();
        register.register("application", application);
        context.setIdentifierRegister(register);

        Set<Element> elements = parser.parse(context, tokens("!elements", "element.parent==application"));
        assertTrue(elements.contains(componentA));
        assertTrue(elements.contains(componentB));
        assertTrue(elements.contains(componentC));
    }

}