package com.structurizr.view;

import com.structurizr.Workspace;
import com.structurizr.model.Location;
import com.structurizr.model.Model;
import com.structurizr.model.Person;
import com.structurizr.model.SoftwareSystem;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class ViewSetTests {

    @Test
    public void test_copyLayoutInformationFrom() {
        Workspace workspace1 = new Workspace("1", "");
        Model model1 = workspace1.getModel();
        SoftwareSystem softwareSystem1 = model1.addSoftwareSystem(Location.Internal, "Software System", "");
        Person person1 = model1.addPerson(Location.External, "User", "");
        person1.uses(softwareSystem1, "uses");
        ViewSet viewSet1 = workspace1.getViews();
        SystemContextView systemContextView1 = viewSet1.createContextView(softwareSystem1);
        systemContextView1.addAllPeople();
        systemContextView1.getElements().iterator().next().setX(100);
        systemContextView1.setPaperSize(PaperSize.A3_Landscape);

        Workspace workspace2 = new Workspace("2", "");
        Model model2 = workspace2.getModel();
        SoftwareSystem softwareSystem2 = model2.addSoftwareSystem(Location.Internal, "Software System", "");
        Person person2 = model2.addPerson(Location.External, "User", "");
        person2.uses(softwareSystem2, "uses");
        ViewSet viewSet2 = workspace2.getViews();
        SystemContextView systemContextView2 = viewSet2.createContextView(softwareSystem2);
        systemContextView2.addAllPeople();

        workspace2.getViews().copyLayoutInformationFrom(workspace1.getViews());
        assertEquals(100, systemContextView2.getElements().iterator().next().getX());
        assertEquals(PaperSize.A3_Landscape, systemContextView2.getPaperSize());
    }

}
