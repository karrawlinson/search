printf "\n\n Starting elasticsearch node ..."
docker run --name elasticsearch -p 9200:9200 -p 9300:9300 -d -e "discovery.type=single-node" docker.elastic.co/elasticsearch/elasticsearch:7.12.1

printf "\n\n Pausing to allow startup ..."
sleep 30  

printf "\n\n Creating index ..."
curl -X PUT 'http://localhost:9200/movies' -H 'Content-Type: application/json' --data-binary "@data/index.json"

printf "\n\n Bulk loading movies ..."
curl -X POST http://localhost:9200/movies/_bulk -H 'Content-Type: application/json' --data-binary "@data/movies.json"

printf "\n\n Starting up the service ..."
java -jar build/libs/search-0.0.1-SNAPSHOT.jar
