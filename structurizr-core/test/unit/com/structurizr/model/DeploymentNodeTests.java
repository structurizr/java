package com.structurizr.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.structurizr.AbstractWorkspaceTestBase;
import com.structurizr.Workspace;
import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;

/**
 * @author Klaus Lehner, Catalysts GmbH
 */
public class DeploymentNodeTests extends AbstractWorkspaceTestBase {

    @Test
    public void test_serialize() throws IOException {
        SoftwareSystem ss1 = model.addSoftwareSystem("SS1", "");
        Container container1 = ss1.addContainer("Container1", "", "");
        DeploymentNode node1 = model.addDeploymentNode("Node1", "", "");
        node1.add(container1);

        ObjectMapper mapper = new ObjectMapper();
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        mapper.setSerializationInclusion(JsonInclude.Include.NON_EMPTY);

        String json = mapper.writeValueAsString(workspace);

        Workspace deserializedWorkspace = mapper.readValue(json, Workspace.class);
        Assert.assertNotNull(deserializedWorkspace);
    }
}
