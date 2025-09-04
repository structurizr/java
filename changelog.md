# Changelog

## v4.2.0 (unreleased)

- structurizr-java: Fixes https://github.com/structurizr/java/issues/437 (Make ComponentFinder.run() not fail on empty Set<DiscoveredComponent>).
- structurizr-dsl: Adds support for `iconPosition` on element styles (options are `Top`, `Bottom`, `Left`).
- structurizr-dsl: Adds support for defining element and relationship styles for light and dark mode.
- structurizr-dsl: Adds a `Bucket` shape.
- structurizr-dsl: Adds a `Shell` shape.
- structurizr-dsl: Adds a `Terminal` shape.
- structurizr-dsl: Adds an 'instanceOf' keyword (an alternative for `softwareSystemInstance` and `containerInstance`).
- structurizr-dsl: Relationships to/from software system/container instances can be now defined by using the software system/container identifier.
- structurizr-dsl: Fixes https://github.com/structurizr/java/issues/435 (Relationship archetype not applied to implicit-source relationships).
- structurizr-dsl: Adds support for removing relationships between software system instance/container instances, with a view to redefining them via infrastructure nodes.
- structurizr-dsl: Adds support for a `jump` property on relationship styles.
- structurizr-import: Adds support for `plantuml.inline`, `mermaid.inline`, and `kroki.inline` properties to inline the resulting PNG/SVG file into the workspace.
- structurizr-inspection: Adds a way to disable inspections via a workspace property named `structurizr.inspection` (`false` to disable).
- structurizr-inspection: Default inspector adds a summary of error/warning/info/ignore counts as workspace properties.
- structurizr-inspection: Fixes `model.deploymentnode.technology` (it was checking the description property rather than technology).

## v4.1.0 (28th May 2025)

- structurizr-client: Fixes https://github.com/structurizr/java/issues/413 (Cannot push to main branch, when branch feature is activated).
- structurizr-dsl: Allows archetypes to be used via workspace extension.
- structurizr-dsl: Adds archetype support for custom elements.
- structurizr-dsl: Fixes https://github.com/structurizr/java/issues/399 (Archetype tags sometimes missing).
- structurizr-dsl: Fixes https://github.com/structurizr/java/issues/392 (SVG not supported in base 64 encoding not mentioned in documentation).
- structurizr-dsl: Adds support for setting the symbols surrounding element/relationship metadata used when rendering diagrams.
- structurizr-dsl: Fixes https://github.com/structurizr/java/issues/408 (Animation steps cannot be added to deployment views via static structure element references).
- structurizr-dsl: Adds support for specifying view animation steps via element expressions.
- structurizr-dsl: Fixes https://github.com/structurizr/java/issues/404 (deploymentGroup does not obey !identifiers hierarchical).
- structurizr-export: Adds support for rank and node separation to the StructurizrPlantUMLExporter.

## v4.0.0 (28th March 2025)

- structurizr-dsl: Fixes https://github.com/structurizr/java/issues/374 (!identifiers hierarchical isn't propagated when extending a workspace).
- structurizr-dsl: Adds the ability to use the `group` keyword inside a component definition, to set the group name of that component.
- structurizr-dsl: Adds the ability to use the `group` keyword inside the component finder strategy `forEach` block.
- structurizr-dsl: Adds a reluctant version of `include *` (`include *?`) for system context views that only adds relationships to/from the scoped software system.
- structurizr-dsl: Adds a reluctant version of `include *` (`include *?`) for container views that only adds relationships to/from the containers in the scoped software system.
- structurizr-dsl: Adds a reluctant version of `include *` (`include *?`) for component views that only adds relationships to/from the components in the scoped container.
- structurizr-dsl: Removes deprecated `!ref` and `!extend` keywords.
- structurizr-dsl: Adds support for Java style `"""` multi-line text blocks.
- structurizr-dsl: Adds support for defining element and relationship archetypes.

## 3.2.1 (10th December 2024)

- structurizr-core: Fixes https://github.com/structurizr/java/issues/362 (Ordering of replicated relationships in deployment environment is non-deterministic).

## 3.2.0 (6th December 2024)

- structurizr-dsl: Adds support for `element!=` expressions.
- structurizr-dsl: `!elements` and `!relationships` now work inside deployment environment blocks.
- structurizr-dsl: `description` and `technology` now work inside `!elements` blocks.

## 3.1.0 (4th November 2024)

- structurizr-client: Workspace archive file now includes the branch name in the filename.
- structurizr-component: Adds `ImplementationWithPrefixSupportingTypesStrategy`.
- structurizr-component: Adds `ImplementationWithSuffixSupportingTypesStrategy`.
- structurizr-dsl: Adds `supportingTypes implementation-prefix <prefix>`.
- structurizr-dsl: Adds `supportingTypes implementation-suffix <suffix>`.
- structurizr-dsl: Fixes https://github.com/structurizr/java/issues/346 (`// comment \` joins lines).
- structurizr-dsl: Anonymous identifiers for relationships (i.e. relationships not assigned to an identifier) are excluded from the model, and therefore also excluded from the serialised JSON.
- structurizr-dsl: Adds a way to configure whether the DSL source is retained via a workspace property named `structurizr.dsl.source` - `true` (default) or `false`.
- structurizr-dsl: Adds the ability to define a PlantUML/Mermaid image view that is an export of a workspace view.
- structurizr-dsl: Adds support for `url`, `properties`, and `perspectives` nested inside `!elements` and `!relationships`.
- structurizr-dsl: Fixes https://github.com/structurizr/java/issues/347 (`->container->` expression does not work as expected in deployment view).
- structurizr-dsl: Adds support for `!elements group` (https://github.com/structurizr/java/issues/351).

## 3.0.0 (19th September 2024)

- structurizr-client: Adds support to get/put workspace branches on the [cloud service](https://docs.structurizr.com/cloud/workspace-branches) and [on-premises installation](https://docs.structurizr.com/onpremises/workspace-branches).
- structurizr-core: Adds name-value properties to dynamic view relationship views (https://github.com/structurizr/java/issues/316).
- structurizr-component: Initial rewrite of the original `structurizr-analysis` library - provides a way to automatically find components in a Java codebase.
- structurizr-dsl: Removes deprecated `!constant` keyword.
- structurizr-dsl: Adds name-value properties to dynamic view relationship views.
- structurizr-dsl: Fixes https://github.com/structurizr/java/issues/312 (!include doesn't work with files encoded as UTF-8 BOM).
- structurizr-dsl: Adds a way to explicitly specify the order of relationships in dynamic views.
- structurizr-dsl: Adds support for element technology expressions (e.g. `element.technology==Java` and `element.technology!=Java`).
- structurizr-dsl: Deprecates `!ref` and `!extend`.
- structurizr-dsl: Adds an `!element` keyword that can be used to find a single element by identifier or canonical name (replaces `!ref` and `!extend`).
- structurizr-dsl: Adds an `!elements` keyword that can be used to find a set of elements via an expression.
- structurizr-dsl: Adds an `!relationship` keyword that can be used to find a single relationship by identifier (replaces `!ref` and `!extend`).
- structurizr-dsl: Adds a `!relationships` keyword that can be used to find a set of relationships via an expression.
- structurizr-dsl: Adds a DSL wrapper around the `structurizr-component` component finder (`!components`).
- structurizr-dsl: Adds support for local theme files to be specified via `theme` (https://github.com/structurizr/java/issues/331).
- structurizr-dsl: An exception is now thrown when trying to use disallowed features in restricted mode (e.g. `!docs`, `!include <file>`, etc).
- structurizr-export: Adds support for icons to the Ilograph exporter (https://github.com/structurizr/java/issues/332).
- structurizr-export: Adds support for imports to the Ilograph exporter (https://github.com/structurizr/java/issues/332).
- structurizr-export: Fixes https://github.com/structurizr/java/issues/337 (Malformed subgraph name in Mermaid render).

## 2.2.0 (2nd July 2024)

- structurizr-dsl: Adds support for element/relationship property expressions (https://github.com/structurizr/java/issues/297).
- structurizr-dsl: Adds a way to specify the implied relationships strategy via a fully qualified class name when using `!impliedRelationships`.
- structurizr-dsl: Adds the ability to include single files as documentation (https://github.com/structurizr/java/issues/303). 

## 2.1.4 (18th June 2024)

- structurizr-core: Fixes https://github.com/structurizr/java/issues/306 (Workspace.trim() is not correctly removing relationships when the destination of a relationship is removed from the workspace).

## 2.1.3 (16th June 2024)

- structurizr-core: Fixes https://github.com/structurizr/java/issues/298 (Unknown model item type on 'element').

## 2.1.2 (30th April 2024)

- structurizr-core: Adds better backwards compatibility to deal with old workspaces and those created by third party tooling that are missing view `order` property on views.
- structurizr-export: Fixes https://github.com/structurizr/java/issues/263 (C4PlantUMLExporter not following C4-PlantUML best practices with c4plantuml.tags true).

## 2.1.1 (3rd March 2024)

- structurizr-core: Fixes problem with ordering of relationship view vertices.

## 2.1.0 (2nd March 2024)

- structurizr-core: `ViewSet.isEmpty()` was missing a check for image views.
- structurizr-core: Promotes `ModelView.copyLayoutInformationFrom()` to be public, to allow individual view layout information to be merged.
- structurizr-client: Fixes https://github.com/structurizr/java/issues/257 (Serialization to JSON is not deterministic).
- structurizr-dsl: Fixes https://github.com/structurizr/java/issues/252 (DSL parser does not seem to handle curly brackets balance).
- structurizr-dsl: Deprecates `!constant`, adds `!const` and `!var` (see https://github.com/structurizr/java/issues/253).
- structurizr-export: Fixes https://github.com/structurizr/java/issues/258 (Plantuml renderer: Group and system of same name yields puml code resulting in error).

## 2.0.0 (22nd February 2024)

- structurizr-core: Removes deprecated concepts - enterprise and software system/person location.
- structurizr-core: Adds `Workspace.trim()` to trim a workspace of unused elements (i.e. those not associated with any views).
- structurizr-core: Adds support for SVG image views (https://github.com/structurizr/java/issues/249).
- structurizr-core: View keys will be automatically generated if not specified.
- structurizr-client: Removes `StructurizrClient` (use `WorkspaceApiClient` instead).
- structurizr-client: Merges https://github.com/structurizr/java/pull/238 (fix: re-enable system properties for theme http client).
- structurizr-dsl: Removes `enterprise` keyword.
- structurizr-dsl: Adds `!decisions` as a synonym for `!adrs`.
- structurizr-dsl: Allows `!identifiers` to be used inside `model`.
- structurizr-dsl: Fixes https://github.com/structurizr/java/issues/233 (Implied relationships not configured correctly when DSL workspace extends a JSON workspace).
- structurizr-import: Adds support for importing decisions managed by Log4brains.
- structurizr-import: Adds support for importing decisions in MADR format.
- structurizr-import: Fixes https://github.com/structurizr/java/issues/251 (Importing docs fails on files without extension).
- structurizr-inspection: Initial version.