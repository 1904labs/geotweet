# GeoTweet 

This program is intended to capture tweets from twitter and write them to a kafka topic.  

## Run Script 

Executing the start.sh script in the same directory as this README.md file will start the program running 
It can be run locally or configured as a docker container and run with a docker-compose script. 

## Configuration 

The run script assumes there will be a properties file in the same diretory that it is located in. 
The run script will not be checked in it needs the following properties and as this changes we will update here. 

| Key 				| Value							| Description			|
| ----------------------------- | ----------------------------------------------------- | ----------------------------- |
| API_KEY			| Provided from twitter account.			| https://developer.twitter.com/en/docs/basics/authentication/oauth-1-0a |
| API_SECRET_KEY		| Provided from twitter account. 			| https://developer.twitter.com/en/docs/basics/authentication/oauth-1-0a |
| ACCESS_TOKEN			| Provided from twitter account.			| https://developer.twitter.com/en/docs/basics/authentication/oauth-1-0a |
| ACCESS_TOKEN_SECRET		| Provided from twitter account.			| https://developer.twitter.com/en/docs/basics/authentication/oauth-1-0a |
| KAFKA_SERVERS			| Variable depending on development environment.	| http://kafka.apache.org/documentation.html#producerconfigs |
| KAFKA_TOPIC			| Variable depending on development environment.	| http://kafka.apache.org/documentation.html#producerconfigs |
| KAFKA_ACKS_CONFIG		| Variable depending on development environment.	| http://kafka.apache.org/documentation.html#producerconfigs |
| KAFKA_LINGER_MS_CONFIG	| Variable depending on development environment.	| http://kafka.apache.org/documentation.html#producerconfigs |

All of these properties can be provided by a -Dproperty.file=geotweet.properties

The format of this file is in [java properties](https://docs.oracle.com/javase/tutorial/essential/environment/properties.html) file format.



