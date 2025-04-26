package com.structurizr.dsl;

import com.structurizr.view.CustomView;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

class CustomViewAnimationStepParserTests extends AbstractTests {

    private CustomViewAnimationStepParser parser = new CustomViewAnimationStepParser();

    @Test
    void test_parseExplicit_ThrowsAnException_WhenElementsAreMissing() {
        try {
            parser.parse((CustomViewDslContext)null, tokens("animationStep"));
            fail();
        } catch (Exception e) {
            assertEquals("Expected: animationStep <identifier|element expression> [identifier|element expression...]", e.getMessage());
        }
    }

    @Test
    void test_parseImplicit_ThrowsAnException_WhenElementsAreMissing() {
        try {
            parser.parse((CustomViewAnimationDslContext) null, tokens());
            fail();
        } catch (Exception e) {
            assertEquals("Expected: <identifier|element expression> [identifier|element expression...]", e.getMessage());
        }
    }

    @Test
    void test_parse_ThrowsAnException_WhenTheElementDoesNotExist() {
        CustomView view = workspace.getViews().createCustomView("key", "Title", "Description");

        CustomViewAnimationDslContext context = new CustomViewAnimationDslContext(view);
        IdentifiersRegister map = new IdentifiersRegister();
        context.setIdentifierRegister(map);

        try {
            parser.parse(context, tokens("e"));
            fail();
        } catch (Exception e) {
            assertEquals("The element/relationship \"e\" does not exist", e.getMessage());
        }
    }

}