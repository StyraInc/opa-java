<!-- Start SDK Example Usage [usage] -->
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