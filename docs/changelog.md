# Changelog

## 1.15.0 (unreleased to Maven Central)

- Adds documentation section filenames into the model.
- Adds support for custom elements on dynamic views.

## 1.14.1 (15th August 2022)

- Enables `structurizr-core` to be used as a transitive dependency by consumers of `structurizr-client`.

## 1.14.0 (14th August 2022)

- Adds a helper method (`AbstractImpliedRelationshipsStrategy.createImpliedRelationship`) to create implied relationships, which can then be used by custom implementations.
- Provides a way to add specific relationships to dynamic views. 

## 1.13.0 (25th June 2022)

- Adds support for name/value properties on element and relationship styles.

## 1.12.2 (30th March 2022)

- Adds support for sorting views by the order in which they are created.

## 1.12.1 (2nd March 2022)

- Renamed `Decision.Link.type` to `Decision.Link.description`. 

## 1.12.0 (1st March 2022)

- Breaking API changes to how documentation and decisions are managed.
- Moved documentation importers/templates to [structurizr-documentation](https://github.com/structurizr/documentation).
- Moved examples to [structurizr-examples](https://github.com/structurizr/examples)
- Removal of deprecated `Model.addImplicitRelationships()` method.

## 1.11.0 (18th February 2022)

- Fixes #167 (ImpliedRelationship Strategy replication of URL and perspectives).
- Makes the `Decision.setContent()` method public, to allow pre-processing of content before workspace upload/rendering.

## 1.10.1 (1st February 2022)

- Makes the `Section.setContent()` method public, to allow pre-processing of content before workspace upload/rendering. 

## 1.10.0 (29th December 2021)

- Adds support for different relationship line styles (solid, dashed, dotted).
- Adds the ability to indicate that individual views should not merge layout from remotes. 
- Adds name/value properties to the view set configuration.

## 1.9.10 (26th November 2021)

- Promotes a couple of methods to be public; no functional changes.

## 1.9.9 (16th October 2021)

- Adds the implied relationships functionality for custom elements.
- "addDefaultElements" will now also add any connected custom elements.

## 1.9.8 (1st October 2021)

- Adds support for relationships from deployment nodes to infrastructure nodes.

## 1.9.7 (9th September 2021)

- Adds support for software system/container instances in multiple deployment groups.

## 1.9.6 (31st August 2021)

- Added validation logic to reject unsupported image data URIs.
- Fixes #166 (ContainerInstance/SoftwareSystemInstance and auto-generation of deployment diagram).

## 1.9.5 (7th June 2021)

- Provides a way to store view dimensions.

## 1.9.4 (22nd May 2021)

- Bug fixes to prevent parents and children to both be added to container/component views.

## 1.9.3 (11th May 2021)

- Added an `addTheme` method on `Configuration`.
- Removed the `addDefaultStyles` method on `Styles`.

## 1.9.2 (27th April 2021)

- Adds a `Diamond` shape.

## 1.9.1 (28th March 2021)

- Adds a `findTerminology` method on the `Terminology` class.
- `Styles.findElementStyle` better mirrors how the Structurizr web renderer deals with element styling.

## 1.9.0 (20th March 2021)

- Adds support for adding individual infrastructure nodes, software system instances, and container instances to a deployment view.
- Adds support for removing software system instances from deployment views.
- Improved support for themes (e.g. when exporting to PlantUML), which now works the same as described at [Structurizr - Themes](https://structurizr.com/help/themes).
- Adds support for "deployment groups", providing a way to scope how software system/container instance relationships are replicated when added to deployment nodes. __breaking change__

## 1.8.0 (20th February 2021)

- Adds support for custom elements and custom views (experimental).
- Bug fixes and improved workspace validation.

## 1.7.2 (2nd February 2021)

- Bug fixes.

## 1.7.1 (28th January 2021)

- Bug fixes.

## 1.7.0 (6th January 2021)

- Removes the dynamic view restrictions related to adding containers/components outside the scoped software system/container.
- Adds an "externalBoundariesVisible" property to DynamicView, so that external software system/container boundaries can be shown/hidden.
- Enhanced the rules relating to whether elements can be added to a view or not.
- Enhanced the logic to merge layout information of elements on views.

## 1.6.3 (30th November 2020)

- When adding a relationship to a dynamic view, the first relationship between the source and destination would be chosen, even if there are multiple relationships with different technologies. This release adds a way to indicate which relationship (based upon technology) should be chosen.
- Suppress description warnings for software system instances.

## 1.6.2 (10th October 2020)

- Resolves an issue with the AutomaticDocumentationTemplate, where images were being included as documentation content.

## 1.6.1 (27th September 2020)

- Added a "recursive" option to the AutomaticDocumentationTemplate, so that sub-directories can optionally be scanned too.

## 1.6.0 (18th September 2020)

- Changed the way that internal canonical element names are generated, to improve layout merging for deployment views.
- getParent() of SoftwareSystemInstance and ContainerInstance now returns the parent deployment node.
- Refactoring and bug fixes.

## 1.5.0 (4th August 2020)

- Fixes #151: linked relationship tags were not being taken into account when finding relationship styling.
- Fixes #153: Allow relationships in DynamicView to go both ways without two relationships between Elements in Model.
- Adds support for software system instances on deployment views (#150: how do I provide tech details for an external system to show in Deployment View?)
- The interaction style on relationships no longer defaults to Synchronous.
- Adds support for software system instances on deployment views.

## 1.4.8 (15th July 2020)

- Implied relationships now also copy the interaction style and tags.
- Fixes a serialisation problem with themes and styles.

## 1.4.7 (6th July 2020)

- Remove default stroke styling.

## 1.4.6 (6th July 2020)

- Adds a way to load styles from external themes.

## 1.4.5 (21st June 2020)

- Bug fixes.

## 1.4.4 (21st June 2020)

- Adds an "addDefaultElements()" method to the static/deployment views.
- Adds an "addDefaultStyles()" method to Styles.
- Adds a "createDefaultViews()" method to Views.

## 1.4.3 (19th June 2020)

- Fixes a bug where all deployment nodes would be added to a deployment view, even if that view had an environment set.
- Adds support for removing deployment nodes, infrastructure nodes, and container instances from deployment views.
- Fixes a bug where deployment node instances could set to a non-positive integer.

## 1.4.2 (18th June 2020)

- Adds the ability to add container instances and infrastructure nodes to the same animation step on a deployment view.
- Adds the ability to override the Structurizr client agent string. 

## 1.4.1 (14th June 2020)

- Fixes a bug that defaults the relationship interaction style to Synchronous, when it's specifically set to null.

## 1.4.0 (5th June 2020)

- Added a "Component" element shape.
- Added a "Dotted" element border style.
- Components from any container can now be added to a component view.
- Added an externalContainersBoundariesVisible property to ComponentView, to set whether container boundaries should be visible for "external" components (those outside the container in scope).
- Improved the support for creating [implied relationships](docs/implied-relationships.md).
- Added the ability to customize the symbols used when rendering metadata.
- Adds support for infrastructure nodes.
- Adds support for multiple themes.
- Adds support for curved relationship routing.

## 1.3.5 (26th March 2020)

- Added an externalSoftwareSystemBoundariesVisible property to ContainerView, to set whether software system boundaries should be visible for "external" containers (those outside the software system in scope).
- Added a 16:10 ratio paper size.

## 1.3.4 (29th February 2020)

- Split View.setAutomaticLayout(boolean) to enableAutomaticLayout() and disableAutomaticLayout() (__breaking change__).
- Added A1 and A0 paper sizes.
- Adds support for themes.
- Adds support for tags on deployment nodes.
- Adds support for animations on deployment views.
- Adds support for URLs on relationships.

## 1.3.3 (24th December 2019)

- Fixes a deserialization issue with component views.

## 1.3.2 (22nd November 2019)

- Added support for element stroke colours.

## 1.3.1 (29th October 2019)

- The automatic layout algorithm can now be configured on individual views.
- The structurizr-annotations library can now be more easily used with OSGi applications.
- Fixes a bug with the PlantUML and WebSequenceDiagram writers, where relationships were sorted incorrectly (alphabetically, rather than numerically).
- Fixes a bug that allows relationships to be created between parents and children.
- The way layout information is copied between different versions of a view is now configurable by setting a custom LayoutMergeStrategy on a per view basis.

## 1.3.0 (3rd March 2019)

- Added the ability to lock and unlock workspaces, to prevent concurrent updates.

## 1.2.0 (3rd January 2019)

- Fixes an issue with Java 11 and SSL handshaking.
- The terminology for relationships can now be customised.
- Added support for icons on element styles.
- Top-level deployment nodes can now be given an environment property, to represent which deployment environment they belong to (e.g. "Development", "Live", etc).
- Relationships can no longer be created between container instances (__breaking change__).
- When adding elements to views, you can now optionally specify whether relationships to/from that element are added.
- Provided a way to customize the sort order when displaying the list of views.

## 1.1.0 (8th November 2018)

- Added the ability to specify users who should have read-write or read-only workspace access, via the ```workspace.getConfiguration().addUser(username, role)``` method. 

## 1.0.0 (17th Oct 2018)

- Added name-value properties to relationships.
- Added the ability to define animations on the static structure diagrams.
- Removed support for colours in the corporate branding feature (__breaking change__).
- The PlantUML writer can now export sequence diagrams.

## 1.0.0-RC7

- HTTP-based health check interval and timeout can be specified via the factory method now (__breaking change__). Also added some documentation and an example.
- Added an ```endParallelSequence(boolean)``` method to the ```DynamicView``` class, which allows sequence numbering to continue.
- Fixed a bug where the software system associated with a SystemContextView could be removed from the view.
- Added support for architecture decision records.

## 1.0.0-RC6

- Component finders are no longer idempotent, and an exception will be thrown if the same component is discovered more than once (__breaking change__).
- Removed the "groups" property of documentation sections (__breaking change__).
- Added some new shapes: web browser, mobile device (portrait and landscape), and robot.
- Addition of @NonNull annotations (JSR 305: Annotations for Software Defect Detection).
- Added the ability to enable/disable the enterprise boundary on system landscape and system context views.
- Added the ability to customise the terminology used when rendering views.
- Added the ability to hide element metadata and/or descriptions.
- The Spring component finder now supports the @Endpoint annotation.
- Bug fixes and performance enhancements.