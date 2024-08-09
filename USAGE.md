<!-- Start SDK Example Usage [usage] -->
```java
package hello.world;

import com.styra.opa.openapi.OpaApiClient;
import com.styra.opa.openapi.models.errors.SDKError;
import com.styra.opa.openapi.models.operations.ExecuteDefaultPolicyWithInputResponse;
import com.styra.opa.openapi.models.shared.GzipAcceptEncoding;
import com.styra.opa.openapi.models.shared.Input;
import com.styra.opa.openapi.models.shared.Security;
import java.lang.Exception;

public class Application {

    public static void main(String[] args) throws Exception {
        try {
            OpaApiClient sdk = OpaApiClient.builder()
                .security(Security.builder()
                    .bearerAuth("<YOUR_BEARER_TOKEN_HERE>")
                    .build())
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
        } catch (SDKError e) {
            // handle exception
            throw e;
        } catch (Exception e) {
            // handle exception
            throw e;
        }

    }
}
```

```java
package hello.world;

import com.styra.opa.openapi.OpaApiClient;
import com.styra.opa.openapi.models.errors.SDKError;
import com.styra.opa.openapi.models.operations.ExecutePolicyWithInputRequest;
import com.styra.opa.openapi.models.operations.ExecutePolicyWithInputRequestBody;
import com.styra.opa.openapi.models.operations.ExecutePolicyWithInputResponse;
import com.styra.opa.openapi.models.shared.Input;
import com.styra.opa.openapi.models.shared.Security;
import java.lang.Exception;

public class Application {

    public static void main(String[] args) throws Exception {
        try {
            OpaApiClient sdk = OpaApiClient.builder()
                .security(Security.builder()
                    .bearerAuth("<YOUR_BEARER_TOKEN_HERE>")
                    .build())
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
        } catch (SDKError e) {
            // handle exception
            throw e;
        } catch (Exception e) {
            // handle exception
            throw e;
        }

    }
}
```

```java
package hello.world;

import com.styra.opa.openapi.OpaApiClient;
import com.styra.opa.openapi.models.errors.SDKError;
import com.styra.opa.openapi.models.operations.ExecuteBatchPolicyWithInputRequest;
import com.styra.opa.openapi.models.operations.ExecuteBatchPolicyWithInputRequestBody;
import com.styra.opa.openapi.models.operations.ExecuteBatchPolicyWithInputResponse;
import com.styra.opa.openapi.models.shared.Input;
import com.styra.opa.openapi.models.shared.Security;
import java.lang.Exception;
import java.util.Map;

public class Application {

    public static void main(String[] args) throws Exception {
        try {
            OpaApiClient sdk = OpaApiClient.builder()
                .security(Security.builder()
                    .bearerAuth("<YOUR_BEARER_TOKEN_HERE>")
                    .build())
                .build();

            ExecuteBatchPolicyWithInputRequest req = ExecuteBatchPolicyWithInputRequest.builder()
                .path("app/rbac")
                .requestBody(ExecuteBatchPolicyWithInputRequestBody.builder()
                        .inputs(Map.ofEntries(
                                Map.entry("key", Input.of("<value>"))))
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
        } catch (SDKError e) {
            // handle exception
            throw e;
        } catch (Exception e) {
            // handle exception
            throw e;
        }

    }
}
```
<!-- End SDK Example Usage [usage] -->