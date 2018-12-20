#!/bin/sh
set -e
cd `dirname "$0"`
thisDir=`pwd`

# assert env vars exist with bash parameter expansion (http://wiki.bash-hackers.org/syntax/pe#display_error_if_null_or_unset)
: ${CERT_BASE64:?}        # produce with `cat cert.pem | base64 -w0` and pass that string
: ${CERT_KEY_BASE64:?}    # produce with `cat thekey.pem | base64 -w0` and pass that output string
: ${RECORDS_DIR_PATH:?}   # e.g. /data/aekos
: ${MN_URL:?}             # e.g. https://dataone-dev.tern.org.au/mn
: ${CRON_SCHEDULE:?}      # e.g. 10 10 * * *

certPath=/tmp/thecert.pem
echo $CERT_BASE64 | base64 -d > $certPath
certKeyPath=/tmp/thekey.pem
echo $CERT_KEY_BASE64 | base64 -d > $certKeyPath
chmod 400 $certPath $certKeyPath

redirectToDockerLogs="> /dev/stdout 2> /dev/stderr"
echo "$CRON_SCHEDULE CERT_PATH=$certPath KEY_PATH=$certKeyPath sh $thisDir/run.sh $redirectToDockerLogs" > /var/spool/cron/crontabs/root
crond -l 2 -f

