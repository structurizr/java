package com.structurizr.io.dot;

import com.structurizr.Workspace;
import com.structurizr.example.ExampleWorkspace;

import java.io.StringWriter;

/**
 * Demonstrates the export to DOT. Paste graphs into https://stamm-wilbrandt.de/GraphvizFiddle/
 * to visualise them.
 */
public class DotWriterExample {

    public static void main(String[] args) {
        Workspace workspace = ExampleWorkspace.create(false
        );
        StringWriter stringWriter = new StringWriter();
        DotWriter dotWriter = new DotWriter();
        dotWriter.write(workspace, stringWriter);

        System.out.println(stringWriter);
    }

}
