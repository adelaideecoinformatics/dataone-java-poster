#!/bin/sh
set -e
cd `dirname "$0"`/..
python -m pytest
