#!/bin/bash
# Automates creating the jOAI server config because driving UIs by hand in a docker world is lame.
#
# This script is sort of idempotent in that it won't keep creating duplicates. You can only have one
# repo per filesystem path. So you can keep re-running the script but it *won't* update exist entries,
# only create new ones.
set -e

globalUrlPrefix=$1
if [ -z "$globalUrlPrefix" ]; then
  echo '[ERROR] no base URL to jOAI instance passed as param 1'
  echo "usage: $0 <base url>"
  echo "   eg: $0 localhost:38080"
  exit 1
fi
echo "[INFO] using base url=$globalUrlPrefix"

function createHarvester {
  local \
    urlPrefix \
    repoName \
    repoBaseUrl \
    metadataPrefix \
    harvestDir
  local "${@}"
  echo "[INFO] creating repo '$repoName'"
  curl --silent "$urlPrefix/admin/harvester.do" \
   -X POST \
   -F 'shUid=0' \
   -F 'scheduledHarvest=save' \
   -F "shRepositoryName=$repoName" \
   -F "shBaseURL=$repoBaseUrl" \
   -F 'shSetSpec=' \
   -F "shMetadataPrefix=$metadataPrefix" \
   -F 'shEnabledDisabled=enabled' \
   -F 'shHarvestingInterval=2' \
   -F 'shIntervalGranularity=days' \
   -F 'shRunAtTime=03:00' \
   -F 'shDir=custom' \
   -F "shHarvestDir=$harvestDir" \
   -F 's=+' \
   -F 'shDontZipFiles=true' \
   -F 'shSet=dontsplit' > /dev/null
  echo "[INFO] successfully created repo '$repoName'"
}

createHarvester \
  urlPrefix=$globalUrlPrefix \
  repoName="AEKOS" \
  repoBaseUrl='http://oai.ecoinformatics.org.au:8080/oai/provider' \
  metadataPrefix=eml \
  harvestDir=/data/aekos

createHarvester \
  urlPrefix=$globalUrlPrefix \
  repoName="TERN ACEF" \
  repoBaseUrl='https://acef.tern.org.au/geonetwork/srv/eng/oaipmh' \
  metadataPrefix=iso19139 \
  harvestDir=/data/tern-acef

createHarvester \
  urlPrefix=$globalUrlPrefix \
  repoName="TERN Auscover" \
  repoBaseUrl='http://data.auscover.org.au/geonetwork/srv/en/oaipmh' \
  metadataPrefix=iso19139 \
  harvestDir=/data/tern-auscover

createHarvester \
  urlPrefix=$globalUrlPrefix \
  repoName="TERN LTERN" \
  repoBaseUrl='https://www.ltern.org.au/knb/dataProvider' \
  metadataPrefix=eml \
  harvestDir=/data/tern-ltern

createHarvester \
  urlPrefix=$globalUrlPrefix \
  repoName="TERN Supersites" \
  repoBaseUrl='https://supersites.tern.org.au/knb/dataProvider' \
  metadataPrefix=eml \
  harvestDir=/data/tern-supersites

echo '[INFO] all done'
