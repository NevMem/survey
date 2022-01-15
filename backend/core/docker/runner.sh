#!/usr/bin/env bash

source env.sh

set -eu

cp nginx-production-config.conf /etc/nginx/sites-enabled/ethno.conf
cat /etc/nginx/sites-enabled/ethno.conf
rm /etc/nginx/sites-enabled/default

echo "starting nginx"
nginx
echo "done"

echo "starting core"
/core/bin/core
