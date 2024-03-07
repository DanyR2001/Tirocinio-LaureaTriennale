docker-compose -f ./OLAP/dockercompose.yml stop 
sleep 5
echo "pass"
docker-compose -f ./SUPERSET/superset/docker-compose-non-dev.yml stop 
sleep 5
echo "pass"
docker-compose -f ./ETL/ETL-Parser/docker-compose.yml stop 
sleep 5
echo "Stop complited"