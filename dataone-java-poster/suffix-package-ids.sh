#!/bin/bash
# finds and suffixes all packageIds of EML files in a directory
cd `dirname $0`
TARGET_DIR=$1
SUFFIX=$2
USAGE_MSG="  Usage: $0 <target-directory> <suffix>"
EXAMPLE_MSG="  Example: $0 /path/to/dir .20150912"
EML_FILE_GLOB="*.xml" # close enough
if [ -z "$TARGET_DIR" ];then
  echo "Startup error: you need to pass the target directory as the first arg" 
  echo $USAGE_MSG
  echo $EXAMPLE_MSG
  exit 1
fi
if [ -z "$SUFFIX" ];then
  echo "Startup error: you need to pass the suffix to append as the second arg" 
  echo $USAGE_MSG
  echo $EXAMPLE_MSG
  exit 1
fi

FILES_PROCESSED=0
for f in $TARGET_DIR/$EML_FILE_GLOB; do
  SELECTOR='packageId="\([-A-Za-z0-9._/]*\)"'
  if [[ "$(grep "$SELECTOR" $f)" == "" ]]; then
    echo "Skipping file due to no regex match: $f"
    continue
  fi
  REPLACEMENT="packageId=\"\1$SUFFIX\""
  sed -i "s/$SELECTOR/$REPLACEMENT/" "$f"
  ((FILES_PROCESSED++))
done
echo "Processed $FILES_PROCESSED files"
