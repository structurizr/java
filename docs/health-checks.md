# HTTP-based health checks

Structurizr's health checks feature allows you to supplement your deployment models with HTTP-based health checks to get an "at a glance" view of the health of your software systems. See [Structurizr - Health Checks](https://structurizr.com/help/health-checks) for more details.

When defining your software architecture model using the client library, HTTP-based health checks can be added to the Container Instances in your deployment model. Each health check is defined by a name, an endpoint URL, a polling interval (e.g. 60 seconds), a timeout (e.g. 1000 milliseconds), and optionally one or more HTTP headers.

[HttpHealthChecks.java](https://github.com/structurizr/java/blob/master/structurizr-examples/src/com/structurizr/example/HttpHealthChecks.java) shows an example of how to setup the health checks, and the result can be see online at [https://structurizr.com/share/39441/health](https://structurizr.com/share/39441/health).