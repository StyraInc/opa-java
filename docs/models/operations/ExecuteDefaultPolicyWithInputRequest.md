# ExecuteDefaultPolicyWithInputRequest


## Fields

| Field                                                                                                                                                                                                         | Type                                                                                                                                                                                                          | Required                                                                                                                                                                                                      | Description                                                                                                                                                                                                   |
| ------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------- | ------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------- | ------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------- | ------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------- |
| `pretty`                                                                                                                                                                                                      | *Optional<? extends Boolean>*                                                                                                                                                                                 | :heavy_minus_sign:                                                                                                                                                                                            | If parameter is `true`, response will formatted for humans.                                                                                                                                                   |
| `acceptEncoding`                                                                                                                                                                                              | [Optional<? extends com.styra.opa.openapi.models.shared.GzipAcceptEncoding>](../../models/shared/GzipAcceptEncoding.md)                                                                                       | :heavy_minus_sign:                                                                                                                                                                                            | Indicates the server should respond with a gzip encoded body. The server will send the compressed response only if its length is above `server.encoding.gzip.min_length` value. See the configuration section |
| `input`                                                                                                                                                                                                       | [com.styra.opa.openapi.models.shared.Input](../../models/shared/Input.md)                                                                                                                                     | :heavy_check_mark:                                                                                                                                                                                            | The input document                                                                                                                                                                                            |