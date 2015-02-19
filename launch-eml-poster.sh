#!/bin/bash
# Command line wrapper to launch the EML-POSTer Java app
cd `dirname $0`
ENDPOINT=$1
SYSMETA_FILENAME=$2
EML_FILENAME=$3
OPERATION=$4
CERTIFICATE_FILE=/tmp/x509up_u1000
CERT_SCRIPT="build_certificate.sh"
USAGE_MSG="  Usage: $0 <endpoint URL> <sysmeta file path> <eml file path> [operation]"
EXAMPLE_MSG="  Example: $0 https://130.220.209.107/mn /tmp/sysmeta.xml /tmp/eml.xml create"
LOGS_DIR=logs
UNIQUE_ID=`date +%Y%m%d_%H%M`
if [ ! -f $CERTIFICATE_FILE ];then
  echo "WARNING: the (default) certificate file doesn't exist ($CERTIFICATE_FILE)"
  echo " You should run the $CERT_SCRIPT script to create it"
  echo "...pausing for effect..."
  sleep 2
fi
if [ -z "$ENDPOINT" ];then
  echo "Startup error: you need to pass the endpoint URL as the first arg" 
  echo $USAGE_MSG
  echo $EXAMPLE_MSG
  exit 1
fi
if [ -z "$SYSMETA_FILENAME" ];then
  echo "Startup error: you need to pass the filename of the sysmeta (System Metadata) file as the second arg" 
  echo $USAGE_MSG
  echo $EXAMPLE_MSG
  exit 1
fi
if [ -z "$EML_FILENAME" ];then
  echo "Startup error: you need to pass the filename of the EML file as the third arg" 
  echo $USAGE_MSG
  echo $EXAMPLE_MSG
  exit 1
fi
mkdir -p $LOGS_DIR
PROG_ARGS="--eml-poster.endpoint=$ENDPOINT"
PROG_ARGS="$PROG_ARGS --eml-poster.file.eml=$EML_FILENAME"
PROG_ARGS="$PROG_ARGS --eml-poster.file.sysmeta=$SYSMETA_FILENAME"
if [ ! -z "$OPERATION" ];then
  PROG_ARGS="$PROG_ARGS --eml-poster.operation=$OPERATION"
fi

java -Xms256m -Xmx5G -XX:MaxPermSize=256m -XX:+HeapDumpOnOutOfMemoryError -noverify \
-cp target/eml-poster-1.0-SNAPSHOT.jar:target/eml-poster-dependencies-1.0-SNAPSHOT.jar \
au.org.ecoinformatics.eml.poster.EcoinformaticsEmlPosterApplication \
$PROG_ARGS 2>&1 | tee $LOGS_DIR/eml-poster_$UNIQUE_ID-$PART_NAME.log

