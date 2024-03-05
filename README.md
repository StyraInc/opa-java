# openapi

<div align="left">
    <a href="https://speakeasyapi.dev/"><img src="https://custom-icon-badges.demolab.com/badge/-Built%20By%20Speakeasy-212015?style=for-the-badge&logoColor=FBE331&logo=speakeasy&labelColor=545454" /></a>
    <a href="https://opensource.org/licenses/MIT">
        <img src="https://img.shields.io/badge/License-MIT-blue.svg" style="width: 100px; height: 28px;" />
    </a>
</div>


## 🏗 **Welcome to your new SDK!** 🏗

It has been generated successfully based on your OpenAPI spec. However, it is not yet ready for production use. Here are some next steps:
- [ ] 🛠 Make your SDK feel handcrafted by [customizing it](https://www.speakeasyapi.dev/docs/customize-sdks)
- [ ] ♻️ Refine your SDK quickly by iterating locally with the [Speakeasy CLI](https://github.com/speakeasy-api/speakeasy)
- [ ] 🎁 Publish your SDK to package managers by [configuring automatic publishing](https://www.speakeasyapi.dev/docs/productionize-sdks/publish-sdks)
- [ ] ✨ When ready to productionize, delete this section from the README

<!-- Start SDK Installation [installation] -->
## SDK Installation

### Gradle

```groovy
implementation 'com.styra.opa:openapi:0.0.1'
```
<!-- End SDK Installation [installation] -->

<!-- Start SDK Example Usage [usage] -->
## SDK Example Usage

### Example

```java
package hello.world;

import com.styra.opa.Opa;
import com.styra.opa.models.operations.ExecutePolicyWithInputRequest;
import com.styra.opa.models.operations.ExecutePolicyWithInputRequestBody;
import com.styra.opa.models.operations.ExecutePolicyWithInputResponse;
import com.styra.opa.models.shared.Explain;
import com.styra.opa.models.shared.GzipAcceptEncoding;
import com.styra.opa.models.shared.GzipContentEncoding;

public class Application {
    public static void main(String[] args) {
        try {
            Opa sdk = Opa.builder()            .build();

            com.styra.opa.models.operations.ExecutePolicyWithInputRequest req = new ExecutePolicyWithInputRequest(
                "<value>",
                new ExecutePolicyWithInputRequestBody(
                    new java.util.HashMap<String, java.lang.Object>(
                    ){{
                        put("key", "<value>");
                    }})){{
                contentEncoding = GzipContentEncoding.GZIP;
                acceptEncoding = GzipAcceptEncoding.GZIP;
                pretty = false;
                provenance = false;
                explain = Explain.NOTES;
                metrics = false;
                instrument = false;
                strictBuiltinErrors = false;

            }};

            com.styra.opa.models.operations.ExecutePolicyWithInputResponse res = sdk.executePolicyWithInput(req);

            if (res.successfulPolicyEvaluation != null) {
                // handle response
            }
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

## Server Selection

### Select Server by Index

You can override the default server globally using the `setServerIndex` option when initializing the SDK client instance. The selected server will then be used as the default on the operations that use it. This table lists the indexes associated with the available servers:

| # | Server | Variables |
| - | ------ | --------- |
| 0 | `http://localhost:8181` | None |




### Override Server URL Per-Client

The default server can also be overridden globally using the `setServerURL` option when initializing the SDK client instance. For example:
<!-- End Server Selection [server] -->

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
