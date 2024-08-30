# Changelog

## unreleased

- structurizr-core: Adds name-value properties to dynamic view relationship views (https://github.com/structurizr/java/issues/316).
- structurizr-component: Initial rewrite of the original `structurizr-analysis` library - provides a way to automatically find components in a Java codebase.
- structurizr-dsl: Adds name-value properties to dynamic view relationship views.
- structurizr-dsl: Fixes https://github.com/structurizr/java/issues/312 (!include doesn't work with files encoded as UTF-8 BOM).
- structurizr-dsl: Adds a way to explicitly specify the order of relationships in dynamic views.
- structurizr-dsl: Adds support for element technology expressions (e.g. "element.technology==Java").
- structurizr-dsl: Adds an `!elements` keyword that can be used to find a set of elements via an expression.
- structurizr-dsl: Adds a `!relationships` keyword that can be used to find a set of relationships via an expression.
- structurizr-dsl: Adds a DSL wrapper around the `structurizr-component` component finder.
- structurizr-dsl: Adds support for local theme files to be specified via `theme` (https://github.com/structurizr/java/issues/331).

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