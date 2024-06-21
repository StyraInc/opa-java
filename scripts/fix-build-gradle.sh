#!/bin/sh

# This script patches the `build.gradle` that speakeasy generates to also
# append ./scripts/build-footer.gradle, and to replace the plugins { } block
# with the one in ./scripts/build-plugins.gradle.
#
# The plugins block is directly parsed out using an AWK script, while the
# footer replaces everything between the comment `// === build-footer ===` and
# the end of the file with whatever is in the file in ./scripts.

# It is useful to be able to develop from MacOS, but it's sed implementation
# chokes. Rather than hard-coding MacOS stuff, we just check if the system sed
# is not GNU sed, and if so try to detect an installed gsed.
SED="$(which sed)"
if "$SED" --version 2>&1 | head -n1 | grep -q 'GNU sed' ; then
	printf "" # pass
else
	if [ ! -x "$(which gsed)" ] ; then
		echo "FATAL: '$SED' is not GNU sed, and gsed is not found." 1>&2
		echo "HINT: on MacOS, try brew install gsed" 1>&2
		exit 1
	fi

	SED="$(which gsed)"
	if "$SED" --version 2>&1 | head -n1 | grep -q 'GNU sed' ; then
		printf "" # pass
	else
		echo "FATAL: '$SED' is your system 'gsed', but is not GNU sed." 1>&2
		exit 1
	fi
fi

cd "$(dirname "$0")/.."
set -e
set -u
set -x

trap 'rm -f build.gradle.tmp*' EXIT

# Rewrite the artifact and group ID to be one level "up", so we publish
# com.styra.opa rather than com.styra.opa.openapi.
"$SED" 's#into("META-INF/maven/com.styra.opa/openapi")#into("META-INF/maven/com.styra/opa")#g' < build.gradle | \
	"$SED" 's#group = "com.styra.opa"#group = "com.styra"#g' | \
	"$SED" "s#groupId = 'com.styra.opa'#groupId = 'com.styra'#g" | \
	"$SED" "s#artifactId = 'openapi'#artifactId = 'opa'#g" | \
	"$SED" 's#archiveBaseName = "openapi"#archiveBaseName = "opa"#g' | \
	"$SED" 's#libs/openapi-#libs/opa-#g' > build.gradle.tmp

# Finalize changes to build.gradle
mv build.gradle.tmp build.gradle

# Automatically remove any unused deps SE may have added.
./gradlew fixGradleLint

# Update settings.gradle to set the root project name.
"$SED" -i "s/rootProject[.]name = 'openapi'/rootProject.name = 'opa'/g" ./settings.gradle

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

if grep -q 'rootProject[.]name.*openapi' < settings.gradle ; then
	echo "WARNING: possible incorrect root project name in settings.gradle after running '$0'" 1>&2
	exit 1
fi
