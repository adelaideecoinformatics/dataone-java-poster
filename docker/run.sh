#!/bin/bash
# Calls the eml_pusher.
cd `dirname $0`
CURR_DIR=`pwd`

echo "[INFO] running eml_pusher job with args: source_dir=$RECORDS_DIR_PATH, cert=$CERT_PATH, key=$KEY_PATH, mn=$MN_URL"

eml_pusher \
  --verbose \
  --source_dir $RECORDS_DIR_PATH \
  --cert_file $CERT_PATH \
  --cert_key_file $KEY_PATH \
  --destination_url $MN_URL \
  --log_file /dev/stdout


