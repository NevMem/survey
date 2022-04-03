set -eu

docker-compose down
cd ../
./gradlew backend:core:build
./gradlew backend:worker:build
./gradlew backend:push:build
cd backend
docker-compose build
docker-compose up -d
