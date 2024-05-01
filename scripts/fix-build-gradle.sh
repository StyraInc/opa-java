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

# This AWK program replaces the plugins section of build.gradle with a custom
# one, specified in the variable `f`. The variable `p` is used to track whether
# we are still parsing the `plugins` block or not. In explanation of each of
# its clauses:
#
#     p==1 && $1 != "}" {next}
#
#         If we are still parsing the plugins block, and the current line does
#         not start with `}`, continue to the next line without doing anything.
#
#     $1 == "plugins" && $2 == "{" && p !=1 {p=1; while((getline l<f)){print(l)}; next}
#
#         If the line starts with `plugins {` (note that `plugins{` will not be
#         accepted), and we aren't already parsing the plugins block, set `p=1`
#         so we know we are in the plugins block, then print every line of the
#         file `f` to the output. Finally, proceed to processing the next line
#         without considering any of the remaining clauses.
#
#     $1 == "}" && p == 1 {p=0;next}
#
#         If the line starts with `}` and we are parsing the plugins block,
#         set `p=0` and continue to the next line. This intentionally avoids
#         printing the closing `}`, as `scripts/build-plugins.gradle` should
#         include that itself.
#
#     {print($0)}
#
#         Print the line. This causes any lines outside of the plugin block to
#         be printed without modification.
#
# Notice also the use of `trap`, which ensures that if the AWK script fails for
# some reason, we will clean up the .tmp file it creates on our way out the
# door. By writing to a temp file and then overwriting the main one, we can
# ensure that the modification process is atomic (assuming `mv` is atomic on
# the hose filesystem).
#
# This does seem to be adding an extra trailing newline to after the plugins
# block each time that it runs, presumably due to the default print($0) action.
awk -v f=./scripts/build-plugins.gradle 'p==1 && $1 != "}" {next} $1 == "plugins" && $2 == "{" && p !=1 {p=1; while((getline l<f)){print(l)}; next} $1 == "}" && p == 1 {p=0;next} {print($0)}' < build.gradle > build.gradle.tmp1


# This AWK script works on a similar principle to the above. It first searches
# for an existing `// === build-footer ===` comment and if it finds one, uses
# `exit` to jump directly to the `END` block, otherwise simply outputting the
# lines that it sees unmodified. When the `END` block is run, it simply outputs
# the footer file.
awk -v f=./scripts/build-footer.gradle '$1 == "//" && $2 == "===" && $3 == "build-footer" && $4 == "===" {exit} {print($0)} END {print("// === build-footer ==="); while((getline l<f)){print(l)}}' < build.gradle.tmp1 > build.gradle.tmp2

# Rewrite the artifact and group ID to be one level "up", so we publish
# com.styra.opa rather than com.styra.opa.openapi.
sed 's#into("META-INF/maven/com.styra.opa/openapi")#into("META-INF/maven/com.styra/opa")#g' < build.gradle.tmp2 | \
	sed 's#group = "com.styra.opa"#group = "com.styra"#g' | \
	sed "s#groupId = 'com.styra.opa'#groupId = 'com.styra'#g" | \
	sed "s#artifactId = 'openapi'#artifactId = 'opa'#g" | \
	sed 's#archiveBaseName = "openapi"#archiveBaseName = "opa"#g' | \
	sed 's#libs/openapi-#libs/opa-#g' > build.gradle.tmp3

# Finalize changes to build.gradle
mv build.gradle.tmp3 build.gradle

# Automatically remove any unused deps SE may have added.
./gradlew fixGradleLint
