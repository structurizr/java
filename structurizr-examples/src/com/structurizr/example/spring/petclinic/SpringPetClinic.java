package com.structurizr.example.spring.petclinic;

import com.structurizr.Workspace;

import static com.structurizr.example.spring.petclinic.ClassicSpringWorkspaceFactory.buildWorkspace;

/**
 * This is a C4 representation of the Spring PetClinic sample app
 * (https://github.com/spring-projects/spring-petclinic/) and you can see
 * the resulting diagrams at https://www.structurizr.com/public/1
 * <p>
 * Use the following command to run this example: ./gradlew :structurizr-example:springPetClinic
 * <p>
 * Please note, you will need to modify the paths specified in the structurizr-examples/build.gradle file.
 */
public class SpringPetClinic {

    public static void main(String[] args) throws Exception {
        final String sourceRoot = getSourceRoot(args);
        final Workspace workspace = buildWorkspace(sourceRoot);
        upload(workspace);
    }

    private static String getSourceRoot(String[] args) {
        final String sourceRoot = extractSourceRoot(args);
        verifyPetclinicOnClasspath();
        return sourceRoot;
    }

    private static String extractSourceRoot(String[] args) {
        if (args.length == 0) {
            System.out.println("The path to the Spring PetClinic source code must be provided.");
            System.exit(-1);
        }
        return args[0];
    }

    private static void verifyPetclinicOnClasspath() {
        try {
            Class.forName("org.springframework.samples.petclinic.model.Vet");
        } catch (ClassNotFoundException e) {
            System.err.println("Please check that the compiled version of the Spring PetClinic application is on the classpath");
            System.exit(-1);
        }
    }

    private static void upload(Workspace workspace) throws Exception {
        System.out.println("Workspace would be merged if key was known");
        System.out.println(workspace.getModel().getElements().size() + " elements");
        workspace.getModel().getElements().forEach( x-> System.out.println(x.getCanonicalName()));

//        final StructurizrClient structurizrClient = new StructurizrClient("key", "secret");
//        structurizrClient.mergeWorkspace(1, workspace);
    }
}