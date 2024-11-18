docker-compose down

docker build -t opdee:latest ./

docker-compose up --build --force-recreate --remove-orphans