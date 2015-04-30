#!/bin/bash
TARGET_DIR=$1
TARGET_ENDPOINT=$2
SYSMETA_SUFFIX=$3
OPERATION=$4
OPERATION_DEFAULT=create
USAGE_MSG="
  Usage: $0 <target-dir> <target-endpoint> <sysmeta-suffix> [create(default)|delete] \n
  Example: $0 /data/harvested-data/some-project https://dataone.ecoinformatics.org.au/mn -sysmeta\n
  Note: the protocol prefix for the endpoint is crucial!"
TARGET_FILE_EXT=xml
if [ ! -d "$TARGET_DIR" ];then
  echo "Startup error: you need to pass a target directory that exists as the first arg"
  echo -e $USAGE_MSG
  exit 1
fi
if [ -z "$TARGET_ENDPOINT" ];then
  echo "Startup error: you need to pass a target endpoint as the second arg"
  echo -e $USAGE_MSG
  exit 1
fi
if [ -z "$SYSMETA_SUFFIX" ];then
  echo "Startup error: you need to pass the suffix that is used for the sysmeta files as the third arg"
  echo "For example if you had the pair of files /data/some-eml.xml and /data/some-eml.xml-sysmeta then '-sysmeta' is the suffix"
  echo -e $USAGE_MSG
  exit 1
fi
if [ -z "$OPERATION" ];then
  OPERATION=$OPERATION_DEFAULT
fi
TARGET_DIR=`cd $TARGET_DIR && pwd`
for f in $TARGET_DIR/*.$TARGET_FILE_EXT; do 
  ./launch-eml-poster.sh $TARGET_ENDPOINT $f$SYSMETA_SUFFIX $f $OPERATION
done
