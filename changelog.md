# Changelog

## unreleased

- structurizr-core: `ViewSet.isEmpty()` was missing a check for image views.

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