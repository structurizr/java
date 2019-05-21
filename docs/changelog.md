# Changelog

## 1.3.1 (unreleased)

- The automatic layout algorithm can now be configured on individual views.
- The structurizr-annotations library can now be more easily used with OSGi applications.

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