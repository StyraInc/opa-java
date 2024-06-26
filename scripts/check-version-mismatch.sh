#!/bin/sh

# This script checks that the version numbers reported in RELEASES.md and in
# .speakeasy/gen.yaml match.

set -e
set -u
set -x

GEN_YAML_VERSION="$(opa eval -f raw -I 'input.java.version' < .speakeasy/gen.yaml)"

RELEASES_MD_VERSION="$(awk '/^### Generated/ {s=1} s==1 && /java v[0-9]/ {s=0; print}' < RELEASES.md  | tail -n1 | cut -d'[' -f 2 | cut -d']' -f 1 | cut -d' ' -f2 | tr -d 'v')"

if [ "$GEN_YAML_VERSION" != "$RELEASES_MD_VERSION" ] ; then
	printf "ERROR: .speakeasy/gen.yaml (%s) and RELEASES.md (%s) disagree on the current version, creating a release in this state may result in inconsistent versions appearing in Maven and GitHub\n" "$GEN_YAML_VERSION" "$RELEASES_MD_VERSION" 1>&2
	exit 1
fi

exit 0
