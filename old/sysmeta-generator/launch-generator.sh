#!/bin/bash
# Reads either the single file or all files in the directory and creates a system metadata
# record for each file. The sysmeta record will have the filename of the input with the
# suffix appended.
cd `dirname $0`
IN_PATH=$1
OUT_SUFFIX=$2
USAGE_MSG="  Usage: $0 <input-path> <output-suffix>"
LOGS_DIR=logs
UNIQUE_ID=`date +%Y%m%d_%H%M`
mkdir -p $LOGS_DIR
if [ -z "$IN_PATH" ];then
  echo "Startup error: you need to pass an input path"
  echo $USAGE_MSG
  exit 1
fi
if [ -z "$OUT_SUFFIX" ];then
  echo "Startup error: you need to pass the output file suffix"
  echo $USAGE_MSG
  exit 1
fi

PROG_ARGS="--sysmeta-generator.input.path=$IN_PATH"
PROG_ARGS="$PROG_ARGS --sysmeta-generator.file.output.suffix=$OUT_SUFFIX"

java -Xms256m -Xmx5G -XX:MaxPermSize=256m -noverify \
-cp target/sysmeta-generator-1.0-SNAPSHOT.jar:target/sysmeta-generator-dependencies-1.0-SNAPSHOT.jar \
au.org.aekos.sysmetagen.AekosSysMetaGeneratorApplication \
$PROG_ARGS 2>&1 | tee $LOGS_DIR/sysmeta_generator_$UNIQUE_ID.log
