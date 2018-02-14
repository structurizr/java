# API client

The Structurizr for Java library includes a client for the [Structurizr web API](https://api.structurizr.com), which allows you to get and put workspaces using JSON over HTTPS. This page provides a quick overview of how to use the API client.

## Configuration

The are two ways to configure the API client.

### 1. Programmatically

The easiest way to configure the API client is to provide values for the API key and API secret programmatically. Each workspace has its own API key and secret, the values for which can be found on [your Structurizr dashboard](https://structurizr.com/dashboard).

```java
StructurizrClient structurizrClient = new StructurizrClient("key", "secret");
```

If you're using the [on-premises installation](https://structurizr.com/help/on-premises-ui), there is a three argument version of the constructor where you can also specify the API URL.

```java
StructurizrClient structurizrClient = new StructurizrClient("url", "key", "secret");
```

### 2. Properties file

If you would like to separate your API credentials from the code, you can configure the values in a Java properties file. This should be named ```structurizr.properties``` and located on the classpath.

```
structurizr.api.url=https://api.structurizr.com
structurizr.api.key=key
structurizr.api.secret=secret
```

The API client can then be constructed using the default, no args, constructor.

```java
StructurizrClient structurizrClient = new StructurizrClient();
```

## Usage

The following operations are available on the API client.

### 1. getWorkspace

This allows you to get the content of a remote workspace.

```java
Workspace workspace = structurizrClient.getWorkspace(1234);
```

By default, a copy of the workspace (as a JSON document) is archived to the current working directory. You can modify this behaviour by calling ```setWorkspaceArchiveLocation```. A ```null``` value will disable archiving.

### 2. putWorkspace

This allows you to overwrite an existing remote workspace. If the ```mergeFromRemote``` property (on the ```StructurizrClient``` instance) is set to ```true``` (this is the default), any layout information (i.e. the location of boxes on diagrams) is preserved where possible (i.e. where diagram elements haven't been renamed).

```java
structurizrClient.putWorkspace(1234, workspace);
```