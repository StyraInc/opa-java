# ExecuteBatchPolicyWithInputResponse


## Fields

| Field                                                                                                                                                     | Type                                                                                                                                                      | Required                                                                                                                                                  | Description                                                                                                                                               |
| --------------------------------------------------------------------------------------------------------------------------------------------------------- | --------------------------------------------------------------------------------------------------------------------------------------------------------- | --------------------------------------------------------------------------------------------------------------------------------------------------------- | --------------------------------------------------------------------------------------------------------------------------------------------------------- |
| `contentType`                                                                                                                                             | *String*                                                                                                                                                  | :heavy_check_mark:                                                                                                                                        | HTTP response content type for this operation                                                                                                             |
| `statusCode`                                                                                                                                              | *int*                                                                                                                                                     | :heavy_check_mark:                                                                                                                                        | HTTP response status code for this operation                                                                                                              |
| `rawResponse`                                                                                                                                             | [HttpResponse<InputStream>](https://docs.oracle.com/en/java/javase/11/docs/api/java.net.http/java/net/http/HttpResponse.html)                             | :heavy_check_mark:                                                                                                                                        | Raw HTTP response; suitable for custom response parsing                                                                                                   |
| `batchSuccessfulPolicyEvaluation`                                                                                                                         | [Optional<BatchSuccessfulPolicyEvaluation>](../../models/shared/BatchSuccessfulPolicyEvaluation.md)                                                       | :heavy_minus_sign:                                                                                                                                        | All batched policy executions succeeded.<br/>The server also returns 200 if the path refers to an undefined document. In this case, responses will be empty.<br/> |
| `batchMixedResults`                                                                                                                                       | [Optional<BatchMixedResults>](../../models/shared/BatchMixedResults.md)                                                                                   | :heavy_minus_sign:                                                                                                                                        | Mixed success and failures.                                                                                                                               |
| `headers`                                                                                                                                                 | Map<String, List<*String*>>                                                                                                                               | :heavy_check_mark:                                                                                                                                        | N/A                                                                                                                                                       |