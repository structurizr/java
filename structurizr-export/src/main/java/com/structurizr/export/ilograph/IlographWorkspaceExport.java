package com.structurizr.export.ilograph;

import com.structurizr.export.WorkspaceExport;

public class IlographWorkspaceExport extends WorkspaceExport {

    public IlographWorkspaceExport(String definition) {
        super(definition);
    }

    @Override
    public String getFileExtension() {
        return "idl";
    }

}
