# Procedure: Modify Styra-Managed Code

Because the `opa-java` repository is largely managed by Speakeasy's code generation tooling, there are a few additional restrictions that should be kept in mind when changing the Styra-managed "porcelain" API code.

Since Speakeasy is configured to create the generated Java code in the package `com.styra.opa.openapi`, you should avoid including any human-written code in this package. To avoid having your code accidentally overwritten, you should add any new files you create to [`.genignore`](https://github.com/StyraInc/opa-java/blob/main/.genignore).

If you need to modify the `build.gradle` file, you should be aware it is automatically re-generated on a regular basis as it is managed by Speakeasy's tooling. `opa-java` also has additional tools in place to handle needed changes to this file, see [*Procedure: Regenerate Speakeasy-Manged Code*](./procedure-change-speakeasy.md) for more information.
