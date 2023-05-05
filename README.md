
# PID Tracker Spring Boot Application
[![Springboot](https://img.shields.io/badge/Springboot-v3.0-green)](https://docs.spring.io/spring-boot/docs/current/reference/html/)
[![MySql](https://img.shields.io/badge/MySQL-v8.0.32-orange)](https://www.mysql.com/)
[![ReactJS](https://img.shields.io/badge/ReactJS-v17.0.2-blue)](https://react.dev/)


This is a web-based application that allows developers to upload PIDs (Project Initiation Document) and reviewers to review them. The application has features such as user authentication and authorization, file upload and compression, automatic assignment of reviewers, email notifications, and more.

## Features
- **User Authentication and Authorization with Spring Security**:
The application uses Spring Security to authenticate and authorize users. Users can sign up, login and logout from the application. JWT token is used to maintain user session.

- **Creating a New User**:
The endpoint /api/users/signup is used to create a new user. The endpoint requires the following fields in the request body: firstName, lastName, email, password, roles. The roles field is an array of strings representing the user's roles.

- **Uploading a PID**:
The endpoint /api/pid/upload is used to upload a new PID. The endpoint requires the following fields in the request body: title, description, keywords, file. The file is compressed and stored in the backend in blob format.

- **Review Assignment**:
The application uses Round Robin algorithm to assign PIDs to reviewers. A reviewer can reject a review, in which case the PID is automatically reassigned to another reviewer.

- **Automatic Reassignment of PIDs After Deadline**:
If a reviewer fails to review a PID before the deadline, the PID is automatically reassigned to another reviewer.

- **Dashboard**:
The home page contains a dashboard that displays the number of PIDs uploaded by the user, the status of their PIDs, and other relevant metrics.

- **Email Notifications**:
The application sends email notifications to developers and reviewers when a new PID is uploaded. A background function is scheduled to send reminders about the uploaded PIDs to the reviewers.

- **Integration of Word View**:
The application integrates Word View into the portal itself.

- **Admin Panel**:
The application has an admin panel for managing users, PIDs and reviewers.

- **Urgency Status Tag**:
The application has an urgency status tag to indicate the urgency of the PID.

- **Marking Other Users as Proxy**:
A reviewer can mark another user as proxy to review a PID on their behalf.

## Technologies Used
The project has been implemented using the following technologies:

- **Spring Boot:** A popular Java framework for building web applications.
- **Spring Web:** A Spring Boot module for building web-based applications and RESTful APIs.
- **Spring Data JPA:** A module of the Spring Framework that simplifies database access by providing a high-level abstraction over SQL.
- **H2 Database:** An open-source in-memory database that can be embedded into Java applications.
- **Spring Security:** A powerful and highly customizable authentication and access-control framework for Spring applications.
- **JJWT:** A Java library for creating and verifying JSON Web Tokens (JWT).

## Architecture Diagrams

![](https://github.com/AyushPaul/pid_tracker_springboot/blob/main/Picture1.png)
![](https://github.com/AyushPaul/pid_tracker_springboot/blob/main/Picture2.png)

## Project Structure
The project is divided into several packages, each with a specific purpose:
- `com.ayush.pidtracker`: This is the root package for the project.
- `com.ayush.pidtracker.config`: This package contains the configuration classes for the application.
- `com.ayush.pidtracker.controller`: This package contains the controllers for the application.
- `com.ayush.pidtracker.dto`: This package contains the Data Transfer Objects used by the application.
- `com.ayush.pidtracker.entity`: This package contains the entities used by the application.
- `com.ayush.pidtracker.exception`: This package contains the exception classes used by the application.
- `com.ayush.pidtracker.repository`: This package contains the repositories used by the application.
- `com.ayush.pidtracker.security`: This package contains the security classes used by the application.
- `com.ayush.pidtracker.service`: This package contains the services used by the application.
- `com.ayush.pidtracker.util`: This package contains the utility classes used by the application.

## Enpoints
Here are the endpoints that have been created for the application:

- **/api/signup (POST)**: This endpoint is used for user sign up. Users provide their details in the request body and the details are saved to the database using Spring Data JPA.

- **/api/login (POST)**: This endpoint is used for user login. Users provide their email and password in the request body and the application checks the database for the user's details. If the details are correct, a JWT token is generated and returned to the user.

- **/api/upload (POST)**: This endpoint is used for file upload. Users provide the required details such as title, description, category, and file in the request body. The file is stored in the backend in blob format and compressed using the Java Zip API.

- **/api/download/{pid} (GET)**: This endpoint is used for file download. Users provide the PID of the file they want to download in the URL path parameter. The file is downloaded in its original format.

- **/api/pid (GET)**: This endpoint is used to get a list of PIDs that are pending review.

- **/api/review (POST)**: This endpoint is used by reviewers to review PIDs. Reviewers provide the PID they want to review along with their review in the request body.

- **/api/reject/{pid} (POST)**: This endpoint is used by reviewers to reject a review. Reviewers provide the PID they want to reject in the URL path parameter. The application then automatically reassigns the review to another reviewer.

- **/api/dashboard (GET)**: This endpoint is used to get the dashboard details such as the number of PIDs uploaded by the user and the status of their PIDs (e.g. pending review, approved, rejected).






## User Login/Signup



https://user-images.githubusercontent.com/67481937/224963154-e8b82182-e5da-4724-b943-9febb660c871.mp4

## File Sharing





https://user-images.githubusercontent.com/67481937/226710546-c2a3872f-538c-48f2-b1b3-0e9d15ecd3d7.mp4

## Backend Endpoints




https://user-images.githubusercontent.com/67481937/227757328-943723bd-17cd-4cbc-9b03-ba93a2a94ded.mp4

## Demo





https://user-images.githubusercontent.com/67481937/228021611-d4dc86e8-c212-42ff-ae1b-864d8ce0f309.mp4

## Conclusion
This project has implemented several features that make it easy for users to upload and review PIDs. The use of Spring Security, JWT token, and Round Robin algorithm for review assignment ensures the security and efficiency of the application. The integration of Word View and the automatic reassignment of PIDs after deadline are also useful features that enhance the user experience.