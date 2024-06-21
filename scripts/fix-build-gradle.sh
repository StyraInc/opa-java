#!/bin/sh

# This script patches the `build.gradle` that speakeasy generates to also
# append ./scripts/build-footer.gradle, and to replace the plugins { } block
# with the one in ./scripts/build-plugins.gradle.
#
# The plugins block is directly parsed out using an AWK script, while the
# footer replaces everything between the comment `// === build-footer ===` and
# the end of the file with whatever is in the file in ./scripts.

cd "$(dirname "$0")/.."
set -e
set -u
set -x

trap 'rm -f build.gradle.tmp*' EXIT

# Rewrite the artifact and group ID to be one level "up", so we publish
# com.styra.opa rather than com.styra.opa.openapi.
sed 's#into("META-INF/maven/com.styra.opa/openapi")#into("META-INF/maven/com.styra/opa")#g' < build.gradle | \
	sed 's#group = "com.styra.opa"#group = "com.styra"#g' | \
	sed "s#groupId = 'com.styra.opa'#groupId = 'com.styra'#g" | \
	sed "s#artifactId = 'openapi'#artifactId = 'opa'#g" | \
	sed 's#archiveBaseName = "openapi"#archiveBaseName = "opa"#g' | \
	sed 's#libs/openapi-#libs/opa-#g' > build.gradle.tmp

# Finalize changes to build.gradle
mv build.gradle.tmp build.gradle

# Automatically remove any unused deps SE may have added.
./gradlew fixGradleLint

# Update settings.gradle to set the root project name.
sed -i "s#rootProject.name = 'openapi'#rootProject.name = 'opa'#g" ./settings.gradle

# Check for any suspicious strings that might indicate a bad group or artifact
# ID. If this check fails, most likely the generated build.gradle has changed
# in a way that the sed pipeline above failed to account for.
groupartifactlint() {
	set +e
	re="$1"
	m="$(grep -E -n --context=3 "$re" build.gradle)"
	if [ "$(echo "$m" | wc -c)" -gt 1 ] ; then
		echo "WARNING: possible incorrect group/artifact ID rewrite, '$0' may need updated" 1>&2
		echo "$m" 1>&2
		exit 1
	fi
}

set +x
groupartifactlint 'com[.]styra[.]opa[.]openapi'
groupartifactlint 'opa[.]openapi'
groupartifactlint 'com[.]styra[.]opa/openapi' 
