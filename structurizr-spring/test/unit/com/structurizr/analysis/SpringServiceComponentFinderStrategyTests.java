package com.structurizr.analysis;

import com.structurizr.Workspace;
import com.structurizr.model.CodeElement;
import com.structurizr.model.Component;
import com.structurizr.model.Container;
import com.structurizr.model.Model;
import com.structurizr.model.SoftwareSystem;
import org.junit.Test;

import java.util.Set;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class SpringServiceComponentFinderStrategyTests {

    private Workspace workspace = new Workspace("Name", "Description");
    private Model model = workspace.getModel();
    private SoftwareSystem softwareSystem = model.addSoftwareSystem("Name", "Description");
    private Container container = softwareSystem.addContainer("Name", "Description", "Technology");
    private SpringServiceComponentFinderStrategy strategy = new SpringServiceComponentFinderStrategy();

    @Test
    public void test_findComponents_WithAlternatesAndCollapseAlternatesTrue_FindsSpringServices() throws Exception {
        strategy.setCollapseAlternates(true);

        findComponents();

        assertEquals(1, container.getComponents().size());
        Component component = container.getComponentWithName("SomeService");
        assertEquals("test.SpringServiceComponentFinderStrategy.SomeService", component.getType().getType());
        assertEquals("", component.getDescription());
        assertEquals("Spring Service", component.getTechnology());

        // Confirm SomeServiceAlternateImpl collapsed in as supporting code
        final Set<CodeElement> code = component.getCode();
        assertTrue(code.stream().anyMatch(c -> "SomeServiceDefaultImpl".equals(c.getName())));
        assertTrue(code.stream().anyMatch(c -> "SomeServiceAlternateImpl".equals(c.getName())));
    }

    @Test(expected = IllegalStateException.class)
    public void test_findComponents_WithAlternatesAndCollapseAlternatesFalse_ThrowsException() throws Exception {
        strategy.setCollapseAlternates(false);

        findComponents();
    }

    private void findComponents() throws Exception {
        ComponentFinder componentFinder = new ComponentFinder(
                container,
                "test.SpringServiceComponentFinderStrategy",
                strategy
        );
        componentFinder.findComponents();
    }

}
