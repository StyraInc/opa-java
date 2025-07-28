# How to write integration tests with the OPA Java SDK

## Using Testcontainers

When writing integration tests for code that utilizes the SDK, you will likely need some way to have an OPA running, so that the `OPAClient` has an OPA server to communicate with. One good way to do this is with [Testcontainers for Java](https://java.testcontainers.org/), which lets you easily manage short-lived Docker containers tied to the lifecycle of your unit tests. This example demonstrates a simple Testcontainers setup to allow the SDK to be used during unit testing.

Aside from the SDK and JUnit Jupiter, you also need to add the following dependencies to your project:

- [`org.testcontainers/testcontainers-bom`](https://central.sonatype.com/artifact/org.testcontainers/testcontainers-bom)
- [`org.testcontainers/testcontainers`](https://central.sonatype.com/artifact/org.testcontainers/testcontainers)
- [`org.testcontainers/junit-jupiter`](https://central.sonatype.com/artifact/org.testcontainers/junit-jupiter)

You will also need to create a folder for your policies to be stored in, for example `./testdata/policy`. This folder will be used with `opa run --bundle`, and should be structured appropriately.

It is also necessary to prepare the `Dockerfile` and entry point script for the container to use:

`./src/test/resources/opa.Dockerfile`:

```dockerfile
FROM alpine:latest

ADD entrypoint.sh /entrypoint.sh

RUN chmod +x /entrypoint.sh

COPY --from=openpolicyagent/opa:latest-static /opa /usr/bin/opa

ENTRYPOINT ["/entrypoint.sh"]
```

`./src/test/resources/entrypoint.sh`:

```bash
#!/bin/sh

set -e
set -u
set -x

opa $@
```

> [!TIP]
> It is possible to simply use the `openpolicyagent/opa` image directly rather than providing a custom `Dockerfile` and entry point script, however doing it this way provides a clear cut point for later customizing the `Dockerfile` or adding more complex logic to the entry point script. For example, the [OPA Java SDK's unit tests](https://github.com/StyraInc/opa-java/tree/main/src/test) also include nginx acting as a reverse proxy.

All that now remains is updating the Java code for the unit tests to utilize Testcontainers.

`src/test/java/com/example/DemoTest.java`:

```java
package com.example;

// ...
import com.styra.opa.OPAClient;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.testcontainers.containers.BindMode;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.images.builder.ImageFromDockerfile;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
// ...

// ...
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Map;
// ...

@Testcontainers
class DemoTest {

    private int opaPort = 8181;

    private String opaAddress;

    // Creates a container from our Dockerfile and entry point script, which
    // we expect to expose opaPort. The test policy folder we created earlier
    // will be bind mounted to /policy, and we have OPA run with that folder as
    // a bundle.
    @Container
    public GenericContainer<?> opaContainer = new GenericContainer<>(
        new ImageFromDockerfile()
                // .withFileFromClasspath(path_in_build_context, path_in_resources_dir)
                .withFileFromClasspath("Dockerfile", "opa.Dockerfile")
                .withFileFromClasspath("entrypoint.sh", "entrypoint.sh")
        )
        .withExposedPorts(opaPort)
        .withFileSystemBind("./testdata/policy", "/policy", BindMode.READ_ONLY)
        .withCommand("run -s --bundle /policy");

    // Testcontainers avoids collisions by randomizing the port exposed to the
    // host, so we need to capture it so we know what URL to give the SDK.
    @BeforeEach
    public void setUp() {
        address = "http://" + opaContainer.getHost() + ":" + opaContainer.getMappedPort(opaPort);
    }

    // Uncomment to see the container logs after each unit tests runs.
    //
    // @AfterEach
    // public void dumpLogs() {
    //    System.out.println("==== container logs from OPA container ====");
    //    final String logs = opaContainer.getLogs();
    //    System.out.println(logs);
    // }

    @Test
    public void testExample() {
        OPAClient opa = new OPAClient(address);

        // ... the rest of your unit test that uses the OPAClient ...
    }

    // ...

}
```

Some working examples of this test strategy:

- [The OPA Java SDK's tests](https://github.com/StyraInc/opa-java/blob/main/src/test/java/com/styra/opa/OPATest.java).
- [The OPA Spring Boot SDK's tests](https://github.com/StyraInc/opa-springboot/blob/main/src/test/java/com/styra/opa/springboot/OPAAuthorizationManagerTest.java).

> [!TIP]
> You could also choose to make the `OPAClient` a private field of the test class (`DemoTest` in this example), and place `opa = new OPAClient(address)` in the `setUp()` function. Which approach is better is a matter of style and preference.
