package com.structurizr.example;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.structurizr.SpringComponentFinder;
import com.structurizr.model.*;
import com.structurizr.view.ComponentView;
import com.structurizr.view.ContainerView;
import com.structurizr.view.ContextView;

/**
 * This is a C4 representation of the Spring PetClinic sample app (https://github.com/spring-projects/spring-petclinic/).
 */
public class SpringPetClinic {

    public static void main(String[] args) throws Exception {
        Model model = new Model();

        // create the basic model (the stuff we can't get from the code)
        SoftwareSystem springPetClinic = model.addSoftwareSystem(Location.Internal, "Spring PetClinic", "");
        Person user = model.addPerson(Location.External, "User", "");
        user.uses(springPetClinic, "Views information using");

        Container webApplication = springPetClinic.addContainer("Web Application", "", "Apache Tomcat 7.x");
        Container relationalDatabase = springPetClinic.addContainer("Relational Database", "", "HSQLDB");
        user.uses(webApplication, "Views information using");
        webApplication.uses(relationalDatabase, "Reads from and writes to");

        // and now automatically find all Spring @Controller, @Component, @Service and @Repository components
        SpringComponentFinder componentFinder = new SpringComponentFinder(webApplication, "org.springframework.samples.petclinic");
        componentFinder.findComponents();
        componentFinder.findComponentDependencies();

        // finally create some views
        ContextView contextView = model.createContextView(springPetClinic);
        contextView.addAllSoftwareSystems();
        contextView.addAllPeople();

        ContainerView containerView = model.createContainerView(springPetClinic);
        containerView.addAllPeople();
        containerView.addAllSoftwareSystems();
        containerView.addAllContainers();

        ComponentView componentView = model.createComponentView(springPetClinic, webApplication);
        componentView.addAllComponents();

        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.enable(SerializationFeature.INDENT_OUTPUT);
        objectMapper.enable(DeserializationFeature.ACCEPT_EMPTY_STRING_AS_NULL_OBJECT);
        String modelAsJson = objectMapper.writeValueAsString(model);
        System.out.println(modelAsJson);
    }

}