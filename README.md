# OPA Java SDK

[![License](https://img.shields.io/badge/License-Apache_2.0-blue.svg)](https://opensource.org/licenses/Apache-2.0)

<!-- Start SDK Installation [installation] -->
## SDK Installation

### Getting started

Speakeasy offers [automatic publishing](https://www.speakeasyapi.dev/docs/advanced-setup/publish-sdks) of SDK artifacts via GitHub Actions. 

Once published to a repository (local or otherwise), the SDK artifact is used as below:

Gradle:
```groovy
implementation 'com.styra.opa:api:0.4.0'
```

Maven:
```xml
<dependency>
    <groupId>com.styra.opa</groupId>
    <artifactId>api</artifactId>
    <version>0.4.0</version>
</dependency>
```

### How to build
After cloning the git repository to your file system you can build the SDK artifact from source to the `build` directory by running `./gradlew build` on *nix systems or `gradlew.bat` on Windows systems.

If you wish to build from source and publish the SDK artifact to your local Maven repository (on your filesystem) then use the following command (after cloning the git repo locally):

On *nix:
```bash
./gradlew publishToMavenLocal -Pskip.signing
```
On Windows:
```bash
gradlew.bat publishToMavenLocal -Pskip.signing
```
<!-- End SDK Installation [installation] -->

<!-- Start SDK Example Usage [usage] -->
## SDK Example Usage

### Example

```java
package hello.world;

import com.styra.opa.models.operations.*;
import com.styra.opa.models.operations.ExecutePolicyWithInputRequest;
import com.styra.opa.models.operations.ExecutePolicyWithInputRequestBody;
import com.styra.opa.models.operations.ExecutePolicyWithInputResponse;
import com.styra.opa.models.shared.*;
import com.styra.opa.models.shared.Explain;
import com.styra.opa.models.shared.GzipAcceptEncoding;
import com.styra.opa.models.shared.GzipContentEncoding;
import com.styra.opa.opa;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.util.Optional;
import static java.util.Map.entry;

public class Application {

    public static void main(String[] args) {
        try {
            Opa sdk = Opa.builder()
                .build();

            ExecutePolicyWithInputRequest req = ExecutePolicyWithInputRequest.builder()
                .path("app/rbac")
                .requestBody(ExecutePolicyWithInputRequestBody.builder()
                        .input(Input.of(java.util.Map.ofEntries(
                                    entry("user", "alice"),
                                    entry("action", "read"),
                                    entry("object", "id123"),
                                    entry("type", "dog"))))
                        .build())
                .contentEncoding(GzipContentEncoding.GZIP)
                .acceptEncoding(GzipAcceptEncoding.GZIP)
                .pretty(false)
                .provenance(false)
                .explain(Explain.NOTES)
                .metrics(false)
                .instrument(false)
                .strictBuiltinErrors(false)
                .build();

            ExecutePolicyWithInputResponse res = sdk.executePolicyWithInput()
                .request(req)
                .call();

            if (res.successfulPolicyEvaluation().isPresent()) {
                // handle response
            }
        } catch (com.styra.opa.models.errors.SDKError e) {
            // handle exception
        } catch (Exception e) {
            // handle exception
        }
    }
}
```
<!-- End SDK Example Usage [usage] -->

<!-- Start Available Resources and Operations [operations] -->
## Available Resources and Operations

### [Opa SDK](docs/sdks/opa/README.md)

* [executePolicy](docs/sdks/opa/README.md#executepolicy) - Execute a policy
* [executePolicyWithInput](docs/sdks/opa/README.md#executepolicywithinput) - Execute a policy given an input
* [health](docs/sdks/opa/README.md#health) - Verify the server is operational
<!-- End Available Resources and Operations [operations] -->

<!-- Start Server Selection [server] -->
## Server Selection

### Select Server by Index

You can override the default server globally by passing a server index to the `serverIndex` builder method when initializing the SDK client instance. The selected server will then be used as the default on the operations that use it. This table lists the indexes associated with the available servers:

| # | Server | Variables |
| - | ------ | --------- |
| 0 | `http://localhost:8181` | None |

#### Example

```java
package hello.world;

import com.styra.opa.models.operations.*;
import com.styra.opa.models.operations.ExecutePolicyRequest;
import com.styra.opa.models.operations.ExecutePolicyResponse;
import com.styra.opa.models.shared.*;
import com.styra.opa.models.shared.Explain;
import com.styra.opa.models.shared.GzipAcceptEncoding;
import com.styra.opa.opa;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.util.Optional;
import static java.util.Map.entry;

public class Application {

    public static void main(String[] args) {
        try {
            Opa sdk = Opa.builder()
                .serverIndex(0)
                .build();

            ExecutePolicyRequest req = ExecutePolicyRequest.builder()
                .path("app/rbac")
                .acceptEncoding(GzipAcceptEncoding.GZIP)
                .pretty(false)
                .provenance(false)
                .explain(Explain.NOTES)
                .metrics(false)
                .instrument(false)
                .strictBuiltinErrors(false)
                .build();

            ExecutePolicyResponse res = sdk.executePolicy()
                .request(req)
                .call();

            if (res.successfulPolicyEvaluation().isPresent()) {
                // handle response
            }
        } catch (com.styra.opa.models.errors.SDKError e) {
            // handle exception
        } catch (Exception e) {
            // handle exception
        }
    }
}
```


### Override Server URL Per-Client

The default server can also be overridden globally by passing a URL to the `serverURL` builder method when initializing the SDK client instance. For example:
```java
package hello.world;

import com.styra.opa.models.operations.*;
import com.styra.opa.models.operations.ExecutePolicyRequest;
import com.styra.opa.models.operations.ExecutePolicyResponse;
import com.styra.opa.models.shared.*;
import com.styra.opa.models.shared.Explain;
import com.styra.opa.models.shared.GzipAcceptEncoding;
import com.styra.opa.opa;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.util.Optional;
import static java.util.Map.entry;

public class Application {

    public static void main(String[] args) {
        try {
            Opa sdk = Opa.builder()
                .serverURL("http://localhost:8181")
                .build();

            ExecutePolicyRequest req = ExecutePolicyRequest.builder()
                .path("app/rbac")
                .acceptEncoding(GzipAcceptEncoding.GZIP)
                .pretty(false)
                .provenance(false)
                .explain(Explain.NOTES)
                .metrics(false)
                .instrument(false)
                .strictBuiltinErrors(false)
                .build();

            ExecutePolicyResponse res = sdk.executePolicy()
                .request(req)
                .call();

            if (res.successfulPolicyEvaluation().isPresent()) {
                // handle response
            }
        } catch (com.styra.opa.models.errors.SDKError e) {
            // handle exception
        } catch (Exception e) {
            // handle exception
        }
    }
}
```
<!-- End Server Selection [server] -->

<!-- Start Error Handling [errors] -->
## Error Handling

Handling errors in this SDK should largely match your expectations.  All operations return a response object or raise an error.  If Error objects are specified in your OpenAPI Spec, the SDK will throw the appropriate Exception type.

| Error Object           | Status Code            | Content Type           |
| ---------------------- | ---------------------- | ---------------------- |
| models/errors/SDKError | 4xx-5xx                | */*                    |

### Example

```java
package hello.world;

import com.styra.opa.models.operations.*;
import com.styra.opa.models.operations.ExecutePolicyRequest;
import com.styra.opa.models.operations.ExecutePolicyResponse;
import com.styra.opa.models.shared.*;
import com.styra.opa.models.shared.Explain;
import com.styra.opa.models.shared.GzipAcceptEncoding;
import com.styra.opa.opa;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.util.Optional;
import static java.util.Map.entry;

public class Application {

    public static void main(String[] args) {
        try {
            Opa sdk = Opa.builder()
                .build();

            ExecutePolicyRequest req = ExecutePolicyRequest.builder()
                .path("app/rbac")
                .acceptEncoding(GzipAcceptEncoding.GZIP)
                .pretty(false)
                .provenance(false)
                .explain(Explain.NOTES)
                .metrics(false)
                .instrument(false)
                .strictBuiltinErrors(false)
                .build();

            ExecutePolicyResponse res = sdk.executePolicy()
                .request(req)
                .call();

            if (res.successfulPolicyEvaluation().isPresent()) {
                // handle response
            }
        } catch (com.styra.opa.models.errors.SDKError e) {
            // handle exception
        } catch (Exception e) {
            // handle exception
        }
    }
}
```
<!-- End Error Handling [errors] -->

<!-- Placeholder for Future Speakeasy SDK Sections -->

# Development

## Maturity

This SDK is in beta, and there may be breaking changes between versions without a major version update. Therefore, we recommend pinning usage
to a specific package version. This way, you can install the same version each time without breaking changes unless you are intentionally
looking for the latest version.

## Contributions

While we value open-source contributions to this SDK, this library is generated programmatically.
Feel free to open a PR or a Github issue as a proof of concept and we'll do our best to include it in a future release!

### SDK Created by [Speakeasy](https://docs.speakeasyapi.dev/docs/using-speakeasy/client-sdks)
