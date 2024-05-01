#!/bin/sh

# This script is called by sdk_generation.yaml after Speakeasy's generation is
# complete, but before adding the "post generation fixup" commit. Any additional
# steps that need

cd "$(dirname "$0"/..)"
set -e
set -u
set -x

./scripts/fix-build-gradle.sh
