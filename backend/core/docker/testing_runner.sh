#!/usr/bin/env bash

set -eu

./wait-for-it.sh s3local:8000 -t 60
./wait-for-it.sh database:5432 -t 60

echo "starting core"
core-0.0.1/bin/core
