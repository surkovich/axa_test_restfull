#!/bin/bash
echo starting maria DB instance

# run server
docker compose up

docker inspect -f '{{range .NetworkSettings.Networks}}{{.IPAddress}}{{end}}' mariadb_axa