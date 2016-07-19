package com.structurizr.example.spring.petclinic;

import com.structurizr.Workspace;
import com.structurizr.documentation.Documentation;
import com.structurizr.model.Element;
import com.structurizr.model.Model;
import com.structurizr.model.Relationship;
import com.structurizr.view.*;

import java.util.Set;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;

public class ClassicSpringWorkspaceFactoryTest {

    private static final String SPRING_PETCLINIC_PATH = "./../../spring-petclinic";

    @org.junit.Test
    public void compareWorkspaceFactories() throws Exception {
        final Workspace classicCreatedWorkspace = ClassicSpringWorkspaceFactory.buildWorkspace(SPRING_PETCLINIC_PATH);
        final Workspace functionalCreatedWorkspace = FunctionalSpringWorkspaceFactory.buildWorkspace(SPRING_PETCLINIC_PATH);
        assertEqual(classicCreatedWorkspace, functionalCreatedWorkspace);

    }

    public void assertEqual(Workspace w, Workspace other) {
        assertEqualViewSet(w.getViews(), other.getViews());
        assertEqual(w.getDocumentation(), other.getDocumentation());
        assertEqual(w.getModel(), other.getModel());
        assertThat(w.getDescription()).isEqualTo(other.getDescription());
        assertThat(w.getName()).isEqualTo(other.getName());
    }

    private void assertEqual(Model model, Model other) {
        model.getElements().forEach(x -> assertIsPresent(x, other));
    }

    private void assertIsPresent(Element x, Model other) {
        assertEqualRelationShip(x.getRelationships(), other.getRelationships());
        final Element otherElement = other.getElement(x.getId());
        assertEqual(x, otherElement);

    }

    private void assertEqual(Element x, Element otherElement) {
        assertThat(x.getDescription()).isEqualTo(otherElement.getDescription());
        assertThat(x.getName()).isEqualTo(otherElement.getName());
        assertThat(x.getCanonicalName()).isEqualTo(otherElement.getCanonicalName());
    }

    private void assertEqualRelationShip(Set<Relationship> relationships, Set<Relationship> otherRelationships) {
        relationships.forEach(x -> assertIsPresent(x, otherRelationships));
    }

    private void assertIsPresent(Relationship x, Set<Relationship> otherRelationships) {
        final Set<Relationship> collect = otherRelationships.stream().filter(y -> y.getId().equals(x.getId())).collect(Collectors.toSet());
        assertThat(collect).withFailMessage("Relation ship with id [" + x.getId() + "] and description [" + x.getDescription() + "] was not present.").isNotEmpty();
        final Relationship other = collect.iterator().next();
        assertEqualRelationShip(x, other);
    }

    private void assertEqualRelationShip(Relationship x, Relationship other) {
        assertThat(x.getDescription()).isEqualTo(other.getDescription());
        assertThat(x.getDestinationId()).isEqualTo(other.getDestinationId());
        assertThat(x.getId()).isEqualTo(other.getId());
        assertThat(x.getSourceId()).isEqualTo(other.getSourceId());
        assertThat(x.getTechnology()).isEqualTo(other.getTechnology());
        assertThat(x.getTags()).isEqualTo(other.getTags());
        assertThat(x.getInteractionStyle()).isEqualTo(other.getInteractionStyle());
    }


    private void assertEqual(Documentation documentation, Documentation other) {
        //TODO no documentation yet
    }

    private void assertEqualViewSet(ViewSet views, ViewSet other) {
        views.getComponentViews().forEach(x -> assertEqualComponentView(x, other.getViewWithKey(x.getKey())));
        views.getContainerViews().forEach(x -> assertEqualContainerView(x, other.getViewWithKey(x.getKey())));
        views.getSystemContextViews().forEach(x -> assertEqualSystemContextView(x, other.getViewWithKey(x.getKey())));
        views.getEnterpriseContextViews().forEach(x -> assertEqualEnterpriseContextView(x, other.getViewWithKey(x.getKey())));
    }

    private void assertEqualComponentView(ComponentView x, View otherViewWithKey) {
        assertThat(otherViewWithKey).isNotNull();
        assertThat(otherViewWithKey).isInstanceOf(ComponentView.class);
        assertEqualComponentView(x, ComponentView.class.cast(otherViewWithKey));
    }

    private void assertEqualContainerView(ContainerView x, View otherViewWithKey) {
        assertThat(otherViewWithKey).isNotNull();
        assertThat(otherViewWithKey).isInstanceOf(ContainerView.class);
        assertEqualContainerView(x, ContainerView.class.cast(otherViewWithKey));
    }

    private void assertEqualSystemContextView(SystemContextView x, View otherViewWithKey) {
        assertThat(otherViewWithKey).isNotNull();
        assertThat(otherViewWithKey).isInstanceOf(SystemContextView.class);
        assertEqualStaticView(x, SystemContextView.class.cast(otherViewWithKey));
    }

    private void assertEqualEnterpriseContextView(EnterpriseContextView x, View otherViewWithKey) {
        assertThat(otherViewWithKey).isNotNull();
        assertThat(otherViewWithKey).isInstanceOf(EnterpriseContextView.class);
        assertEqualStaticView(x, EnterpriseContextView.class.cast(otherViewWithKey));
    }

    private void assertEqualComponentView(ComponentView componentView, ComponentView other) {
        assertThat(componentView.getClass()).isEqualTo(other.getClass());
        assertThat(componentView.getContainerId()).isEqualTo(other.getContainerId());
        assertThat(componentView.getName()).isEqualTo(other.getName());
        assertThat(componentView.getDescription()).isEqualTo(other.getDescription());
        assertThat(componentView.getKey()).isEqualTo(other.getKey());
        assertThat(componentView.getSoftwareSystemId()).isEqualTo(other.getSoftwareSystemId());
        componentView.getElements().forEach(x -> assertEqualElementView(x, other.getElementView(x.getElement())));
        componentView.getRelationships().forEach(x -> assertEqualRelationShipView(x, other.getRelationshipView(x.getRelationship())));
    }

    private void assertEqualRelationShipView(RelationshipView x, RelationshipView other) {
        assertThat(x.getId()).isEqualTo(other.getId());
        assertThat(x.getDescription()).isEqualTo(other.getDescription());
        assertThat(x.getOrder()).isEqualTo(other.getOrder());
        assertEqualRelationShip(x.getRelationship(), other.getRelationship());
    }


    private void assertEqualElementView(ElementView x, ElementView other) {
        assertThat(x.getId()).isEqualTo(other.getId());
        assertThat(x.getX()).isEqualTo(other.getX());
        assertThat(x.getY()).isEqualTo(other.getY());
        assertEqual(x.getElement(), other.getElement());
    }

    private void assertEqualContainerView(ContainerView x, ContainerView other) {
        assertThat(x.getSoftwareSystemId()).isEqualTo(other.getSoftwareSystemId());
        assertThat(x.getName()).isEqualTo(other.getName());
        assertThat(x.getDescription()).isEqualTo(other.getDescription());
        assertThat(x.getKey()).isEqualTo(other.getKey());
        assertThat(x.getSoftwareSystemId()).isEqualTo(other.getSoftwareSystemId());
    }

    private void assertEqualStaticView(StaticView x, StaticView other) {
        assertThat(x.getName()).isEqualTo(other.getName());
        assertThat(x.getDescription()).isEqualTo(other.getDescription());
        assertThat(x.getKey()).isEqualTo(other.getKey());
        assertThat(x.getSoftwareSystemId()).isEqualTo(other.getSoftwareSystemId());
    }


}