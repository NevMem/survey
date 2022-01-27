set -eu

mkdir tmp

cd ../../../
./gradlew backend:worker:build --no-daemon
cd ./backend/worker/docker

cp ../build/distributions/worker-0.0.1.zip ./tmp/

python3 ../../../tools/bundle_env_vars.py --configuration ./env_vars_config.json --output ./env.sh

echo cr.yandex/crp9sskk3fu52ukovaco/ethno-worker:$(git rev-parse --short HEAD)
docker build . --network=host -t cr.yandex/crp9sskk3fu52ukovaco/ethno-worker:$(git rev-parse --short HEAD)
docker push cr.yandex/crp9sskk3fu52ukovaco/ethno-worker:$(git rev-parse --short HEAD)

rm -rf tmp
