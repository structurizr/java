package com.structurizr;

import com.structurizr.neo4j.SimpleLoader;
import com.structurizr.util.WorkspaceUtils;
import org.neo4j.driver.AuthTokens;
import org.neo4j.driver.Driver;
import org.neo4j.driver.GraphDatabase;
import org.neo4j.driver.Result;

import java.io.File;

public class Example {

    public static void main(String[] args) throws Exception {
        Workspace workspace = WorkspaceUtils.loadWorkspaceFromJson(new File("workspace.json"));

        try (Driver driver = GraphDatabase.driver("bolt://localhost:7687", AuthTokens.basic("neo4j", "password"))) {
            try (var session = driver.session()) {
                session.run("DROP DATABASE structurizr IF EXISTS");
                Result result = session.run("CREATE DATABASE structurizr");
                System.out.println(result.consume());
            }

            new SimpleLoader().load(workspace, driver, "structurizr");
        }
    }

}