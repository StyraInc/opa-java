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

ensure_plugin() {
	# This function ensures that the build.gradle file contains the
	# specified plugin. For this function to be idempotent, then the
	# replacement string MUST match the search regex.
	#
	# $1 . . . plugin string search regex
	# $2 . . . replacement string
	# $3 . . . build.gradle file to modify

	pluginstring="$1"
	replacestring="$2"
	gradlefile="$3"

	# p tracks weather or not we are inside of the plugin block
	#
	# s is the search regex
	#
	# r is the replacement string
	#
	# The general logic here is that we search for the plugins block, then 
	# check if each line matches the search regex. If not, then we print it,
	# but otherwise we skip to the next line. When we see the closing brace,
	# we print out the replacement string.
	awk -v r="$replacestring" -v s="$pluginstring" 'p==1 && $1 != "}" {if($0 ~ s) {next} else {print($0); next} } $1 == "plugins" && $2 == "{" && p!=1 {p=1} $1 == "}" && p == 1 {p=0; printf("    %s\n}\n", r); next} {print($0)}' < "$gradlefile" > "$gradlefile.tmp"
	mv "$gradlefile.tmp" "$gradlefile"
}

# NOTE: the reason why we need to do this here in this way, rather than just
# using the additionalPlugins in .speakeasy/gen.yaml is somewhat esoteric. When
# the Speakeasy GitHub Action runs to generate a new build of the SDK, it will
# try to compile the SDK that it generates to ensure that it works. Since
# Gradle includes the nebula linting task as a dependency of every other task,
# and there isn't a way to suppress the autoLintGradle task for normal builds,
# it will always fail due to the linting errors in the generated build.gradle
# file. The thing is, this very script uses fixGradleLint to fix those very
# errors, so we have a chicken-and-egg problem. The script can't run until
# SE's generation finishes, but SE's generation can't compile the code until
# the script runs.
#
# The solution is to not use additionalPlugins for nebula.lint, that way the
# generated build.gradle won't have it. We use this ensure_plugin shell
# function to idemptently poke the right nebula.lint version back in AFTER SE's
# generation workflow has already completed.
ensure_plugin 'id "nebula.lint".*' 'id "nebula.lint" version "17.8.0"' './build.gradle'

# Rewrite the artifact and group ID to be one level "up", so we publish
# com.styra.opa rather than com.styra.opa.openapi.
"$SED" 's#into("META-INF/maven/com.styra.opa/openapi")#into("META-INF/maven/com.styra/opa")#g' < build.gradle | \
	"$SED" 's#group = "com.styra.opa"#group = "com.styra"#g' | \
	"$SED" 's#groupId = "com.styra.opa"#groupId = "com.styra"#g' | \
	"$SED" 's#artifactId = "openapi"#artifactId = "opa"#g' | \
	"$SED" 's#archiveBaseName = "openapi"#archiveBaseName = "opa"#g' | \
	"$SED" 's#libs/openapi-#libs/opa-#g' > build.gradle.tmp

# Finalize changes to build.gradle
mv build.gradle.tmp build.gradle

# Automatically remove any unused deps SE may have added.
./gradlew fixGradleLint

# Update settings.gradle to set the root project name.
"$SED" -i "s/rootProject[.]name = 'openapi'/rootProject.name = 'opa'/g" ./settings.gradle

# Update gradle.properties to set the groupId and artifactId
"$SED" -i "s/groupId=com.styra.opa/groupId=com.styra/g" ./gradle.properties
"$SED" -i "s/artifactId=openapi/artifactId=opa/g" ./gradle.properties


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
