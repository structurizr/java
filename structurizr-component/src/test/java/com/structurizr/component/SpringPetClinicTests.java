package com.structurizr.component;

import com.structurizr.Workspace;
import com.structurizr.component.description.FirstSentenceDescriptionStrategy;
import com.structurizr.component.filter.ExcludeTypesByRegexFilter;
import com.structurizr.component.matcher.AnnotationTypeMatcher;
import com.structurizr.component.matcher.ImplementsTypeMatcher;
import com.structurizr.component.url.PrefixSourceUrlStrategy;
import com.structurizr.model.Component;
import com.structurizr.model.Container;
import com.structurizr.model.Person;
import com.structurizr.model.SoftwareSystem;
import com.structurizr.util.StringUtils;
import org.junit.jupiter.api.Test;

import java.io.File;

import static org.junit.jupiter.api.Assertions.*;

public class SpringPetClinicTests {

    @Test
    void springPetClinic() {
        String springPetClinicHome = System.getenv().getOrDefault("SPRING_PETCLINIC_HOME", "");
        System.out.println(springPetClinicHome);
        if (!StringUtils.isNullOrEmpty(springPetClinicHome)) {
            System.out.println("Running Spring PetClinic example...");

            Workspace workspace = new Workspace("Spring PetClinic", "Description");
            Person clinicEmployee = workspace.getModel().addPerson("Clinic Employee");
            SoftwareSystem softwareSystem = workspace.getModel().addSoftwareSystem("Spring PetClinic");
            Container webApplication = softwareSystem.addContainer("Web Application");
            Container relationalDatabaseSchema = softwareSystem.addContainer("Relational Database Schema");

            ComponentFinder componentFinder = new ComponentFinderBuilder()
                    .forContainer(webApplication)
                    .fromClasses(new File(springPetClinicHome, "target/spring-petclinic-3.3.0-SNAPSHOT.jar"))
                    .fromSource(new File(springPetClinicHome, "src/main/java"))
                    .withStrategy(
                            new ComponentFinderStrategyBuilder()
                                    .matchedBy(new AnnotationTypeMatcher("org.springframework.stereotype.Controller"))
                                    .filteredBy(new ExcludeTypesByRegexFilter(".*.CrashController"))
                                    .withTechnology("Spring MVC Controller")
                                    .withUrl(new PrefixSourceUrlStrategy("https://github.com/spring-projects/spring-petclinic/blob/main/src/main/java"))
                                    .forEach((component -> {
                                        clinicEmployee.uses(component, "Uses");
                                        component.addTags(component.getTechnology());
                                    }))
                                    .build()
                    )
                    .withStrategy(
                            new ComponentFinderStrategyBuilder()
                                    .matchedBy(new ImplementsTypeMatcher("org.springframework.data.repository.Repository"))
                                    .withDescription(new FirstSentenceDescriptionStrategy())
                                    .withTechnology("Spring Data Repository")
                                    .withUrl(new PrefixSourceUrlStrategy("https://github.com/spring-projects/spring-petclinic/blob/main/src/main/java"))
                                    .forEach((component -> {
                                        component.uses(relationalDatabaseSchema, "Reads from and writes to");
                                        component.addTags(component.getTechnology());
                                    }))
                                    .build()
                    )
                    .build();

            componentFinder.findComponents();
            assertEquals(7, webApplication.getComponents().size());

            Component welcomeController = webApplication.getComponentWithName("Welcome Controller");
            assertNotNull(welcomeController);
            assertEquals("org.springframework.samples.petclinic.system.WelcomeController", welcomeController.getProperties().get("component.type"));
            assertEquals("org/springframework/samples/petclinic/system/WelcomeController.java", welcomeController.getProperties().get("component.src"));
            assertEquals("https://github.com/spring-projects/spring-petclinic/blob/main/src/main/java/org/springframework/samples/petclinic/system/WelcomeController.java", welcomeController.getUrl());
            assertTrue(clinicEmployee.hasEfferentRelationshipWith(welcomeController));

            Component ownerController = webApplication.getComponentWithName("Owner Controller");
            assertNotNull(ownerController);
            assertEquals("org.springframework.samples.petclinic.owner.OwnerController", ownerController.getProperties().get("component.type"));
            assertEquals("org/springframework/samples/petclinic/owner/OwnerController.java", ownerController.getProperties().get("component.src"));
            assertEquals("https://github.com/spring-projects/spring-petclinic/blob/main/src/main/java/org/springframework/samples/petclinic/owner/OwnerController.java", ownerController.getUrl());
            assertTrue(clinicEmployee.hasEfferentRelationshipWith(ownerController));

            Component petController = webApplication.getComponentWithName("Pet Controller");
            assertNotNull(petController);
            assertEquals("org.springframework.samples.petclinic.owner.PetController", petController.getProperties().get("component.type"));
            assertEquals("org/springframework/samples/petclinic/owner/PetController.java", petController.getProperties().get("component.src"));
            assertEquals("https://github.com/spring-projects/spring-petclinic/blob/main/src/main/java/org/springframework/samples/petclinic/owner/PetController.java", petController.getUrl());
            assertTrue(clinicEmployee.hasEfferentRelationshipWith(petController));

            Component vetController = webApplication.getComponentWithName("Vet Controller");
            assertNotNull(vetController);
            assertEquals("org.springframework.samples.petclinic.vet.VetController", vetController.getProperties().get("component.type"));
            assertEquals("org/springframework/samples/petclinic/vet/VetController.java", vetController.getProperties().get("component.src"));
            assertEquals("https://github.com/spring-projects/spring-petclinic/blob/main/src/main/java/org/springframework/samples/petclinic/vet/VetController.java", vetController.getUrl());
            assertTrue(clinicEmployee.hasEfferentRelationshipWith(vetController));

            Component visitController = webApplication.getComponentWithName("Visit Controller");
            assertNotNull(visitController);
            assertEquals("org.springframework.samples.petclinic.owner.VisitController", visitController.getProperties().get("component.type"));
            assertEquals("org/springframework/samples/petclinic/owner/VisitController.java", visitController.getProperties().get("component.src"));
            assertEquals("https://github.com/spring-projects/spring-petclinic/blob/main/src/main/java/org/springframework/samples/petclinic/owner/VisitController.java", visitController.getUrl());
            assertTrue(clinicEmployee.hasEfferentRelationshipWith(visitController));

            Component ownerRepository = webApplication.getComponentWithName("Owner Repository");
            assertNotNull(ownerRepository);
            assertEquals("Repository class for Owner domain objects All method names are compliant with Spring Data naming conventions so this interface can easily be extended for Spring Data.", ownerRepository.getDescription());
            assertEquals("org.springframework.samples.petclinic.owner.OwnerRepository", ownerRepository.getProperties().get("component.type"));
            assertEquals("org/springframework/samples/petclinic/owner/OwnerRepository.java", ownerRepository.getProperties().get("component.src"));
            assertEquals("https://github.com/spring-projects/spring-petclinic/blob/main/src/main/java/org/springframework/samples/petclinic/owner/OwnerRepository.java", ownerRepository.getUrl());
            assertTrue(ownerRepository.hasEfferentRelationshipWith(relationalDatabaseSchema, "Reads from and writes to"));

            Component vetRepository = webApplication.getComponentWithName("Vet Repository");
            assertNotNull(vetRepository);
            assertEquals("Repository class for Vet domain objects All method names are compliant with Spring Data naming conventions so this interface can easily be extended for Spring Data.", vetRepository.getDescription());
            assertEquals("org.springframework.samples.petclinic.vet.VetRepository", vetRepository.getProperties().get("component.type"));
            assertEquals("org/springframework/samples/petclinic/vet/VetRepository.java", vetRepository.getProperties().get("component.src"));
            assertEquals("https://github.com/spring-projects/spring-petclinic/blob/main/src/main/java/org/springframework/samples/petclinic/vet/VetRepository.java", vetRepository.getUrl());
            assertTrue(vetRepository.hasEfferentRelationshipWith(relationalDatabaseSchema, "Reads from and writes to"));

            assertTrue(welcomeController.getRelationships().isEmpty());
            assertNotNull(petController.getEfferentRelationshipWith(ownerRepository));
            assertNotNull(visitController.getEfferentRelationshipWith(ownerRepository));
            assertNotNull(ownerController.getEfferentRelationshipWith(ownerRepository));
            assertNotNull(vetController.getEfferentRelationshipWith(vetRepository));
        } else {
            System.out.println("Skipping Spring PetClinic example...");
        }
    }

}
