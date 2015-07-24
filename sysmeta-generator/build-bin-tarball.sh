#!/bin/bash
# Creates a tarball with everything required to run standalone. Useful for pushing to a remote box to run
cd `dirname $0`
OUTPUT_FILE=target/sysmeta-generator-bin.tar.gz
mvn clean package -Pshade
tar czf $OUTPUT_FILE --transform 's,^,sysmeta-generator/,' \
  target/sysmeta-generator-dependencies-1.0-SNAPSHOT.jar \
  target/sysmeta-generator-1.0-SNAPSHOT.jar \
  launch-generator.sh

if [ "$?" != "0" ]; then
  exit $?
fi

echo "Created archive at `pwd`/$OUTPUT_FILE"
