# OpaApiClient SDK

## Overview

Enterprise OPA documentation
<https://docs.styra.com/enterprise-opa>

### Available Operations

* [executeDefaultPolicyWithInput](#executedefaultpolicywithinput) - Execute the default decision  given an input
* [executePolicy](#executepolicy) - Execute a policy
* [executePolicyWithInput](#executepolicywithinput) - Execute a policy given an input
* [executeBatchPolicyWithInput](#executebatchpolicywithinput) - Execute a policy given a batch of inputs
* [compileQueryWithPartialEvaluation](#compilequerywithpartialevaluation) - Partially evaluate a query
* [health](#health) - Verify the server is operational

## executeDefaultPolicyWithInput

Execute the default decision  given an input

### Example Usage

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

### Parameters

| Parameter                                                                                                                                                                                                     | Type                                                                                                                                                                                                          | Required                                                                                                                                                                                                      | Description                                                                                                                                                                                                   |
| ------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------- | ------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------- | ------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------- | ------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------- |
| `pretty`                                                                                                                                                                                                      | *Optional\<Boolean>*                                                                                                                                                                                          | :heavy_minus_sign:                                                                                                                                                                                            | If parameter is `true`, response will formatted for humans.                                                                                                                                                   |
| `acceptEncoding`                                                                                                                                                                                              | [Optional\<GzipAcceptEncoding>](../../models/shared/GzipAcceptEncoding.md)                                                                                                                                    | :heavy_minus_sign:                                                                                                                                                                                            | Indicates the server should respond with a gzip encoded body. The server will send the compressed response only if its length is above `server.encoding.gzip.min_length` value. See the configuration section |
| `input`                                                                                                                                                                                                       | [Input](../../models/shared/Input.md)                                                                                                                                                                         | :heavy_check_mark:                                                                                                                                                                                            | The input document                                                                                                                                                                                            |

### Response

**[ExecuteDefaultPolicyWithInputResponse](../../models/operations/ExecuteDefaultPolicyWithInputResponse.md)**

### Errors

| Error Type                | Status Code               | Content Type              |
| ------------------------- | ------------------------- | ------------------------- |
| models/errors/ClientError | 400, 404                  | application/json          |
| models/errors/ServerError | 500                       | application/json          |
| models/errors/SDKError    | 4XX, 5XX                  | \*/\*                     |

## executePolicy

Execute a policy

### Example Usage

```java
package hello.world;

import com.styra.opa.openapi.OpaApiClient;
import com.styra.opa.openapi.models.errors.ClientError;
import com.styra.opa.openapi.models.errors.ServerError;
import com.styra.opa.openapi.models.operations.ExecutePolicyRequest;
import com.styra.opa.openapi.models.operations.ExecutePolicyResponse;
import java.lang.Exception;

public class Application {

    public static void main(String[] args) throws ClientError, ServerError, Exception {

        OpaApiClient sdk = OpaApiClient.builder()
            .build();

        ExecutePolicyRequest req = ExecutePolicyRequest.builder()
                .path("app/rbac")
                .build();

        ExecutePolicyResponse res = sdk.executePolicy()
                .request(req)
                .call();

        if (res.successfulPolicyResponse().isPresent()) {
            // handle response
        }
    }
}
```

### Parameters

| Parameter                                                               | Type                                                                    | Required                                                                | Description                                                             |
| ----------------------------------------------------------------------- | ----------------------------------------------------------------------- | ----------------------------------------------------------------------- | ----------------------------------------------------------------------- |
| `request`                                                               | [ExecutePolicyRequest](../../models/operations/ExecutePolicyRequest.md) | :heavy_check_mark:                                                      | The request object to use for the request.                              |

### Response

**[ExecutePolicyResponse](../../models/operations/ExecutePolicyResponse.md)**

### Errors

| Error Type                | Status Code               | Content Type              |
| ------------------------- | ------------------------- | ------------------------- |
| models/errors/ClientError | 400                       | application/json          |
| models/errors/ServerError | 500                       | application/json          |
| models/errors/SDKError    | 4XX, 5XX                  | \*/\*                     |

## executePolicyWithInput

Execute a policy given an input

### Example Usage

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

### Parameters

| Parameter                                                                                 | Type                                                                                      | Required                                                                                  | Description                                                                               |
| ----------------------------------------------------------------------------------------- | ----------------------------------------------------------------------------------------- | ----------------------------------------------------------------------------------------- | ----------------------------------------------------------------------------------------- |
| `request`                                                                                 | [ExecutePolicyWithInputRequest](../../models/operations/ExecutePolicyWithInputRequest.md) | :heavy_check_mark:                                                                        | The request object to use for the request.                                                |

### Response

**[ExecutePolicyWithInputResponse](../../models/operations/ExecutePolicyWithInputResponse.md)**

### Errors

| Error Type                | Status Code               | Content Type              |
| ------------------------- | ------------------------- | ------------------------- |
| models/errors/ClientError | 400                       | application/json          |
| models/errors/ServerError | 500                       | application/json          |
| models/errors/SDKError    | 4XX, 5XX                  | \*/\*                     |

## executeBatchPolicyWithInput

Execute a policy given a batch of inputs

### Example Usage

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

### Parameters

| Parameter                                                                                           | Type                                                                                                | Required                                                                                            | Description                                                                                         |
| --------------------------------------------------------------------------------------------------- | --------------------------------------------------------------------------------------------------- | --------------------------------------------------------------------------------------------------- | --------------------------------------------------------------------------------------------------- |
| `request`                                                                                           | [ExecuteBatchPolicyWithInputRequest](../../models/operations/ExecuteBatchPolicyWithInputRequest.md) | :heavy_check_mark:                                                                                  | The request object to use for the request.                                                          |

### Response

**[ExecuteBatchPolicyWithInputResponse](../../models/operations/ExecuteBatchPolicyWithInputResponse.md)**

### Errors

| Error Type                     | Status Code                    | Content Type                   |
| ------------------------------ | ------------------------------ | ------------------------------ |
| models/errors/ClientError      | 400                            | application/json               |
| models/errors/BatchServerError | 500                            | application/json               |
| models/errors/SDKError         | 4XX, 5XX                       | \*/\*                          |

## compileQueryWithPartialEvaluation

Partially evaluate a query

### Example Usage

```java
package hello.world;

import com.styra.opa.openapi.OpaApiClient;
import com.styra.opa.openapi.models.errors.ClientError;
import com.styra.opa.openapi.models.errors.ServerError;
import com.styra.opa.openapi.models.operations.*;
import com.styra.opa.openapi.models.shared.Input;
import java.lang.Exception;
import java.util.List;

public class Application {

    public static void main(String[] args) throws ClientError, ServerError, Exception {

        OpaApiClient sdk = OpaApiClient.builder()
            .build();

        CompileQueryWithPartialEvaluationRequest req = CompileQueryWithPartialEvaluationRequest.builder()
                .path("app/rbac")
                .requestBody(CompileQueryWithPartialEvaluationRequestApplicationJSONRequestBody.builder()
                    .input(Input.of(List.of()))
                    .build())
                .build();

        CompileQueryWithPartialEvaluationResponse res = sdk.compileQueryWithPartialEvaluation()
                .request(req)
                .call();

        if (res.compileResultJSON().isPresent()) {
            // handle response
        }
    }
}
```

### Parameters

| Parameter                                                                                                       | Type                                                                                                            | Required                                                                                                        | Description                                                                                                     |
| --------------------------------------------------------------------------------------------------------------- | --------------------------------------------------------------------------------------------------------------- | --------------------------------------------------------------------------------------------------------------- | --------------------------------------------------------------------------------------------------------------- |
| `request`                                                                                                       | [CompileQueryWithPartialEvaluationRequest](../../models/operations/CompileQueryWithPartialEvaluationRequest.md) | :heavy_check_mark:                                                                                              | The request object to use for the request.                                                                      |

### Response

**[CompileQueryWithPartialEvaluationResponse](../../models/operations/CompileQueryWithPartialEvaluationResponse.md)**

### Errors

| Error Type                | Status Code               | Content Type              |
| ------------------------- | ------------------------- | ------------------------- |
| models/errors/ClientError | 400                       | application/json          |
| models/errors/ServerError | 500                       | application/json          |
| models/errors/SDKError    | 4XX, 5XX                  | \*/\*                     |

## health

The health API endpoint executes a simple built-in policy query to verify that the server is operational. Optionally it can account for bundle activation as well (useful for “ready” checks at startup).

### Example Usage

```java
package hello.world;

import com.styra.opa.openapi.OpaApiClient;
import com.styra.opa.openapi.models.errors.UnhealthyServer;
import com.styra.opa.openapi.models.operations.HealthResponse;
import java.lang.Exception;

public class Application {

    public static void main(String[] args) throws UnhealthyServer, Exception {

        OpaApiClient sdk = OpaApiClient.builder()
            .build();

        HealthResponse res = sdk.health()
                .call();

        if (res.healthyServer().isPresent()) {
            // handle response
        }
    }
}
```

### Parameters

| Parameter                                                                                                                                                                                                                                                                     | Type                                                                                                                                                                                                                                                                          | Required                                                                                                                                                                                                                                                                      | Description                                                                                                                                                                                                                                                                   |
| ----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------- | ----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------- | ----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------- | ----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------- |
| `bundles`                                                                                                                                                                                                                                                                     | *Optional\<Boolean>*                                                                                                                                                                                                                                                          | :heavy_minus_sign:                                                                                                                                                                                                                                                            | Boolean parameter to account for bundle activation status in response. This includes any discovery bundles or bundles defined in the loaded discovery configuration.                                                                                                          |
| `plugins`                                                                                                                                                                                                                                                                     | *Optional\<Boolean>*                                                                                                                                                                                                                                                          | :heavy_minus_sign:                                                                                                                                                                                                                                                            | Boolean parameter to account for plugin status in response.                                                                                                                                                                                                                   |
| `excludePlugin`                                                                                                                                                                                                                                                               | List\<*String*>                                                                                                                                                                                                                                                               | :heavy_minus_sign:                                                                                                                                                                                                                                                            | String parameter to exclude a plugin from status checks. Can be added multiple times. Does nothing if plugins is not true. This parameter is useful for special use cases where a plugin depends on the server being fully initialized before it can fully initialize itself. |

### Response

**[HealthResponse](../../models/operations/HealthResponse.md)**

### Errors

| Error Type                    | Status Code                   | Content Type                  |
| ----------------------------- | ----------------------------- | ----------------------------- |
| models/errors/UnhealthyServer | 500                           | application/json              |
| models/errors/SDKError        | 4XX, 5XX                      | \*/\*                         |