#!/bin/bash
# Updates the patch file by running a git diff between the current files
# and a known "clean" (i.e. straight out the XJC generator) git commit.
cd `dirname $0`
MODIFIED_JAXB_TREE_ROOT=./src/main/jaxb/
CLEAN_JAXB_TREE_ROOT=target/jaxb-tmp/
PATCH_FILE=./src/main/patches/au/org/ecoinformatics/eml/jaxb-generated-classes.patch

mvn clean process-sources -Pjaxb-tmp

echo "Generating patch into patch file $PATCH_FILE"
#git diff --no-prefix $CLEAN_COMMIT -- $JAXB_TREE_ROOT > $PATCH_FILE
diff -ru $MODIFIED_JAXB_TREE_ROOT $CLEAN_JAXB_TREE_ROOT > $PATCH_FILE
