package com.structurizr.componentfinder.func;

import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableSet;
import com.structurizr.model.Component;

import java.util.Collection;

import static com.google.common.base.Preconditions.checkNotNull;

public class FunctionalComponentFinder {
    private final ComponentScanner componentScanner;
    private final DependenciesScanner dependenciesScanner;


    private FunctionalComponentFinder(Builder builder) {
        componentScanner = builder.buildScanner();
        dependenciesScanner = builder.dependenciesScanner;
    }

    public static Builder builder() {
        return new Builder();
    }

    public Collection<Component> findComponents(final String packageToScan) {
        final ScanResult scanResult = componentScanner.scanForComponents(packageToScan);
        dependenciesScanner.addDependencies(scanResult);
        return scanResult.getComponents();
    }


    public static final class Builder {
        private ImmutableSet.Builder<TypeBasedComponentFactory> factoriesBuilder = ImmutableSet.builder();
        private DependenciesScanner dependenciesScanner;

        private Builder() {
        }


        public Builder withDirectDependencyScanner() {
            return withDependenciesScanner(DirectDependenciesScanner.INSTANCE);
        }

        public Builder withStructurizrDependencyScanner() {
            return withDependenciesScanner(StructurizrDependenciesScanner.INSTANCE);
        }

        public Builder withDependenciesScanner(DependenciesScanner val) {
            dependenciesScanner = val;
            return this;
        }

        public FunctionalComponentFinder build() {
            checkNotNull(dependenciesScanner, "A dependency scanner is required.");
            return new FunctionalComponentFinder(this);
        }


        public Builder addComponentFactory(TypeBasedComponentFactory componentFactory) {
            factoriesBuilder.add(componentFactory);
            return this;
        }

        private ComponentScanner buildScanner() {
            final ImmutableSet<TypeBasedComponentFactory> factories = factoriesBuilder.build();
            Preconditions.checkState(!factories.isEmpty(), "No component factories were added. No components can be created.");
            return new ComponentScanner(factories);
        }
    }
}

