mkdir tmp

pip3 install -r requirements.txt

python3 ../../tools/generate_metrics_yml.py

docker build . --network=host -t cr.yandex/crp9sskk3fu52ukovaco/ethno-metrics:$(git rev-parse --short HEAD)
docker push cr.yandex/crp9sskk3fu52ukovaco/ethno-metrics:$(git rev-parse --short HEAD)

rm -rf tmp
