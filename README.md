Start dependencies using docker compose
1. cd src/main/resources/docker
2. docker-compose up

Start application nodes
1. Start node 1 on port 8081 and configure it to run mock scheduler by passing jvm arguments as shown below<br>
   <i>-Dserver.port=8081 -Daccount-transition-cron-expr="0/5 * * ? * *"</i>
2. Start node 2 on port 8082<br>
    <i>-Dserver.port=8082</i>
    