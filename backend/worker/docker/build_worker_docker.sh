set -eu

mkdir tmp

cd ../../../
./gradlew backend:worker:build
cd ./backend/worker/docker

cp ../backend/worker/build/distributions/worker-0.0.1.zip ./tmp/

python3 ../tools/bundle_env_vars.py ./env_vars_config.sh ./env.sh

echo cr.yandex/crp9sskk3fu52ukovaco/ethno-worker:$(git rev-parse --short HEAD)
docker build . --network=host -t cr.yandex/crp9sskk3fu52ukovaco/ethno-worker:$(git rev-parse --short HEAD)
docker push cr.yandex/crp9sskk3fu52ukovaco/ethno-worker:$(git rev-parse --short HEAD)

rm -rf tmp
