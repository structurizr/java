package com.structurizr.dsl;

import com.structurizr.Workspace;
import com.structurizr.http.HttpClient;
import com.structurizr.model.Element;
import com.structurizr.model.Relationship;

import java.io.File;
import java.net.URL;
import java.net.URLClassLoader;

abstract class DslContext {

    private static final String PLUGINS_DIRECTORY_NAME = "plugins";

    static final String CONTEXT_START_TOKEN = "{";
    static final String CONTEXT_END_TOKEN = "}";

    private Workspace workspace;
    private boolean extendingWorkspace;
    private boolean dslPortable = true;

    protected IdentifiersRegister identifiersRegister = new IdentifiersRegister();

    private Features features = new Features();
    private HttpClient httpClient = new HttpClient();

    Workspace getWorkspace() {
        return workspace;
    }

    void setWorkspace(Workspace workspace) {
        this.workspace = workspace;
    }

    boolean isExtendingWorkspace() {
        return extendingWorkspace;
    }

    void setExtendingWorkspace(boolean extendingWorkspace) {
        this.extendingWorkspace = extendingWorkspace;
    }

    boolean isDslPortable() {
        return dslPortable;
    }

    void setDslPortable(boolean bool) {
        this.dslPortable = bool;
    }

    void setIdentifierRegister(IdentifiersRegister identifersRegister) {
        this.identifiersRegister = identifersRegister;
    }

    String findIdentifier(Element element) {
        return identifiersRegister.findIdentifier(element);
    }

    Element getElement(String identifier) {
        return getElement(identifier, null);
    }

    Element getElement(String identifier, Class<? extends Element> type) {
        Element element = null;
        identifier = identifier.toLowerCase();

        if (identifiersRegister.getIdentifierScope() == IdentifierScope.Hierarchical) {
            ElementDslContext elementDslContext = null;
            if (this instanceof ElementDslContext) {
                elementDslContext = (ElementDslContext)this;
            } else if (this instanceof ElementsDslContext) {
                ElementsDslContext elementsDslContext = (ElementsDslContext)this;
                if (elementsDslContext.getParentDslContext() instanceof ElementDslContext) {
                    elementDslContext = (ElementDslContext)elementsDslContext.getParentDslContext();
                }
            }

            if (elementDslContext != null) {
                Element parent = elementDslContext.getElement();
                while (parent != null && element == null) {
                    String parentIdentifier = identifiersRegister.findIdentifier(parent);

                    element = identifiersRegister.getElement(parentIdentifier + "." + identifier);
                    parent = parent.getParent();

                    element = checkElementType(element, type);
                }
            } else if (this instanceof DeploymentEnvironmentDslContext) {
                DeploymentEnvironmentDslContext deploymentEnvironmentDslContext = (DeploymentEnvironmentDslContext)this;
                DeploymentEnvironment deploymentEnvironment = deploymentEnvironmentDslContext.getEnvironment();
                String parentIdentifier = identifiersRegister.findIdentifier(deploymentEnvironment);

                element = identifiersRegister.getElement(parentIdentifier + "." + identifier);
            }

            if (element == null) {
                // default to finding a top-level element
                element = identifiersRegister.getElement(identifier);
            }
        } else {
            element = identifiersRegister.getElement(identifier);
        }

        element = checkElementType(element, type);
        return element;
    }

    Element checkElementType(Element element, Class<? extends Element> type) {
        if (element != null && type != null) {
            if (!element.getClass().isAssignableFrom(type)) {
                element = null;
            }
        }

        return element;
    }

    Relationship getRelationship(String identifier) {
        return identifiersRegister.getRelationship(identifier.toLowerCase());
    }

    Features getFeatures() {
        return features;
    }

    void setFeatures(Features features) {
        this.features = features;
    }

    HttpClient getHttpClient() {
        return httpClient;
    }

    void setHttpClient(HttpClient httpClient) {
        this.httpClient = httpClient;
    }

    protected <T> Class<? extends T> loadClass(String fqn, File dslFile) throws Exception {
        File pluginsDirectory = new File(dslFile.getParent(), PLUGINS_DIRECTORY_NAME);
        URL[] urls = new URL[0];

        if (pluginsDirectory.exists()) {
            File[] jarFiles = pluginsDirectory.listFiles((dir, name) -> name.endsWith(".jar"));
            if (jarFiles != null) {
                urls = new URL[jarFiles.length];
                for (int i = 0; i < jarFiles.length; i++) {
                    try {
                        urls[i] = jarFiles[i].toURI().toURL();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }

        URLClassLoader childClassLoader = new URLClassLoader(urls, getClass().getClassLoader());
        return (Class<? extends T>) childClassLoader.loadClass(fqn);
    }

    void end() {
    }

    protected abstract String[] getPermittedTokens();

}