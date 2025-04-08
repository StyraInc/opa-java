# OPA Java SDK

[![License](https://img.shields.io/badge/License-Apache_2.0-blue.svg)](https://opensource.org/licenses/Apache-2.0)
[![Maven Central Version](https://img.shields.io/maven-central/v/com.styra/opa?label=Maven%20Central&logo=apache-maven&color=%2324b6e0)](https://central.sonatype.com/artifact/com.styra/opa)

> [!IMPORTANT]
> The documentation for this SDK lives at https://docs.styra.com/sdk, with reference documentation available at https://styrainc.github.io/opa-java/javadoc, and development documentation at https://styrainc.github.io/opa-java/

You can use the Styra OPA SDK to connect to [Open Policy Agent](https://www.openpolicyagent.org/) and [Enterprise OPA](https://www.styra.com/enterprise-opa/) deployments.

## SDK Installation

This package is published on Maven Central as [`com.styra/opa`](https://central.sonatype.com/artifact/com.styra/opa/overview). The Maven Central page includes up-to-date instructions to add it as a dependency to your Java project, tailored for a variety of build systems including Maven and Gradle.

If you wish to build from source and publish the SDK artifact to your local Maven repository (on your filesystem) then use the following command (after cloning the git repo locally):

On Linux/MacOS:
```bash
./gradlew publishToMavenLocal -Pskip.signing
```

On Windows:
```bash
gradlew.bat publishToMavenLocal -Pskip.signing
```

## SDK Example Usage (high-level)

```java
package org.example;

import com.styra.opa.OPAClient;
import com.styra.opa.OPAException;

import java.util.Map;
import java.util.List;

import static java.util.Map.entry;

public class App {
    public static void main(String[] args) throws Exception {
        // Create an OPA instance, this handles any state needed for interacting
        // with OPA, and can be re-used for multiple requests if needed.
        String opaURL = "http://localhost:8181";
        OPAClient opa = new OPAClient(opaURL);

        // This will be the input to our policy.
        java.util.Map<String,Object> input = java.util.Map.ofEntries(
            entry("subject", "alice"),
            entry("action", "read"),
            entry("resource", "/finance/reports/fy2038_budget.csv")
        );

        // We will read the list of policy violations, and whether the request
        // is allowed or not into these.
        java.util.List<Object> violations;
        boolean allowed;

        // Perform the request against OPA.
        try {
            allowed = opa.check("policy/allow", input);
            violations = opa.evaluate("policy/violations", input);
        } catch (OPAException e ) {
            // Note that OPAException usually wraps other exception types, in
            // case you need to do more complex error handling.
            System.out.println("exception while making request against OPA: " + e);
            throw e; // crash the program
        }

        System.out.println("allowed: " + allowed);
        System.out.println("violations: " + violations);
    }
}
```

> [!NOTE]
> For low-level SDK usage, see the sections below.

---

<!-- No SDK Installation [installation] -->

# OPA OpenApi SDK (low-level)

<!-- Start Summary [summary] -->
## Summary

For more information about the API: [Enterprise OPA documentation](https://docs.styra.com/enterprise-opa)
<!-- End Summary [summary] -->

<!-- Start Table of Contents [toc] -->
## Table of Contents
<!-- $toc-max-depth=2 -->
* [OPA Java SDK](#opa-java-sdk)
  * [SDK Installation](#sdk-installation)
  * [SDK Example Usage (high-level)](#sdk-example-usage-high-level)
* [OPA OpenApi SDK (low-level)](#opa-openapi-sdk-low-level)
  * [SDK Example Usage](#sdk-example-usage)
  * [Available Resources and Operations](#available-resources-and-operations)
  * [Server Selection](#server-selection)
  * [Error Handling](#error-handling)
  * [Authentication](#authentication)
  * [Development](#development)
  * [Community](#community)

<!-- End Table of Contents [toc] -->

<!-- Start SDK Example Usage [usage] -->
## SDK Example Usage

### Example 1

```java
package hello.world;

import com.styra.opa.openapi.OpaApiClient;
import com.styra.opa.openapi.models.errors.ClientError;
import com.styra.opa.openapi.models.errors.ServerError;
import com.styra.opa.openapi.models.operations.ExecuteDefaultPolicyWithInputResponse;
import com.styra.opa.openapi.models.shared.Input;
import java.lang.Exception;

public class Application {

    public static void main(String[] args) throws ClientError, ServerError, Exception {

        OpaApiClient sdk = OpaApiClient.builder()
            .build();

        ExecuteDefaultPolicyWithInputResponse res = sdk.executeDefaultPolicyWithInput()
                .input(Input.of(4963.69))
                .call();

        if (res.result().isPresent()) {
            // handle response
        }
    }
}
```

### Example 2

```java
package hello.world;

import com.styra.opa.openapi.OpaApiClient;
import com.styra.opa.openapi.models.errors.ClientError;
import com.styra.opa.openapi.models.errors.ServerError;
import com.styra.opa.openapi.models.operations.*;
import com.styra.opa.openapi.models.shared.Input;
import java.lang.Exception;

public class Application {

    public static void main(String[] args) throws ClientError, ServerError, Exception {

        OpaApiClient sdk = OpaApiClient.builder()
            .build();

        ExecutePolicyWithInputRequest req = ExecutePolicyWithInputRequest.builder()
                .path("app/rbac")
                .requestBody(ExecutePolicyWithInputRequestBody.builder()
                    .input(Input.of(true))
                    .build())
                .build();

        ExecutePolicyWithInputResponse res = sdk.executePolicyWithInput()
                .request(req)
                .call();

        if (res.successfulPolicyResponse().isPresent()) {
            // handle response
        }
    }
}
```

### Example 3

```java
package hello.world;

import com.styra.opa.openapi.OpaApiClient;
import com.styra.opa.openapi.models.errors.BatchServerError;
import com.styra.opa.openapi.models.errors.ClientError;
import com.styra.opa.openapi.models.operations.*;
import com.styra.opa.openapi.models.shared.Input;
import java.lang.Exception;
import java.util.Map;

public class Application {

    public static void main(String[] args) throws ClientError, BatchServerError, Exception {

        OpaApiClient sdk = OpaApiClient.builder()
            .build();

        ExecuteBatchPolicyWithInputRequest req = ExecuteBatchPolicyWithInputRequest.builder()
                .path("app/rbac")
                .requestBody(ExecuteBatchPolicyWithInputRequestBody.builder()
                    .inputs(Map.ofEntries(
                        Map.entry("key", Input.of(6919.52))))
                    .build())
                .build();

        ExecuteBatchPolicyWithInputResponse res = sdk.executeBatchPolicyWithInput()
                .request(req)
                .call();

        if (res.batchSuccessfulPolicyEvaluation().isPresent()) {
            // handle response
        }
    }
}
```
<!-- End SDK Example Usage [usage] -->

<!-- Start Available Resources and Operations [operations] -->
## Available Resources and Operations

<details open>
<summary>Available methods</summary>

### [OpaApiClient SDK](docs/sdks/opaapiclient/README.md)

* [executeDefaultPolicyWithInput](docs/sdks/opaapiclient/README.md#executedefaultpolicywithinput) - Execute the default decision  given an input
* [executePolicy](docs/sdks/opaapiclient/README.md#executepolicy) - Execute a policy
* [executePolicyWithInput](docs/sdks/opaapiclient/README.md#executepolicywithinput) - Execute a policy given an input
* [executeBatchPolicyWithInput](docs/sdks/opaapiclient/README.md#executebatchpolicywithinput) - Execute a policy given a batch of inputs
* [compileQueryWithPartialEvaluation](docs/sdks/opaapiclient/README.md#compilequerywithpartialevaluation) - Partially evaluate a query
* [health](docs/sdks/opaapiclient/README.md#health) - Verify the server is operational

</details>
<!-- End Available Resources and Operations [operations] -->

<!-- Start Server Selection [server] -->
## Server Selection

### Override Server URL Per-Client

The default server can be overridden globally using the `.serverURL(String serverUrl)` builder method when initializing the SDK client instance. For example:
```java
package hello.world;

import com.styra.opa.openapi.OpaApiClient;
import com.styra.opa.openapi.models.errors.ClientError;
import com.styra.opa.openapi.models.errors.ServerError;
import com.styra.opa.openapi.models.operations.ExecuteDefaultPolicyWithInputResponse;
import com.styra.opa.openapi.models.shared.Input;
import java.lang.Exception;

public class Application {

    public static void main(String[] args) throws ClientError, ServerError, Exception {

        OpaApiClient sdk = OpaApiClient.builder()
                .serverURL("http://localhost:8181")
            .build();

        ExecuteDefaultPolicyWithInputResponse res = sdk.executeDefaultPolicyWithInput()
                .input(Input.of(4963.69))
                .call();

        if (res.result().isPresent()) {
            // handle response
        }
    }
}
```
<!-- End Server Selection [server] -->

<!-- Start Error Handling [errors] -->
## Error Handling

Handling errors in this SDK should largely match your expectations. All operations return a response object or raise an exception.

By default, an API error will throw a `models/errors/SDKError` exception. When custom error responses are specified for an operation, the SDK may also throw their associated exception. You can refer to respective *Errors* tables in SDK docs for more details on possible exception types for each operation. For example, the `executeDefaultPolicyWithInput` method throws the following exceptions:

| Error Type                | Status Code | Content Type     |
| ------------------------- | ----------- | ---------------- |
| models/errors/ClientError | 400, 404    | application/json |
| models/errors/ServerError | 500         | application/json |
| models/errors/SDKError    | 4XX, 5XX    | \*/\*            |

### Example

```java
package hello.world;

import com.styra.opa.openapi.OpaApiClient;
import com.styra.opa.openapi.models.errors.ClientError;
import com.styra.opa.openapi.models.errors.ServerError;
import com.styra.opa.openapi.models.operations.ExecuteDefaultPolicyWithInputResponse;
import com.styra.opa.openapi.models.shared.Input;
import java.lang.Exception;

public class Application {

    public static void main(String[] args) throws ClientError, ServerError, Exception {

        OpaApiClient sdk = OpaApiClient.builder()
            .build();

        ExecuteDefaultPolicyWithInputResponse res = sdk.executeDefaultPolicyWithInput()
                .input(Input.of(4963.69))
                .call();

        if (res.result().isPresent()) {
            // handle response
        }
    }
}
```
<!-- End Error Handling [errors] -->

<!-- Start Authentication [security] -->
## Authentication

### Per-Client Security Schemes

This SDK supports the following security scheme globally:

| Name         | Type | Scheme      |
| ------------ | ---- | ----------- |
| `bearerAuth` | http | HTTP Bearer |

You can set the security parameters through the `security` builder method when initializing the SDK client instance. For example:
```java
package hello.world;

import com.styra.opa.openapi.OpaApiClient;
import com.styra.opa.openapi.models.errors.ClientError;
import com.styra.opa.openapi.models.errors.ServerError;
import com.styra.opa.openapi.models.operations.ExecuteDefaultPolicyWithInputResponse;
import com.styra.opa.openapi.models.shared.Input;
import com.styra.opa.openapi.models.shared.Security;
import java.lang.Exception;

public class Application {

    public static void main(String[] args) throws ClientError, ServerError, Exception {

        OpaApiClient sdk = OpaApiClient.builder()
                .security(Security.builder()
                    .bearerAuth("<YOUR_BEARER_TOKEN_HERE>")
                    .build())
            .build();

        ExecuteDefaultPolicyWithInputResponse res = sdk.executeDefaultPolicyWithInput()
                .input(Input.of(4963.69))
                .call();

        if (res.result().isPresent()) {
            // handle response
        }
    }
}
```
<!-- End Authentication [security] -->

<!-- Placeholder for Future Speakeasy SDK Sections -->

## Development

This repository includes components generated by [Speakeasy](https://www.speakeasyapi.dev/) based on [this OpenAPI spec](https://github.com/StyraInc/enterprise-opa/tree/main/openapi), as well as human authored code that simplifies usage. The Speakeasy generated code resides in the [com.styra.opa.sdk](https://styrainc.github.io/opa-java/javadoc/com/styra/opa/sdk/package-summary.html) package and offers the greatest level of control, but is more verbose and complicated. The hand written [com.styra.opa](https://styrainc.github.io/opa-java/javadoc/com/styra/opa/package-summary.html) package offers a simplified API designed to make using the OPA REST API straightforward for common use cases.

### Build Instructions

**To build the SDK**, use `./gradlew build`, the resulting JAR will be placed in `./build/libs/api.jar`.

**To build the documentation** site, including JavaDoc, run `./scripts/build_docs.sh OUTPUT_DIR`. You should replace `OUTPUT_DIR` with a directory on your local system where you would like the generated docs to be placed. You can also preview the documentation site ephemerally using `./scripts/serve_docs.sh`, which will serve the docs on `http://localhost:8000` until you use Ctrl+C to exit the script.

**To run the unit tests**, you can use `./gradlew test`.

**To run the linter**, you can use `./gradlew lint`

## Community

For questions, discussions and announcements related to Styra products, services and open source projects, please join
the Styra community on [Slack](https://communityinviter.com/apps/styracommunity/signup)!
