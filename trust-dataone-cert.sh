#!/bin/bash
#
# Downloads the public certificate from the a DataONE member node
# and adds it to the trusted certificates keystore. This is required
# to be able to connect to the server to POST data to it.
# You can re-run this script as many times as you want, it'll handle an existing alias and remove it
# See https://confluence.atlassian.com/display/JIRAKB/Unable+to+Connect+to+SSL+Services+due+to+PKIX+Path+Building+Failed+sun.security.provider.certpath.SunCertPathBuilderException for more information on how this works.
# See http://pythonhosted.org/dataone.generic_member_node/setup-local-authn-ca.html for how to get certificates into a dataONE (GMN) member node

NODE_ADDRESS=130.220.209.107
CERTIFICATE_PATH=/tmp/x509up_u1000
SERVER_NAME=ecoinformatics-dataone
DEFAULT_KEYSTORE_PASSWD=changeit
ARG_JAVA_HOME=$1
if [ -z "$ARG_JAVA_HOME" ]; then
  echo "Error! You need to tell us the path to the JRE 'bin' directory."
  echo " This directory needs to contain the 'keytool' binary and ../lib/security/cacerts will exist"
  echo "  usage: $0 <path/to/java/bin>"
  echo "  e.g: $0 /usr/lib/jvm/java-7-oracle/jre/bin"
  exit 1
fi

SECURITY_DIR=$ARG_JAVA_HOME/../lib/security
if [ ! -d "$SECURITY_DIR" ]; then
  echo "Error! The passed java bin directory doesn't have $SECURITY_DIR as a valid relative path"
  echo "  Have you checked inside the jre/ directory in the java install?"
  exit 1
fi

function runKeytoolCommand {
  COMMAND=$1
  EXTRA_INPUT=$2
  #>&2 echo $COMMAND
  printf "$DEFAULT_KEYSTORE_PASSWD\n$EXTRA_INPUT\n" | $ARG_JAVA_HOME/keytool -keystore $SECURITY_DIR/cacerts $COMMAND
  >&2 printf "(Entered default password of \"$DEFAULT_KEYSTORE_PASSWD\" for you)\n"
}

echo ">>> Download certificate to $CERTIFICATE_PATH"
openssl s_client -connect $NODE_ADDRESS:443 < /dev/null | sed -ne '/-BEGIN CERTIFICATE-/,/-END CERTIFICATE-/p' > $CERTIFICATE_PATH
printf "<<< Download certificate\n\n"
echo ">>> Check already added alises"
ALREADY_LISTED_ALIASES_COUNT=`runKeytoolCommand "-list" | grep $SERVER_NAME | wc -l`
if [ "$ALREADY_LISTED_ALIASES_COUNT" == "1" ]; then
  echo "Alias already listed. Removing it so we can re-add it."
  runKeytoolCommand "-delete -alias $SERVER_NAME"
fi
printf "<<< Check already added alises\n\n"
echo ">>> Adding trusted certificate"
echo "using the default password of: \"$DEFAULT_KEYSTORE_PASSWD\" and automatically trusting it"
runKeytoolCommand "-import -alias $SERVER_NAME -file $CERTIFICATE_PATH" "yes"
printf "<<< Adding trusted certificate\n\n"
echo "Script completed :D"
if [ "$(whoami)" != "root" ]; then
  echo "WARNING! You didn't run as sudo/root so if that failed, try again as a superuser"
fi
