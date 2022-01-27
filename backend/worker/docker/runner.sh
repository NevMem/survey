#!/usr/bin/env bash

set -eu

source env.sh

echo "starting worker"
worker-0.0.1/bin/worker
