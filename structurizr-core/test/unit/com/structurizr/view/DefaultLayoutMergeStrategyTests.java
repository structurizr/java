package com.structurizr.view;

import com.structurizr.Workspace;
import com.structurizr.model.Container;
import com.structurizr.model.SoftwareSystem;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class DefaultLayoutMergeStrategyTests {

    @Test
    public void test_copyLayoutInformation_WhenCanonicalNamesHaveNotChanged() {
        Workspace workspace1 = new Workspace("1", "");
        SoftwareSystem softwareSystem1 = workspace1.getModel().addSoftwareSystem("Software System");
        Container container1 = softwareSystem1.addContainer("Container", "", "");
        ContainerView view1 = workspace1.getViews().createContainerView(softwareSystem1, "key", "");
        view1.add(container1);
        view1.getElementView(container1).setX(123);
        view1.getElementView(container1).setY(456);

        Workspace workspace2 = new Workspace("2", "");
        SoftwareSystem softwareSystem2 = workspace2.getModel().addSoftwareSystem("Software System");
        Container container2 = softwareSystem2.addContainer("Container", "", "");
        ContainerView view2 = workspace2.getViews().createContainerView(softwareSystem2, "key", "");
        view2.add(container2);

        DefaultLayoutMergeStrategy strategy = new DefaultLayoutMergeStrategy();
        strategy.copyLayoutInformation(view1, view2);

        assertEquals(123, view2.getElementView(container2).getX());
        assertEquals(456, view2.getElementView(container2).getY());
    }

    @Test
    public void test_copyLayoutInformation_WhenAParentElementNameHasChanged() {
        Workspace workspace1 = new Workspace("1", "");
        SoftwareSystem softwareSystem1 = workspace1.getModel().addSoftwareSystem("Software System");
        Container container1 = softwareSystem1.addContainer("Container", "", "");
        ContainerView view1 = workspace1.getViews().createContainerView(softwareSystem1, "key", "");
        view1.add(container1);
        view1.getElementView(container1).setX(123);
        view1.getElementView(container1).setY(456);

        Workspace workspace2 = new Workspace("2", "");
        SoftwareSystem softwareSystem2 = workspace2.getModel().addSoftwareSystem("Software System with a new name");
        Container container2 = softwareSystem2.addContainer("Container", "", "");
        ContainerView view2 = workspace2.getViews().createContainerView(softwareSystem2, "key", "");
        view2.add(container2);

        DefaultLayoutMergeStrategy strategy = new DefaultLayoutMergeStrategy();
        strategy.copyLayoutInformation(view1, view2);

        assertEquals(123, view2.getElementView(container2).getX());
        assertEquals(456, view2.getElementView(container2).getY());
    }

    @Test
    public void test_copyLayoutInformation_WhenAnElementNameHasChangedButTheDescriptionHasNotChanged() {
        Workspace workspace1 = new Workspace("1", "");
        SoftwareSystem softwareSystem1 = workspace1.getModel().addSoftwareSystem("Software System");
        Container container1 = softwareSystem1.addContainer("Container", "Container description", "");
        ContainerView view1 = workspace1.getViews().createContainerView(softwareSystem1, "key", "");
        view1.add(container1);
        view1.getElementView(container1).setX(123);
        view1.getElementView(container1).setY(456);

        Workspace workspace2 = new Workspace("2", "");
        SoftwareSystem softwareSystem2 = workspace2.getModel().addSoftwareSystem("Software System");
        Container container2 = softwareSystem2.addContainer("Container with a new name", "Container description", "");
        ContainerView view2 = workspace2.getViews().createContainerView(softwareSystem2, "key", "");
        view2.add(container2);

        DefaultLayoutMergeStrategy strategy = new DefaultLayoutMergeStrategy();
        strategy.copyLayoutInformation(view1, view2);

        assertEquals(123, view2.getElementView(container2).getX());
        assertEquals(456, view2.getElementView(container2).getY());
    }

    @Test
    public void test_copyLayoutInformation_WhenAnElementNameAndDescriptionHaveChangedButTheIdHasNotChanged() {
        Workspace workspace1 = new Workspace("1", "");
        SoftwareSystem softwareSystem1 = workspace1.getModel().addSoftwareSystem("Software System");
        Container container1 = softwareSystem1.addContainer("Container", "Container description", "");
        ContainerView view1 = workspace1.getViews().createContainerView(softwareSystem1, "key", "");
        view1.add(container1);
        view1.getElementView(container1).setX(123);
        view1.getElementView(container1).setY(456);

        Workspace workspace2 = new Workspace("2", "");
        SoftwareSystem softwareSystem2 = workspace2.getModel().addSoftwareSystem("Software System");
        Container container2 = softwareSystem2.addContainer("Container with a new name", "Container with a new description", "");
        ContainerView view2 = workspace2.getViews().createContainerView(softwareSystem2, "key", "");
        view2.add(container2);

        DefaultLayoutMergeStrategy strategy = new DefaultLayoutMergeStrategy();
        strategy.copyLayoutInformation(view1, view2);

        assertEquals(123, view2.getElementView(container2).getX());
        assertEquals(456, view2.getElementView(container2).getY());
    }

    @Test
    public void test_copyLayoutInformation_WhenAnElementNameAndDescriptionAndIdHaveChanged() {
        Workspace workspace1 = new Workspace("1", "");
        SoftwareSystem softwareSystem1 = workspace1.getModel().addSoftwareSystem("Software System");
        Container container1 = softwareSystem1.addContainer("Container", "Container description", "");
        ContainerView view1 = workspace1.getViews().createContainerView(softwareSystem1, "key", "");
        view1.add(container1);
        view1.getElementView(container1).setX(123);
        view1.getElementView(container1).setY(456);

        Workspace workspace2 = new Workspace("2", "");
        SoftwareSystem softwareSystem2 = workspace2.getModel().addSoftwareSystem("Software System");
        softwareSystem2.addContainer("Web Application", "Description", ""); // this element has ID 2
        Container container2 = softwareSystem2.addContainer("Database", "Description", "");
        ContainerView view2 = workspace2.getViews().createContainerView(softwareSystem2, "key", "");
        view2.add(container2);

        DefaultLayoutMergeStrategy strategy = new DefaultLayoutMergeStrategy();
        strategy.copyLayoutInformation(view1, view2);

        assertEquals(0, view2.getElementView(container2).getX());
        assertEquals(0, view2.getElementView(container2).getY());
    }

}