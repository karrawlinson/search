### Introduction

REST API to search movies using an Elasticsearch node loaded with a movie dataset. 

The data folder contains a dataset of approx 50K movies between 1970 and 2020

### Dependencies

Docker is required to run an elasticsearch node and Java 11 is required for the REST API service

Additionally Lombok is used to remove boilerplate code from the domain classes so you may need to configure Lombok for your IDE: https://www.baeldung.com/lombok-ide

### Getting Started

The repo contains a ``startup.sh`` folder for convenience which runs the following commands to startup the cluster/service and load the data:

```
docker run --name elasticsearch -p 9200:9200 -p 9300:9300 -d -e "discovery.type=single-node" docker.elastic.co/elasticsearch/elasticsearch:7.12.1

curl -X PUT 'http://localhost:9200/movies' -H 'Content-Type: application/json' --data-binary "@data/index.json"

curl -X POST http://localhost:9200/movies/_bulk -H 'Content-Type: application/json' --data-binary "@data/movies.json"

java -jar build/libs/search-0.0.1-SNAPSHOT.jar
```

The Elasticsearch node should then be available on port 9200 e.g. http://localhost:9200/movies/_search?q=star%20wars 

Similarly Swagger docs should be available at http://localhost:8080/swagger-ui.html

When finished the Java process can be stopped and the Elasticsearch node can be shutdown with ``docker rm -f elasticsearch``

### Sample Usage

The screenshots below show examples of using the Swagger UI to find movies

###### Autosuggest movies:
![plot](https://github.com/karrawlinson/search/raw/main/images/suggest.PNG)

###### Search based on title:
![plot](https://github.com/karrawlinson/search/raw/main/images/search.PNG)

###### Search similar movies:
![plot](https://github.com/karrawlinson/search/raw/main/images/similar.PNG)

###### Search movie details e.g. director and filter:
![plot](https://github.com/karrawlinson/search/raw/main/images/searchall.PNG)

### Reference Documentation
For further reference, please consider the following sections:

* [Official Gradle documentation](https://docs.gradle.org)
* [Spring Boot Gradle Plugin Reference Guide](https://docs.spring.io/spring-boot/docs/2.4.12/gradle-plugin/reference/html/)
* [Create an OCI image](https://docs.spring.io/spring-boot/docs/2.4.12/gradle-plugin/reference/html/#build-image)
* [Spring Boot DevTools](https://docs.spring.io/spring-boot/docs/2.5.6/reference/htmlsingle/#using-boot-devtools)
* [Spring Boot Actuator](https://docs.spring.io/spring-boot/docs/2.5.6/reference/htmlsingle/#production-ready)
* [Spring Data Elasticsearch (Access+Driver)](https://docs.spring.io/spring-boot/docs/2.5.6/reference/htmlsingle/#boot-features-elasticsearch)
* [Config Client Quick Start](https://docs.spring.io/spring-cloud-config/docs/current/reference/html/#_client_side_usage)
* [Vault Client Quick Start](https://docs.spring.io/spring-cloud-vault/docs/current/reference/html/#client-side-usage)