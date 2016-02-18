#!/bin/bash
# Calls the eml_pusher and emails the log.
# This is a template and you should rename it to represent the project it loads
# as the log files are named after the script.
cd `dirname $0`
CURR_DIR=`pwd`
LOGS_DIR=$CURR_DIR/$0-logs
if [ ! -d $LOGS_DIR ]; then
  mkdir $LOGS_DIR
fi
UNIQUE_ID=`date +%Y%m%d_%H%M%S`

SOURCE_DIR=/data/harvested-data/REPLACE_ME # TODO point to EML files directory
CERT_FILE=$CURR_DIR/authorizeduser-cert.pem # TODO change to suit where cert lives
DEST_URL=https://dataone-dev.ecoinformatics.org.au/mn # TODO change to prod if required
LOG_FILE=$LOGS_DIR/$UNIQUE_ID.log

eml_pusher \
  --verbose \
  --source_dir $SOURCE_DIR \
  --cert_file $CERT_FILE \
  --destination_url $DEST_URL \
  --log_file $LOG_FILE

./log-emailer.py $LOG_FILE
