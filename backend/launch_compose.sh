docker-compose down
cd ../
./gradlew backend:core:build
cd backend
docker-compose build
docker-compose up -d database
docker-compose up -d s3local
docker-compose up -d core