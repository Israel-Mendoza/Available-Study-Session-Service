# Subject-Topics Service

This service is responsible for managing subjects and their associated topics. It provides a RESTful API to perform CRUD operations on subjects and topics.

## Features
- Create, read, update, and delete subjects
- Create, read, update, and delete topics associated with subjects
- Input validation and error handling

## Getting Started
1. Clone the repository:
   ```sh
   git clone https://github.com/Israel-Mendoza/Subject-Topics-Service.git
    cd Subject-Topics-Service
   ``` 
   
2. Use the application.properties file to configure the database connection and other settings.
3. Build and run the application using your preferred method (e.g., IDE, command line).
    Use the following command to run with the `dev` profile (embedded H2 database):
    ```sh
    ./gradlew bootRun --args='--spring.profiles.active=dev'
    ```
   Use the following command to run with the `prod` profile (MySQL database):
    ```sh
    ./gradlew bootRun --args='--spring.profiles.active=prod'
    ```
4. Access the API endpoints using a tool like Postman or curl.
5. To stop the application, simply terminate the process.

## Testing
To run the tests, use the following command:
```sh
./gradlew test
```

Currently, integration tests are configured to use testcontainers with a MySQL container.
Make sure Docker is running on your machine before executing the tests.

## Requirements
- Java 21 or higher
- Gradle
- For `prod` profile:
  - Currently, the application is configured to connect to a MySQL database running on `localhost:3306`.
  - You can use my provided Docker Compose setup to run a MySQL instance. To do so, follow these steps:
    1. Clone the MySQL Docker Compose repository:
    2. Navigate to the cloned directory.
    3. Start the MySQL container using Docker Compose:
    ```sh
    git clone https://github.com/Israel-Mendoza/Study-Session-Subjects-Db.git
    cd Study-Session-Subjects-Db
    docker-compose up --build -d
    ```
    4. Ensure the database connection settings in `application.properties` match the MySQL container settings.
    5. That instance is mapping port `3307` on the host to port `3306` in the container, so you may need to update the `spring.datasource.url` property in `application.properties` to:
    ```
    spring.datasource.url=jdbc:mysql://localhost:3307/subjects_db
    ```
- For `dev` profile:
  - No additional requirements, as it uses an embedded H2 database.
- Docker (for running integration tests with testcontainers)
- Docker Compose (if you want to run the MySQL database using the provided Docker Compose setup
- A running instance of the MySQL database (configured in application.properties) for PROD.