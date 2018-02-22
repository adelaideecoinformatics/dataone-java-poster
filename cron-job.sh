#!/bin/bash
# Calls the eml_pusher and emails the log.
#
# This is a template and you should rename it to represent the project it loads
# as the log files are named after the script. Look for the TODOs and change
# those values.
#
# Designed to be run with a virtualenv. The script will activate it for you.
#
# Use something like this in crontab (runs once a week on Friday):
#   20 01 * * 5 /home/gmn/cron-jobs/aekos-eml-load.sh >> /home/gmn/cron-jobs/aekos-eml-load.output 2>&1
cd `dirname $0`
CURR_DIR=`pwd`
CURR_SCRIPT=`basename $0`
LOGS_DIR=$CURR_DIR/$CURR_SCRIPT-logs
if [ ! -d $LOGS_DIR ]; then
  mkdir $LOGS_DIR
fi
UNIQUE_ID=`date +%Y%m%d_%H%M%S`
VENV=$CURR_DIR/pusher_venv/bin/activate # TODO update if required

source $VENV

SOURCE_DIR=/data/harvested-data/REPLACE_ME # TODO point to EML files directory
CERT_FILE=$CURR_DIR/authorizeduser-cert.pem # TODO change to suit where cert lives, look in /var/local/dataone/certs/local_ca/newcerts if you don't have one
CERT_KEY_FILE=$CURR_DIR/authorizeduser-cert-key.pem # TODO change to suit where key lives, look in /var/local/dataone/certs/local_ca/private if you don't have one
DEST_URL=https://dataone-dev.tern.org.au/mn # TODO change to prod if required
LOG_FILE=$LOGS_DIR/$UNIQUE_ID.log

type eml_pusher &> /dev/null || {
  echo >&2 "eml_pusher is required but it's not installed. Aborting.";
  echo >&2 "You can install it with 'pip install --upgrade https://github.com/adelaideecoinformatics/ecoinf-dataone/archive/master.zip'";
  exit 1;
}

eml_pusher \
  --verbose \
  --source_dir $SOURCE_DIR \
  --cert_file $CERT_FILE \
  --cert_key_file $CERT_KEY_FILE \
  --destination_url $DEST_URL \
  --log_file $LOG_FILE

./log-emailer.py $LOG_FILE
