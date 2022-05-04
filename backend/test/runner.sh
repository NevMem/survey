#!/usr/bin/env bash

set -eu

./wait-for-it.sh core:8080 -t 240

pytest -v --html=/report/tests_report.html --self-contained-html
