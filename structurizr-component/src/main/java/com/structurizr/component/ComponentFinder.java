package com.structurizr.component;

import com.structurizr.component.provider.TypeProvider;
import com.structurizr.model.Component;
import com.structurizr.model.Container;
import com.structurizr.util.StringUtils;
import org.apache.bcel.Repository;
import org.apache.bcel.classfile.ConstantPool;
import org.apache.bcel.classfile.Method;
import org.apache.bcel.generic.*;

import java.util.*;

/**
 * Allows you to find components in a Java codebase based upon a set of pluggable and customisable rules.
 * Use the {@link ComponentFinderBuilder} to create an instance of this class.
 */
public final class ComponentFinder {

    private static final String COMPONENT_TYPE_PROPERTY_NAME = "component.type";
    private static final String COMPONENT_SOURCE_PROPERTY_NAME = "component.src";

    private final TypeRepository typeRepository = new TypeRepository();
    private final Container container;
    private final List<ComponentFinderStrategy> componentFinderStrategies = new ArrayList<>();

    ComponentFinder(Container container, Collection<TypeProvider> typeProviders, List<ComponentFinderStrategy> componentFinderStrategies) {
        if (container == null) {
            throw new IllegalArgumentException("A container must be specified.");
        }

        this.container = container;

        for (TypeProvider typeProvider : typeProviders) {
            Set<com.structurizr.component.Type> types = typeProvider.getTypes();
            for (com.structurizr.component.Type type : types) {
                if (type.getJavaClass() != null) {
                    // this is the BCEL identified type
                    typeRepository.add(type);
                } else {
                    // this is the source code identified type
                    com.structurizr.component.Type bcelType = typeRepository.getType(type.getFullyQualifiedName());
                    if (bcelType != null) {
                        bcelType.setDescription(type.getDescription());
                        bcelType.setSource(type.getSource());
                    }
                }
            }
        }

        Repository.clearCache();
        for (com.structurizr.component.Type type : typeRepository.getTypes()) {
            if (type.getJavaClass() != null) {
                Repository.addClass(type.getJavaClass());
                findDependencies(type);
            }
        }

        this.componentFinderStrategies.addAll(componentFinderStrategies);
    }

    private void findDependencies(com.structurizr.component.Type type) {
        ConstantPool cp = type.getJavaClass().getConstantPool();
        ConstantPoolGen cpg = new ConstantPoolGen(cp);
        for (Method m : type.getJavaClass().getMethods()) {
            MethodGen mg = new MethodGen(m, type.getJavaClass().getClassName(), cpg);
            InstructionList il = mg.getInstructionList();
            if (il == null) {
                continue;
            }

            InstructionHandle[] instructionHandles = il.getInstructionHandles();
            for (InstructionHandle instructionHandle : instructionHandles) {
                Instruction instruction = instructionHandle.getInstruction();
                if (!(instruction instanceof InvokeInstruction)) {
                    continue;
                }

                InvokeInstruction invokeInstruction = (InvokeInstruction)instruction;
                ReferenceType referenceType = invokeInstruction.getReferenceType(cpg);
                if (!(referenceType instanceof ObjectType)) {
                    continue;
                }

                ObjectType objectType = (ObjectType)referenceType;
                String referencedClassName = objectType.getClassName();
                com.structurizr.component.Type referencedType = typeRepository.getType(referencedClassName);
                if (referencedType != null) {
                    type.addDependency(referencedType);
                }
            }
        }
    }

    /**
     * Find components, using all configured rules, in the order they were added.
     */
    public void findComponents() {
        Set<DiscoveredComponent> discoveredComponents = new HashSet<>();
        Map<DiscoveredComponent, Component> componentMap = new HashMap<>();

        for (ComponentFinderStrategy componentFinderStrategy : componentFinderStrategies) {
            discoveredComponents.addAll(componentFinderStrategy.findComponents(typeRepository));
        }

        for (DiscoveredComponent discoveredComponent : discoveredComponents) {
            Component component = container.addComponent(discoveredComponent.getName());
            component.addProperty(COMPONENT_TYPE_PROPERTY_NAME, discoveredComponent.getPrimaryType().getFullyQualifiedName());
            if (!StringUtils.isNullOrEmpty(discoveredComponent.getPrimaryType().getSource())) {
                component.addProperty(COMPONENT_SOURCE_PROPERTY_NAME, discoveredComponent.getPrimaryType().getSource());
            }
            component.setDescription(discoveredComponent.getDescription());
            component.setTechnology(discoveredComponent.getTechnology());
            componentMap.put(discoveredComponent, component);
        }

        // find dependencies between all components
        for (DiscoveredComponent discoveredComponent : discoveredComponents) {
            Set<com.structurizr.component.Type> typeDependencies = discoveredComponent.getAllDependencies();
            for (Type typeDependency : typeDependencies) {
                for (DiscoveredComponent c : discoveredComponents) {
                    if (c != discoveredComponent) {
                        if (c.getAllTypes().contains(typeDependency)) {
                            Component componentDependency = componentMap.get(c);
                            componentMap.get(discoveredComponent).uses(componentDependency, "");
                        }
                    }
                }
            }
        }

        // now visit all components
        for (DiscoveredComponent discoveredComponent : componentMap.keySet()) {
            Component component = componentMap.get(discoveredComponent);
            discoveredComponent.getComponentFinderStrategy().visit(component);
        }
    }

}