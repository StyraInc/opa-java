#!/bin/sh

# This script checks weather or not the latest GitHub release matches the
# latest Maven Central release version. If the latest release was in the last
# 24 hours, then it will always exit 0. If the releases don't match, it will
# exit 1 and print a pertinent error message.

set -e
set -u
set -x

# https://docs.github.com/en/rest/releases/releases?apiVersion=2022-11-28#list-releases
GITHUB_REPO="styrainc/opa-java"
GITHUB_API_URL="https://api.github.com/repos/$GITHUB_REPO/releases"

# https://central.sonatype.org/search/rest-api-guide/
#
# Note that there is further filtering done using opa eval later when
# MAVEN_RELEASE is calculated.
MAVEN_URL='https://search.maven.org/solrsearch/select?q=g:com.styra&rows=20&wt=json'

# If the release is less than this many seconds old, ignore it.
# 86400s == 1 day.
IGNORE_WINDOW=86400

# Notice the tr -d v to strip the leading v from the release tag.
GH_RESPONSE="$(curl -LSs "$GITHUB_API_URL")"
RELEASE_TAG="$(printf '%s' "$GH_RESPONSE" | opa eval -f pretty -I 'input[0].tag_name' | tr -d '"' | tr -d 'v')"
RELEASE_TS="$(printf '%s' "$GH_RESPONSE"  | opa eval -f pretty -I 'input[0].published_at' | tr -d '"')"
RELEASE_AGE="$(printf '{"ts": "%s"}' "$RELEASE_TS" | opa eval -f pretty -I 'ceil((time.now_ns() - time.parse_rfc3339_ns(input.ts))*0.000000001)')"

printf "DEBUG: RELEASE_TAG='%s' RELEASE_TS='%s' RELEASE_AGE=%ds\n" "$RELEASE_TAG" "$RELEASE_TS" "$RELEASE_AGE" 1>&2

if [ "$RELEASE_AGE" -lt "$IGNORE_WINDOW" ] ; then
	printf "DEBUG: release is too new (%ds < %ds), skipping version check\n" "$RELEASE_AGE" "$IGNORE_WINDOW" 1>&2
	exit 0
fi

MAVEN_RESPONSE="$(curl -LSs "$MAVEN_URL")"
MAVEN_RELEASE="$(printf '%s' "$MAVEN_RESPONSE" | opa eval -f pretty -I '[d | d := input.response.docs[_]; d.a == "opa"][0].latestVersion' | tr -d '"')"

if [ "$MAVEN_RELEASE" != "$RELEASE_TAG" ] ; then
	echo "latest Maven release '$MAVEN_RELEASE' does not match latest GitHub release tag '$RELEASE_TAG'"
	exit 1
fi

echo "latest Maven release '$MAVEN_RELEASE' matches latest GitHub release tag '$RELEASE_TAG'"
exit 0
