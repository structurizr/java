package com.structurizr.componentfinder.func;

import com.structurizr.model.Component;
import com.structurizr.model.Container;

public class CreatedComponent {
    private final Component component;
    private final Class<?> originClass;

    private CreatedComponent(Component component, Class<?> originClass) {
        this.component = component;
        this.originClass = originClass;
    }

    public static CreatedComponent createFromClass(Container c, Class<?> type) {
        return CreatedComponent.create(createComponentFromClass(c, type), type);
    }

    public Component getComponent() {
        return component;
    }

    public Class<?> getOriginClass() {
        return originClass;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        CreatedComponent that = (CreatedComponent) o;

        if (!component.getName().equals(that.component.getName())) return false;
        if (!component.getCanonicalName().equals(that.component.getCanonicalName())) return false;
        if (!component.getType().equals(that.component.getType())) return false;
        if (!component.getPackage().equals(that.component.getPackage())) return false;
        return originClass.equals(that.originClass);

    }

    @Override
    public int hashCode() {
        int result = component.getName().hashCode();
        result = 31 * result + component.getCanonicalName().hashCode();
        result = 31 * result + component.getType().hashCode();
        result = 31 * result + component.getPackage().hashCode();
        result = 31 * result + originClass.hashCode();
        return result;
    }

    private static Component createComponentFromClass(Container c, Class<?> type) {
        final String typeName = extractTypeName(type);
        final Component existingComponentForType = c.getComponentOfType(typeName);
        if (existingComponentForType == null)
            return c.addComponent(type.getSimpleName(), typeName, "", "");
        else
            return existingComponentForType;
    }

    private static CreatedComponent create(Component component, Class<?> originClass) {
        return new CreatedComponent(component, originClass);
    }

    private static String extractTypeName(Class<?> type) {
        if (type.isAnonymousClass())
            return type.getEnclosingClass().getCanonicalName() + "-Anonymous";
        else
            return type.getCanonicalName();
    }
}
