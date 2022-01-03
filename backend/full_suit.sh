docker-compose down
docker-compose build
docker-compose up -d database
docker-compose up -d s3local
sleep 1
docker-compose up -d core
sleep 1
docker-compose run test
