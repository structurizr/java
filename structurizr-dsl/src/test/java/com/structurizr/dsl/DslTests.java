package com.structurizr.dsl;

import com.structurizr.Workspace;
import com.structurizr.documentation.Section;
import com.structurizr.model.*;
import com.structurizr.util.StringUtils;
import com.structurizr.util.WorkspaceUtils;
import com.structurizr.view.*;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.io.File;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

class DslTests extends AbstractTests {

    @Test
    void test_test() throws Exception {
        StructurizrDslParser parser = new StructurizrDslParser();
        parser.parse(new File("src/test/resources/dsl/test.dsl"));

        assertFalse(parser.getWorkspace().isEmpty());
    }

    @Test
    void test_utf8() throws Exception {
        StructurizrDslParser parser = new StructurizrDslParser();
        parser.parse(new File("src/test/resources/dsl/utf8.dsl"));

        Workspace workspace = parser.getWorkspace();
        Model model = workspace.getModel();

        assertEquals(1, model.getPeople().size());
        Person user = model.getPersonWithName("你好 Usér \uD83D\uDE42");
        assertNotNull(user);
    }

    @Test
    void test_gettingstarted() throws Exception {
        StructurizrDslParser parser = new StructurizrDslParser();
        parser.parse(new File("src/test/resources/dsl/getting-started.dsl"));

        Workspace workspace = parser.getWorkspace();
        Model model = workspace.getModel();
        ViewSet views = workspace.getViews();

        assertEquals(1, model.getPeople().size());
        Person user = model.getPersonWithName("User");

        assertEquals(1, workspace.getModel().getSoftwareSystems().size());
        SoftwareSystem softwareSystem = model.getSoftwareSystemWithName("Software System");

        assertEquals(1, workspace.getModel().getRelationships().size());
        Relationship relationship = user.getRelationships().iterator().next();
        assertEquals("Uses", relationship.getDescription());
        assertSame(softwareSystem, relationship.getDestination());

        assertEquals(1, views.getViews().size());
        assertEquals(1, views.getSystemContextViews().size());
        SystemContextView view = views.getSystemContextViews().iterator().next();
        assertEquals("SystemContext-001", view.getKey());
        assertEquals(2, view.getElements().size());
        assertEquals(1, view.getRelationships().size());
    }

    @Test
    void test_aws() throws Exception {
        StructurizrDslParser parser = new StructurizrDslParser();
        parser.parse(new File("src/test/resources/dsl/amazon-web-services.dsl"));

        Workspace workspace = parser.getWorkspace();

        assertEquals(13, workspace.getModel().getElements().size());
        assertEquals(0, workspace.getModel().getPeople().size());
        assertEquals(1, workspace.getModel().getSoftwareSystems().size());
        assertEquals(2, workspace.getModel().getSoftwareSystemWithName("Spring PetClinic").getContainers().size());
        assertEquals(1, workspace.getModel().getDeploymentNodes().size());
        assertEquals(6, workspace.getModel().getElements().stream().filter(e -> e instanceof DeploymentNode).count());
        assertEquals(2, workspace.getModel().getElements().stream().filter(e -> e instanceof ContainerInstance).count());
        assertEquals(2, workspace.getModel().getElements().stream().filter(e -> e instanceof InfrastructureNode).count());

        assertEquals(4, workspace.getModel().getRelationships().size());

        assertEquals(0, workspace.getViews().getSystemLandscapeViews().size());
        assertEquals(0, workspace.getViews().getSystemContextViews().size());
        assertEquals(0, workspace.getViews().getContainerViews().size());
        assertEquals(0, workspace.getViews().getComponentViews().size());
        assertEquals(0, workspace.getViews().getDynamicViews().size());
        assertEquals(1, workspace.getViews().getDeploymentViews().size());

        DeploymentView deploymentView = workspace.getViews().getDeploymentViews().iterator().next();
        assertEquals(10, deploymentView.getElements().size());
        assertEquals(3, deploymentView.getRelationships().size());
        assertEquals(4, deploymentView.getAnimations().size());

        assertEquals(3, workspace.getViews().getConfiguration().getStyles().getElements().size());
        assertEquals(0, workspace.getViews().getConfiguration().getStyles().getRelationships().size());

        assertEquals(1, workspace.getViews().getConfiguration().getThemes().length);
    }

    @Test
    void test_awsLocal() throws Exception {
        StructurizrDslParser parser = new StructurizrDslParser();
        parser.parse(new File("src/test/resources/dsl/amazon-web-services-local.dsl"));

        Workspace workspace = parser.getWorkspace();

        assertEquals(13, workspace.getModel().getElements().size());
        assertEquals(0, workspace.getModel().getPeople().size());
        assertEquals(1, workspace.getModel().getSoftwareSystems().size());
        assertEquals(2, workspace.getModel().getSoftwareSystemWithName("Spring PetClinic").getContainers().size());
        assertEquals(1, workspace.getModel().getDeploymentNodes().size());
        assertEquals(6, workspace.getModel().getElements().stream().filter(e -> e instanceof DeploymentNode).count());
        assertEquals(2, workspace.getModel().getElements().stream().filter(e -> e instanceof ContainerInstance).count());
        assertEquals(2, workspace.getModel().getElements().stream().filter(e -> e instanceof InfrastructureNode).count());

        assertEquals(4, workspace.getModel().getRelationships().size());

        assertEquals(0, workspace.getViews().getSystemLandscapeViews().size());
        assertEquals(0, workspace.getViews().getSystemContextViews().size());
        assertEquals(0, workspace.getViews().getContainerViews().size());
        assertEquals(0, workspace.getViews().getComponentViews().size());
        assertEquals(0, workspace.getViews().getDynamicViews().size());
        assertEquals(1, workspace.getViews().getDeploymentViews().size());

        DeploymentView deploymentView = workspace.getViews().getDeploymentViews().iterator().next();
        assertEquals(10, deploymentView.getElements().size());
        assertEquals(3, deploymentView.getRelationships().size());
        assertEquals(4, deploymentView.getAnimations().size());

        assertEquals(3, workspace.getViews().getConfiguration().getStyles().getElements().size());
        assertEquals(0, workspace.getViews().getConfiguration().getStyles().getRelationships().size());

        assertEquals(1, workspace.getViews().getConfiguration().getThemes().length);
    }

    @Test
    void test_bigbankplc() throws Exception {
        StructurizrDslParser parser = new StructurizrDslParser();
        parser.parse(new File("src/test/resources/dsl/big-bank-plc.dsl"));

        Workspace workspace = parser.getWorkspace();

        assertEquals(51, workspace.getModel().getElements().size());
        assertEquals(3, workspace.getModel().getPeople().size());
        assertEquals(4, workspace.getModel().getSoftwareSystems().size());
        assertEquals(5, workspace.getModel().getSoftwareSystemWithName("Internet Banking System").getContainers().size());
        assertEquals(6, workspace.getModel().getSoftwareSystemWithName("Internet Banking System").getContainerWithName("API Application").getComponents().size());
        assertEquals(5, workspace.getModel().getDeploymentNodes().size());
        assertEquals(21, workspace.getModel().getElements().stream().filter(e -> e instanceof DeploymentNode).count());
        assertEquals(2, workspace.getModel().getElements().stream().filter(e -> e instanceof SoftwareSystemInstance).count());
        assertEquals(10, workspace.getModel().getElements().stream().filter(e -> e instanceof ContainerInstance).count());
        assertEquals(0, workspace.getModel().getElements().stream().filter(e -> e instanceof InfrastructureNode).count());

        assertEquals(42, workspace.getModel().getRelationships().size());

        assertEquals(1, workspace.getViews().getSystemLandscapeViews().size());
        assertEquals(1, workspace.getViews().getSystemContextViews().size());
        assertEquals(1, workspace.getViews().getContainerViews().size());
        assertEquals(1, workspace.getViews().getComponentViews().size());
        assertEquals(1, workspace.getViews().getDynamicViews().size());
        assertEquals(2, workspace.getViews().getDeploymentViews().size());

        assertEquals(7, workspace.getViews().getSystemLandscapeViews().iterator().next().getElements().size());
        assertEquals(9, workspace.getViews().getSystemLandscapeViews().iterator().next().getRelationships().size());

        assertEquals(4, workspace.getViews().getSystemContextViews().iterator().next().getElements().size());
        assertEquals(4, workspace.getViews().getSystemContextViews().iterator().next().getRelationships().size());

        assertEquals(8, workspace.getViews().getContainerViews().iterator().next().getElements().size());
        assertEquals(10, workspace.getViews().getContainerViews().iterator().next().getRelationships().size());

        assertEquals(11, workspace.getViews().getComponentViews().iterator().next().getElements().size());
        assertEquals(13, workspace.getViews().getComponentViews().iterator().next().getRelationships().size());

        assertEquals(4, workspace.getViews().getDynamicViews().iterator().next().getElements().size());
        assertEquals(6, workspace.getViews().getDynamicViews().iterator().next().getRelationships().size());

        assertEquals(13, workspace.getViews().getDeploymentViews().stream().filter(v -> v.getKey().equals("DevelopmentDeployment")).findFirst().get().getElements().size());
        assertEquals(4, workspace.getViews().getDeploymentViews().stream().filter(v -> v.getKey().equals("DevelopmentDeployment")).findFirst().get().getRelationships().size());

        assertEquals(20, workspace.getViews().getDeploymentViews().stream().filter(v -> v.getKey().equals("LiveDeployment")).findFirst().get().getElements().size());
        assertEquals(7, workspace.getViews().getDeploymentViews().stream().filter(v -> v.getKey().equals("LiveDeployment")).findFirst().get().getRelationships().size());

        assertEquals(11, workspace.getViews().getConfiguration().getStyles().getElements().size());
        assertEquals(0, workspace.getViews().getConfiguration().getStyles().getRelationships().size());

        assertEquals(0, workspace.getViews().getConfiguration().getThemes().length);

        assertEquals(0, workspace.getDocumentation().getSections().size());
        assertEquals(0, workspace.getDocumentation().getDecisions().size());
    }

    @Test
    void test_bigbankplc_systemlandscape() throws Exception {
        StructurizrDslParser parser = new StructurizrDslParser();
        parser.parse(new File("src/test/resources/dsl/big-bank-plc/system-landscape.dsl"));

        Workspace workspace = parser.getWorkspace();

        assertEquals(7, workspace.getModel().getElements().size());
        assertEquals(3, workspace.getModel().getPeople().size());
        assertEquals(4, workspace.getModel().getSoftwareSystems().size());

        assertEquals(9, workspace.getModel().getRelationships().size());

        assertEquals(1, workspace.getViews().getSystemLandscapeViews().size());
        assertEquals(0, workspace.getViews().getSystemContextViews().size());
        assertEquals(0, workspace.getViews().getContainerViews().size());
        assertEquals(0, workspace.getViews().getComponentViews().size());
        assertEquals(0, workspace.getViews().getDynamicViews().size());
        assertEquals(0, workspace.getViews().getDeploymentViews().size());

        assertEquals(7, workspace.getViews().getSystemLandscapeViews().iterator().next().getElements().size());
        assertEquals(9, workspace.getViews().getSystemLandscapeViews().iterator().next().getRelationships().size());

        assertEquals(4, workspace.getViews().getConfiguration().getStyles().getElements().size());
        assertEquals(0, workspace.getViews().getConfiguration().getStyles().getRelationships().size());

        assertEquals(0, workspace.getViews().getConfiguration().getThemes().length);
    }

    @Test
    void test_bigbankplc_internetbankingsystem() throws Exception {
        StructurizrDslParser parser = new StructurizrDslParser();
        parser.parse(new File("src/test/resources/dsl/big-bank-plc/internet-banking-system.dsl"));

        Workspace workspace = parser.getWorkspace();

        assertEquals(51, workspace.getModel().getElements().size());
        assertEquals(3, workspace.getModel().getPeople().size());
        assertEquals(4, workspace.getModel().getSoftwareSystems().size());
        assertEquals(5, workspace.getModel().getSoftwareSystemWithName("Internet Banking System").getContainers().size());
        assertEquals(6, workspace.getModel().getSoftwareSystemWithName("Internet Banking System").getContainerWithName("API Application").getComponents().size());
        assertEquals(5, workspace.getModel().getDeploymentNodes().size());
        assertEquals(21, workspace.getModel().getElements().stream().filter(e -> e instanceof DeploymentNode).count());
        assertEquals(2, workspace.getModel().getElements().stream().filter(e -> e instanceof SoftwareSystemInstance).count());
        assertEquals(10, workspace.getModel().getElements().stream().filter(e -> e instanceof ContainerInstance).count());
        assertEquals(0, workspace.getModel().getElements().stream().filter(e -> e instanceof InfrastructureNode).count());

        assertEquals(42, workspace.getModel().getRelationships().size());

        assertEquals(0, workspace.getViews().getSystemLandscapeViews().size());
        assertEquals(1, workspace.getViews().getSystemContextViews().size());
        assertEquals(1, workspace.getViews().getContainerViews().size());
        assertEquals(1, workspace.getViews().getComponentViews().size());
        assertEquals(1, workspace.getViews().getDynamicViews().size());
        assertEquals(2, workspace.getViews().getDeploymentViews().size());

        assertEquals(4, workspace.getViews().getSystemContextViews().iterator().next().getElements().size());
        assertEquals(4, workspace.getViews().getSystemContextViews().iterator().next().getRelationships().size());

        assertEquals(8, workspace.getViews().getContainerViews().iterator().next().getElements().size());
        assertEquals(10, workspace.getViews().getContainerViews().iterator().next().getRelationships().size());

        assertEquals(11, workspace.getViews().getComponentViews().iterator().next().getElements().size());
        assertEquals(13, workspace.getViews().getComponentViews().iterator().next().getRelationships().size());

        assertEquals(4, workspace.getViews().getDynamicViews().iterator().next().getElements().size());
        assertEquals(6, workspace.getViews().getDynamicViews().iterator().next().getRelationships().size());

        assertEquals(13, workspace.getViews().getDeploymentViews().stream().filter(v -> v.getKey().equals("DevelopmentDeployment")).findFirst().get().getElements().size());
        assertEquals(4, workspace.getViews().getDeploymentViews().stream().filter(v -> v.getKey().equals("DevelopmentDeployment")).findFirst().get().getRelationships().size());

        assertEquals(20, workspace.getViews().getDeploymentViews().stream().filter(v -> v.getKey().equals("LiveDeployment")).findFirst().get().getElements().size());
        assertEquals(7, workspace.getViews().getDeploymentViews().stream().filter(v -> v.getKey().equals("LiveDeployment")).findFirst().get().getRelationships().size());

        assertEquals(11, workspace.getViews().getConfiguration().getStyles().getElements().size());
        assertEquals(0, workspace.getViews().getConfiguration().getStyles().getRelationships().size());

        assertEquals(0, workspace.getViews().getConfiguration().getThemes().length);

        assertEquals(4, workspace.getModel().getSoftwareSystemWithName("Internet Banking System").getDocumentation().getSections().size());
        assertEquals(1, workspace.getModel().getSoftwareSystemWithName("Internet Banking System").getDocumentation().getDecisions().size());
    }

    @Test
    void test_frs() throws Exception {
        StructurizrDslParser parser = new StructurizrDslParser();
        parser.parse(new File("src/test/resources/dsl/financial-risk-system.dsl"));


        Workspace workspace = parser.getWorkspace();

        assertEquals(9, workspace.getModel().getElements().size());
        assertEquals(2, workspace.getModel().getPeople().size());
        assertEquals(7, workspace.getModel().getSoftwareSystems().size());
        assertEquals(0, workspace.getModel().getDeploymentNodes().size());
        assertEquals(0, workspace.getModel().getElements().stream().filter(e -> e instanceof DeploymentNode).count());
        assertEquals(0, workspace.getModel().getElements().stream().filter(e -> e instanceof ContainerInstance).count());
        assertEquals(0, workspace.getModel().getElements().stream().filter(e -> e instanceof InfrastructureNode).count());

        assertEquals(9, workspace.getModel().getRelationships().size());

        assertEquals(0, workspace.getViews().getSystemLandscapeViews().size());
        assertEquals(1, workspace.getViews().getSystemContextViews().size());
        assertEquals(0, workspace.getViews().getContainerViews().size());
        assertEquals(0, workspace.getViews().getComponentViews().size());
        assertEquals(0, workspace.getViews().getDynamicViews().size());
        assertEquals(0, workspace.getViews().getDeploymentViews().size());

        assertEquals(9, workspace.getViews().getSystemContextViews().iterator().next().getElements().size());
        assertEquals(9, workspace.getViews().getSystemContextViews().iterator().next().getRelationships().size());

        assertEquals(5, workspace.getViews().getConfiguration().getStyles().getElements().size());
        assertEquals(4, workspace.getViews().getConfiguration().getStyles().getRelationships().size());

        assertEquals(0, workspace.getViews().getConfiguration().getThemes().length);
    }

    @Test
    void test_includeLocalFile() throws Exception {
        StructurizrDslParser parser = new StructurizrDslParser();
        parser.parse(new File("src/test/resources/dsl/include-file.dsl"));

        Workspace workspace = parser.getWorkspace();
        Model model = workspace.getModel();

        assertEquals(1, model.getSoftwareSystems().size());
        assertNotNull(model.getSoftwareSystemWithName("Software System"));

        assertEquals("", DslUtils.getDsl(workspace));
    }

    @Test
    void test_includeLocalDirectory() throws Exception {
        File hiddenFile = new File("src/test/resources/dsl/include/model/software-system/.DS_Store");
        if (hiddenFile.exists()) {
            hiddenFile.delete();
        }

        StructurizrDslParser parser = new StructurizrDslParser();
        parser.parse(new File("src/test/resources/dsl/include-directory.dsl"));

        Workspace workspace = parser.getWorkspace();
        Model model = workspace.getModel();

        assertEquals(3, workspace.getModel().getSoftwareSystems().size());
        SoftwareSystem softwareSystem1 = model.getSoftwareSystemWithName("Software System 1");
        assertNotNull(softwareSystem1);
        assertEquals(1, softwareSystem1.getDocumentation().getSections().size());

        SoftwareSystem softwareSystem2 = model.getSoftwareSystemWithName("Software System 2");
        assertNotNull(softwareSystem2);
        assertEquals(1, softwareSystem2.getDocumentation().getSections().size());

        SoftwareSystem softwareSystem3 = model.getSoftwareSystemWithName("Software System 3");
        assertNotNull(softwareSystem3);
        assertEquals(1, softwareSystem3.getDocumentation().getSections().size());

        assertEquals("", DslUtils.getDsl(workspace));
    }

    @Test
    void test_includeLocalDirectory_WhenThereAreHiddenFiles() throws Exception {
        File hiddenFile = new File("src/test/resources/dsl/include/model/software-system/.DS_Store");
        if (hiddenFile.exists()) {
            hiddenFile.delete();
        }
        Files.writeString(hiddenFile.toPath(), "hello world");

        StructurizrDslParser parser = new StructurizrDslParser();
        parser.parse(new File("src/test/resources/dsl/include-directory.dsl"));
    }

    @Test
    @Tag("IntegrationTest")
    void test_includeUrl() throws Exception {
        StructurizrDslParser parser = new StructurizrDslParser();
        parser.parse(new File("src/test/resources/dsl/include-url.dsl"));

        Workspace workspace = parser.getWorkspace();
        Model model = workspace.getModel();

        assertEquals(1, model.getSoftwareSystems().size());
        assertNotNull(model.getSoftwareSystemWithName("Software System"));

        assertEquals("""
                workspace {
                
                    model {
                        !include https://raw.githubusercontent.com/structurizr/java/refs/heads/master/structurizr-dsl/src/test/resources/dsl/include/model.dsl
                    }
                
                }""", DslUtils.getDsl(workspace));
    }

    @Test
    void test_includeLocalFile_ThrowsAnException_WhenRunningInRestrictedMode() {
        try {
            StructurizrDslParser parser = new StructurizrDslParser();
            parser.setRestricted(true);

            // the model include will be ignored, so no software systems
            parser.parse(new File("src/test/resources/dsl/include-file.dsl"));
            fail();
        } catch (Exception e) {
            System.out.println(e.getMessage());
            assertTrue(e.getMessage().startsWith("!include <file> is not available when the parser is running in restricted mode"));
        }
    }

    @Test
    @Tag("IntegrationTest")
    void test_extendWorkspaceFromJsonFile() throws Exception {
        String dslFile = "src/test/resources/dsl/extend/extend-workspace-from-json-file.dsl";
        StructurizrDslParser parser = new StructurizrDslParser();
        parser.parse(new File(dslFile));

        Workspace workspace = parser.getWorkspace();
        Model model = workspace.getModel();
        assertEquals(CreateImpliedRelationshipsUnlessAnyRelationshipExistsStrategy.class, model.getImpliedRelationshipsStrategy().getClass());

        assertEquals(1, model.getPeople().size());
        Person user = model.getPersonWithName("User");

        assertEquals(3, workspace.getModel().getSoftwareSystems().size());
        SoftwareSystem softwareSystem = model.getSoftwareSystemWithName("Software System 1");
        assertTrue(user.hasEfferentRelationshipWith(softwareSystem, "Uses"));

        assertEquals(2, softwareSystem.getContainers().size());
        assertNotNull(softwareSystem.getContainers().stream().filter(c -> c.getName().equals("Web Application 1")).findFirst());
        assertNotNull(softwareSystem.getContainers().stream().filter(c -> c.getName().equals("Web Application 2")).findFirst());

        assertEquals("", DslUtils.getDsl(workspace));
    }

    @Test
    @Tag("IntegrationTest")
    void test_extendWorkspaceFromJsonUrl() throws Exception {
        String dslFile = "src/test/resources/dsl/extend/extend-workspace-from-json-url.dsl";
        StructurizrDslParser parser = new StructurizrDslParser();
        parser.parse(new File(dslFile));

        Workspace workspace = parser.getWorkspace();
        Model model = workspace.getModel();
        assertEquals(CreateImpliedRelationshipsUnlessAnyRelationshipExistsStrategy.class, model.getImpliedRelationshipsStrategy().getClass());

        assertEquals(1, model.getPeople().size());
        Person user = model.getPersonWithName("User");

        assertEquals(3, workspace.getModel().getSoftwareSystems().size());
        SoftwareSystem softwareSystem = model.getSoftwareSystemWithName("Software System 1");
        assertTrue(user.hasEfferentRelationshipWith(softwareSystem, "Uses"));

        assertEquals(2, softwareSystem.getContainers().size());
        assertNotNull(softwareSystem.getContainers().stream().filter(c -> c.getName().equals("Web Application 1")).findFirst());
        assertNotNull(softwareSystem.getContainers().stream().filter(c -> c.getName().equals("Web Application 2")).findFirst());

        assertEquals("""
                        workspace extends https://raw.githubusercontent.com/structurizr/dsl/master/src/test/dsl/extend/workspace.json {
                        
                            model {
                                // !element with DSL identifier
                                !element softwareSystem1 {
                                    webapp1 = container "Web Application 1"
                                }
                        
                                // !element with canonical name
                                !element "SoftwareSystem://Software System 1" {
                                    webapp2 = container "Web Application 2"
                                }
                        
                                user -> softwareSystem1 "Uses"
                                softwareSystem3.webapp -> softwareSystem3.db
                            }
                        
                        }""", DslUtils.getDsl(workspace));
    }

    @Test
    void test_extendWorkspaceFromJsonFile_WhenRunningInRestrictedMode() throws Exception {
        StructurizrDslParser parser = new StructurizrDslParser();
        parser.setRestricted(true);

        File dslFile = new File("src/test/resources/dsl/extend/extend-workspace-from-json-file.dsl");

        try {
            // this will fail, because the model import will be ignored
            parser.parse(dslFile);
            fail();
        } catch (StructurizrDslParserException e) {
            assertEquals("Cannot import workspace from a file when running in restricted mode at line 1 of " + dslFile.getAbsolutePath() + ": workspace extends workspace.json {", e.getMessage());
        }
    }

    @ParameterizedTest
    @Tag("IntegrationTest")
    @ValueSource(strings = { "src/test/resources/dsl/extend/extend-workspace-from-dsl-file.dsl", "src/test/resources/dsl/extend/extend-workspace-from-dsl-url.dsl" })
    void test_extendWorkspaceFromDsl(String dslFile) throws Exception {
        StructurizrDslParser parser = new StructurizrDslParser();
        parser.parse(new File(dslFile));

        Workspace workspace = parser.getWorkspace();
        assertEquals(IdentifierScope.Hierarchical, parser.getIdentifierScope());

        Model model = workspace.getModel();
        assertEquals(CreateImpliedRelationshipsUnlessAnyRelationshipExistsStrategy.class, model.getImpliedRelationshipsStrategy().getClass());

        assertEquals(1, model.getPeople().size());
        Person user = model.getPersonWithName("User");

        assertEquals(3, workspace.getModel().getSoftwareSystems().size());
        SoftwareSystem softwareSystem = model.getSoftwareSystemWithName("Software System 1");
        assertTrue(user.hasEfferentRelationshipWith(softwareSystem, "Uses"));

        assertEquals(1, softwareSystem.getContainers().size());
        assertEquals("Web Application", softwareSystem.getContainers().iterator().next().getName());
    }

    @Test
    void test_extendWorkspaceFromDslFile_WhenRunningInRestrictedMode() throws Exception {
        StructurizrDslParser parser = new StructurizrDslParser();
        parser.setRestricted(true);

        File dslFile = new File("src/test/resources/dsl/extend/extend-workspace-from-dsl-file.dsl");
        try {
            // this will fail, because the model import will be ignored
            parser.parse(dslFile);
            fail();
        } catch (StructurizrDslParserException e) {
            assertEquals("Cannot import workspace from a file when running in restricted mode at line 1 of " + dslFile.getAbsolutePath() +": workspace extends workspace.dsl {", e.getMessage());
        }
    }

    @Test
    void test_extendWorkspaceFromDslFiles() throws Exception {
        StructurizrDslParser parser = new StructurizrDslParser();
        parser.parse(new File("src/test/resources/dsl/extend/4.dsl"));

        Workspace workspace = parser.getWorkspace();
        Model model = workspace.getModel();
        ViewSet views = workspace.getViews();

        assertEquals(3, model.getPeople().size());
        assertEquals(1, views.getViews().size());
    }

    @Test
    void test_findElement() throws Exception {
        StructurizrDslParser parser = new StructurizrDslParser();
        parser.parse(new File("src/test/resources/dsl/find-element.dsl"));

        assertNotNull(parser.getWorkspace().getModel().getElementWithCanonicalName("InfrastructureNode://Live/Amazon Web Services/New deployment node/New infrastructure node"));
        assertNotNull(parser.getWorkspace().getModel().getElementWithCanonicalName("InfrastructureNode://Live/Amazon Web Services/US-East-1/New deployment node 1/New infrastructure node 1"));
        assertNotNull(parser.getWorkspace().getModel().getElementWithCanonicalName("InfrastructureNode://Live/Amazon Web Services/US-East-1/New deployment node 2/New infrastructure node 2"));
    }

    @Test
    void test_findElement_Hierarchical() throws Exception {
        File dslFile = new File("src/test/resources/dsl/find-element-hierarchical.dsl");

        StructurizrDslParser parser = new StructurizrDslParser();
        parser.parse(dslFile);

        Component component = parser.getWorkspace().getModel().getSoftwareSystemWithName("A").getContainerWithName("B").getComponentWithName("C");
        assertEquals("Value1", component.getProperties().get("Name1"));
        assertEquals("Value2", component.getProperties().get("Name2"));
        assertEquals("Value3", component.getProperties().get("Name3"));
    }

    @Test
    void test_findElements_InFlatGroup() throws Exception {
        StructurizrDslParser parser = new StructurizrDslParser();
        parser.parse(new File("src/test/resources/dsl/find-elements-in-flat-group.dsl"));

        Person user = parser.getWorkspace().getModel().getPersonWithName("User");
        assertTrue(user.hasEfferentRelationshipWith(parser.getWorkspace().getModel().getSoftwareSystemWithName("A"), "Uses"));
        assertTrue(user.hasEfferentRelationshipWith(parser.getWorkspace().getModel().getSoftwareSystemWithName("B"), "Uses"));
        assertTrue(user.hasEfferentRelationshipWith(parser.getWorkspace().getModel().getSoftwareSystemWithName("C"), "Uses"));
    }

    @Test
    void test_findElements_InNestedGroup() throws Exception {
        StructurizrDslParser parser = new StructurizrDslParser();
        parser.parse(new File("src/test/resources/dsl/find-elements-in-nested-group.dsl"));

        Person user1 = parser.getWorkspace().getModel().getPersonWithName("User 1");
        assertTrue(user1.hasEfferentRelationshipWith(parser.getWorkspace().getModel().getSoftwareSystemWithName("A"), "Uses"));
        assertTrue(user1.hasEfferentRelationshipWith(parser.getWorkspace().getModel().getSoftwareSystemWithName("B"), "Uses"));
        assertTrue(user1.hasEfferentRelationshipWith(parser.getWorkspace().getModel().getSoftwareSystemWithName("C"), "Uses"));

        Person user2 = parser.getWorkspace().getModel().getPersonWithName("User 2");
        assertTrue(user2.hasEfferentRelationshipWith(parser.getWorkspace().getModel().getSoftwareSystemWithName("A"), "Uses"));
        assertFalse(user2.hasEfferentRelationshipWith(parser.getWorkspace().getModel().getSoftwareSystemWithName("B"), "Uses"));
        assertFalse(user2.hasEfferentRelationshipWith(parser.getWorkspace().getModel().getSoftwareSystemWithName("C"), "Uses"));
    }

    @Test
    void test_parallel1() throws Exception {
        StructurizrDslParser parser = new StructurizrDslParser();
        parser.parse(new File("src/test/resources/dsl/parallel1.dsl"));

        assertFalse(parser.getWorkspace().isEmpty());
        DynamicView view = parser.getWorkspace().getViews().getDynamicViews().iterator().next();
        List<RelationshipView> relationships = new ArrayList<>(view.getRelationships());
        assertEquals(4, relationships.size());
        assertEquals("1", relationships.get(0).getOrder());
        assertEquals("2", relationships.get(1).getOrder());
        assertEquals("3", relationships.get(2).getOrder());
        assertEquals("3", relationships.get(3).getOrder());
    }

    @Test
    void test_parallel2() throws Exception {
        StructurizrDslParser parser = new StructurizrDslParser();
        parser.parse(new File("src/test/resources/dsl/parallel2.dsl"));

        assertFalse(parser.getWorkspace().isEmpty());
        DynamicView view = parser.getWorkspace().getViews().getDynamicViews().iterator().next();
        List<RelationshipView> relationships = new ArrayList<>(view.getRelationships());
        assertEquals(4, relationships.size());
        assertEquals("1", relationships.get(0).getOrder());
        assertEquals("2", relationships.get(1).getOrder());
        assertEquals("2", relationships.get(2).getOrder());
        assertEquals("3", relationships.get(3).getOrder());
    }

    @Test
    void test_parallel3() throws Exception {
        StructurizrDslParser parser = new StructurizrDslParser();
        parser.parse(new File("src/test/resources/dsl/parallel3.dsl"));

        assertFalse(parser.getWorkspace().isEmpty());
        DynamicView view = parser.getWorkspace().getViews().getDynamicViews().iterator().next();
        List<RelationshipView> relationships = new ArrayList<>(view.getRelationships());
        assertEquals(4, relationships.size());
        assertEquals("1", relationships.get(0).getOrder());
        assertEquals("2", relationships.get(1).getOrder());
        assertEquals("2", relationships.get(2).getOrder());
        assertEquals("3", relationships.get(3).getOrder());
    }

    @Test
    void test_groups() throws Exception {
        StructurizrDslParser parser = new StructurizrDslParser();
        parser.parse(new File("src/test/resources/dsl/groups.dsl"));

        ContainerView containerView = parser.getWorkspace().getViews().getContainerViews().iterator().next();
        assertEquals(4, containerView.getElements().size());

        DeploymentView deploymentView = parser.getWorkspace().getViews().getDeploymentViews().iterator().next();
        assertEquals(6, deploymentView.getElements().size());
    }

    @Test
    void test_nested_groups() throws Exception {
        StructurizrDslParser parser = new StructurizrDslParser();
        parser.parse(new File("src/test/resources/dsl/groups-nested.dsl"));

        SoftwareSystem a = parser.getWorkspace().getModel().getSoftwareSystemWithName("A");
        assertEquals("Organisation/Department A", a.getGroup());

        Container aApi = a.getContainerWithName("A API");
        assertEquals("Capability 1/Service A", aApi.getGroup());

        Component aApiEndpoint = aApi.getComponentWithName("API Endpoint");
        assertEquals("a-api.jar/API Layer", aApiEndpoint.getGroup());

        Component aApiRepository = aApi.getComponentWithName("Repository");
        assertEquals("a-api.jar/Data Layer", aApiRepository.getGroup());

        Container aDatabase = a.getContainerWithName("A Database");
        assertEquals("Capability 1/Service A", aDatabase.getGroup());

        Container bApi = a.getContainerWithName("B API");
        assertEquals("Capability 1/Service B", bApi.getGroup());

        Container bDatabase = a.getContainerWithName("B Database");
        assertEquals("Capability 1/Service B", bDatabase.getGroup());

        SoftwareSystem b = parser.getWorkspace().getModel().getSoftwareSystemWithName("B");
        assertEquals("Organisation/Department B", b.getGroup());

        SoftwareSystem c = parser.getWorkspace().getModel().getSoftwareSystemWithName("C");
        assertEquals("Organisation", c.getGroup());
    }

    @Test
    void test_hierarchicalIdentifiers() throws Exception {
        StructurizrDslParser parser = new StructurizrDslParser();
        parser.parse(new File("src/test/resources/dsl/hierarchical-identifiers.dsl"));

        Workspace workspace = parser.getWorkspace();
        assertEquals(0, workspace.getModel().getSoftwareSystemWithName("B").getRelationships().size());
    }

    @Test
    void test_hierarchicalIdentifiersWhenUnassigned() throws Exception {
        StructurizrDslParser parser = new StructurizrDslParser();
        parser.parse(new File("src/test/resources/dsl/hierarchical-identifiers-when-unassigned.dsl"));

        Workspace workspace = parser.getWorkspace();
        IdentifiersRegister identifiersRegister = parser.getIdentifiersRegister();

        assertEquals(6, identifiersRegister.getElementIdentifiers().size());
        for (String identifier : identifiersRegister.getElementIdentifiers()) {
            assertFalse(identifier.startsWith("null"));
        }
    }

    @Test
    void test_hierarchicalIdentifiersAndDeploymentNodes() throws Exception {
        StructurizrDslParser parser = new StructurizrDslParser();
        parser.parse(new File("src/test/resources/dsl/hierarchical-identifiers-and-deployment-nodes-1.dsl"));
    }

    @Test
    void test_hierarchicalIdentifiersAndDeploymentNodes_WhenSoftwareSystemNameClashes() throws Exception {
        StructurizrDslParser parser = new StructurizrDslParser();
        parser.parse(new File("src/test/resources/dsl/hierarchical-identifiers-and-deployment-nodes-2.dsl"));
    }

    @Test
    void test_hierarchicalIdentifiersAndDeploymentNodes_WhenSoftwareContainerClashes() throws Exception {
        StructurizrDslParser parser = new StructurizrDslParser();
        parser.parse(new File("src/test/resources/dsl/hierarchical-identifiers-and-deployment-nodes-3.dsl"));
    }

    @Test
    void test_plugin_ThrowsAnException_WhenTheParserIsRunningInRestrictedMode() {
        try {
            StructurizrDslParser parser = new StructurizrDslParser();
            parser.setRestricted(true);
            parser.parse(new File("src/test/resources/dsl/plugin-without-parameters.dsl"));
            fail();
        } catch (Exception e) {
            System.out.println(e.getMessage());
            assertTrue(e.getMessage().startsWith("!plugin is not available when the parser is running in restricted mode"));
        }
    }

    @Test
    void test_pluginWithoutParameters() throws Exception {
        StructurizrDslParser parser = new StructurizrDslParser();
        parser.parse(new File("src/test/resources/dsl/plugin-without-parameters.dsl"));

        assertNotNull(parser.getWorkspace().getModel().getPersonWithName("Java"));

        // check source isn't retained
        assertEquals("", DslUtils.getDsl(parser.getWorkspace()));
    }

    @Test
    void test_pluginWithParameters() throws Exception {
        StructurizrDslParser parser = new StructurizrDslParser();
        parser.parse(new File("src/test/resources/dsl/plugin-with-parameters.dsl"));

        assertNotNull(parser.getWorkspace().getModel().getPersonWithName("Java"));

        // check source isn't retained
        assertEquals("", DslUtils.getDsl(parser.getWorkspace()));
    }

    @Test
    void test_script_ThrowsAnException_WhenTheParserIsInRestrictedMode() {
        try {
            StructurizrDslParser parser = new StructurizrDslParser();
            parser.setRestricted(true);
            parser.parse(new File("src/test/resources/dsl/script-external.dsl"));
            fail();
        } catch (Exception e) {
            assertTrue(e.getMessage().startsWith("!script is not available when the parser is running in restricted mode"));
        }
    }

    @Test
    void test_externalScript() throws Exception {
        StructurizrDslParser parser = new StructurizrDslParser();
        parser.parse(new File("src/test/resources/dsl/script-external.dsl"));

        assertNotNull(parser.getWorkspace().getModel().getPersonWithName("Groovy"));
        assertNotNull(parser.getWorkspace().getModel().getPersonWithName("Kotlin"));
        assertNotNull(parser.getWorkspace().getModel().getPersonWithName("Ruby"));

        // check source isn't retained
        assertEquals("", DslUtils.getDsl(parser.getWorkspace()));
    }

    @Test
    void test_externalScriptWithParameters() throws Exception {
        StructurizrDslParser parser = new StructurizrDslParser();
        parser.parse(new File("src/test/resources/dsl/script-external-with-parameters.dsl"));

        assertNotNull(parser.getWorkspace().getModel().getPersonWithName("Groovy"));

        // check source isn't retained
        assertEquals("", DslUtils.getDsl(parser.getWorkspace()));
    }

    @Test
    void test_inlineScript() throws Exception {
        StructurizrDslParser parser = new StructurizrDslParser();
        parser.parse(new File("src/test/resources/dsl/script-inline.dsl"));

        assertNotNull(parser.getWorkspace().getModel().getPersonWithName("Groovy"));
        assertNotNull(parser.getWorkspace().getModel().getPersonWithName("Kotlin"));
        assertNotNull(parser.getWorkspace().getModel().getPersonWithName("Ruby"));

        assertTrue(parser.getWorkspace().getModel().getPersonWithName("User").hasTag("Groovy"));
        assertTrue(parser.getWorkspace().getModel().getPersonWithName("User").getRelationships().iterator().next().hasTag("Groovy"));
        assertEquals("Groovy", parser.getWorkspace().getViews().getSystemLandscapeViews().iterator().next().getDescription());

        // check source isn't retained
        assertEquals("", DslUtils.getDsl(parser.getWorkspace()));
    }

    @Test
    void test_docs() throws Exception {
        StructurizrDslParser parser = new StructurizrDslParser();
        parser.parse(new File("src/test/resources/dsl/docs/workspace.dsl"));

        SoftwareSystem softwareSystem = parser.getWorkspace().getModel().getSoftwareSystemWithName("Software System");
        Container container = softwareSystem.getContainerWithName("Container");
        Component component = container.getComponentWithName("Component");

        Collection<Section> sections = parser.getWorkspace().getDocumentation().getSections();
        assertEquals(1, sections.size());
        assertEquals("""
            ## Workspace
            
            Content...""", sections.iterator().next().getContent());

        sections = softwareSystem.getDocumentation().getSections();
        assertEquals(1, sections.size());
        assertEquals("""
            ## Software System
                           
            Content...""", sections.iterator().next().getContent());

        sections = container.getDocumentation().getSections();
        assertEquals(1, sections.size());
        assertEquals("""
            ## Container
                           
            Content...""", sections.iterator().next().getContent());

        sections = component.getDocumentation().getSections();
        assertEquals(1, sections.size());
        assertEquals("""
            ## Component
                           
            Content...""", sections.iterator().next().getContent());

        assertEquals(1, component.getDocumentation().getSections().size());

        // check source isn't retained
        assertEquals("", DslUtils.getDsl(parser.getWorkspace()));
    }

    @Test
    void test_docs_ThrowsAnException_WhenTheParserIsInRestrictedMode() {
        try {
            StructurizrDslParser parser = new StructurizrDslParser();
            parser.setRestricted(true);
            parser.parse(new File("src/test/resources/dsl/docs/workspace.dsl"));
            fail();
        } catch (Exception e) {
            assertTrue(e.getMessage().startsWith("!docs is not available when the parser is running in restricted mode"));
        }
    }

    @Test
    void test_decisions() throws Exception {
        StructurizrDslParser parser = new StructurizrDslParser();
        parser.parse(new File("src/test/resources/dsl/decisions/workspace.dsl"));

        SoftwareSystem softwareSystem = parser.getWorkspace().getModel().getSoftwareSystemWithName("Software System");
        Container container = softwareSystem.getContainerWithName("Container");
        Component component = container.getComponentWithName("Component");

        // adrtools decisions
        assertEquals(10, parser.getWorkspace().getDocumentation().getDecisions().size());
        assertEquals(10, softwareSystem.getDocumentation().getDecisions().size());

        // madr decisions
        assertEquals(19, container.getDocumentation().getDecisions().size());

        // log4brains decisions
        assertEquals(4, component.getDocumentation().getDecisions().size());

        // check source isn't retained
        assertEquals("", DslUtils.getDsl(parser.getWorkspace()));
    }

    @Test
    void test_decisions_ThrowsAnException_WhenTheParserIsInRestrictedMode() {
        try {
            StructurizrDslParser parser = new StructurizrDslParser();
            parser.setRestricted(true);
            parser.parse(new File("src/test/resources/dsl/decisions/workspace.dsl"));
            fail();
        } catch (Exception e) {
            assertTrue(e.getMessage().startsWith("!adrs is not available when the parser is running in restricted mode"));
        }
    }

    @Test
    void test_this() throws Exception {
        StructurizrDslParser parser = new StructurizrDslParser();
        parser.parse(new File("src/test/resources/dsl/this.dsl"));
    }

    @Test
    void test_workspaceWithControlCharacters() throws Exception {
        StructurizrDslParser parser = new StructurizrDslParser();
        parser.parse(new File("src/test/resources/dsl/workspace-with-bom.dsl"));
    }

    @Test
    void test_excludeRelationships() throws Exception {
        StructurizrDslParser parser = new StructurizrDslParser();
        parser.parse(new File("src/test/resources/dsl/exclude-relationships.dsl"));
    }

    @Test
    void test_unexpectedTokensBeforeWorkspace() {
        File dslFile = new File("src/test/resources/dsl/unexpected-tokens-before-workspace.dsl");

        try {
            StructurizrDslParser parser = new StructurizrDslParser();
            parser.parse(dslFile);
            fail();
        } catch (StructurizrDslParserException e) {
            assertEquals("Unexpected tokens (expected: workspace) at line 1 of " + dslFile.getAbsolutePath() + ": hello world", e.getMessage());
        }
    }

    @Test
    void test_unexpectedTokensAfterWorkspace() {
        File dslFile = new File("src/test/resources/dsl/unexpected-tokens-after-workspace.dsl");

        try {
            StructurizrDslParser parser = new StructurizrDslParser();
            parser.parse(dslFile);
            fail();
        } catch (StructurizrDslParserException e) {
            assertEquals("Unexpected tokens at line 4 of " + dslFile.getAbsolutePath() + ": hello world", e.getMessage());
        }
    }

    @Test
    void test_unexpectedTokensInWorkspace() {
        File dslFile = new File("src/test/resources/dsl/unexpected-tokens-in-workspace.dsl");

        try {
            StructurizrDslParser parser = new StructurizrDslParser();
            parser.parse(dslFile);
            fail();
        } catch (StructurizrDslParserException e) {
            assertEquals("Unexpected tokens (expected: name, description, properties, !docs, !decisions, !identifiers, !impliedRelationships, model, views, configuration) at line 3 of " + dslFile.getAbsolutePath() + ": softwareSystem \"Name\"", e.getMessage());
        }
    }

    @Test
    void test_urlNotPermittedInGroup() {
        File dslFile = new File("src/test/resources/dsl/group-url.dsl");

        try {
            StructurizrDslParser parser = new StructurizrDslParser();
            parser.parse(dslFile);
            fail();
        } catch (StructurizrDslParserException e) {
            assertEquals("Unexpected tokens (expected: !docs, !decisions, group, container, description, tags, url, properties, perspectives, ->) at line 6 of " + dslFile.getAbsolutePath() + ": url \"https://example.com\"", e.getMessage());
        }
    }

    @Test
    void test_multipleWorkspaceTokens_ThrowsAnException() {
        File dslFile = new File("src/test/resources/dsl/multiple-workspace-tokens.dsl");

        try {
            StructurizrDslParser parser = new StructurizrDslParser();
            parser.parse(dslFile);
            fail();
        } catch (StructurizrDslParserException e) {
            assertEquals("Multiple workspaces are not permitted in a DSL definition at line 9 of " + dslFile.getAbsolutePath() + ": workspace {", e.getMessage());
        }
    }

    @Test
    void test_multipleModelTokens_ThrowsAnException() {
        File dslFile = new File("src/test/resources/dsl/multiple-model-tokens.dsl");

        try {
            StructurizrDslParser parser = new StructurizrDslParser();
            parser.parse(dslFile);
            fail();
        } catch (StructurizrDslParserException e) {
            assertEquals("Multiple models are not permitted in a DSL definition at line 7 of " + dslFile.getAbsolutePath() + ": model {", e.getMessage());
        }
    }

    @Test
    void test_multipleViewTokens_ThrowsAnException() {
        File dslFile = new File("src/test/resources/dsl/multiple-view-tokens.dsl");

        try {
            StructurizrDslParser parser = new StructurizrDslParser();
            parser.parse(dslFile);
            fail();
        } catch (StructurizrDslParserException e) {
            assertEquals("Multiple view sets are not permitted in a DSL definition at line 13 of " + dslFile.getAbsolutePath() + ": views {", e.getMessage());
        }
    }

    @Test
    void test_dynamicViewWithExplicitRelationships() throws Exception {
        StructurizrDslParser parser = new StructurizrDslParser();
        parser.parse(new File("src/test/resources/dsl/dynamic-view-with-explicit-relationships.dsl"));
    }

    @Test
    void test_dynamicViewWithCustomElements() throws Exception {
        StructurizrDslParser parser = new StructurizrDslParser();
        parser.parse(new File("src/test/resources/dsl/dynamic-view-with-custom-elements.dsl"));
    }

    @Test
    void test_dynamicViewWithExplicitOrdering() throws Exception {
        StructurizrDslParser parser = new StructurizrDslParser();
        parser.parse(new File("src/test/resources/dsl/dynamic-view-with-explicit-ordering.dsl"));
        DynamicView view = parser.getWorkspace().getViews().getDynamicViews().iterator().next();
        Set<RelationshipView> relationships = view.getRelationships();
        Iterator<RelationshipView> it = relationships.iterator();
        assertEquals("2", it.next().getOrder());
        assertEquals("3", it.next().getOrder());
    }

    @Test
    void test_workspaceProperties() throws Exception {
        StructurizrDslParser parser = new StructurizrDslParser();
        parser.parse(new File("src/test/resources/dsl/workspace-properties.dsl"));

        assertEquals("false", parser.getWorkspace().getProperties().get("structurizr.dslEditor"));
    }

    @Test
    void test_viewsWithoutKeys() throws Exception {
        StructurizrDslParser parser = new StructurizrDslParser();
        parser.parse(new File("src/test/resources/dsl/views-without-keys.dsl"));

        assertTrue(parser.getWorkspace().getViews().getSystemLandscapeViews().stream().anyMatch(view -> view.getKey().equals("SystemLandscape-001")));
        assertTrue(parser.getWorkspace().getViews().getSystemLandscapeViews().stream().anyMatch(view -> view.getKey().equals("SystemLandscape-002")));
        assertTrue(parser.getWorkspace().getViews().getSystemLandscapeViews().stream().anyMatch(view -> view.getKey().equals("SystemLandscape-003")));
    }

    @Test
    void test_identifiers() throws Exception {
        StructurizrDslParser parser = new StructurizrDslParser();
        parser.parse(new File("src/test/resources/dsl/identifiers.dsl"));

        Workspace workspace = parser.getWorkspace();
        Model model = workspace.getModel();

        Person user = model.getPersonWithName("User");
        SoftwareSystem softwareSystem = model.getSoftwareSystemWithName("Software System");
        Container container = softwareSystem.getContainerWithName("Container");
        Relationship relationship = user.getEfferentRelationshipWith(container);
        Relationship impliedRelationship = user.getEfferentRelationshipWith(softwareSystem);

        IdentifiersRegister register = parser.getIdentifiersRegister();
        assertEquals("user", register.findIdentifier(user));
        assertEquals("softwaresystem", register.findIdentifier(softwareSystem));
        assertEquals("softwaresystem.container", register.findIdentifier(container));
        assertEquals("rel", register.findIdentifier(relationship));

        assertSame(user, register.getElement("user"));
        assertSame(softwareSystem, register.getElement("softwareSystem"));
        assertSame(container, register.getElement("softwareSystem.container"));
        assertSame(relationship, register.getRelationship("rel"));

        assertEquals("user", user.getProperties().get("structurizr.dsl.identifier"));
        assertEquals("softwaresystem", softwareSystem.getProperties().get("structurizr.dsl.identifier"));
        assertEquals("softwaresystem.container", container.getProperties().get("structurizr.dsl.identifier"));
        assertEquals("rel", relationship.getProperties().get("structurizr.dsl.identifier"));
        assertNull(impliedRelationship.getProperties().get("structurizr.dsl.identifier"));
    }

    @Test
    void test_relationshipWithoutIdentifier() throws Exception {
        StructurizrDslParser parser = new StructurizrDslParser();
        parser.parse(new File("src/test/resources/dsl/relationship-without-identifier.dsl"));

        Workspace workspace = parser.getWorkspace();
        IdentifiersRegister register = parser.getIdentifiersRegister();
        assertEquals(1, workspace.getModel().getRelationships().size());
        Relationship relationship = workspace.getModel().getRelationships().iterator().next();

        assertTrue(register.findIdentifier(relationship).matches("[\\w]{8}-[\\w]{4}-[\\w]{4}-[\\w]{4}-[\\w]{12}"));
        assertNull(relationship.getProperties().get("structurizr.dsl.identifier")); // identifier is not included in model
    }

    @Test
    void test_imageViews_ViaFiles() throws Exception {
        StructurizrDslParser parser = new StructurizrDslParser();
        parser.parse(new File("src/test/resources/dsl/image-views/workspace-via-file.dsl"));

        Workspace workspace = parser.getWorkspace();
        assertEquals(5, workspace.getViews().getImageViews().size());

        ImageView plantumlView = (ImageView)workspace.getViews().getViewWithKey("plantuml");
        assertEquals("diagram.puml", plantumlView.getTitle());
        assertEquals("http://localhost:7777/svg/SoWkIImgAStDuNBAJrBGjLDmpCbCJbMmKiX8pSd9vt98pKi1IW80", plantumlView.getContent());
        assertEquals("image/svg+xml", plantumlView.getContentType());

        ImageView mermaidView = (ImageView)workspace.getViews().getViewWithKey("mermaid");
        assertEquals("diagram.mmd", mermaidView.getTitle());
        assertEquals("http://localhost:8888/svg/Zmxvd2NoYXJ0IFRECiAgICBTdGFydCAtLT4gU3RvcA==", mermaidView.getContent());
        assertEquals("image/svg+xml", mermaidView.getContentType());

        ImageView krokiView = (ImageView)workspace.getViews().getViewWithKey("kroki");
        assertEquals("diagram.dot", krokiView.getTitle());
        assertEquals("http://localhost:9999/graphviz/png/eNpLyUwvSizIUHBXqPZIzcnJ17ULzy_KSanlAgB1EAjQ", krokiView.getContent());
        assertEquals("image/png", krokiView.getContentType());

        ImageView pngView = (ImageView)workspace.getViews().getViewWithKey("png");
        assertEquals("image.png", pngView.getTitle());
        assertEquals("data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAAHAAAAB3CAIAAABUh1OkAAAIp0lEQVR4Xu2dW0wUVxzGQVGx4WIFLXhBEDSoraJUS1M1Vn3ok5f4YDRFYlpIMBAv1QJpBMFFlovEeEGkVjFFwyJKvQSbUqiuFySUCrRgQUVBCViKsEbjLcb+5dTx9JwddHf+M0PJ+fI9zM45Z+eb354butlxeCGEKgf2hJAyCaDIEkCRJYAiiwV6716XwZCakJC8ebNBS8fHG7ZsST53zszkYaRXPN5ygVmgyclpdXUtLS3duvjw4UKTycREoqVvPN58YBYogOebaebbt7tTUlKYSLT0jcebD8wChc7MN9PSGRkZTCRausfjzQQWQJVaAEW2AIpsARTZAiiytQZqMp12cHCorr7BF1m1GkDpDLbmeaPtBEpyEA0Z4jxr1sdnzpj5arxtvQElQIuLz8G1goM/Ys7TGW7c+KuqqqG5uYtvbp8VAS0tvQxpSkrK581bOGaMD1+Nt5ZAQ0O/mDZtxoABA8rKKujztmawyYqASplyc03w8tq1dvKyqenv6OiN3t6jnJwGjR8fkJa2i2l45MgPwcGzBg8eMnr0mKSkNP79JdsNtLGx3dXVLTe3YPbseRERUXSR3JC/deteXFziuHF+EHvkSK8NG+KkJomJRrgROO/l5b127dc3b3byVyRGAArRly//fPLk96XSVau+9PDwPHAgv7z8j9TUnQAuI2MP3dDXd7zVUt52A92xI3vUqNHAaO/eXAjT1NQhFckBjYxc6+4+bPfu7y5frjt9+pfMzL2k/vr1sf7+Ew4dOlpRUQ9dAcZiVNRX/BWJFQF9p0eOjo4eHiOOHi0mRXV1LfBJwv1IlSEoBKIbcqUT+UsQ2w00JOQTANHycrh0QLzs7ENSkVWg9fV34KPdvj2LeZ+GhjZnZ+eiop+kM7t27R827F2mmmRFQE+dKjObfzt79lcY8hMmBEJ3g6ITJ36GIuh9UmUYd3Dm+vW7UkO5Ut72AYVUAwcOlK4CnxnM8lKpVaAnT5YywYjhHqWuQwSLMJy5erWVqUmsCCg9r0MXcHFxhcnlbYDCmLJayts+oJGR6+A9B77SgB5JF7UJKLmdY8d+hA+JNkwm/HVbuMD2A92373sY+8Clvv62tSH/76B+NeT3UaXrcIc8LIkjRrwXG5sA2w/JU6Z8IC0yVoHKDXnoidAld+78lr+QVSsCSrZNlZV/wgQaGDh57tz5pDQsLBymrYMH86FTwBLPL0p+fv4wS0BpevpuiEtvAxjbAXT//iNOTk41NU30ybi4LbCYkP2mVaAtPR88TI579hzgFyU4D12kvPx36JtQgd4AMFYElAg6pqfnyBUrVtXW3iSl0EdgHYQdBtk2kbmVbpiXd3z69A8BNCzEsCPh31+yHUAXLvxszpxPmZMXLlST67bIA4VRHBMTD9xhloDwGzd+IzU3GndMmjRl0KDBMIcGBQXTd8TYTqCa2Q6g+loARbYAimwBFNkCKLIFUGQLoMju60DT09OZSLR0j8ebCcwCjY83IP5rtq2uqWnMyclhItHSNx5vPjALtLT0bG5uAd9SA1dVNcbExD558oSJREvHeLytBmaBggoLC43GNO2dnZ399OlTNg0nveLxthrYClAlunjxInuqT0q9nMhAe1+j+47UyymAIksARZYAiixkoOpN9rhSLycyUCEBFFkCKLIEUGQhA1VvsseVejmRgaq3HcGVejkFUGQJoMgSQJGFDFS9yR5X6uVEBiqnyspKBweHR48esQUykurb2lB3CaDIEkCRpSnQwsLCwMDAoUOHzpgxo7a2lhTdv39/9erVw4cPd3d3DwsLe/jwoVSfAXr37t2lS5e6urp6eHhERUX1/t95egkZqNxkT7isXLmyq6vr8ePHoaGhISEhpGjZsmVLliyxWCxAdsGCBdHR0VJ9BiiUAtAHDx60t7dPnTo1JiaGvoRNksupXMhA5bYjhEtzczN5aTabnZyc4KCjo8PR0bGhoYGcLy4u9vT0lOrTQNva2uCgvr6e1MzPz/fy8iLHdkgup3JpClSaCsnLZ8+eVVe//J6x+yu5ubk5Ozs/f/6cB3rlyhU4gO5J3qG8vBw+idcXsFFyOZVLZ6AweOGgtbX1v9Xf3ENNJpPooVaAwvGiRYtgbu3s7IRjoFZSUkLXpxvOnz8fJlxYtWB1CgoK2rRp0+sL2Ci5nMqFDFRusu8FKCxH4eHhMHW6uLhMnDgxMzOTrk83BNyLFy+GVR62BGvWrIHFjbqCbZLLqVzIQPUVTCB5eXmwkWALNFS/Agq6dOkSbHVhW1ZRUcGWaaL+BvRFD1MfHx9vb2+YZ7OysjTusP0Q6Isepv7+/rANGDt2LMDVssMiA1VvsrdVElMiPz8/usOqlxMZqHQDfVYBAQFlZWX/m22TekFtFfRQ6JUEIgx82JDNnDkzJyeH9FD1cvZPoBJN4Ojr6xsREcHMoerl7IdAgSZ0SVjl6S7JSL2cyEDVm+zfUmQfyndJRurlRAaqr8RfSv1QAiiyBFBkIQNVb7LHlXo5kYGqtx3BlXo5BVBkCaDIEkCRhQxUvckeV+rlRAYqJIAiiwVqsVjS0rYnJ6du3ZqipQ0G47ZtxvPnzzN5GOkVj7dcYBZoamqGjs/XKSg43vvjf/SNx5sPzAJNSEjmm2nm1lZL74//0Tcebz4wC1T33/HpfUOjezzeTGABVKkFUGQLoMgWQJEtgCJba6D877X3bjWAyv2oNYrtBEr/Srh4/A9tRUDF4394KwIqZRKP/5GMAFQ8/oe2IqDi8T+8FQEVj//hrQgoPa+Lx/8QowEVj/8hVgRUPP6HtyKgROLxP7TtBKqZ7QCqrwVQZAugyBZAkS2AIlsARbYAimwBFNlvABofr+dXM+7csfQOVN94vPnALNCUlHQdvzyUn3+sqKiIiURL33i8+cAs0O5ui9GYnpSUkpi4TWMbDClmM/tlNkY6xuNtNTALVEihBFBkCaDIEkCR9Q9wwQ4NbycOmAAAAABJRU5ErkJggg==", pngView.getContent());
        assertEquals("image/png", pngView.getContentType());

        ImageView svgView = (ImageView)workspace.getViews().getViewWithKey("svg");
        assertEquals("image.svg", svgView.getTitle());
        assertEquals("data:image/svg+xml;base64,PD94bWwgdmVyc2lvbj0iMS4wIiBlbmNvZGluZz0idXMtYXNjaWkiIHN0YW5kYWxvbmU9Im5vIj8+PHN2ZyB4bWxucz0iaHR0cDovL3d3dy53My5vcmcvMjAwMC9zdmciIHhtbG5zOnhsaW5rPSJodHRwOi8vd3d3LnczLm9yZy8xOTk5L3hsaW5rIiBjb250ZW50U3R5bGVUeXBlPSJ0ZXh0L2NzcyIgaGVpZ2h0PSIxMjBweCIgcHJlc2VydmVBc3BlY3RSYXRpbz0ibm9uZSIgc3R5bGU9IndpZHRoOjExM3B4O2hlaWdodDoxMjBweDtiYWNrZ3JvdW5kOiNGRkZGRkY7IiB2ZXJzaW9uPSIxLjEiIHZpZXdCb3g9IjAgMCAxMTMgMTIwIiB3aWR0aD0iMTEzcHgiIHpvb21BbmRQYW49Im1hZ25pZnkiPjxkZWZzLz48Zz48bGluZSBzdHlsZT0ic3Ryb2tlOiMxODE4MTg7c3Ryb2tlLXdpZHRoOjAuNTtzdHJva2UtZGFzaGFycmF5OjUuMCw1LjA7IiB4MT0iMjYiIHgyPSIyNiIgeTE9IjM2LjI5NjkiIHkyPSI4NS40Mjk3Ii8+PGxpbmUgc3R5bGU9InN0cm9rZTojMTgxODE4O3N0cm9rZS13aWR0aDowLjU7c3Ryb2tlLWRhc2hhcnJheTo1LjAsNS4wOyIgeDE9IjgyIiB4Mj0iODIiIHkxPSIzNi4yOTY5IiB5Mj0iODUuNDI5NyIvPjxyZWN0IGZpbGw9IiNFMkUyRjAiIGhlaWdodD0iMzAuMjk2OSIgcng9IjIuNSIgcnk9IjIuNSIgc3R5bGU9InN0cm9rZTojMTgxODE4O3N0cm9rZS13aWR0aDowLjU7IiB3aWR0aD0iNDMiIHg9IjUiIHk9IjUiLz48dGV4dCBmaWxsPSIjMDAwMDAwIiBmb250LWZhbWlseT0ic2Fucy1zZXJpZiIgZm9udC1zaXplPSIxNCIgbGVuZ3RoQWRqdXN0PSJzcGFjaW5nIiB0ZXh0TGVuZ3RoPSIyOSIgeD0iMTIiIHk9IjI0Ljk5NTEiPkJvYjwvdGV4dD48cmVjdCBmaWxsPSIjRTJFMkYwIiBoZWlnaHQ9IjMwLjI5NjkiIHJ4PSIyLjUiIHJ5PSIyLjUiIHN0eWxlPSJzdHJva2U6IzE4MTgxODtzdHJva2Utd2lkdGg6MC41OyIgd2lkdGg9IjQzIiB4PSI1IiB5PSI4NC40Mjk3Ii8+PHRleHQgZmlsbD0iIzAwMDAwMCIgZm9udC1mYW1pbHk9InNhbnMtc2VyaWYiIGZvbnQtc2l6ZT0iMTQiIGxlbmd0aEFkanVzdD0ic3BhY2luZyIgdGV4dExlbmd0aD0iMjkiIHg9IjEyIiB5PSIxMDQuNDI0OCI+Qm9iPC90ZXh0PjxyZWN0IGZpbGw9IiNFMkUyRjAiIGhlaWdodD0iMzAuMjk2OSIgcng9IjIuNSIgcnk9IjIuNSIgc3R5bGU9InN0cm9rZTojMTgxODE4O3N0cm9rZS13aWR0aDowLjU7IiB3aWR0aD0iNDkiIHg9IjU4IiB5PSI1Ii8+PHRleHQgZmlsbD0iIzAwMDAwMCIgZm9udC1mYW1pbHk9InNhbnMtc2VyaWYiIGZvbnQtc2l6ZT0iMTQiIGxlbmd0aEFkanVzdD0ic3BhY2luZyIgdGV4dExlbmd0aD0iMzUiIHg9IjY1IiB5PSIyNC45OTUxIj5BbGljZTwvdGV4dD48cmVjdCBmaWxsPSIjRTJFMkYwIiBoZWlnaHQ9IjMwLjI5NjkiIHJ4PSIyLjUiIHJ5PSIyLjUiIHN0eWxlPSJzdHJva2U6IzE4MTgxODtzdHJva2Utd2lkdGg6MC41OyIgd2lkdGg9IjQ5IiB4PSI1OCIgeT0iODQuNDI5NyIvPjx0ZXh0IGZpbGw9IiMwMDAwMDAiIGZvbnQtZmFtaWx5PSJzYW5zLXNlcmlmIiBmb250LXNpemU9IjE0IiBsZW5ndGhBZGp1c3Q9InNwYWNpbmciIHRleHRMZW5ndGg9IjM1IiB4PSI2NSIgeT0iMTA0LjQyNDgiPkFsaWNlPC90ZXh0Pjxwb2x5Z29uIGZpbGw9IiMxODE4MTgiIHBvaW50cz0iNzAuNSw2My40Mjk3LDgwLjUsNjcuNDI5Nyw3MC41LDcxLjQyOTcsNzQuNSw2Ny40Mjk3IiBzdHlsZT0ic3Ryb2tlOiMxODE4MTg7c3Ryb2tlLXdpZHRoOjEuMDsiLz48bGluZSBzdHlsZT0ic3Ryb2tlOiMxODE4MTg7c3Ryb2tlLXdpZHRoOjEuMDsiIHgxPSIyNi41IiB4Mj0iNzYuNSIgeTE9IjY3LjQyOTciIHkyPSI2Ny40Mjk3Ii8+PHRleHQgZmlsbD0iIzAwMDAwMCIgZm9udC1mYW1pbHk9InNhbnMtc2VyaWYiIGZvbnQtc2l6ZT0iMTMiIGxlbmd0aEFkanVzdD0ic3BhY2luZyIgdGV4dExlbmd0aD0iMzAiIHg9IjMzLjUiIHk9IjYyLjM2MzgiPmhlbGxvPC90ZXh0PjwhLS1TUkM9W1N5ZkZLajJyS3QzQ29LbkVMUjFJbzRaRG9TYTcwMDAwXS0tPjwvZz48L3N2Zz4=", svgView.getContent());
        assertEquals("image/svg+xml", svgView.getContentType());

        // check that source isn't retained
        assertEquals("", DslUtils.getDsl(workspace));
    }

    @Test
    @Tag("IntegrationTest")
    void test_imageViews_ViaUrls() throws Exception {
        StructurizrDslParser parser = new StructurizrDslParser();
        parser.parse(new File("src/test/resources/dsl/image-views/workspace-via-url.dsl"));

        Workspace workspace = parser.getWorkspace();
        assertEquals(5, workspace.getViews().getImageViews().size());

        ImageView plantumlView = (ImageView)workspace.getViews().getViewWithKey("plantuml");
        assertEquals("diagram.puml", plantumlView.getTitle());
        assertEquals("http://localhost:7777/svg/SoWkIImgAStDuNBAJrBGjLDmpCbCJbMmKiX8pSd9vt98pKi1IW80", plantumlView.getContent());
        assertEquals("image/svg+xml", plantumlView.getContentType());

        ImageView mermaidView = (ImageView)workspace.getViews().getViewWithKey("mermaid");
        assertEquals("diagram.mmd", mermaidView.getTitle());
        assertEquals("http://localhost:8888/svg/Zmxvd2NoYXJ0IFRECiAgICBTdGFydCAtLT4gU3RvcA==", mermaidView.getContent());
        assertEquals("image/svg+xml", mermaidView.getContentType());

        ImageView krokiView = (ImageView)workspace.getViews().getViewWithKey("kroki");
        assertEquals("diagram.dot", krokiView.getTitle());
        assertEquals("http://localhost:9999/graphviz/svg/eNpLyUwvSizIUHBXqPZIzcnJ17ULzy_KSanlAgB1EAjQ", krokiView.getContent());
        assertEquals("image/svg+xml", krokiView.getContentType());

        ImageView pngView = (ImageView)workspace.getViews().getViewWithKey("png");
        assertEquals("image.png", pngView.getTitle());
        assertEquals("https://raw.githubusercontent.com/structurizr/java/master/structurizr-dsl/src/test/resources/dsl/image-views/image.png", pngView.getContent());
        assertEquals("image/png", pngView.getContentType());

        ImageView svgView = (ImageView)workspace.getViews().getViewWithKey("svg");
        assertEquals("image.svg", svgView.getTitle());
        assertEquals("https://raw.githubusercontent.com/structurizr/java/master/structurizr-dsl/src/test/resources/dsl/image-views/image.svg", svgView.getContent());
        assertEquals("image/svg+xml", svgView.getContentType());

        // check that source is retained
        assertEquals("""
                workspace {
                
                    views {
                        properties {
                            "plantuml.url" "http://localhost:7777"
                            "plantuml.format" "svg"
                            "mermaid.url" "http://localhost:8888"
                            "mermaid.format" "svg"
                            "mermaid.compress" "false"
                            "kroki.url" "http://localhost:9999"
                            "kroki.format" "svg"
                        }
                
                        image * "plantuml" {
                            plantuml https://raw.githubusercontent.com/structurizr/java/master/structurizr-dsl/src/test/resources/dsl/image-views/diagram.puml
                        }
                
                        image * "mermaid" {
                            mermaid https://raw.githubusercontent.com/structurizr/java/master/structurizr-dsl/src/test/resources/dsl/image-views/diagram.mmd
                        }
                
                        image * "kroki" {
                            kroki graphviz https://raw.githubusercontent.com/structurizr/java/master/structurizr-dsl/src/test/resources/dsl/image-views/diagram.dot
                        }
                
                        image * "png" {
                            image https://raw.githubusercontent.com/structurizr/java/master/structurizr-dsl/src/test/resources/dsl/image-views/image.png
                        }
                
                        image * "svg" {
                            image https://raw.githubusercontent.com/structurizr/java/master/structurizr-dsl/src/test/resources/dsl/image-views/image.svg
                        }
                    }
                
                }""", DslUtils.getDsl(workspace));
    }

    @Test
    void test_imageViews_ViaSource() throws Exception {
        StructurizrDslParser parser = new StructurizrDslParser();
        parser.parse(new File("src/test/resources/dsl/image-views/workspace-via-source.dsl"));

        Workspace workspace = parser.getWorkspace();
        assertEquals(3, workspace.getViews().getImageViews().size());

        ImageView plantumlView = (ImageView)workspace.getViews().getViewWithKey("plantuml");
        assertNull(plantumlView.getTitle());
        assertEquals("http://localhost:7777/svg/SoWkIImgAStDuNBAJrBGjLDmpCbCJbMmKiX8pSd9vt98pKi1IW80", plantumlView.getContent());
        assertEquals("image/svg+xml", plantumlView.getContentType());

        ImageView mermaidView = (ImageView)workspace.getViews().getViewWithKey("mermaid");
        assertNull(mermaidView.getTitle());
        assertEquals("http://localhost:8888/svg/Zmxvd2NoYXJ0IFRECiAgICBTdGFydCAtLT4gU3RvcA==", mermaidView.getContent());
        assertEquals("image/svg+xml", mermaidView.getContentType());

        ImageView krokiView = (ImageView)workspace.getViews().getViewWithKey("kroki");
        assertNull(krokiView.getTitle());
        assertEquals("http://localhost:9999/graphviz/png/eNpLyUwvSizIUHBXqPZIzcnJ17ULzy_KSanlAgB1EAjQ", krokiView.getContent());
        assertEquals("image/png", krokiView.getContentType());
    }

    @Test
    void test_EmptyDeploymentEnvironment() throws Exception {
        StructurizrDslParser parser = new StructurizrDslParser();
        parser.parse(new File("src/test/resources/dsl/deployment-environment-empty.dsl"));

        assertEquals(1, parser.getWorkspace().getModel().getDeploymentNodes().size());
    }

    @Test
    void test_MultiLineSupport() throws Exception {
        StructurizrDslParser parser = new StructurizrDslParser();
        parser.parse(new File("src/test/resources/dsl/multi-line.dsl"));

        assertNotNull(parser.getWorkspace().getModel().getSoftwareSystemWithName("Software System"));
    }

    @Test
    void test_MultiLineWithError() {
        File dslFile = new File("src/test/resources/dsl/multi-line-with-error.dsl");

        try {
            StructurizrDslParser parser = new StructurizrDslParser();
            parser.parse(dslFile);
            fail();
        } catch (StructurizrDslParserException e) {
            // check that the error message includes the original line number
            assertEquals("Unexpected tokens (expected: !docs, !decisions, group, container, description, tags, url, properties, perspectives, ->) at line 8 of " + dslFile.getAbsolutePath() + ": component \"Component\" // components not permitted inside software systems", e.getMessage());
        }
    }

    @Test
    void test_RelationshipAlreadyExists() {
        File dslFile = new File("src/test/resources/dsl/relationship-already-exists.dsl");

        try {
            StructurizrDslParser parser = new StructurizrDslParser();
            parser.parse(dslFile);
            fail();
        } catch (StructurizrDslParserException e) {
            assertEquals("A relationship between \"SoftwareSystem://B\" and \"SoftwareSystem://A\" already exists at line 10 of " + dslFile.getAbsolutePath() + ": b -> a", e.getMessage());
        }
    }

    @Test
    void test_ExcludeImpliedRelationship() throws Exception {
        StructurizrDslParser parser = new StructurizrDslParser();
        parser.parse(new File("src/test/resources/dsl/exclude-implied-relationship.dsl"));

        // check the system landscape view doesn't include any relationships
        assertEquals(0, parser.getWorkspace().getViews().getSystemLandscapeViews().iterator().next().getRelationships().size());
    }

    @Test
    void test_IncludeImpliedRelationship() throws Exception {
        StructurizrDslParser parser = new StructurizrDslParser();
        parser.parse(new File("src/test/resources/dsl/include-implied-relationship.dsl"));

        // check the system landscape view includes a relationship
        assertEquals(1, parser.getWorkspace().getViews().getSystemLandscapeViews().iterator().next().getRelationships().size());
    }

    @Test
    void test_GroupWithoutBrace() throws Exception {
        File dslFile = new File("src/test/resources/dsl/group-without-brace.dsl");

        try {
            StructurizrDslParser parser = new StructurizrDslParser();
            parser.parse(dslFile);
            fail();
        } catch (StructurizrDslParserException e) {
            assertEquals("Expected: group <name> { at line 4 of " + dslFile.getAbsolutePath() + ": group \"Name\"", e.getMessage());
        }
    }

    @Test
    void test_ISO8859Encoding() throws Exception {
        StructurizrDslParser parser = new StructurizrDslParser();
        parser.setCharacterEncoding(StandardCharsets.ISO_8859_1);
        parser.parse(new File("src/test/resources/dsl/iso-8859.dsl"));
        assertNotNull(parser.getWorkspace().getModel().getSoftwareSystemWithName("Namé"));
    }

    @Test
    void test_ScriptInDynamicView() throws Exception {
        File dslFile = new File("src/test/resources/dsl/script-in-dynamic-view.dsl");

        StructurizrDslParser parser = new StructurizrDslParser();
        parser.parse(dslFile);
    }

    @Test
    void test_Enterprise() {
        File dslFile = new File("src/test/resources/dsl/enterprise.dsl");

        try {
            StructurizrDslParser parser = new StructurizrDslParser();
            parser.parse(dslFile);
            fail();
        } catch (StructurizrDslParserException e) {
            assertEquals("The enterprise keyword was previously deprecated, and has now been removed - please use group instead (https://docs.structurizr.com/dsl/language#group) at line 4 of " + dslFile.getAbsolutePath() + ": enterprise \"Name\" {", e.getMessage());
        }
    }

    @Test
    void test_Constant() {
        File dslFile = new File("src/test/resources/dsl/constant.dsl");

        try {
            StructurizrDslParser parser = new StructurizrDslParser();
            parser.parse(dslFile);
            fail();
        } catch (StructurizrDslParserException e) {
            assertEquals("!constant was previously deprecated, and has now been removed - please use !const or !var instead at line 3 of " + dslFile.getAbsolutePath() + ": !constant NAME VALUE", e.getMessage());
        }
    }

    @Test
    void test_ConstantsAndVariablesFromWorkspaceExtension() throws Exception {
        File dslFile = new File("src/test/resources/dsl/constants-and-variables-from-workspace-extension-child.dsl");

        StructurizrDslParser parser = new StructurizrDslParser();
        parser.parse(dslFile);

        SoftwareSystem softwareSystem = parser.getWorkspace().getModel().getSoftwareSystemWithName("Name");
        assertNotNull(softwareSystem);
        assertEquals("Description", softwareSystem.getDescription());
    }

    @Test
    void test_UnbalancedCurlyBraces() {
        try {
            StructurizrDslParser parser = new StructurizrDslParser();
            parser.parse("""
                    workspace {
                        model {
                            person "User"
                        }
                    """);
            fail();
        } catch (StructurizrDslParserException e) {
            assertEquals("Unexpected end of DSL content - are one or more closing curly braces missing?", e.getMessage());
        }
    }

    @Test
    void test_Const() {
        try {
            StructurizrDslParser parser = new StructurizrDslParser();
            parser.parse("""
                    workspace {
                        !const name value1
                        !const name value2
                    }
                    """);
            fail();
        } catch (StructurizrDslParserException e) {
            assertEquals("A constant/variable \"name\" already exists at line 3: !const name value2", e.getMessage());
        }
    }

    @Test
    void test_Var_CannotOverrideConst() {
        try {
            StructurizrDslParser parser = new StructurizrDslParser();
            parser.parse("""
                    workspace {
                        !const name value1
                        !var name value2
                    }
                    """);
            fail();
        } catch (StructurizrDslParserException e) {
            assertEquals("A constant \"name\" already exists at line 3: !var name value2", e.getMessage());
        }
    }

    @Test
    void springPetClinic() throws Exception {
        String springPetClinicHome = System.getenv().getOrDefault("SPRING_PETCLINIC_HOME", "");
        System.out.println(springPetClinicHome);
        if (!StringUtils.isNullOrEmpty(springPetClinicHome)) {
            System.out.println("Running Spring PetClinic example...");

            try {
                File workspaceFile = new File("src/test/resources/dsl/spring-petclinic/workspace.dsl");
                StructurizrDslParser parser = new StructurizrDslParser();
                parser.setRestricted(true);
                parser.parse(workspaceFile);
                fail();
            } catch (Exception e) {
                System.out.println(e.getMessage());
                assertTrue(e.getMessage().startsWith("!components is not available when the parser is running in restricted mode"));
            }

            File workspaceFile = new File("src/test/resources/dsl/spring-petclinic/workspace.dsl");
            StructurizrDslParser parser = new StructurizrDslParser();
            parser.parse(workspaceFile);

            Person clinicEmployee = (Person)parser.getIdentifiersRegister().getElement("clinicEmployee");

            Container webApplication = (Container)parser.getIdentifiersRegister().getElement("springPetClinic.webApplication");
            Container relationalDatabaseSchema = (Container)parser.getIdentifiersRegister().getElement("springPetClinic.relationalDatabaseSchema");

            assertEquals(7, webApplication.getComponents().size());

            Component welcomeController = webApplication.getComponentWithName("Welcome Controller");
            assertNotNull(welcomeController);
            assertEquals("org.springframework.samples.petclinic.system.WelcomeController", welcomeController.getProperties().get("component.type"));
            assertEquals("org/springframework/samples/petclinic/system/WelcomeController.java", welcomeController.getProperties().get("component.src"));
            assertEquals("https://github.com/spring-projects/spring-petclinic/blob/main/src/main/java/org/springframework/samples/petclinic/system/WelcomeController.java", welcomeController.getUrl());
            assertSame(welcomeController, parser.getIdentifiersRegister().getElement("springPetClinic.webApplication.welcomecontroller"));
            assertEquals("Web Controllers", welcomeController.getGroup());
            assertTrue(clinicEmployee.hasEfferentRelationshipWith(welcomeController));

            Component ownerController = webApplication.getComponentWithName("Owner Controller");
            assertNotNull(ownerController);
            assertEquals("org.springframework.samples.petclinic.owner.OwnerController", ownerController.getProperties().get("component.type"));
            assertEquals("org/springframework/samples/petclinic/owner/OwnerController.java", ownerController.getProperties().get("component.src"));
            assertEquals("https://github.com/spring-projects/spring-petclinic/blob/main/src/main/java/org/springframework/samples/petclinic/owner/OwnerController.java", ownerController.getUrl());
            assertSame(ownerController, parser.getIdentifiersRegister().getElement("springPetClinic.webApplication.ownerController"));
            assertEquals("Web Controllers", ownerController.getGroup());
            assertTrue(clinicEmployee.hasEfferentRelationshipWith(ownerController));

            Component petController = webApplication.getComponentWithName("Pet Controller");
            assertNotNull(petController);
            assertEquals("org.springframework.samples.petclinic.owner.PetController", petController.getProperties().get("component.type"));
            assertEquals("org/springframework/samples/petclinic/owner/PetController.java", petController.getProperties().get("component.src"));
            assertEquals("https://github.com/spring-projects/spring-petclinic/blob/main/src/main/java/org/springframework/samples/petclinic/owner/PetController.java", petController.getUrl());
            assertSame(petController, parser.getIdentifiersRegister().getElement("springPetClinic.webApplication.petcontroller"));
            assertEquals("Web Controllers", petController.getGroup());
            assertTrue(clinicEmployee.hasEfferentRelationshipWith(petController));

            Component vetController = webApplication.getComponentWithName("Vet Controller");
            assertNotNull(vetController);
            assertEquals("org.springframework.samples.petclinic.vet.VetController", vetController.getProperties().get("component.type"));
            assertEquals("org/springframework/samples/petclinic/vet/VetController.java", vetController.getProperties().get("component.src"));
            assertEquals("https://github.com/spring-projects/spring-petclinic/blob/main/src/main/java/org/springframework/samples/petclinic/vet/VetController.java", vetController.getUrl());
            assertSame(vetController, parser.getIdentifiersRegister().getElement("springPetClinic.webApplication.vetcontroller"));
            assertEquals("Web Controllers", vetController.getGroup());
            assertTrue(clinicEmployee.hasEfferentRelationshipWith(vetController));

            Component visitController = webApplication.getComponentWithName("Visit Controller");
            assertNotNull(visitController);
            assertEquals("org.springframework.samples.petclinic.owner.VisitController", visitController.getProperties().get("component.type"));
            assertEquals("org/springframework/samples/petclinic/owner/VisitController.java", visitController.getProperties().get("component.src"));
            assertEquals("https://github.com/spring-projects/spring-petclinic/blob/main/src/main/java/org/springframework/samples/petclinic/owner/VisitController.java", visitController.getUrl());
            assertSame(visitController, parser.getIdentifiersRegister().getElement("springPetClinic.webApplication.visitcontroller"));
            assertEquals("Web Controllers", visitController.getGroup());
            assertTrue(clinicEmployee.hasEfferentRelationshipWith(visitController));

            Component ownerRepository = webApplication.getComponentWithName("Owner Repository");
            assertNotNull(ownerRepository);
            assertEquals("Repository class for Owner domain objects All method names are compliant with Spring Data naming conventions so this interface can easily be extended for Spring Data.", ownerRepository.getDescription());
            assertEquals("org.springframework.samples.petclinic.owner.OwnerRepository", ownerRepository.getProperties().get("component.type"));
            assertEquals("org/springframework/samples/petclinic/owner/OwnerRepository.java", ownerRepository.getProperties().get("component.src"));
            assertEquals("https://github.com/spring-projects/spring-petclinic/blob/main/src/main/java/org/springframework/samples/petclinic/owner/OwnerRepository.java", ownerRepository.getUrl());
            assertSame(ownerRepository, parser.getIdentifiersRegister().getElement("springPetClinic.webApplication.ownerrepository"));
            assertEquals("Data Repositories", ownerRepository.getGroup());
            assertTrue(ownerRepository.hasEfferentRelationshipWith(relationalDatabaseSchema, "Reads from and writes to"));

            Component vetRepository = webApplication.getComponentWithName("Vet Repository");
            assertNotNull(vetRepository);
            assertEquals("Repository class for Vet domain objects All method names are compliant with Spring Data naming conventions so this interface can easily be extended for Spring Data.", vetRepository.getDescription());
            assertEquals("org.springframework.samples.petclinic.vet.VetRepository", vetRepository.getProperties().get("component.type"));
            assertEquals("org/springframework/samples/petclinic/vet/VetRepository.java", vetRepository.getProperties().get("component.src"));
            assertEquals("https://github.com/spring-projects/spring-petclinic/blob/main/src/main/java/org/springframework/samples/petclinic/vet/VetRepository.java", vetRepository.getUrl());
            assertSame(vetRepository, parser.getIdentifiersRegister().getElement("springPetClinic.webApplication.vetrepository"));
            assertEquals("Data Repositories", vetRepository.getGroup());
            assertTrue(vetRepository.hasEfferentRelationshipWith(relationalDatabaseSchema, "Reads from and writes to"));

            assertTrue(welcomeController.getRelationships().isEmpty());
            assertNotNull(petController.getEfferentRelationshipWith(ownerRepository));
            assertNotNull(visitController.getEfferentRelationshipWith(ownerRepository));
            assertNotNull(ownerController.getEfferentRelationshipWith(ownerRepository));
            assertNotNull(vetController.getEfferentRelationshipWith(vetRepository));

            // checks that source isn't retained
            assertEquals("", DslUtils.getDsl(workspace));

        } else {
            System.out.println("Skipping Spring PetClinic example...");
        }
    }

    @Test
    void test_bulkOperations() throws Exception {
        File dslFile = new File("src/test/resources/dsl/bulk-operations.dsl");

        StructurizrDslParser parser = new StructurizrDslParser();
        parser.parse(dslFile);
    }

    @Test
    void test_ImageView_WhenParserIsInRestrictedMode() {
        File dslFile = new File("src/test/resources/dsl/image-view.dsl");

        try {
            StructurizrDslParser parser = new StructurizrDslParser();
            parser.setRestricted(true);
            parser.parse(dslFile);
            fail();
        } catch (StructurizrDslParserException e) {
            assertEquals("Images must be specified as a URL when running in restricted mode at line 5 of " + dslFile.getAbsolutePath() + ": image image.png", e.getMessage());
        }
    }

    @Test
    void test_sourceIsRetained() throws Exception {
        File parentDslFile = new File("src/test/resources/dsl/source-parent.dsl");
        StructurizrDslParser parser = new StructurizrDslParser();
        parser.parse(parentDslFile);
        Workspace workspace = parser.getWorkspace();
        assertEquals("""
workspace {

    model {
        a = softwareSystem "A"
    }

}""", DslUtils.getDsl(workspace));

        // source not retained because workspace extends a file-based resource
        File childDslFile = new File("src/test/resources/dsl/source-child.dsl");
        parser = new StructurizrDslParser();
        parser.parse(childDslFile);
        workspace = parser.getWorkspace();
        assertEquals("", DslUtils.getDsl(workspace));
    }

    @Test
    void test_sourceIsNotRetained() throws Exception {
        File parentDslFile = new File("src/test/resources/dsl/source-not-retained.dsl");
        StructurizrDslParser parser = new StructurizrDslParser();
        parser.parse(parentDslFile);
        Workspace workspace = parser.getWorkspace();
        assertNull(workspace.getProperties().get(DslUtils.STRUCTURIZR_DSL_PROPERTY_NAME));
    }

    @Test
    void test_archetypes() throws Exception {
        File parentDslFile = new File("src/test/resources/dsl/archetypes.dsl");
        StructurizrDslParser parser = new StructurizrDslParser();
        parser.parse(parentDslFile);
        Workspace workspace = parser.getWorkspace();

        Container customerApi = workspace.getModel().getSoftwareSystemWithName("X").getContainerWithName("Customer API");
        assertTrue(customerApi.getTagsAsSet().contains("Application"));
        assertTrue(customerApi.getTagsAsSet().contains("Spring Boot"));
        assertEquals("Spring Boot", customerApi.getTechnology());

        Relationship relationship = workspace.getModel().getSoftwareSystemWithName("A").getEfferentRelationshipWith(workspace.getModel().getSoftwareSystemWithName("X"));
        assertEquals("HTTPS", relationship.getTechnology());
    }

    @Test
    void test_archetypesForCustomElements() throws Exception {
        File parentDslFile = new File("src/test/resources/dsl/archetypes-for-custom-elements.dsl");
        StructurizrDslParser parser = new StructurizrDslParser();
        parser.parse(parentDslFile);
        Workspace workspace = parser.getWorkspace();

        CustomElement b = workspace.getModel().getCustomElementWithName("B");
        assertEquals("Hardware System", b.getMetadata());
        assertTrue(b.getTagsAsSet().contains("Hardware System"));
    }

    @Test
    void test_archetypesForDefaults() throws Exception {
        File parentDslFile = new File("src/test/resources/dsl/archetypes-for-defaults.dsl");
        StructurizrDslParser parser = new StructurizrDslParser();
        parser.parse(parentDslFile);
        Workspace workspace = parser.getWorkspace();

        SoftwareSystem a = workspace.getModel().getSoftwareSystemWithName("A");
        assertEquals("Default Description", a.getDescription());
        assertTrue(a.hasTag("Default Tag"));
        assertTrue(a.hasProperty("Default Property Name", "Default Property Value"));
        assertEquals("Default Perspective Description", (a.getPerspectives().stream().filter(p -> p.getName().equals("Default Perspective Name")).findFirst().get().getDescription()));

        SoftwareSystem b = workspace.getModel().getSoftwareSystemWithName("B");
        assertEquals("Default Description", b.getDescription());
        assertTrue(b.hasTag("Default Tag"));
        assertTrue(b.hasProperty("Default Property Name", "Default Property Value"));
        assertEquals("Default Perspective Description", (b.getPerspectives().stream().filter(p -> p.getName().equals("Default Perspective Name")).findFirst().get().getDescription()));

        Relationship r = a.getEfferentRelationshipWith(b);
        assertEquals("Default Description", r.getDescription());
        assertEquals("Default Technology", r.getTechnology());
        assertTrue(r.hasTag("Default Tag"));
        assertTrue(r.hasProperty("Default Property Name", "Default Property Value"));
        assertEquals("Default Perspective Description", (r.getPerspectives().stream().filter(p -> p.getName().equals("Default Perspective Name")).findFirst().get().getDescription()));
    }

    @Test
    void test_archetypesFromWorkspaceExtension() throws Exception {
        File parentDslFile = new File("src/test/resources/dsl/archetypes-from-workspace-extension-child.dsl");
        StructurizrDslParser parser = new StructurizrDslParser();
        parser.parse(parentDslFile);
        Workspace workspace = parser.getWorkspace();

        SoftwareSystem a = workspace.getModel().getSoftwareSystemWithName("A");
        assertEquals("Default Description", a.getDescription());
        assertTrue(a.hasTag("Default Tag"));
        assertTrue(a.hasProperty("Default Property Name", "Default Property Value"));
        assertEquals("Default Perspective Description", (a.getPerspectives().stream().filter(p -> p.getName().equals("Default Perspective Name")).findFirst().get().getDescription()));

        SoftwareSystem b = workspace.getModel().getSoftwareSystemWithName("B");
        assertEquals("Default Description", b.getDescription());
        assertTrue(b.hasTag("Default Tag"));
        assertTrue(b.hasProperty("Default Property Name", "Default Property Value"));
        assertEquals("Default Perspective Description", (b.getPerspectives().stream().filter(p -> p.getName().equals("Default Perspective Name")).findFirst().get().getDescription()));

        Relationship r = a.getEfferentRelationshipWith(b);
        assertEquals("Default Description", r.getDescription());
        assertEquals("Default Technology", r.getTechnology());
        assertTrue(r.hasTag("Default Tag"));
        assertTrue(r.hasProperty("Default Property Name", "Default Property Value"));
        assertEquals("Default Perspective Description", (r.getPerspectives().stream().filter(p -> p.getName().equals("Default Perspective Name")).findFirst().get().getDescription()));
    }

    @Test
    void test_archetypesForExtension() throws Exception {
        File parentDslFile = new File("src/test/resources/dsl/archetypes-for-extension.dsl");
        StructurizrDslParser parser = new StructurizrDslParser();
        parser.parse(parentDslFile);
        Workspace workspace = parser.getWorkspace();

        SoftwareSystem a = workspace.getModel().getSoftwareSystemWithName("A");
        assertEquals("Description of A.", a.getDescription());
        assertTrue(a.hasTag("Default Tag"));

        SoftwareSystem b = workspace.getModel().getSoftwareSystemWithName("B");
        assertEquals("Description of B.", b.getDescription());
        assertTrue(b.hasTag("Default Tag"));
        assertTrue(b.hasTag("External Software System"));

        Relationship r = a.getEfferentRelationshipWith(b);
        assertEquals("Makes API calls to", r.getDescription());
        assertEquals("HTTPS", r.getTechnology());
        assertTrue(r.hasTag("Default Tag"));
        assertTrue(r.hasTag("Synchronous"));
        assertTrue(r.hasTag("HTTPS"));
    }

    @Test
    void test_archetypesForImplicitRelationships() throws Exception {
        File parentDslFile = new File("src/test/resources/dsl/archetypes-for-implicit-relationships.dsl");
        StructurizrDslParser parser = new StructurizrDslParser();
        parser.parse(parentDslFile);
        Workspace workspace = parser.getWorkspace();

        SoftwareSystem a = workspace.getModel().getSoftwareSystemWithName("A");
        SoftwareSystem b = workspace.getModel().getSoftwareSystemWithName("B");

        Relationship r = b.getEfferentRelationshipWith(a);
        assertEquals("Makes API calls to", r.getDescription());
        assertEquals("HTTPS", r.getTechnology());
        assertTrue(r.hasTag("HTTPS"));
    }

    @Test
    void test_textBlock() throws Exception {
        StructurizrDslParser parser = new StructurizrDslParser();
        parser.parse(new File("src/test/resources/dsl/text-block.dsl"));

        workspace = parser.getWorkspace();
        SoftwareSystem softwareSystem = workspace.getModel().getSoftwareSystemWithName("Name");
        assertEquals("""
            - Line 1
            - Line 2
            - Line 3""", softwareSystem.getDescription());

        assertEquals("""
                workspace {
                
                    model {
                        softwareSystem = softwareSystem "Name" {
                            description ""\"
                                - Line 1
                                - Line 2
                                - Line 3
                            ""\"
                        }
                    }
                
                }""", DslUtils.getDsl(workspace));
    }

    @Test
    void test_customViewAnimation() throws Exception {
        StructurizrDslParser parser = new StructurizrDslParser();
        parser.parse(new File("src/test/resources/dsl/custom-view-animation.dsl"));

        Workspace workspace = parser.getWorkspace();
        CustomElement a = workspace.getModel().getCustomElementWithName("A");
        CustomElement b = workspace.getModel().getCustomElementWithName("B");

        for (CustomView view : workspace.getViews().getCustomViews()) {
            assertEquals(2, view.getAnimations().size());

            // step 1
            assertTrue(view.getAnimations().get(0).getElements().contains(a.getId()));

            // step 2
            assertTrue(view.getAnimations().get(1).getElements().contains(b.getId()));
        }
    }

    @Test
    void test_staticViewAnimation() throws Exception {
        StructurizrDslParser parser = new StructurizrDslParser();
        parser.parse(new File("src/test/resources/dsl/static-view-animation.dsl"));

        Workspace workspace = parser.getWorkspace();
        SoftwareSystem a = workspace.getModel().getSoftwareSystemWithName("A");
        SoftwareSystem b = workspace.getModel().getSoftwareSystemWithName("B");

        for (SystemLandscapeView view : workspace.getViews().getSystemLandscapeViews()) {
            assertEquals(2, view.getAnimations().size());

            // step 1
            assertTrue(view.getAnimations().get(0).getElements().contains(a.getId()));

            // step 2
            assertTrue(view.getAnimations().get(1).getElements().contains(b.getId()));
        }
    }

    @Test
    void test_deploymentViewAnimation() throws Exception {
        StructurizrDslParser parser = new StructurizrDslParser();
        parser.parse(new File("src/test/resources/dsl/deployment-view-animation.dsl"));

        Workspace workspace = parser.getWorkspace();
        Container webapp = workspace.getModel().getSoftwareSystemWithName("Software System").getContainerWithName("Web Application");
        Container db = workspace.getModel().getSoftwareSystemWithName("Software System").getContainerWithName("Database Schema");
        ContainerInstance webappInstance = workspace.getModel().getDeploymentNodeWithName("Deployment Node", "Live").getContainerInstances().stream().filter(ci -> ci.getContainer().equals(webapp)).findFirst().get();
        ContainerInstance dbInstance = workspace.getModel().getDeploymentNodeWithName("Deployment Node", "Live").getContainerInstances().stream().filter(ci -> ci.getContainer().equals(db)).findFirst().get();

        for (DeploymentView deploymentView : workspace.getViews().getDeploymentViews()) {
            assertEquals(2, deploymentView.getAnimations().size());

            // step 1
            assertTrue(deploymentView.getAnimations().get(0).getElements().contains(webappInstance.getId()));

            // step 2
            assertTrue(deploymentView.getAnimations().get(1).getElements().contains(dbInstance.getId()));
        }
    }

    @Test
    void test_deploymentGroups_Flat() throws Exception {
        StructurizrDslParser parser = new StructurizrDslParser();
        parser.parse(new File("src/test/resources/dsl/deployment-groups-flat.dsl"));

        Workspace workspace = parser.getWorkspace();

        Container api = workspace.getModel().getSoftwareSystemWithName("Software System").getContainerWithName("API");
        Container db = workspace.getModel().getSoftwareSystemWithName("Software System").getContainerWithName("DB");

        DeploymentNode server1 = workspace.getModel().getDeploymentNodeWithName("Server 1", "WithoutDeploymentGroups");
        ContainerInstance apiInstance1 = server1.getContainerInstances().stream().filter(ci -> ci.getContainer().equals(api)).findFirst().get();
        ContainerInstance dbInstance1 = server1.getContainerInstances().stream().filter(ci -> ci.getContainer().equals(db)).findFirst().get();

        DeploymentNode server2 = workspace.getModel().getDeploymentNodeWithName("Server 2", "WithoutDeploymentGroups");
        ContainerInstance apiInstance2 = server2.getContainerInstances().stream().filter(ci -> ci.getContainer().equals(api)).findFirst().get();
        ContainerInstance dbInstance2 = server2.getContainerInstances().stream().filter(ci -> ci.getContainer().equals(db)).findFirst().get();

        assertTrue(apiInstance1.hasEfferentRelationshipWith(dbInstance1));
        assertTrue(apiInstance1.hasEfferentRelationshipWith(dbInstance2));
        assertTrue(apiInstance2.hasEfferentRelationshipWith(dbInstance2));
        assertTrue(apiInstance2.hasEfferentRelationshipWith(dbInstance1));

        server1 = workspace.getModel().getDeploymentNodeWithName("Server 1", "WithDeploymentGroups");
        apiInstance1 = server1.getContainerInstances().stream().filter(ci -> ci.getContainer().equals(api)).findFirst().get();
        dbInstance1 = server1.getContainerInstances().stream().filter(ci -> ci.getContainer().equals(db)).findFirst().get();

        server2 = workspace.getModel().getDeploymentNodeWithName("Server 2", "WithDeploymentGroups");
        apiInstance2 = server2.getContainerInstances().stream().filter(ci -> ci.getContainer().equals(api)).findFirst().get();
        dbInstance2 = server2.getContainerInstances().stream().filter(ci -> ci.getContainer().equals(db)).findFirst().get();

        assertTrue(apiInstance1.hasEfferentRelationshipWith(dbInstance1));
        assertFalse(apiInstance1.hasEfferentRelationshipWith(dbInstance2));
        assertTrue(apiInstance2.hasEfferentRelationshipWith(dbInstance2));
        assertFalse(apiInstance2.hasEfferentRelationshipWith(dbInstance1));
    }

    @Test
    void test_deploymentGroups_Hierarchical() throws Exception {
        StructurizrDslParser parser = new StructurizrDslParser();
        parser.parse(new File("src/test/resources/dsl/deployment-groups-hierarchical.dsl"));

        Workspace workspace = parser.getWorkspace();

        Container api = workspace.getModel().getSoftwareSystemWithName("Software System").getContainerWithName("API");
        Container db = workspace.getModel().getSoftwareSystemWithName("Software System").getContainerWithName("DB");

        DeploymentNode server1 = workspace.getModel().getDeploymentNodeWithName("Server 1", "WithoutDeploymentGroups");
        ContainerInstance apiInstance1 = server1.getContainerInstances().stream().filter(ci -> ci.getContainer().equals(api)).findFirst().get();
        ContainerInstance dbInstance1 = server1.getContainerInstances().stream().filter(ci -> ci.getContainer().equals(db)).findFirst().get();

        DeploymentNode server2 = workspace.getModel().getDeploymentNodeWithName("Server 2", "WithoutDeploymentGroups");
        ContainerInstance apiInstance2 = server2.getContainerInstances().stream().filter(ci -> ci.getContainer().equals(api)).findFirst().get();
        ContainerInstance dbInstance2 = server2.getContainerInstances().stream().filter(ci -> ci.getContainer().equals(db)).findFirst().get();

        assertTrue(apiInstance1.hasEfferentRelationshipWith(dbInstance1));
        assertTrue(apiInstance1.hasEfferentRelationshipWith(dbInstance2));
        assertTrue(apiInstance2.hasEfferentRelationshipWith(dbInstance2));
        assertTrue(apiInstance2.hasEfferentRelationshipWith(dbInstance1));

        server1 = workspace.getModel().getDeploymentNodeWithName("Server 1", "WithDeploymentGroups");
        apiInstance1 = server1.getContainerInstances().stream().filter(ci -> ci.getContainer().equals(api)).findFirst().get();
        dbInstance1 = server1.getContainerInstances().stream().filter(ci -> ci.getContainer().equals(db)).findFirst().get();

        server2 = workspace.getModel().getDeploymentNodeWithName("Server 2", "WithDeploymentGroups");
        apiInstance2 = server2.getContainerInstances().stream().filter(ci -> ci.getContainer().equals(api)).findFirst().get();
        dbInstance2 = server2.getContainerInstances().stream().filter(ci -> ci.getContainer().equals(db)).findFirst().get();

        assertTrue(apiInstance1.hasEfferentRelationshipWith(dbInstance1));
        assertFalse(apiInstance1.hasEfferentRelationshipWith(dbInstance2));
        assertTrue(apiInstance2.hasEfferentRelationshipWith(dbInstance2));
        assertFalse(apiInstance2.hasEfferentRelationshipWith(dbInstance1));
    }

    @Test
    void test_colorSchemes() throws Exception {
        StructurizrDslParser parser = new StructurizrDslParser();
        parser.parse(new File("src/test/resources/dsl/color-schemes.dsl"));

        Workspace workspace = parser.getWorkspace();

        ElementStyle elementStyle = workspace.getViews().getConfiguration().getStyles().getElementStyle("Element");
        assertEquals(Shape.RoundedBox, elementStyle.getShape());

        elementStyle = workspace.getViews().getConfiguration().getStyles().getElementStyle("Element", ColorScheme.Light);
        assertEquals("#000000", elementStyle.getColor());

        elementStyle = workspace.getViews().getConfiguration().getStyles().getElementStyle("Element", ColorScheme.Dark);
        assertEquals("#ffffff", elementStyle.getColor());

        RelationshipStyle relationshipStyle = workspace.getViews().getConfiguration().getStyles().getRelationshipStyle("Relationship");
        assertEquals(LineStyle.Solid, relationshipStyle.getStyle());

        relationshipStyle = workspace.getViews().getConfiguration().getStyles().getRelationshipStyle("Relationship", ColorScheme.Light);
        assertEquals("#000000", relationshipStyle.getColor());

        relationshipStyle = workspace.getViews().getConfiguration().getStyles().getRelationshipStyle("Relationship", ColorScheme.Dark);
        assertEquals("#ffffff", relationshipStyle.getColor());
    }

    @Test
    void test_noRelationship() throws Exception {
        StructurizrDslParser parser = new StructurizrDslParser();
        parser.parse(new File("src/test/resources/dsl/no-relationship.dsl"));

        Workspace workspace = parser.getWorkspace();

        Container ui = (Container)parser.getIdentifiersRegister().getElement("ss.ui");
        Container backend = (Container)parser.getIdentifiersRegister().getElement("ss.backend");

        // environment One: ui -> backend
        ContainerInstance uiInstance = workspace.getModel().getElements().stream().filter(e -> e instanceof ContainerInstance).map(e -> (ContainerInstance)e).filter(ci -> ci.getEnvironment().equals("One") && ci.getContainer().equals(ui)).findFirst().get();
        ContainerInstance backendInstance = workspace.getModel().getElements().stream().filter(e -> e instanceof ContainerInstance).map(e -> (ContainerInstance)e).filter(ci -> ci.getEnvironment().equals("One") && ci.getContainer().equals(backend)).findFirst().get();

        assertNotNull(uiInstance);
        assertNotNull(backendInstance);
        assertTrue(uiInstance.hasEfferentRelationshipWith(backendInstance));

        // environment Two: ui -> load balancer -> backend
        uiInstance = workspace.getModel().getElements().stream().filter(e -> e instanceof ContainerInstance).map(e -> (ContainerInstance)e).filter(ci -> ci.getEnvironment().equals("Two") && ci.getContainer().equals(ui)).findFirst().get();
        backendInstance = workspace.getModel().getElements().stream().filter(e -> e instanceof ContainerInstance).map(e -> (ContainerInstance)e).filter(ci -> ci.getEnvironment().equals("Two") && ci.getContainer().equals(backend)).findFirst().get();
        InfrastructureNode loadBalancer = workspace.getModel().getElements().stream().filter(e -> e instanceof InfrastructureNode).map(e -> (InfrastructureNode)e).filter(ci -> ci.getEnvironment().equals("Two")).findFirst().get();

        assertNotNull(uiInstance);
        assertNotNull(backendInstance);
        assertFalse(uiInstance.hasEfferentRelationshipWith(backendInstance));

        assertEquals("Makes API requests to", uiInstance.getEfferentRelationshipWith(loadBalancer).getDescription());
        assertEquals("JSON/HTTPS", uiInstance.getEfferentRelationshipWith(loadBalancer).getTechnology());

        assertEquals("Forwards API requests to", loadBalancer.getEfferentRelationshipWith(backendInstance).getDescription());
        assertEquals("", loadBalancer.getEfferentRelationshipWith(backendInstance).getTechnology());

        // environment Three: ui -> load balancer -> backend
        uiInstance = workspace.getModel().getElements().stream().filter(e -> e instanceof ContainerInstance).map(e -> (ContainerInstance)e).filter(ci -> ci.getEnvironment().equals("Three") && ci.getContainer().equals(ui)).findFirst().get();
        backendInstance = workspace.getModel().getElements().stream().filter(e -> e instanceof ContainerInstance).map(e -> (ContainerInstance)e).filter(ci -> ci.getEnvironment().equals("Three") && ci.getContainer().equals(backend)).findFirst().get();
        loadBalancer = workspace.getModel().getElements().stream().filter(e -> e instanceof InfrastructureNode).map(e -> (InfrastructureNode)e).filter(ci -> ci.getEnvironment().equals("Three")).findFirst().get();

        assertNotNull(uiInstance);
        assertNotNull(backendInstance);
        assertFalse(uiInstance.hasEfferentRelationshipWith(backendInstance));

        assertEquals("Makes API requests to", uiInstance.getEfferentRelationshipWith(loadBalancer).getDescription());
        assertEquals("JSON/HTTPS", uiInstance.getEfferentRelationshipWith(loadBalancer).getTechnology());

        assertEquals("Forwards API requests to", loadBalancer.getEfferentRelationshipWith(backendInstance).getDescription());
        assertEquals("", loadBalancer.getEfferentRelationshipWith(backendInstance).getTechnology());
    }

}