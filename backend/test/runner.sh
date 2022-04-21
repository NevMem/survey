#!/usr/bin/env bash

set -eu

./wait-for-it.sh core:8080 -t 240

pytest -vs -m "v2"
