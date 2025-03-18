docker network create videos_adm_services

mkdir -m 777 .docker
mkdir -m 777 .docker/keycloak

docker compose -f services/docker-compose.yml up -d

echo "starting containers..."
sleep 20