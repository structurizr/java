# API client

This page provides a quick overview of how to use the API client.

## Configuration

The are two ways to configure the API client.

### 1. Programmatically

The easiest way to configure the API client is to provide values for the API key and API secret programmatically. Each workspace has its own API key and secret, the values for which can be found on [your dashboard](https://structurizr.com/dashboard).

```java
StructurizrClient structurizrClient = new StructurizrClient("key", "secret");
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

This allows you to get the content of a workspace.

```java
Workspace workspace = structurizrClient.getWorkspace(1234);
```

By default, a copy of the workspace (as JSON) is archived to the current working directory. You can modify this behaviour by calling ```setWorkspaceArchiveLocation```. A ```null``` value will disable archiving.

### 2. putWorkspace

This allows you to overwrite an existing workspace.

```java
structurizrClient.putWorkspace(1234, workspace);
```

### 3. mergeWorkspace

This is the same as ```putWorkspace``` except that any layout information (i.e. the location of boxes on diagrams) is preserved where possible (i.e. where diagram elements haven't been renamed).

```java
structurizrClient.mergeWorkspace(1234, workspace);
```

Under the covers, this operation calls ```getWorkspace``` followed by ```putWorkspace```. If the merge doesn't work as expected, you still have the previous version of the workspace (as JSON) in the archive location.