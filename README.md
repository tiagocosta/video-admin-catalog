# video-admin-catalog 
This is a video catalog administration service that commnunicates with a video encoder service (https://github.com/tiagocosta/video-enconder). 

Basically, when a catalog admin user uploads a new video to a cloud bucket (GCP - Cloud Storage), a message is published in a queue (on RabbitMQ) in order to the video encoder service make his work.
Moreover, there is a Kafka Connect pluged in this service to replicate the video catalog data to others services that need it.

ps: this project is under development

## Dependencies
Java 17

Spring Boot 3

RabbitMQ

MySQL

Google Cloud Storage

Kafka Connect
