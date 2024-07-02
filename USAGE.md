<!-- Start SDK Example Usage [usage] -->
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
<!-- End SDK Example Usage [usage] -->