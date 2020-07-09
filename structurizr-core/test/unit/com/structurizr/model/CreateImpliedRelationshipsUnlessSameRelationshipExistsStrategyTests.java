package com.structurizr.model;

import com.structurizr.AbstractWorkspaceTestBase;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class CreateImpliedRelationshipsUnlessSameRelationshipExistsStrategyTests extends AbstractWorkspaceTestBase {

    @Test
    public void test_impliedRelationships_WhenNoSummaryRelationshipsExist() {
        SoftwareSystem a = model.addSoftwareSystem("A", "");
        Container aa = a.addContainer("AA", "", "");
        Component aaa = aa.addComponent("AAA", "", "");

        SoftwareSystem b = model.addSoftwareSystem("B", "");
        Container bb = b.addContainer("BB", "", "");
        Component bbb = bb.addComponent("BBB", "", "");

        model.setImpliedRelationshipsStrategy(new CreateImpliedRelationshipsUnlessSameRelationshipExistsStrategy());

        aaa.uses(bbb, "Uses 1", null, InteractionStyle.Asynchronous, new String[] { "Tag 1", "Tag 2" });

        assertEquals(9, model.getRelationships().size());
        assertTrue(aaa.hasEfferentRelationshipWith(bbb, "Uses 1"));

        // and all relationships should have the same interaction style and tags
        for (Relationship r : model.getRelationships()) {
            assertEquals(InteractionStyle.Asynchronous, r.getInteractionStyle());
            assertTrue(r.getTagsAsSet().contains("Tag 1"));
            assertTrue(r.getTagsAsSet().contains("Tag 2"));
        }

        // AAA->BBB implies AAA->BB AAA->B AA->BBB AA->BB AA->B A->BBB A->BB A->B
        assertTrue(aaa.hasEfferentRelationshipWith(bb, "Uses 1"));
        assertTrue(aaa.hasEfferentRelationshipWith(b, "Uses 1"));

        assertTrue(aa.hasEfferentRelationshipWith(bbb, "Uses 1"));
        assertTrue(aa.hasEfferentRelationshipWith(bb, "Uses 1"));
        assertTrue(aa.hasEfferentRelationshipWith(b, "Uses 1"));

        assertTrue(a.hasEfferentRelationshipWith(bbb, "Uses 1"));
        assertTrue(a.hasEfferentRelationshipWith(bb, "Uses 1"));
        assertTrue(a.hasEfferentRelationshipWith(b, "Uses 1"));

        // and add another relationship with a different description
        aaa.uses(bbb, "Uses 2");

        assertEquals(18, model.getRelationships().size());
        assertTrue(aaa.hasEfferentRelationshipWith(bbb, "Uses 2"));

        // AAA->BBB implies AAA->BB AAA->B AA->BBB AA->BB AA->B A->BBB A->BB A->B
        assertTrue(aaa.hasEfferentRelationshipWith(bb, "Uses 2"));
        assertTrue(aaa.hasEfferentRelationshipWith(b, "Uses 2"));

        assertTrue(aa.hasEfferentRelationshipWith(bbb, "Uses 2"));
        assertTrue(aa.hasEfferentRelationshipWith(bb, "Uses 2"));
        assertTrue(aa.hasEfferentRelationshipWith(b, "Uses 2"));

        assertTrue(a.hasEfferentRelationshipWith(bbb, "Uses 2"));
        assertTrue(a.hasEfferentRelationshipWith(bb, "Uses 2"));
        assertTrue(a.hasEfferentRelationshipWith(b, "Uses 2"));
    }

}