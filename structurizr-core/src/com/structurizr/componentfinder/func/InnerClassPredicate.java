package com.structurizr.componentfinder.func;

import java.util.function.Predicate;

enum InnerClassPredicate implements Predicate<Class<?>> {
    INSTANCE;


    @Override
    public boolean test(Class type) {
        return (type.isAnonymousClass() || type.isLocalClass() || type.isMemberClass());
    }
}
