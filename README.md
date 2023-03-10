# employee-management-service

## Description

This service will be responsible for persisting employees in a database and produce a kafka event whenever an employee is changed (updated/ deleted/ created).  

It will have REST API endpoints for fetching employee list or an individual employee as well as an endpoint for creating, deleting and updating an employee. So basically a CRUD operation for employee.

## How to install and run the project

### Prerequisites

- Java 11

### Installation

1. Clone this repo and execute `./mvnw spring-boot:run`. Or inside an IDE, run the class EmployeeManagementServiceApp
    
   ```sh
    git clone https://github.com/micflib/employee-management-service.git
    ```
2. Build Maven - The service uses Maven as a build automation tool.  To build, run `./mvnw clean install`

### Running the service locally

In order to run this service locally, you can use the provided Docker Compose file to start:
- an instance of MongoDB 
- a one-node Kafka Cluster

`docker-compose up -d`

Then start the service selecting the `local` Spring Boot profile,
or by editing your IDE configuration and setting the active profile.

`./mvnw spring-boot:run -Dspring-boot.run.profiles=local`

Useful links:

- This service exposes a REST API.
  http://localhost:8088/swagger-ui/index.html#/employee-management-controller (Swagger)

- This service persist data to Mongo DB.
  http://localhost:8081/db/employee-management-service/ (Mongo Express)

- This service produces events to Kafka. http://localhost:9000/ (Kafdrop)

### Improvement Plans

- to reliably/atomically update the database and send messages/events (transactional)
- to accommodate non-functional requirements
- to add end-to-end testing
