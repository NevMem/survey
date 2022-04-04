set -eu

mkdir tmp

cd ../../../
./gradlew backend:push:build --no-daemon
cd ./backend/worker/docker

mkdir build
cat $FIREBASE_SDK_KEY > build/firebase_sdk_key.json

cp ../build/distributions/push-0.0.1.zip ./tmp/

python3 ../../../tools/bundle_env_vars.py --configuration ./env_vars_config.json --output ./env.sh

echo cr.yandex/crp9sskk3fu52ukovaco/ethno-push:$(git rev-parse --short HEAD)
docker build . --network=host -t cr.yandex/crp9sskk3fu52ukovaco/ethno-push:$(git rev-parse --short HEAD)
docker push cr.yandex/crp9sskk3fu52ukovaco/ethno-push:$(git rev-parse --short HEAD)

rm -rf tmp
