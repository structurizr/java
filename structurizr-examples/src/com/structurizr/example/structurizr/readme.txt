The Java programs in this package are used to generate the software architecture
model for Structurizr. Although you won't be able to run these programs
to recreate the model (you'll need the source code, which isn't open source),
they do illustrate how to use "Structurizr for Java" to create a software
architecture model for a real-world software system. In summary, during the
Structurizr build process, the programs are executed in the following order:

1. SystemContext (runs standalone)
2. Containers (runs standalone)
3. ComponentsForWebApplicationContainer(requires the source code for and the compiled version of the Structurizr web application)
4. ComponentsForApiContainer (requires the source code for and the compiled version of the Structurizr API application)
5. Styles (runs standalone)
6. UploadToStructurizr (runs standalone)

You can see the resulting diagrams at https://structurizr.com/public/121