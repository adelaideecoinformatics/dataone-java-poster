#!/bin/bash
# Creates a tarball with everything required to run standalone. Useful for pushing to a remote box to run
OUTPUT_FILE=target/SysMetaGenerator-bin.tar.gz
mvn clean package -Pshade
tar czf $OUTPUT_FILE --transform 's,^,sysmeta-generator/,' \
  target/SysMetaGenerator-dependencies-1.0-SNAPSHOT.jar \
  target/SysMetaGenerator-1.0-SNAPSHOT.jar \
  launch-generator.sh \
  run-for-all-xml-in-dir.sh

if [ "$?" != "0" ]; then
  exit $?
fi

echo "Created archive at $OUTPUT_FILE"
