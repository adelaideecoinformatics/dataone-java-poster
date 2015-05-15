#!/bin/bash
# Creates a tarball with everything required to run standalone. Useful for pushing to a remote box to run
cd `dirname $0`
mvn clean package -Pshade
OUTPUT_FILE=target/eml-poster-bin.tar.gz
tar czf $OUTPUT_FILE --transform 's,^,eml-poster/,' \
  target/eml-poster-dependencies-1.0-SNAPSHOT.jar \
  target/eml-poster-1.0-SNAPSHOT.jar \
  launch-eml-poster.sh \
  run-for-all-xml-in-dir.sh \
  rename-sysmeta-to-suit-eml-poster.sh \
  trust-dataone-cert.sh \
  build-certificate.sh \
  suffix-package-ids.sh \
  create-x509-cert.txt
if [ "$?" != "0" ]; then
  exit $?
fi
echo "Created archive at `pwd`/$OUTPUT_FILE"
