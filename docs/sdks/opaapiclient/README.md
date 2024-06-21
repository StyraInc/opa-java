# OpaApiClient SDK


## Overview

Enterprise OPA documentation
<https://docs.styra.com/enterprise-opa>
### Available Operations

* [executeDefaultPolicyWithInput](#executedefaultpolicywithinput) - Execute the default decision  given an input
* [executePolicy](#executepolicy) - Execute a policy
* [executePolicyWithInput](#executepolicywithinput) - Execute a policy given an input
* [executeBatchPolicyWithInput](#executebatchpolicywithinput) - Execute a policy given a batch of inputs
* [health](#health) - Verify the server is operational

## executeDefaultPolicyWithInput

Execute the default decision  given an input

### Example Usage

```java
package hello.world;

import com.styra.opa.openapi.OpaApiClient;
import com.styra.opa.openapi.models.operations.*;
import com.styra.opa.openapi.models.shared.*;
import com.styra.opa.openapi.utils.EventStream;
import java.math.BigDecimal;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.util.Optional;
import org.openapitools.jackson.nullable.JsonNullable;
import static java.util.Map.entry;

public class Application {

    public static void main(String[] args) throws Exception {
        try {
            OpaApiClient sdk = OpaApiClient.builder()
                .build();

            ExecuteDefaultPolicyWithInputResponse res = sdk.executeDefaultPolicyWithInput()
                .pretty(false)
                .acceptEncoding(GzipAcceptEncoding.GZIP)
                .input(Input.of(8203.11d))
                .call();

            if (res.result().isPresent()) {
                // handle response
            }
        } catch (com.styra.opa.openapi.models.errors.ClientError e) {
            // handle exception
            throw e;
        } catch (com.styra.opa.openapi.models.errors.ServerError e) {
            // handle exception
            throw e;
        } catch (com.styra.opa.openapi.models.errors.SDKError e) {
            // handle exception
            throw e;
        } catch (Exception e) {
            // handle exception
            throw e;
        }
    }
}
```

### Parameters

| Parameter                                                                                                                                                                                                     | Type                                                                                                                                                                                                          | Required                                                                                                                                                                                                      | Description                                                                                                                                                                                                   |
| ------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------- | ------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------- | ------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------- | ------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------- |
| `pretty`                                                                                                                                                                                                      | *Optional<? extends Boolean>*                                                                                                                                                                                 | :heavy_minus_sign:                                                                                                                                                                                            | If parameter is `true`, response will formatted for humans.                                                                                                                                                   |
| `acceptEncoding`                                                                                                                                                                                              | [Optional<? extends com.styra.opa.openapi.models.shared.GzipAcceptEncoding>](../../models/shared/GzipAcceptEncoding.md)                                                                                       | :heavy_minus_sign:                                                                                                                                                                                            | Indicates the server should respond with a gzip encoded body. The server will send the compressed response only if its length is above `server.encoding.gzip.min_length` value. See the configuration section |
| `input`                                                                                                                                                                                                       | [com.styra.opa.openapi.models.shared.Input](../../models/shared/Input.md)                                                                                                                                     | :heavy_check_mark:                                                                                                                                                                                            | The input document                                                                                                                                                                                            |


### Response

**[Optional<? extends com.styra.opa.openapi.models.operations.ExecuteDefaultPolicyWithInputResponse>](../../models/operations/ExecuteDefaultPolicyWithInputResponse.md)**
### Errors

| Error Object              | Status Code               | Content Type              |
| ------------------------- | ------------------------- | ------------------------- |
| models/errors/ClientError | 400,404                   | application/json          |
| models/errors/ServerError | 500                       | application/json          |
| models/errors/SDKError    | 4xx-5xx                   | */*                       |

## executePolicy

Execute a policy

### Example Usage

```java
package hello.world;

import com.styra.opa.openapi.OpaApiClient;
import com.styra.opa.openapi.models.operations.*;
import com.styra.opa.openapi.models.shared.*;
import com.styra.opa.openapi.utils.EventStream;
import java.math.BigDecimal;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.util.Optional;
import org.openapitools.jackson.nullable.JsonNullable;
import static java.util.Map.entry;

public class Application {

    public static void main(String[] args) throws Exception {
        try {
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
        } catch (com.styra.opa.openapi.models.errors.ClientError e) {
            // handle exception
            throw e;
        } catch (com.styra.opa.openapi.models.errors.ServerError e) {
            // handle exception
            throw e;
        } catch (com.styra.opa.openapi.models.errors.SDKError e) {
            // handle exception
            throw e;
        } catch (Exception e) {
            // handle exception
            throw e;
        }
    }
}
```

### Parameters

| Parameter                                                                                                       | Type                                                                                                            | Required                                                                                                        | Description                                                                                                     |
| --------------------------------------------------------------------------------------------------------------- | --------------------------------------------------------------------------------------------------------------- | --------------------------------------------------------------------------------------------------------------- | --------------------------------------------------------------------------------------------------------------- |
| `request`                                                                                                       | [com.styra.opa.openapi.models.operations.ExecutePolicyRequest](../../models/operations/ExecutePolicyRequest.md) | :heavy_check_mark:                                                                                              | The request object to use for the request.                                                                      |


### Response

**[Optional<? extends com.styra.opa.openapi.models.operations.ExecutePolicyResponse>](../../models/operations/ExecutePolicyResponse.md)**
### Errors

| Error Object              | Status Code               | Content Type              |
| ------------------------- | ------------------------- | ------------------------- |
| models/errors/ClientError | 400                       | application/json          |
| models/errors/ServerError | 500                       | application/json          |
| models/errors/SDKError    | 4xx-5xx                   | */*                       |

## executePolicyWithInput

Execute a policy given an input

### Example Usage

```java
package hello.world;

import com.styra.opa.openapi.OpaApiClient;
import com.styra.opa.openapi.models.operations.*;
import com.styra.opa.openapi.models.shared.*;
import com.styra.opa.openapi.utils.EventStream;
import java.math.BigDecimal;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.util.Optional;
import org.openapitools.jackson.nullable.JsonNullable;
import static java.util.Map.entry;

public class Application {

    public static void main(String[] args) throws Exception {
        try {
            OpaApiClient sdk = OpaApiClient.builder()
                .build();

            ExecutePolicyWithInputRequest req = ExecutePolicyWithInputRequest.builder()
                .path("app/rbac")
                .requestBody(ExecutePolicyWithInputRequestBody.builder()
                        .input(Input.of(false))
                        .build())
                .build();

            ExecutePolicyWithInputResponse res = sdk.executePolicyWithInput()
                .request(req)
                .call();

            if (res.successfulPolicyResponse().isPresent()) {
                // handle response
            }
        } catch (com.styra.opa.openapi.models.errors.ClientError e) {
            // handle exception
            throw e;
        } catch (com.styra.opa.openapi.models.errors.ServerError e) {
            // handle exception
            throw e;
        } catch (com.styra.opa.openapi.models.errors.SDKError e) {
            // handle exception
            throw e;
        } catch (Exception e) {
            // handle exception
            throw e;
        }
    }
}
```

### Parameters

| Parameter                                                                                                                         | Type                                                                                                                              | Required                                                                                                                          | Description                                                                                                                       |
| --------------------------------------------------------------------------------------------------------------------------------- | --------------------------------------------------------------------------------------------------------------------------------- | --------------------------------------------------------------------------------------------------------------------------------- | --------------------------------------------------------------------------------------------------------------------------------- |
| `request`                                                                                                                         | [com.styra.opa.openapi.models.operations.ExecutePolicyWithInputRequest](../../models/operations/ExecutePolicyWithInputRequest.md) | :heavy_check_mark:                                                                                                                | The request object to use for the request.                                                                                        |


### Response

**[Optional<? extends com.styra.opa.openapi.models.operations.ExecutePolicyWithInputResponse>](../../models/operations/ExecutePolicyWithInputResponse.md)**
### Errors

| Error Object              | Status Code               | Content Type              |
| ------------------------- | ------------------------- | ------------------------- |
| models/errors/ClientError | 400                       | application/json          |
| models/errors/ServerError | 500                       | application/json          |
| models/errors/SDKError    | 4xx-5xx                   | */*                       |

## executeBatchPolicyWithInput

Execute a policy given a batch of inputs

### Example Usage

```java
package hello.world;

import com.styra.opa.openapi.OpaApiClient;
import com.styra.opa.openapi.models.operations.*;
import com.styra.opa.openapi.models.shared.*;
import com.styra.opa.openapi.utils.EventStream;
import java.math.BigDecimal;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.util.Optional;
import org.openapitools.jackson.nullable.JsonNullable;
import static java.util.Map.entry;

public class Application {

    public static void main(String[] args) throws Exception {
        try {
            OpaApiClient sdk = OpaApiClient.builder()
                .build();

            ExecuteBatchPolicyWithInputRequest req = ExecuteBatchPolicyWithInputRequest.builder()
                .path("app/rbac")
                .requestBody(ExecuteBatchPolicyWithInputRequestBody.builder()
                        .inputs(java.util.Map.ofEntries(
                                entry("key", Input.of("<value>"))))
                        .build())
                .build();

            ExecuteBatchPolicyWithInputResponse res = sdk.executeBatchPolicyWithInput()
                .request(req)
                .call();

            if (res.batchSuccessfulPolicyEvaluation().isPresent()) {
                // handle response
            }
        } catch (com.styra.opa.openapi.models.errors.ClientError e) {
            // handle exception
            throw e;
        } catch (com.styra.opa.openapi.models.errors.BatchServerError e) {
            // handle exception
            throw e;
        } catch (com.styra.opa.openapi.models.errors.SDKError e) {
            // handle exception
            throw e;
        } catch (Exception e) {
            // handle exception
            throw e;
        }
    }
}
```

### Parameters

| Parameter                                                                                                                                   | Type                                                                                                                                        | Required                                                                                                                                    | Description                                                                                                                                 |
| ------------------------------------------------------------------------------------------------------------------------------------------- | ------------------------------------------------------------------------------------------------------------------------------------------- | ------------------------------------------------------------------------------------------------------------------------------------------- | ------------------------------------------------------------------------------------------------------------------------------------------- |
| `request`                                                                                                                                   | [com.styra.opa.openapi.models.operations.ExecuteBatchPolicyWithInputRequest](../../models/operations/ExecuteBatchPolicyWithInputRequest.md) | :heavy_check_mark:                                                                                                                          | The request object to use for the request.                                                                                                  |


### Response

**[Optional<? extends com.styra.opa.openapi.models.operations.ExecuteBatchPolicyWithInputResponse>](../../models/operations/ExecuteBatchPolicyWithInputResponse.md)**
### Errors

| Error Object                   | Status Code                    | Content Type                   |
| ------------------------------ | ------------------------------ | ------------------------------ |
| models/errors/ClientError      | 400                            | application/json               |
| models/errors/BatchServerError | 500                            | application/json               |
| models/errors/SDKError         | 4xx-5xx                        | */*                            |

## health

The health API endpoint executes a simple built-in policy query to verify that the server is operational. Optionally it can account for bundle activation as well (useful for “ready” checks at startup).

### Example Usage

```java
package hello.world;

import com.styra.opa.openapi.OpaApiClient;
import com.styra.opa.openapi.models.operations.*;
import com.styra.opa.openapi.models.shared.*;
import com.styra.opa.openapi.utils.EventStream;
import java.math.BigDecimal;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.util.Optional;
import org.openapitools.jackson.nullable.JsonNullable;
import static java.util.Map.entry;

public class Application {

    public static void main(String[] args) throws Exception {
        try {
            OpaApiClient sdk = OpaApiClient.builder()
                .build();

            HealthResponse res = sdk.health()
                .bundles(false)
                .plugins(false)
                .excludePlugin(java.util.List.of(
                    "<value>"))
                .call();

            if (res.healthyServer().isPresent()) {
                // handle response
            }
        } catch (com.styra.opa.openapi.models.errors.UnhealthyServer e) {
            // handle exception
            throw e;
        } catch (com.styra.opa.openapi.models.errors.SDKError e) {
            // handle exception
            throw e;
        } catch (Exception e) {
            // handle exception
            throw e;
        }
    }
}
```

### Parameters

| Parameter                                                                                                                                                                                                                                                                     | Type                                                                                                                                                                                                                                                                          | Required                                                                                                                                                                                                                                                                      | Description                                                                                                                                                                                                                                                                   |
| ----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------- | ----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------- | ----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------- | ----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------- |
| `bundles`                                                                                                                                                                                                                                                                     | *Optional<? extends Boolean>*                                                                                                                                                                                                                                                 | :heavy_minus_sign:                                                                                                                                                                                                                                                            | Boolean parameter to account for bundle activation status in response. This includes any discovery bundles or bundles defined in the loaded discovery configuration.                                                                                                          |
| `plugins`                                                                                                                                                                                                                                                                     | *Optional<? extends Boolean>*                                                                                                                                                                                                                                                 | :heavy_minus_sign:                                                                                                                                                                                                                                                            | Boolean parameter to account for plugin status in response.                                                                                                                                                                                                                   |
| `excludePlugin`                                                                                                                                                                                                                                                               | List<*String*>                                                                                                                                                                                                                                                                | :heavy_minus_sign:                                                                                                                                                                                                                                                            | String parameter to exclude a plugin from status checks. Can be added multiple times. Does nothing if plugins is not true. This parameter is useful for special use cases where a plugin depends on the server being fully initialized before it can fully initialize itself. |


### Response

**[Optional<? extends com.styra.opa.openapi.models.operations.HealthResponse>](../../models/operations/HealthResponse.md)**
### Errors

| Error Object                  | Status Code                   | Content Type                  |
| ----------------------------- | ----------------------------- | ----------------------------- |
| models/errors/UnhealthyServer | 500                           | application/json              |
| models/errors/SDKError        | 4xx-5xx                       | */*                           |
