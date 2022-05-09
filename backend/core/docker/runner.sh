#!/usr/bin/env bash

source env.sh

set -eu

cp nginx-production-config.conf /etc/nginx/nginx.conf
rm /etc/nginx/sites-enabled/default

# cp certificate_full_chain.pem /etc/nginx/certificate_full_chain.pem
# cp private_key.pem /etc/nginx/private_key.pem

ls -la /etc/nginx/

cat /etc/nginx/nginx.conf

echo "starting nginx"
nginx
echo "done"

echo "starting core"
/core/bin/core
