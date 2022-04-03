#!/usr/bin/env bash

set -eu

./wait-for-it.sh database:5432 -t 60

echo "starting push"
push-0.0.1/bin/push
