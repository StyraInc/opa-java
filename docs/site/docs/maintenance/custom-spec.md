# Using a Custom OpenAPI Spec

!!! note

    **This procedure should only be used during development.** The OpenAPI spec in the StyraInc/enterprise-opa repository is the point of truth for how the SDKs should be generated. You should never merge code into `main` which was not generated using the upstream one. Any permenant changes to the OpenAPI spec must go through a PR on the upstream.

During development, you may wish to customize the OpenAPI spec used to generate the SDK. Here is how you can do so:

1. Download the existing [spec](https://github.com/StyraInc/enterprise-opa/tree/main/openapi) to a local file, noting the path you saved it at.
2. Make any needed modifications to the OpenAPI spec.
3. Modify `.speakeasy/workflow.yaml` to change `sources.openapi.inputs[0].location` to point to your local file. It is not necessary to provide a URI scheme for local files.
4. Follow the steps described in [Regenerate Speakeasy-Managed Code](maintenance/change-speakeasy.md) to regenerate the SDK.

