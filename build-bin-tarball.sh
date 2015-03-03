#!/bin/bash
# Creates a tarball with everything required to run standalone. Useful for pushing to a remote box to run
cd `dirname $0`
OUTPUT_FILE=target/EmlPoster-bin.tar.gz
tar czf $OUTPUT_FILE \
  target/eml-poster-dependencies-1.0-SNAPSHOT.jar \
  target/eml-poster-1.0-SNAPSHOT.jar \
  launch-eml-poster.sh \
  run-for-all-xml-in-dir.sh \
  trust-dataone-cert.sh \
  build-certificate.sh \
  create-x509-cert.txt

echo "Created archive at `pwd`/$OUTPUT_FILE"
