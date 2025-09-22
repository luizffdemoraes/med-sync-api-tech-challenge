#!/bin/bash
echo "Iniciando MedSync Healthcare System..."
docker-compose up --build -d
echo "Sistema iniciado! Acessos:"
echo "Scheduling: http://localhost:8080"
echo "History: http://localhost:8081/graphql"
echo "Notification: http://localhost:8082"
echo "RabbitMQ: http://localhost:15672"