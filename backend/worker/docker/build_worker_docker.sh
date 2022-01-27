mkdir tmp

cp ../backend/worker/build/distributions/worker-0.0.1.zip ./tmp/

python3 ../tools/bundle_env_vars.py ./env_vars_config.sh ./env.sh

docker build . --network=host -t cr.yandex/crp9sskk3fu52ukovaco/ethno-metrics:$(git rev-parse --short HEAD)
docker push cr.yandex/crp9sskk3fu52ukovaco/ethno-metrics:$(git rev-parse --short HEAD)

rm -rf tmp
