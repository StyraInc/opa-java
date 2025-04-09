# UnhealthyServer

OPA service is not healthy. If the bundles option is specified this can mean any of the configured bundles have not yet been activated. If the plugins option is specified then at least one plugin is in a non-OK state.


## Fields

| Field               | Type                | Required            | Description         |
| ------------------- | ------------------- | ------------------- | ------------------- |
| `code`              | *String*            | :heavy_check_mark:  | N/A                 |
| `error`             | *Optional\<String>* | :heavy_minus_sign:  | N/A                 |
| `message`           | *Optional\<String>* | :heavy_minus_sign:  | N/A                 |