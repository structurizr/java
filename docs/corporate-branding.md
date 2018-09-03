# Corporate branding

> Note: this page describes a feature that is not available to use with Structurizr's Free Plan.

In addition to [styling diagram elements](styling-elements.md) and [relationships](styling-relationships.md), some corporate branding can be added to diagrams and documentation. This includes:

- A font (font name and optional web font stylesheet URL).
- A logo (a URL to an image file or a data URI).

You can add branding to an existing workspace, as follows:

```java
Branding branding = views.getConfiguration().getBranding();
branding.setLogo(ImageUtils.getImageAsDataUri(new File("./docs/images/structurizr-logo.png")));
```

See [Help - Corporate Branding](https://structurizr.com/help/corporate-branding) for more details, [CorporateBranding.java](https://github.com/structurizr/java/blob/master/structurizr-examples/src/com/structurizr/example/CorporateBranding.java) for a full example, and [https://structurizr.com/share/35031](https://structurizr.com/share/35031) to access the workspace.
