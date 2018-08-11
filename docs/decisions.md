# Decisions

Although architecture decisions can be included in supplementary documentation, Structurizr also provides support for publishing architecture decision records, as described by [as described by Michael Nygard](http://thinkrelevance.com/blog/2011/11/15/documenting-architecture-decisions).

Decision records can either created manually using the API on the [Documentation class](https://github.com/structurizr/java/blob/master/structurizr-core/src/com/structurizr/documentation/Documentation.java), or using the [AdrToolsImporter](https://github.com/structurizr/java/blob/master/structurizr-adr-tools/src/com/structurizr/documentation/AdrToolsImporter.java) to import ADRs from Nat Pryce's popular [adr-tools](https://github.com/npryce/adr-tools) tooling. Here is [an example](https://github.com/structurizr/java/blob/master/structurizr-examples/src/com/structurizr/example/AdrTools.java).

See [Structurizr - Decision Log](https://structurizr.com/help/decision-log) for more details.