
# 🏥 MedSync Healthcare System - **Tech Challenge 3ª Fase**

## 📑 ÍNDICE

* [Descrição do Projeto](#descrição-do-projeto)
* [Funcionalidades e Endpoints](#funcionalidades-e-endpoints)
   * [📅 Scheduling Service](#-scheduling-service)
   * [📨 Notification Service](#-notification-service)
   * [📖 History Service (GraphQL)](#-history-service-graphql)
* [Tecnologias Utilizadas](#tecnologias-utilizadas)
* [Estrutura do Projeto](#estrutura-do-projeto)
* [Clean Architecture](#clean-architecture)
* [Diagrama de Arquitetura](#diagrama-de-arquitetura)
* [Diagrama das tabelas de banco de dados](#diagrama-das-tabelas-de-banco-de-dados)
* [Requisitos](#requisitos)
* [Como Rodar o Projeto](#como-rodar-o-projeto)
* [Documentação da API](#documentação-da-api)
* [Cobertura de código](#cobertura-de-código)
* [Collection POSTMAN](#collection-postman)
* [Environment POSTMAN](#environment-postman)

---

## 📌 Descrição do Projeto

O **MedSync Healthcare System** é uma API modular e containerizada que oferece:

* Agendamento de consultas médicas.
* Envio de notificações automáticas a pacientes.
* Exposição do histórico clínico via **GraphQL**.
* Comunicação assíncrona entre serviços via **RabbitMQ**.

O sistema contempla perfis distintos (**DOCTOR, NURSE, PATIENT**) com permissões específicas conforme os requisitos do desafio.

---

## ⚙️ Funcionalidades e Endpoints

### 📅 Scheduling Service

| Operação                 | Descrição                           | Acesso               |
| ------------------------ | ----------------------------------- | -------------------- |
| `POST /appointments`     | Criar nova consulta                 | DOCTOR/NURSE         |
| `PUT /appointments/{id}` | Editar uma consulta existente       | DOCTOR/NURSE         |
| `GET /appointments/{id}` | Visualizar detalhes de uma consulta | DOCTOR/NURSE/PATIENT |
| `GET /appointments`      | Listar consultas (com filtros)      | DOCTOR/NURSE/PATIENT |

---

### 📨 Notification Service

| Operação                    | Descrição                                    | Acesso  |
| --------------------------- | -------------------------------------------- | ------- |
| Recebe eventos via RabbitMQ | Processa mensagens de agendamento/editadas   | Interno |
| `GET /notifications/{id}`   | Consulta notificações enviadas a um paciente | DOCTOR  |

---

### 📖 History Service (GraphQL)

| Operação (GraphQL)          | Descrição                               | Acesso               |
| --------------------------- | --------------------------------------- | -------------------- |
| `patientHistory(patientId)` | Retorna histórico completo do paciente  | DOCTOR/NURSE/PATIENT |
| `appointments(patientId)`   | Retorna consultas agendadas ou passadas | DOCTOR/NURSE/PATIENT |

---

## 🛠️ Tecnologias Utilizadas

![Java 21](https://img.shields.io/badge/Java-21-ED8B00?style=for-the-badge\&logo=java\&logoColor=white)
![Maven](https://img.shields.io/badge/Maven-C71A36?style=for-the-badge\&logo=apachemaven\&logoColor=white)
![Spring Boot](https://img.shields.io/badge/Spring_Boot-6DB33F?style=for-the-badge\&logo=spring-boot\&logoColor=white)
![Docker](https://img.shields.io/badge/Docker-2496ED?style=for-the-badge\&logo=docker\&logoColor=white)
![RabbitMQ](https://img.shields.io/badge/RabbitMQ-FF6600?style=for-the-badge\&logo=rabbitmq\&logoColor=white)
![Mailtrap](https://img.shields.io/badge/Mailtrap-00B3E6?style=for-the-badge\&logo=mailtrap&logoColor=white)
![PostgreSQL](https://img.shields.io/badge/PostgreSQL-316192?style=for-the-badge\&logo=postgresql\&logoColor=white)
![GraphQL](https://img.shields.io/badge/GraphQL-E10098?style=for-the-badge\&logo=graphql\&logoColor=white)
![Postman](https://img.shields.io/badge/Postman-FF6C37?style=for-the-badge\&logo=postman\&logoColor=white)

---

## 📂 Estrutura do Projeto

```
medsync-healthcare-system/
├── scheduling-service/     # Microsserviço de agendamento (Java + Spring Boot)
├── notification-service/   # Microsserviço de notificações (Java + Spring Boot)
├── history-service/        # Microsserviço de histórico (GraphQL + Java)
├── docker-compose.yml      # Orquestração dos serviços
├── start.sh                # Script de inicialização
├── db/01-init.sql          # Script de criação do banco
└── collection/             # Collections e environments do Postman
```

### 🧹 Clean Architecture
Cada microsserviço segue os princípios da Clean Architecture, garantindo separação de concerns e testabilidade.


#### scheduling-service
```
scheduling-service/
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── br/com/fiap/postech/medsync/scheduling/
│   │   │       │
│   │   │       ├── application/
│   │   │       │   ├── controllers/
│   │   │       │   │   └── AppointmentController.java
│   │   │       │   ├── dtos/
│   │   │       │   │   ├── requests/
│   │   │       │   │   │   ├── CreateAppointmentRequest.java
│   │   │       │   │   │   └── UpdateAppointmentRequest.java
│   │   │       │   │   └── responses/
│   │   │       │   │       └── AppointmentResponse.java
│   │   │       │   └── gateways/
│   │   │       │       └── NotificationGateway.java
│   │   │       │
│   │   │       ├── domain/
│   │   │       │   ├── entities/
│   │   │       │   │   └── Appointment.java
│   │   │       │   ├── gateways/
│   │   │       │   │   └── AppointmentRepositoryGateway.java
│   │   │       │   └── usecases/
│   │   │       │       ├── CreateAppointmentUseCase.java
│   │   │       │       ├── UpdateAppointmentUseCase.java
│   │   │       │       └── CancelAppointmentUseCase.java
│   │   │       │
│   │   │       └── infrastructure/
│   │   │           ├── config/
│   │   │           │   ├── dependency/
│   │   │           │   ├── security/
│   │   │           │   └── RabbitMQConfig.java
│   │   │           ├── exceptions/
│   │   │           │   └── handler/
│   │   │           ├── persistence/
│   │   │           │   ├── entity/
│   │   │           │   │   └── AppointmentJpaEntity.java
│   │   │           │   └── repository/
│   │   │           │       └── AppointmentRepository.java
│   │   │           └── messaging/
│   │   │               └── RabbitMQNotificationGateway.java
│   │   │
│   │   └── resources/
│   │       ├── application.properties
│   │       └── db/migration/
│   │
│   └── test/
│       ├── java/... (estrutura espelhada)
│       └── resources/
├── Dockerfile
└── pom.xml
```

#### notification-service
```
notification-service/
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── br/com/fiap/postech/medsync/notification/
│   │   │       ├── application/
│   │   │       │   └── usecases/
│   │   │       │       ├── CreateNotificationUseCase.java    	 # Interfaces
│   │   │       │       ├── CreateNotificationUseCaseImp.java
│   │   │       │       ├── SendNotificationUseCase.java      	 # Interfaces
│   │   │       │       ├── SendNotificationUseCaseImp.java
│   │   │       │       ├── UpdateNotificationStatusUseCase.java # Interfaces
│   │   │       │       └── UpdateNotificationStatusUseCaseImp.java
│   │   │       │
│   │   │       ├── domain/
│   │   │       │   └── entities/
│   │   │       │   │   ├── Notification.java
│   │   │       │   │   └── NotificationStatus.java
│   │   │       │   └── gateways/
│   │   │       │       ├── NotificationGateway.java			 # Interfaces
│   │   │       │       └── EmailNotificationGateway.java		 # Interfaces
│   │   │       │
│   │   │       └── infrastructure/
│   │   │           ├── config/
│   │   │           │   ├── RabbitMQConfig.java
│   │   │           │   └── EmailConfig.java
│   │   │           ├── exceptions/
│   │   │           │   └── handler/
│   │   │           │ 		└── GlobalExceptionHandler.java
│   │   │           ├── persistence/
│   │   │           │   ├── entity/
│   │   │           │   │   └── NotificationEntity.java
│   │   │           │   └── repository/
│   │   │           │       └── NotificationRepository.java
│   │   │           ├── gateways/
│   │   │           │   ├── NotificationGatewayImp.java        
│   │   │           │   └── EmailNotificationGatewayImpl.java
│   │   │           └── messaging/
│   │   │               └── NotificationMessageConsumer.java 
│   │   │
│   │   └── resources/
│   │       └── application.properties
│   │
│   └── test/
└── pom.xml
```

##### history-service (GraphQL)
```
history-service/
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── br/com/fiap/postech/medsync/history/
│   │   │       │
│   │   │       ├── application/
│   │   │       │   ├── controllers/
│   │   │       │   │   └── MedicalRecordGraphQLController.java
│   │   │       │   ├── dtos/
│   │   │       │   │   └── responses/
│   │   │       │   │       └── MedicalRecordResponse.java
│   │   │       │   └── gateways/
│   │   │       │       └── SchedulingGateway.java
│   │   │       │
│   │   │       ├── domain/
│   │   │       │   ├── entities/
│   │   │       │   │   └── MedicalRecord.java
│   │   │       │   ├── gateways/
│   │   │       │   │   └── MedicalRecordRepositoryGateway.java
│   │   │       │   └── usecases/
│   │   │       │       ├── GetPatientHistoryUseCase.java
│   │   │       │       └── GetMedicalRecordUseCase.java
│   │   │       │
│   │   │       └── infrastructure/
│   │   │           ├── config/
│   │   │           │   └── GraphQLConfig.java
│   │   │           ├── persistence/
│   │   │           │   ├── entity/
│   │   │           │   │   └── MedicalRecordJpaEntity.java
│   │   │           │   └── repository/
│   │   │           │       └── MedicalRecordRepository.java
│   │   │           └── resolvers/
│   │   │               └── MedicalRecordResolver.java
│   │   │
│   │   └── resources/
│   │       ├── application.properties
│   │       └── graphql/
│   │           └── medicalRecord.graphqls
│   │
│   └── test/... (estrutura espelhada)
├── Dockerfile
└── pom.xml
```

---

## 📡 Diagrama de Arquitetura

![Diagrama de Arquitetura](images/docker-network.png)

---

## 🗄️ Diagrama das tabelas de banco de dados

![Diagrama de banco de dados](images/diagrama-db.png)

> O script [`01-init.sql`](db/01-init.sql) define schemas **scheduling** e **history**, incluindo entidades de usuários, pacientes, médicos, consultas e histórico clínico.

---

## 📋 Requisitos

* [Java 21](https://www.oracle.com/java/technologies/javase/jdk21-archive-downloads.html)
* [Maven](https://maven.apache.org/)
* [Docker](https://www.docker.com/)
* [Postman](https://www.postman.com/)

---

## ▶️ Como Rodar o Projeto

1. **Clone o repositório:**

   ```bash
   git clone https://github.com/luizffdemoraes/medsync-healthcare-system
   cd medsync-healthcare-system
   ```

2. **Execute o script de inicialização:**

   ```bash
   ./start.sh
   ```

3. **Acesse os serviços:**

   * Scheduling: [http://localhost:8080](http://localhost:8080)
   * History (GraphQL): [http://localhost:8081/graphql](http://localhost:8081/graphql)
   * Notification: [http://localhost:8082](http://localhost:8082)
   * RabbitMQ Console: [http://localhost:15672](http://localhost:15672)

---

## 📖 Documentação da API

A documentação de endpoints estará disponível via Swagger UI nos serviços REST:

* [http://localhost:8080/swagger-ui/index.html](http://localhost:8080/swagger-ui/index.html)
* [http://localhost:8082/swagger-ui/index.html](http://localhost:8082/swagger-ui/index.html)

---

## 📊 Cobertura de código

Gerada com **JaCoCo**.

```bash
mvn clean test
mvn jacoco:report
```

O relatório estará disponível em:

```
target/site/jacoco/index.html
```

### Exemplo da cobertura gerada:

### notification-service

![notification-coverage](images/notification-coverage.png)

---

## 🧪 Collection POSTMAN

Arquivo disponível em `collection/medsync-healthcare.postman_collection.json`.

## 🌍 Environment POSTMAN

Arquivo disponível em `collection/medsync-healthcare.postman_environment.json`.

---

🔒 **Perfis de Usuários**

| Role    | Permissões                                                           |
| ------- | -------------------------------------------------------------------- |
| DOCTOR  | Visualizar e editar histórico, registrar consultas.                  |
| NURSE   | Registrar consultas e acessar histórico.                             |
| PATIENT | Visualizar apenas suas consultas e receber notificações automáticas. |

---
