# scopriamoilpiemonte-user-service
ScopriamoIlPiemonte User Microservice Repository

## Docker-compose example file
<pre><code>version: "3.7"
services:

   db-utenti:
     image: postgres:latest
     environment:
       POSTGRES_USER: poi
       POSTGRES_PASSWORD: poi
       POSTGRES_DB: Piemonte
     volumes:
       - db-utenti
     ports:
       - "5433:5433"
   
   servizio-utenti:
     image: dangerbaldo/scopriamoilpiemonte-user-service:latest
     container_name: servizio-utenti
     expose:
       - 8081
     ports:
       - 8081:9090
     volumes:
       - servizio-utenti:/usr/app/

   

volumes:
   db-utenti:
   servizio-utenti:
</code></pre>
## Installation
<pre><code>How to run it with docker-compose:
$> docker-compose up
</code></pre>