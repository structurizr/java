# Changelog

## 1.4.9 (unreleased)

- Fixes #151: linked relationship tags were not being taken into account when finding relationship styling.

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