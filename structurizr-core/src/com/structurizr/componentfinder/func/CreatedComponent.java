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

    public static CreatedComponent create(Component component, Class<?> originClass) {
        return new CreatedComponent(component, originClass);
    }

    public static CreatedComponent createFromClass(Container c, Class<?> type) {
        final Component component = c.addComponent(type.getSimpleName(), extractTypeName(type), "", "");
        return CreatedComponent.create(component, type);

    }

    public Component getComponent() {
        return component;
    }

    public Class<?> getOriginClass() {
        return originClass;
    }

    private static String extractTypeName(Class<?> type) {
        if (type.isAnonymousClass())
            return type.getEnclosingClass().getCanonicalName() + "-Anonymous";
        else
            return type.getCanonicalName();
    }

}
