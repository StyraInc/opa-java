# OPA-Java SDK Developer Documentation Site

This site is meant to assist developers of the [opa-java](https://github.com/StyraInc/opa-java/) project itself. Looking for docs on how to use it? Consider the [JavaDoc](./javadoc) or [SDK docs](https://docs.styra.com/sdk).

This site contains the developer documentation for [opa-java](https://github.com/StyraInc/opa-java/), a Java SDK for the [Open Policy Agent](https://www.openpolicyagent.org/) [REST API](https://www.openpolicyagent.org/docs/latest/rest-api/). "Low level" wrappers around the REST API methods are generated using [Speakeasy](https://www.speakeasyapi.dev/). A higher level human-written API is also provided, which is intended to simplify the most common tasks for OPA API consumers. The Speakeasy-generated API can be found in the [`com.styra.opa.openapi`](https://styrainc.github.io/opa-java/javadoc/com/styra/opa/openapi/package-summary.html) package, while the higher level API is located in [`com.styra.opa`](https://styrainc.github.io/opa-java/javadoc/com/styra/opa/package-summary.html).

## Documentation Index

- [Add a Documentation Page](maintenance/add-doc.md)
- [Modify Styra-Managed Code](maintenance/change-managed.md)
- [Regenerate Speakeasy-Managed Code](maintenance/change-speakeasy.md)
- [Release](maintenance/releases.md)
- [Using a Custom OpenAPI Spec](https://www.rfc-editor.org/rfc/rfc3986#section-3)

## Other Helpful Links

* [StyraInc/opa-java GitHub repository](https://github.com/StyraInc/opa-java)
* [opa-java JavaDoc](https://styrainc.github.io/opa-java/javadoc/)
* [Speakeasy generated "models" documentation](./models/)
* [Speakeasy generated "SDKs" documentation](./sdks/)
* [OpenAPI Spec for the OPA REST API](https://github.com/StyraInc/enterprise-opa/tree/main/openapi)

