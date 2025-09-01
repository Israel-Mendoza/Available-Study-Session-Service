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

## Requirements
- Java 21 or higher
- Gradle
- A running instance of the MySQL database (configured in application.properties) for PROD.