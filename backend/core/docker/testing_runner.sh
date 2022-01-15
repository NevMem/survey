#!/usr/bin/env bash

set -eu

cp nginx-config.conf /etc/nginx/sites-enabled/ethno.conf
cat /etc/nginx/sites-enabled/ethno.conf
rm /etc/nginx/sites-enabled/default

echo "starting nginx"
nginx
echo "done"

echo "starting core"
core-0.0.1/bin/core
