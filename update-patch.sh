#!/bin/bash
# Updates the patch file by running a git diff between the current files
# and a known "clean" (i.e. straight out the XJC generator) git commit.
cd `dirname $0`
CLEAN_COMMIT=66e38115b57e887d4566a782ac0d2a99bb3e8c8d
JAXB_TREE_ROOT=./src/main/jaxb/
PATCH_FILE=./src/main/patches/au/org/ecoinformatics/eml/jaxb-generated-classes.patch

echo "Generating patch from directory $JAXB_TREE_ROOT into patch file $PATCH_FILE"
git diff --no-prefix $CLEAN_COMMIT -- $JAXB_TREE_ROOT > $PATCH_FILE
