#!/bin/bash

docker network create retecondivisa 
echo "pass"
docker-compose -f ./OLAP/dockercompose.yml up -d &
sleep 5
echo "pass"
docker-compose -f ./SUPERSET/superset/docker-compose-non-dev.yml up -d &
sleep 5
echo "pass"
docker-compose -f ./ETL/ETL-Parser/docker-compose.yml up -d &
sleep 5
echo "Start-up complited"
