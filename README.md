# video-admin-catalog 
This is a video catalog administration service that communicates with the video encoder service (https://github.com/tiagocosta/video-enconder). 

Basically, when a catalog admin user uploads a new video to a cloud bucket (GCP - Cloud Storage), a message is published in a queue (on RabbitMQ). 
When the message arrives, the video encoder service (that is listening to that queue) downloads the new video file from the bucket to encode it.
After that, the encoder service publishes a message informing that the encoding process has been completed. The
catalog administration service now knows that the video is ready to be streamed and updates its status.

This project uses concepts of Domain Driven Design and Clean Architecture.

## Dependencies
- Java 17
- Spring Boot 3
- RabbitMQ
- MySQL
- Flyway
- Test-containers
- Google Cloud Storage
- Elastic Stack (Elasticsearch, Kibana, Logstash and Filebeat)