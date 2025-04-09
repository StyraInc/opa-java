#!/bin/sh

# This script checks that the version numbers reported in RELEASES.md and in
# .speakeasy/gen.yaml match.

set -e
set -u
# set -x

GEN_YAML_VERSION="$(opa eval -f raw -I 'input.java.version' < .speakeasy/gen.yaml)"

RELEASES_MD_VERSION="$(awk '/^### Generated/ {s=1} s==1 && /java v[0-9]/ {s=0; print}' < RELEASES.md  | tail -n1 | cut -d'[' -f 2 | cut -d']' -f 1 | cut -d' ' -f2 | tr -d 'v')"

BUILD_GRADLE_VERSION="$(awk '$1 == "version"' < build.gradle | cut -d'"' -f2 | tail -n 1)"

SDK_VERSION="$(awk '$3 == "sdkVersion"' < src/main/java/com/styra/opa/openapi/SDKConfiguration.java | cut -d'"' -f2)"

ALL_VERSIONS="$(printf '%s\n%s\n%s\n%s\n' "$GEN_YAML_VERSION" "$RELEASES_MD_VERSION" "$BUILD_GRADLE_VERSION" "$SDK_VERSION")"

if [ "$(echo "$ALL_VERSIONS" | sort | uniq | wc -l)" -gt 1 ] ; then
	echo "ERROR: version mismatch detected! This may result in incorrect GitHub and/or Maven version numbers during release." 1>&2
	echo ".speakeasy/gen.yaml: $GEN_YAML_VERSION" 1>&2
	echo "RELEASES.md: $RELEASES_MD_VERSION" 1>&2
	echo "build.gradle: $BUILD_GRADLE_VERSION" 1>&2
	echo "src/main/java/com/styra/opa/openapi/SDKConfiguration.java: $SDK_VERSION" 1>&2
	exit 1
fi

exit 0
