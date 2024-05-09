#  Releases

!!! note

    There are manual steps required before releases will appear on Maven Central, scroll down for more information.

Releases normally happen when the Speakeasy automation in the [SDK generation workflow](https://github.com/StyraInc/opa-java/blob/main/.github/workflows/sdk_generation.yaml) detects that a dependency needs updated, or the code generation changes. It should automatically create a PR (for example, [#40](https://github.com/StyraInc/opa-java/pull/40)) which updates the [`RELEASES.md`](https://github.com/StyraInc/opa-java/blob/main/RELEASES.md) file. This workflow runs nightly.

You can force the creation of one of these PRs by running the workflow manually and checking "Force generation of SDKs" option. In this situation, the PR is created even if nothing has changed. This can be useful if you need to force a release for some reason or another.

Once merging the PR, the [SDK publishing workflow](https://github.com/StyraInc/opa-java/blob/main/.github/workflows/sdk_publish.yaml) should automatically detect the change to the `RELEASES.md` file and publish a release to the [GitHub releases](https://github.com/StyraInc/opa-java/releases) and to [Maven Central](https://central.sonatype.com/artifact/com.styra/opa).

Once all of the above are done, **you must manually log in to the OSSRH portal to complete additional steps before the release will be visible on Maven Central**. These additional steps can be found in Maven Central's documentation [here](https://central.sonatype.org/publish/release). In summary, you must find the release under "staging repositories", "close" the corresponding repository, and then "release" it after it has been successfully closed. Once the release is finished, you can drop the staging release.

