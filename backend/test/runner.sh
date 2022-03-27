#!/usr/bin/env bash

set -eu

./wait-for-it.sh core:80 -t 240

pytest -vs
