set -eu

docker-compose down
cd ../
./gradlew backend:core:build
./gradlew backend:worker:build
./gradlew backend:push:build
cd backend
docker-compose build
docker-compose up -d database
docker-compose up -d s3local
docker-compose up -d core
docker-compose up -d worker
rm -rf ./report
mkdir ./report
docker-compose run test
docker-compose down
