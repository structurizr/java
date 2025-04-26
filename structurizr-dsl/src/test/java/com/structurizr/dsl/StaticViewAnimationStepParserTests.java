package com.structurizr.dsl;

import com.structurizr.view.SystemLandscapeView;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

class StaticViewAnimationStepParserTests extends AbstractTests {

    private StaticViewAnimationStepParser parser = new StaticViewAnimationStepParser();

    @Test
    void test_parseExplicit_ThrowsAnException_WhenElementsAreMissing() {
        try {
            parser.parse((StaticViewDslContext)null, tokens("animationStep"));
            fail();
        } catch (Exception e) {
            assertEquals("Expected: animationStep <identifier|element expression> [identifier|element expression...]", e.getMessage());
        }
    }

    @Test
    void test_parseImplicit_ThrowsAnException_WhenElementsAreMissing() {
        try {
            parser.parse((StaticViewAnimationDslContext) null, tokens());
            fail();
        } catch (Exception e) {
            assertEquals("Expected: <identifier|element expression> [identifier|element expression...]", e.getMessage());
        }
    }

    @Test
    void test_parse_ThrowsAnException_WhenTheElementDoesNotExist() {
        SystemLandscapeView view = workspace.getViews().createSystemLandscapeView("key", "Description");

        StaticViewAnimationDslContext context = new StaticViewAnimationDslContext(view);
        IdentifiersRegister map = new IdentifiersRegister();
        context.setIdentifierRegister(map);

        try {
            parser.parse(context, tokens("user"));
            fail();
        } catch (Exception e) {
            assertEquals("The element/relationship \"user\" does not exist", e.getMessage());
        }
    }

}