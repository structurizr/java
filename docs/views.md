# Views

Once you've [added elements to a model](model.md), you can create one or more views to visualise parts of the model, which can subsequently be rendered as diagrams by a number of different tools.

Structurizr for Java supports all of the view types described in the [C4 model](https://c4model.com), and the Java classes implementing these views can be found in the [com.structurizr.view](https://github.com/structurizr/java/tree/master/structurizr-core/src/com/structurizr/view) package as follows:

* [SystemContextView](https://github.com/structurizr/java/blob/master/structurizr-core/src/com/structurizr/view/SystemContextView.java)
* [ContainerView](https://github.com/structurizr/java/blob/master/structurizr-core/src/com/structurizr/view/ContainerView.java)
* [ComponentView](https://github.com/structurizr/java/blob/master/structurizr-core/src/com/structurizr/view/ComponentView.java)
* [SystemLandscapeView](https://github.com/structurizr/java/blob/master/structurizr-core/src/com/structurizr/view/SystemLandscapeView.java)
* [DynamicView](https://github.com/structurizr/java/blob/master/structurizr-core/src/com/structurizr/view/DynamicView.java)
* [DeploymentView](https://github.com/structurizr/java/blob/master/structurizr-core/src/com/structurizr/view/DeploymentView.java)

## Creating views

All views are associated with a [ViewSet](https://github.com/structurizr/java/blob/master/structurizr-core/src/com/structurizr/view/ViewSet.java), which is created for you when you create a workspace.

```java
Workspace workspace = new Workspace("Getting Started", "This is a model of my software system.");
ViewSet views = workspace.getViews();
```

Use the various ```create*View``` methods on the ```ViewSet``` class to create views.

