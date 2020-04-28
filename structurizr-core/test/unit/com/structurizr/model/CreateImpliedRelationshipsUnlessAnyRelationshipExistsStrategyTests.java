package com.structurizr.model;

import com.structurizr.AbstractWorkspaceTestBase;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class CreateImpliedRelationshipsUnlessAnyRelationshipExistsStrategyTests extends AbstractWorkspaceTestBase {

    @Test
    public void test_impliedRelationshipsAreCreated() {
        SoftwareSystem a = model.addSoftwareSystem("A", "");
        Container aa = a.addContainer("AA", "", "");
        Component aaa = aa.addComponent("AAA", "", "");

        SoftwareSystem b = model.addSoftwareSystem("B", "");
        Container bb = b.addContainer("BB", "", "");
        Component bbb = bb.addComponent("BBB", "", "");

        model.setImpliedRelationshipsStrategy(new CreateImpliedRelationshipsUnlessAnyRelationshipExistsStrategy());

        aaa.uses(bbb, "Uses 1");

        assertEquals(9, model.getRelationships().size());
        assertTrue(aaa.hasEfferentRelationshipWith(bbb, "Uses 1"));

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
        assertEquals(10, model.getRelationships().size()); // no change
    }

    @Test
    public void test_impliedRelationshipsAreCreated_UnlessAnyRelationshipExists() {
        SoftwareSystem a = model.addSoftwareSystem("A", "");
        Container aa = a.addContainer("AA", "", "");
        Component aaa = aa.addComponent("AAA", "", "");

        SoftwareSystem b = model.addSoftwareSystem("B", "");
        Container bb = b.addContainer("BB", "", "");
        Component bbb = bb.addComponent("BBB", "", "");

        model.setImpliedRelationshipsStrategy(new CreateImpliedRelationshipsUnlessAnyRelationshipExistsStrategy());

        // add some higher level relationships
        aa.uses(bb, "Uses");

        assertEquals(4, model.getRelationships().size());
        assertTrue(aa.hasEfferentRelationshipWith(bb, "Uses"));

        // AA->BB implies AA->B A->BB A->B
        assertTrue(aa.hasEfferentRelationshipWith(b, "Uses"));
        assertTrue(a.hasEfferentRelationshipWith(bb, "Uses"));
        assertTrue(a.hasEfferentRelationshipWith(b, "Uses"));

        // and now a lower level relationship, which will be propagated to parents that don't already have relationships between them
        aaa.uses(bbb, "Uses 1");

        assertEquals(9, model.getRelationships().size());
        assertTrue(aaa.hasEfferentRelationshipWith(bbb, "Uses 1"));

        // AAA->BBB implies AAA->BB AAA->B AA->BBB AA->BB AA->B A->BBB A->BB A->B
        assertTrue(aaa.hasEfferentRelationshipWith(bb, "Uses 1"));
        assertTrue(aaa.hasEfferentRelationshipWith(b, "Uses 1"));

        assertTrue(aa.hasEfferentRelationshipWith(bbb, "Uses 1"));
        assertTrue(aa.hasEfferentRelationshipWith(bb, "Uses")); // existing relationship
        assertTrue(aa.hasEfferentRelationshipWith(b, "Uses"));  // existing relationship

        assertTrue(a.hasEfferentRelationshipWith(bbb, "Uses 1"));
        assertTrue(a.hasEfferentRelationshipWith(bb, "Uses")); // existing relationship
        assertTrue(a.hasEfferentRelationshipWith(b, "Uses")); // existing relationship
    }

}