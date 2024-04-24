<!-- Start SDK Example Usage [usage] -->
```java
package hello.world;

import com.styra.opa.sdk.Opa;
import com.styra.opa.sdk.models.operations.*;
import com.styra.opa.sdk.models.operations.ExecutePolicyWithInputRequest;
import com.styra.opa.sdk.models.operations.ExecutePolicyWithInputRequestBody;
import com.styra.opa.sdk.models.operations.ExecutePolicyWithInputResponse;
import com.styra.opa.sdk.models.shared.*;
import com.styra.opa.sdk.models.shared.Explain;
import com.styra.opa.sdk.models.shared.GzipAcceptEncoding;
import com.styra.opa.sdk.models.shared.GzipContentEncoding;
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
        } catch (com.styra.opa.sdk.models.errors.ClientError e) {
            // handle exception
        } catch (com.styra.opa.sdk.models.errors.ServerError e) {
            // handle exception
        } catch (com.styra.opa.sdk.models.errors.SDKError e) {
            // handle exception
        } catch (Exception e) {
            // handle exception
        }
    }
}
```
<!-- End SDK Example Usage [usage] -->