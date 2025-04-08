# CompileResultSQL

The partially evaluated result of the query, in SQL format. Result will be empty if the query is never true.


## Fields

| Field                                                                              | Type                                                                               | Required                                                                           | Description                                                                        |
| ---------------------------------------------------------------------------------- | ---------------------------------------------------------------------------------- | ---------------------------------------------------------------------------------- | ---------------------------------------------------------------------------------- |
| `result`                                                                           | [Optional\<CompileResultSQLResult>](../../models/shared/CompileResultSQLResult.md) | :heavy_minus_sign:                                                                 | The partially evaluated result of the query as SQL.                                |