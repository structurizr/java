# Usage patterns

## Single program

The simplest way to create a software architecture model is to write a single Java program that first creates the model elements (people, software systems, containers and components) before subsequently creating the required views and uploading the workspace to Structurizr. If you have a particularly large model, or you'd like to share common elements between models, then one approach is to modularise your program appropriately, perhaps using something like [Cat Boot Structurizr](https://github.com/Catalysts/cat-boot/tree/master/cat-boot-structurizr).

## Multiple programs

Another approach is to write a collection of Java programs, that are each responsible for creating a different part of the software architecture model. This is especially useful if you need to use the component finder with different classpaths. You can then write a script, or use your build script, to run these Java programs in sequence. Intermediate versions of the workspace can be saved to and loaded from disk using the [WorkspaceUtils](https://github.com/structurizr/java/blob/master/structurizr-core/src/com/structurizr/util/WorkspaceUtils.java) class. For example:

1. Program 1: Create the basic model elements (people, software systems and containers) and the relationships between them.
2. Program 2: Add components for container 1 (e.g. run the component finder).
3. Program 3: Add components for container 2 (e.g. run the component finder with a different classpath).
4. Program 4: Create views.
5. Program 5: Add styling.
6. Program 6: Upload to Structurizr.

In this example, the first program would write the initial version of the workspace to a local file on disk, which subsequent programs then load and add to, before writing the workspace back to disk.