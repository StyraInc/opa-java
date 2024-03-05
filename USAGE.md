<!-- Start SDK Example Usage [usage] -->
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