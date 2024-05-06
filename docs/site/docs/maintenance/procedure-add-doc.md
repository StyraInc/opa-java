# Procedure: Add a Documentation Page

Documentation for `opa-java` is managed using [MkDocs](https://www.mkdocs.org/). You can build the docs into a folder `out` using the command [`./scripts/build_docs.sh`](https://github.com/StyraInc/opa-java/blob/main/scripts/build_docs.sh), or you can serve an ephemeral, local version of the docs using [`./scripts/serve_docs.sh`](https://github.com/StyraInc/opa-java/blob/main/scripts/serve_docs.sh).

## Appropriate Content for Docs

The docs site for `opa-java` is primarily for maintenance information about the project. Information about how to use specific APIs belongs in the JavaDoc, and can be created or modified by changing comments in the Java code. Higher-level user-facing documentation belongs on the [Styra documentation site](https://docs.styra.com/sdk).

Generally speaking, the MkDocs site should only contain information that would be of interest to someone contributing to the `opa-java` repo.

## Adding a Document

1. Add your document to [`docs/site/docs/`](https://github.com/StyraInc/opa-java/tree/main/docs/site/docs).
2. Update [`docs/site/mkdocs.yml`](https://github.com/StyraInc/opa-java/blob/main/docs/site/mkdocs.yml) so that your document will be presented in the navigation bar.
3. Use `scripts/build_doc.sh` or `scripts/serve_docs.sh` to ensure your docs changes render as you intended.
4. Create a PR with your changes. Your changes will automatically be published by the [docs publishing workflow](https://github.com/StyraInc/opa-java/blob/main/.github/workflows/docs_publish.yaml).

