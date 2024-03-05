# Opa SDK


## Overview

Enterprise OPA documentation
<https://docs.styra.com/enterprise-opa>
### Available Operations

* [executePolicy](#executepolicy) - Execute a policy
* [executePolicyWithInput](#executepolicywithinput) - Execute a policy given an input
* [health](#health) - Verify the server is operational

## executePolicy

Execute a policy

### Example Usage

```java
package hello.world;

import com.styra.opa.Opa;
import com.styra.opa.models.operations.ExecutePolicyRequest;
import com.styra.opa.models.operations.ExecutePolicyResponse;
import com.styra.opa.models.shared.Explain;
import com.styra.opa.models.shared.GzipAcceptEncoding;

public class Application {
    public static void main(String[] args) {
        try {
            Opa sdk = Opa.builder()            .build();

            com.styra.opa.models.operations.ExecutePolicyRequest req = new ExecutePolicyRequest(
                "<value>"){{
                acceptEncoding = GzipAcceptEncoding.GZIP;
                pretty = false;
                provenance = false;
                explain = Explain.NOTES;
                metrics = false;
                instrument = false;
                strictBuiltinErrors = false;

            }};

            com.styra.opa.models.operations.ExecutePolicyResponse res = sdk.executePolicy(req);

            if (res.successfulPolicyEvaluation != null) {
                // handle response
            }
        } catch (Exception e) {
            // handle exception
        }
    }
}
```

### Parameters

| Parameter                                                                                               | Type                                                                                                    | Required                                                                                                | Description                                                                                             |
| ------------------------------------------------------------------------------------------------------- | ------------------------------------------------------------------------------------------------------- | ------------------------------------------------------------------------------------------------------- | ------------------------------------------------------------------------------------------------------- |
| `request`                                                                                               | [com.styra.opa.models.operations.ExecutePolicyRequest](../../models/operations/ExecutePolicyRequest.md) | :heavy_check_mark:                                                                                      | The request object to use for the request.                                                              |


### Response

**[com.styra.opa.models.operations.ExecutePolicyResponse](../../models/operations/ExecutePolicyResponse.md)**


## executePolicyWithInput

Execute a policy given an input

### Example Usage

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

### Parameters

| Parameter                                                                                                                 | Type                                                                                                                      | Required                                                                                                                  | Description                                                                                                               |
| ------------------------------------------------------------------------------------------------------------------------- | ------------------------------------------------------------------------------------------------------------------------- | ------------------------------------------------------------------------------------------------------------------------- | ------------------------------------------------------------------------------------------------------------------------- |
| `request`                                                                                                                 | [com.styra.opa.models.operations.ExecutePolicyWithInputRequest](../../models/operations/ExecutePolicyWithInputRequest.md) | :heavy_check_mark:                                                                                                        | The request object to use for the request.                                                                                |


### Response

**[com.styra.opa.models.operations.ExecutePolicyWithInputResponse](../../models/operations/ExecutePolicyWithInputResponse.md)**


## health

The health API endpoint executes a simple built-in policy query to verify that the server is operational. Optionally it can account for bundle activation as well (useful for “ready” checks at startup).

### Example Usage

```java
package hello.world;

import com.styra.opa.Opa;
import com.styra.opa.models.operations.HealthRequest;
import com.styra.opa.models.operations.HealthResponse;

public class Application {
    public static void main(String[] args) {
        try {
            Opa sdk = Opa.builder()            .build();

            com.styra.opa.models.operations.HealthResponse res = sdk.health(false, false, new String[]{{
                add("<value>"),
            }});

            if (res.healthyServer != null) {
                // handle response
            }
        } catch (Exception e) {
            // handle exception
        }
    }
}
```

### Parameters

| Parameter                                                                                                                                                                                                                                                                     | Type                                                                                                                                                                                                                                                                          | Required                                                                                                                                                                                                                                                                      | Description                                                                                                                                                                                                                                                                   |
| ----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------- | ----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------- | ----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------- | ----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------- |
| `bundles`                                                                                                                                                                                                                                                                     | *Boolean*                                                                                                                                                                                                                                                                     | :heavy_minus_sign:                                                                                                                                                                                                                                                            | Boolean parameter to account for bundle activation status in response. This includes any discovery bundles or bundles defined in the loaded discovery configuration.                                                                                                          |
| `plugins`                                                                                                                                                                                                                                                                     | *Boolean*                                                                                                                                                                                                                                                                     | :heavy_minus_sign:                                                                                                                                                                                                                                                            | Boolean parameter to account for plugin status in response.                                                                                                                                                                                                                   |
| `excludePlugin`                                                                                                                                                                                                                                                               | List<*String*>                                                                                                                                                                                                                                                                | :heavy_minus_sign:                                                                                                                                                                                                                                                            | String parameter to exclude a plugin from status checks. Can be added multiple times. Does nothing if plugins is not true. This parameter is useful for special use cases where a plugin depends on the server being fully initialized before it can fully initialize itself. |


### Response

**[com.styra.opa.models.operations.HealthResponse](../../models/operations/HealthResponse.md)**

