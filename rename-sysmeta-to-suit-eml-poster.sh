#!/bin/bash
TARGET_DIR=$1
USAGE_MSG="
  The eml-poster utility expects a pair of files: foo-eml.xml and foo-eml.xml-sysmeta but \n
  the RdfToEmlCrawler doesn't produce them like this. This script renames them to suit.\n
  Usage: $0 <target-dir> \n
  Example: $0 /data/aekos-eml/"
if [ ! -d "$TARGET_DIR" ];then
  echo "Startup error: you need to pass a target directory that exists as the first arg"
  echo -e $USAGE_MSG
  exit 1
fi
TARGET_DIR=`cd $TARGET_DIR && pwd`
FILES_PROCESSED=0
for f in $TARGET_DIR/*-sysmeta.xml; do
  NEW_NAME=`echo $f | sed -e 's/-sysmeta/-eml/' -e 's/$/-sysmeta/'`
  mv "$f" "$NEW_NAME"
  ((FILES_PROCESSED++))
done
echo "Processed $FILES_PROCESSED files"
