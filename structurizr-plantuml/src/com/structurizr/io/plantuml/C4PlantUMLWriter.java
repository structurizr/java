package com.structurizr.io.plantuml;

import static java.lang.String.format;

import java.awt.Window.Type;
import java.io.IOException;
import java.io.Writer;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.logging.Level;

import com.structurizr.io.plantuml.PlantUMLWriter;
import com.structurizr.model.Component;
import com.structurizr.model.Container;
import com.structurizr.model.ContainerInstance;
import com.structurizr.model.Element;
import com.structurizr.model.Person;
import com.structurizr.model.Relationship;
import com.structurizr.model.SoftwareSystem;
import com.structurizr.view.View;

/**
 * This writer extends the classical one to use the C4-PlantUML sprite library
 * avaiable on GitHub.
 * 
 * To make full use of that sprite library, we use Structurizr properties to
 * tweak rendering.
 * 
 * It is possible to change relationships directions and rendering using the
 * {@link #C4_LAYOUT_DIRECTION} and {@link #C4_LAYOUT_MODE} mode properties.
 * 
 * @see https://github.com/RicardoNiepel/C4-PlantUML
 * @author nicolas-delsaux
 *
 */
public class C4PlantUMLWriter extends PlantUMLWriter {
	/**
	 * When the value associated with this name resolves to the boolean value "true" (the default)
	 * the associated element is concerned internal (if possible).
	 * Currently, this applies to Person, SoftwareSystem,
	 */
	public static final String C4_INTERNAL = "c4:state:internal";
	/**
	 * This property indicates to C4-PlantUML library which relationship type to
	 * use. Possible values are given in the {@link Directions} enum
	 */
	public static final String C4_LAYOUT_DIRECTION = "c4:layout:direction";
	/**
	 * This property indicates to C4-PlantUML library which relationship mode to
	 * use. Possible values are given in the {@link RelationshipModes} enum
	 */
	public static final String C4_LAYOUT_MODE = "c4:layout:mode";
	/**
	 * Defines the {@link Type} of component.
	 */
	public static final String C4_ELEMENT_TYPE = "c4:element:type";

	public static enum Type {
		Default, Db
	}

	public static enum Directions {
		Up, Down, Right, Left
	}

	public static enum RelationshipModes {
		Rel, Neighbor, Back, Back_Neighbor, Lay
	}

	public static class NoMacroFound extends RuntimeException {

	}

	private abstract class C4ElementWriter<WrittenElement extends Element> {

		public void write(View view, WrittenElement element, Writer writer, String prefix) throws IOException {
			final String id = idOf(element);
			final String separator = System.lineSeparator();
			doWrite(view, element, writer, prefix, id, separator);
		}

		abstract void doWrite(View view, WrittenElement element, Writer writer, String prefix, String id,
				String separator) throws IOException;

	}

	private class PersonWriter extends C4ElementWriter<Person> {

		@Override
		void doWrite(View view, Person element, Writer writer, String prefix, String id, String separator)
				throws IOException {
			String macro = "Person_Ext";
			if(Boolean.parseBoolean(element.getProperties().getOrDefault(C4_INTERNAL, "true"))) {
				macro = "Person";
			}
			writer.write(format("%s%s(%s, \"%s\", \"%s\")%s", prefix, macro, id, element.getName(),
					element.getDescription(), separator));
		}
	}

	private class SoftwareSystemWriter extends C4ElementWriter<SoftwareSystem> {
		@Override
		void doWrite(View view, SoftwareSystem element, Writer writer, String prefix, String id, String separator)
				throws IOException {
			boolean internal = Boolean.parseBoolean(element.getProperties().getOrDefault(C4_INTERNAL, "true"));
			Type type = Type.valueOf(element.getProperties().getOrDefault(C4_ELEMENT_TYPE, Type.Default.name()));
			String macro = String.format("System%s%s", 
					type==Type.Default ? "" : type.name(),
							internal ? "" : "_Ext");
			writer.write(format("%s%s(%s, \"%s\", \"%s\")%s", prefix, macro, id, element.getName(),
					element.getDescription(), separator));
		}
	}

	private class ContainerWriter extends C4ElementWriter<Container> {
		@Override
		void doWrite(View view, Container element, Writer writer, String prefix, String id, String separator)
				throws IOException {
			Type type = Type.valueOf(element.getProperties().getOrDefault(C4_ELEMENT_TYPE, Type.Default.name()));
			String macro = String.format("Container%s", 
					type==Type.Default ? "" : type.name());
			writer.write(format("%s%s(%s, \"%s\", \"%s\", \"%s\")%s", prefix, macro, id, element.getName(),
					element.getTechnology(), element.getDescription(), separator));
		}
	}

	private class ComponentWriter extends C4ElementWriter<Component> {
		@Override
		void doWrite(View view, Component element, Writer writer, String prefix, String id, String separator)
				throws IOException {
			Type type = Type.valueOf(element.getProperties().getOrDefault(C4_ELEMENT_TYPE, Type.Default.name()));
			String macro = String.format("Component%s", 
					type==Type.Default ? "" : type.name());
			writer.write(format("%s%s(%s, \"%s\", \"%s\", \"%s\")%s", prefix, macro, id, element.getName(),
					element.getTechnology(), element.getDescription(), separator));
		}
	}

	private class ContainerInstanceWriter extends C4ElementWriter<ContainerInstance> {
		@Override
		void doWrite(View view, ContainerInstance element, Writer writer, String prefix, String id, String separator)
				throws IOException {
			Type type = Type.valueOf(element.getProperties().getOrDefault(C4_ELEMENT_TYPE, 
					element.getContainer().getProperties().getOrDefault(C4_ELEMENT_TYPE,Type.Default.name())
					));
			String macro = String.format("Container%s", 
					type==Type.Default ? "" : type.name());
			writer.write(format("%s%s(%s, \"%s\", \"%s\", \"%s\")%s", prefix, macro, id,
					element.getContainer().getName(), element.getContainer().getTechnology(),
					element.getContainer().getDescription(), separator));
		}
	}

	private static final java.util.logging.Logger logger = java.util.logging.Logger
			.getLogger(C4PlantUMLWriter.class.getName());

	public C4PlantUMLWriter() {
		super();
		try {
			addIncludeURL(new URI("https://raw.githubusercontent.com/RicardoNiepel/C4-PlantUML/master/C4.puml"));
			addIncludeURL(
					new URI("https://raw.githubusercontent.com/RicardoNiepel/C4-PlantUML/master/C4_Context.puml"));
			addIncludeURL(
					new URI("https://raw.githubusercontent.com/RicardoNiepel/C4-PlantUML/master/C4_Container.puml"));
			addIncludeURL(
					new URI("https://raw.githubusercontent.com/RicardoNiepel/C4-PlantUML/master/C4_Component.puml"));
		} catch (URISyntaxException e) {
			logger.log(Level.SEVERE, "Using C4-PlantUML shoulld not trigger URI error", e);
		}
	}

	@Override
	protected void write(View view, ContainerInstance container, Writer writer, int indent) {
		try {
			new ContainerInstanceWriter().write(view, container, writer, calculateIndent(indent));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * We replace the write element method to use macros provided by C4-PlantUML
	 */
	protected void write(View view, Element element, Writer writer, boolean indent) {
		try {
			final String prefix = indent ? "  " : "";
			final String id = idOf(element);
			final String separator = System.lineSeparator();
			getWriterFor(element).write(view, element, writer, prefix);
		} catch (NoMacroFound noMacro) {
			super.write(view, element, writer, indent);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private C4ElementWriter getWriterFor(Element element) {
		if (element instanceof Person) {
			return new PersonWriter();
		} else if (element instanceof SoftwareSystem) {
			return new SoftwareSystemWriter();
		} else if (element instanceof Container) {
			return new ContainerWriter();
		} else if (element instanceof Component) {
			return new ComponentWriter();
		}
		throw new NoMacroFound();
	}

	@Override
	protected void writeRelationship(View view, Relationship relationship, Writer writer) {
		try {
			final String separator = System.lineSeparator();
			String relationshipMacro = null;
			RelationshipModes mode = RelationshipModes.Rel;
			if (relationship.getProperties().containsKey(C4_LAYOUT_MODE)) {
				mode = RelationshipModes.valueOf(relationship.getProperties().get(C4_LAYOUT_MODE));
			}
			switch (mode) {
			case Lay:
			case Rel:
				relationshipMacro = mode.name();
				if (relationship.getProperties().containsKey(C4_LAYOUT_DIRECTION)) {
					Directions direction = Directions.valueOf(relationship.getProperties().get(C4_LAYOUT_DIRECTION));
					relationshipMacro = String.format("%s_%s", relationshipMacro, direction);
				}
				break;
			default:
				relationshipMacro = String.format("%s_%s", relationshipMacro, mode);
			}
			if (relationship.getDescription() == null) {
				writer.write(format("%s(%s, %s)%s", relationshipMacro, idOf(relationship.getSource()),
						idOf(relationship.getDestination()), separator));
			} else {
				if (relationship.getTechnology() == null) {
					writer.write(format("%s(%s, %s, '%s')%s", relationshipMacro, idOf(relationship.getSource()),
							idOf(relationship.getDestination()), relationship.getDescription(), separator));
				} else {
					writer.write(format("%s(%s, %s, '%s', %s)%s", relationshipMacro, idOf(relationship.getSource()),
							idOf(relationship.getDestination()), relationship.getDescription(),
							relationship.getTechnology(), separator));
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}