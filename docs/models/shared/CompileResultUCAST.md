# CompileResultUCAST

The partially evaluated result of the query, in UCAST format. Result will be empty if the query is never true.


## Fields

| Field                                                                                  | Type                                                                                   | Required                                                                               | Description                                                                            |
| -------------------------------------------------------------------------------------- | -------------------------------------------------------------------------------------- | -------------------------------------------------------------------------------------- | -------------------------------------------------------------------------------------- |
| `result`                                                                               | [Optional\<CompileResultUCASTResult>](../../models/shared/CompileResultUCASTResult.md) | :heavy_minus_sign:                                                                     | The partially evaluated result of the query as UCAST.                                  |