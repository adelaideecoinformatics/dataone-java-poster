#/bin/bash
# Connects to the dataone instance and gets the required information to create a certificate locally
# This certificate can then be used to run the EML-POSTer tool

CERT_MAKER_USER="ubuntu"
CERT_MAKER_HOST=""
CERT_MAKER_PEM=""
CERT_MAKER_OUTPUT_FILENAME=/tmp/x509up_u1000
CERT_MAKER_LOCAL_MODE=false

function printHelp {
  cat <<END_OF_HELP
   Creates a certificate from a dataONE instance
   either remote or local.
 
   Usage:
     $0 [OPTIONS]
   Example for remote mode:
     $0 -i tern-projects.pem -c dataone.ecoinformatics.org.au
   Example for local mode:
     sudo bash $0 -l

   Mandatory Options when not in local mode:
     -c <host>         IP or hostname of the DataONE instance
     -i <pem>          PEM file to use to connect
 
   Optional Options:
     -l                run in local mode (current machine is the dataONE instance)
     -h                display this help and exit
END_OF_HELP
}

while getopts ":hc:i:l" opt; do
  case $opt in
    c)
      CERT_MAKER_HOST=$OPTARG
      ;;
    l)
      CERT_MAKER_LOCAL_MODE=true
      ;;
    i)
      CERT_MAKER_PEM=$OPTARG
      if [ ! -f $CERT_MAKER_PEM ]; then
        echo "ERROR: supplied PEM file $CERT_MAKER_PEM doesn't exist"
        exit 1
      fi
      ;;
    h)
      printHelp
      exit 0
      ;;
    \?)
      echo "Invalid option: -$OPTARG" >&2
      printHelp
      exit 1
      ;;
    :)
      echo " " >&2
      echo "ERROR: Option -$OPTARG requires an argument." >&2
      echo " " >&2
      printHelp
      exit 1
      ;;
  esac
done

if [ "$CERT_MAKER_LOCAL_MODE" = false ] && [ -z "$CERT_MAKER_HOST" ]; then
  echo "ERROR: you must pass a host to connect to."
  echo "Run $0 -h for more information"
  exit 1
fi
if [ "$CERT_MAKER_LOCAL_MODE" = false ] && [ -z "$CERT_MAKER_PEM" ]; then
  echo "ERROR: you must pass a PEM file to use to connect."
  echo "Run $0 -h for more information"
  exit 1
fi

CERT_MAKER_CERT_FILENAME=/var/local/dataone/certs/client/client_cert.pem
CERT_MAKER_KEY_FILENAME=/var/local/dataone/certs/client/client_key_nopassword.pem
CERT_MAKER_COMMAND="sudo cat $CERT_MAKER_CERT_FILENAME | awk '/BEGIN CERTIFICATE/,/END CERTIFICATE/' && sudo cat $CERT_MAKER_KEY_FILENAME"
if [ "$CERT_MAKER_LOCAL_MODE" == true ]; then
  echo ">>> Executing command locally"
  CERT_MAKER_CERTIFICATE=$(eval $CERT_MAKER_COMMAND)
  printf "<<< Executing command locally\n\n"
else
  echo ">>> Executing command on server $CERT_MAKER_HOST"
  CERT_MAKER_CERTIFICATE=`ssh -i $CERT_MAKER_PEM -l $CERT_MAKER_USER $CERT_MAKER_HOST $CERT_MAKER_COMMAND`
  printf "<<< Executing command on server $CERT_MAKER_HOST\n\n"
fi

echo ">>> Writing certificate to $CERT_MAKER_OUTPUT_FILENAME"
echo -e "$CERT_MAKER_CERTIFICATE" > $CERT_MAKER_OUTPUT_FILENAME
printf "<<< Writing certificate to $CERT_MAKER_OUTPUT_FILENAME\n\n"
