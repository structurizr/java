# WebSequenceDiagrams

The [WebSequenceDiagramExporter](WebSequenceDiagramsDiagram.java) class provides a way to export dynamic views to
diagram definitions that are compatible with [websequencediagrams.com](https://www.websequencediagrams.com).

## Example usage

You can either export all dynamic views in a workspace:

```
Workspace workspace = ...
WebSequenceDiagramsExporter exporter = new WebSequenceDiagramsExporter();
Collection<Diagram> diagrams = exporter.export(workspace);
```

Or just a single dynamic view:

```
Workspace workspace = ...
DynamicView view = ...
WebSequenceDiagramsExporter exporter = new WebSequenceDiagramsExporter();
Diagram diagram = exporter.export(view);
```