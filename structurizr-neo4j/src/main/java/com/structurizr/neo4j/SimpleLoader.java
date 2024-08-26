package com.structurizr.neo4j;

import com.structurizr.Workspace;
import com.structurizr.model.Element;
import com.structurizr.model.Relationship;
import com.structurizr.util.StringUtils;
import org.neo4j.driver.Driver;
import org.neo4j.driver.SessionConfig;

public class SimpleLoader {

    public void load(Workspace workspace, Driver driver, String database) {
        try (var session = driver.session(SessionConfig.builder().withDatabase(database).build())) {
            for (Element element : workspace.getModel().getElements()) {
                session.run(String.format(
                        "CREATE ( :Element { id: '%s', name: \"%s\", type: \"%s\" })",
                        element.getId(), element.getName(), element.getClass().getSimpleName().toLowerCase()
                ));
            }

            session.run("CREATE INDEX element_index FOR (n:Element) ON (n.id)");

            for (Relationship relationship : workspace.getModel().getRelationships()) {
                session.run(String.format(
                        """
                                MATCH ( from:Element { id: '%s' } ), ( to:Element { id: '%s' } )
                                CREATE (from)-[:HAS_RELATIONSHIP_WITH {role: '%s'}]->(to)""",
                        relationship.getSource().getId(),
                        relationship.getDestination().getId(),
                        !StringUtils.isNullOrEmpty(relationship.getDescription()) ? relationship.getDescription() : "uses"
                ));
            }
        }
    }

}