#!/bin/bash
#
# Downloads the public certificate from the a DataONE member node
# and adds it to the trusted certificates keystore. This is required
# to be able to connect to the server to POST data to it.
# See https://confluence.atlassian.com/display/JIRAKB/Unable+to+Connect+to+SSL+Services+due+to+PKIX+Path+Building+Failed+sun.security.provider.certpath.SunCertPathBuilderException for more information on how this works.
# See http://pythonhosted.org/dataone.generic_member_node/setup-local-authn-ca.html for how to get certificates into a dataONE (GMN) member node

NODE_ADDRESS=130.220.209.107
CERTIFICATE_PATH=/tmp/ecoinformatics-dataone-pub.cert
SERVER_NAME=ecoinformatics-dataone
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

echo "Downloading certificate to $CERTIFICATE_PATH"
openssl s_client -connect $NODE_ADDRESS:443 < /dev/null | sed -ne '/-BEGIN CERTIFICATE-/,/-END CERTIFICATE-/p' > $CERTIFICATE_PATH
echo "Adding trusted certificate..."
echo "  If you get prompted for a password, the default is: changeit"
$ARG_JAVA_HOME/keytool -import -alias $SERVER_NAME -keystore $SECURITY_DIR/cacerts -file $CERTIFICATE_PATH
echo "Done :D"
if [ "$(whoami)" != "root" ]; then
  echo "WARNING! You didn't run as sudo/root so if that failed, try again as a superuser"
fi
