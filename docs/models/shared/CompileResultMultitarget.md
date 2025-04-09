# CompileResultMultitarget

The partially evaluated result of the query, for each target dialect. Result will be empty if the query is never true.


## Fields

| Field                                                                                              | Type                                                                                               | Required                                                                                           | Description                                                                                        |
| -------------------------------------------------------------------------------------------------- | -------------------------------------------------------------------------------------------------- | -------------------------------------------------------------------------------------------------- | -------------------------------------------------------------------------------------------------- |
| `result`                                                                                           | [Optional\<CompileResultMultitargetResult>](../../models/shared/CompileResultMultitargetResult.md) | :heavy_minus_sign:                                                                                 | The partially evaluated result of the query in each target dialect.                                |