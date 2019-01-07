#!/bin/bash
set -e
cd `dirname "$0"`/..
docker build -t ternaustralia/ecoinf-dataone:1.0.5 .

