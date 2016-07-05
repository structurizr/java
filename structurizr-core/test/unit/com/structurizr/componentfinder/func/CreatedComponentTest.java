package com.structurizr.componentfinder.func;

import com.structurizr.componentfinder.typeBased.myapp.MyController;
import com.structurizr.model.Component;
import com.structurizr.model.Container;
import org.junit.Before;
import org.junit.Test;

import java.util.HashSet;
import java.util.Set;

import static com.structurizr.componentfinder.TestConstants.createDefaultContainer;
import static org.assertj.core.api.Assertions.assertThat;

public class CreatedComponentTest {
    private static final String ONE_TAG = "one";
    private static final String TWO_TAG = "two";
    private CreatedComponent firstCreatedComponent;
    private CreatedComponent secondCreatedComponent;


    @Before
    public void setUp() throws Exception {
        final Container container = createDefaultContainer();
        firstCreatedComponent = create(container);
        secondCreatedComponent = create(container);

    }

    @Test
    public void onlyAllowOneComponentInstancePerClass() throws Exception {
        final Component firstComponent = firstCreatedComponent.getComponent();
        final Component secondComponent = secondCreatedComponent.getComponent();
        firstComponent.addTags(ONE_TAG);
        secondComponent.addTags(TWO_TAG);

        assertThat(firstComponent).isEqualTo(secondComponent);
        assertThat(firstComponent.getTags()).isEqualTo(secondComponent.getTags());
        assertThat(firstComponent.getTechnology()).isEqualTo(secondComponent.getTechnology());
        assertThat(firstComponent).isEqualTo(secondComponent);
    }

    @Test
    public void verifyEqualHascodeContract() throws Exception {
        assertThat(firstCreatedComponent).isEqualTo(secondCreatedComponent);

        final Set<CreatedComponent> set = new HashSet<>();
        set.add(firstCreatedComponent);
        set.add(secondCreatedComponent);
        assertThat(set).hasSize(1);
    }


    private CreatedComponent create(Container container) {
        return CreatedComponent.createFromClass(container, MyController.class);
    }

}