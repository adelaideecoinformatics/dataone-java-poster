#!/bin/bash
cd `dirname $0`
CERTIFICATE_FILE=/tmp/x509up_u1000
CERT_SCRIPT="build-certificate.sh"

function printHelpAndExit {
cat <<END-OF-HELP
  eml-poster bash wrapper script.

  Usage: $0 [OPTIONS]
  Examples:
    $0 -e https://dataone.ecoinformatics.org.au/mn -s /tmp/sysmeta.xml -f /tmp/eml.xml
    $0 -e https://dataone.ecoinformatics.org.au/mn -d /tmp/eml-records/

  Mandatory:
    -e <endpoint URL>  full URL (including protocol) of the endpoint to use
   then either both of:
    -f <EML file>      path to EML file to load
    -s <Sysmeta file>  path to Sysmeta file to load
   or only:
    -d <dir>           directory to scan for records to load (expects EML records to
                       end with .xml and corresponding sysmeta to have .xml-sysmeta)

  Optional:
    -o [create|delete] operation to perform, default: create
    -h                 display this help and exit
END-OF-HELP
exit 0
}

LOGS_DIR=logs
UNIQUE_ID=`date +%Y%m%d_%H%M`

if [ ! -f $CERTIFICATE_FILE ];then
  echo "WARNING: the (default) certificate file doesn't exist ($CERTIFICATE_FILE)"
  echo " You should run the $CERT_SCRIPT script to create it"
  echo "...pausing for effect..."
  sleep 2
fi

while getopts ":e:o:f:s:d:h" opt; do
  case $opt in
    e)
      ENDPOINT=$OPTARG
      ;;  
    o)
      OPERATION=$OPTARG
      ;;  
    d)
      DIRECTORY_PATH=$OPTARG
      ;;  
    f)
      EML_FILENAME=$OPTARG
      ;;
    s)
      SYSMETA_FILENAME=$OPTARG
      ;;
    h)
      printHelpAndExit
      ;;  
    \?) 
      echo "Invalid option: -$OPTARG" >&2
      exit 1
      ;;  
    :)  
      echo "Option -$OPTARG requires an argument. Run $0 -h for more information." >&2
      exit 1
      ;;  
  esac
done

if [ -z "$ENDPOINT" ];then
  echo "Startup error: no endpoint supplied. Run $0 -h for help"
  exit 1
fi
PROG_ARGS="--eml-poster.endpoint=$ENDPOINT"
if [ ! -z "$SYSMETA_FILENAME" ];then
  PROG_ARGS="$PROG_ARGS --eml-poster.file.sysmeta=$SYSMETA_FILENAME"
fi
if [ ! -z "$EML_FILENAME" ];then
  PROG_ARGS="$PROG_ARGS --eml-poster.file.eml=$EML_FILENAME"
fi
if [ ! -z "$DIRECTORY_PATH" ];then
  PROG_ARGS="$PROG_ARGS --eml-poster.directory=$DIRECTORY_PATH"
fi
if [ ! -z "$OPERATION" ];then
  PROG_ARGS="$PROG_ARGS --eml-poster.operation=$OPERATION"
fi

mkdir -p $LOGS_DIR

time java -Xms256m -Xmx5G -XX:MaxPermSize=256m -XX:+HeapDumpOnOutOfMemoryError -noverify \
-cp target/eml-poster-1.0-SNAPSHOT.jar:target/eml-poster-dependencies-1.0-SNAPSHOT.jar \
au.org.ecoinformatics.eml.poster.EcoinformaticsEmlPosterApplication \
$PROG_ARGS 2>&1 | tee $LOGS_DIR/eml-poster_$UNIQUE_ID.log

