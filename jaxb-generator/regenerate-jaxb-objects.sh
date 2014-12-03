#!/bin/bash
#
# A bash script to automate the process of generating the JAXB objects
# from the EML XSDs.
#
# author: Tom Saleeba
# date: 2 Dec 2014
cd `dirname $0`
EML_API_ROOT=`cd .. && pwd`
# Settings specific to the version of EML we support \/
EML_XSD_ARCHIVE=$EML_API_ROOT/xsd/eml-2.1.1.tar.gz
EML_XSD_ARCHIVE_SUBDIR_PATH=eml-2.1.1
EML_XSD_MAIN_FILE=eml.xsd
# Settings specific to the version of EML we support /\
JAXB_WORKING_DIR=$EML_API_ROOT/target/jaxb_working_dir
BINDING_FILE=custom-bindings.xjb
JAXB_OUTPUT=$EML_API_ROOT/src/main/java
JAXB_PACKAGE=au.org.ecoinformatics.eml.jaxb
JAXB_PACKAGE_PATH=`echo $JAXB_PACKAGE | sed -e "s/\./\//g" -`

# set up our working space
mkdir -p $JAXB_WORKING_DIR
mkdir -p $JAXB_OUTPUT/$JAXB_PACKAGE_PATH
tar xzf $EML_XSD_ARCHIVE -C $JAXB_WORKING_DIR
cp $BINDING_FILE $JAXB_WORKING_DIR/$EML_XSD_ARCHIVE_SUBDIR_PATH
cd $JAXB_WORKING_DIR/$EML_XSD_ARCHIVE_SUBDIR_PATH
# clean any existing generated classes (hope you didn't manually modify them)
rm -f $JAXB_OUTPUT/$JAXB_PACKAGE_PATH/*
# generate the new JAXB objects
xjc -p $JAXB_PACKAGE -d $JAXB_OUTPUT -b $BINDING_FILE -extension $EML_XSD_MAIN_FILE
