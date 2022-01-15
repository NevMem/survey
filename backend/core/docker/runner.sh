#!/usr/bin/env bash

source env.sh

set -eu

cp nginx-production-config.conf /etc/nginx/sites-enabled/ethno.conf
cat /etc/nginx/sites-enabled/ethno.conf
rm /etc/nginx/sites-enabled/default

ls -la

cp certificate_full_chain.pem /etc/nginx/certificate_full_chain.pem
cp private_key.pem /etc/nginx/private_key.pem

ls -la /etc/nginx/

echo "starting nginx"
nginx
echo "done"

echo "starting core"
/core/bin/core
