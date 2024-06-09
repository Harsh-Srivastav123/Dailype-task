# DailyPe Application

![Diagram](https://github.com/Harsh-Srivastav123/Dailype-task/blob/main/Screenshot%202024-06-09%20035319.png)

## Overview

This repository contains the DailyPe application, a robust CRUD system enhanced with a range of additional features to ensure security, scalability, and efficiency. The project includes basic CRUD functionalities along with various advanced integrations and deployment strategies.


## Tech Stack

- Java 17
- JPA Hibernate
- MySQL
- Java Validation
- AWS RDS
- Docker
- Spring Security
- JWT
- Email Verification
- Event-Driven Architecture
- AWS EC2
- Elastic IP
- GitHub Actions
- YAML
- Nginx
- API Gateway
- Reverse Proxy
- Load Balancer
- Secret Management
- JMeter Testing

## Features

### CRUD Operations with Validation

The application provides basic CRUD functionalities with comprehensive data validation:
- **Create User**: Validates full name, mobile number, PAN number, and manager ID.
- **Get Users**: Retrieves user data with filtering options by user ID, mobile number, or manager ID.
- **Delete User**: Deletes user data by user ID or mobile number.
- **Update User**: Updates user data with validation and supports bulk updates for manager IDs.

### Security

- **Spring Security**: Implements security features to protect the application.
- **JWT Authentication**: Secures APIs with JWT tokens that expire in 5 minutes.
- **Refresh Token API**: Provides a way to get a new JWT token after expiration.

### Email Verification

- **Event-Driven Architecture**: Uses events and listeners to handle email verification asynchronously.
- **Async Functionality**: Ensures that email sending and verification are handled without blocking the main application flow.
- **Verification Expiration**: The verification link expires in 5 minutes, after which users need to recreate their account if not verified.

### AWS Integrations


- **AWS S3**: Utilized for media upload, ensuring scalable and reliable storage.
  - **Image API**: Fetches images without exposing the S3 URL, enhancing security and user experience.
- **AWS EC2 (Ubuntu OS)**: Deployed on AWS EC2 instances running Ubuntu OS for scalable compute capacity.
- **AWS RDS (MySQL Database)**: Manages user profiles and other application data with a scalable and managed MySQL database solution.

### Deployment

- **Docker**: Containerizes the application for easy deployment and scalability. Docker ensures consistent deployment across different environments.
- **GitHub Actions**: Implements a CI/CD pipeline that automates build, test, and deployment processes, ensuring rapid and reliable updates to the application.
- **Nginx**: Used as a load balancer, reverse proxy, and API gateway, improving server availability and performance.

### Horizontal Scaling

The application supports horizontal scaling to manage varying loads efficiently. This ensures high availability and performance during peak times.

### CI/CD Strategy

- Ensures at least one server is up during deployment to maintain application availability.
- Automates deployment processes with GitHub Actions, integrating with Docker and AWS for seamless updates.

### Secure Credential Management

Implements strategies for secure management of credentials without exposing them in the repository. This includes using environment variables and AWS Secrets Manager.

### Branch Separation

The project is organized into separate branches to manage different tasks and features effectively:
- `main`: Contains the stable version of the application.
- `task`: Includes basic CRUD operations and validations.
- `extended-task`: Contains extended features such as security, email verification, and AWS S3 integration.


## Performance Testing

### JMeter Testing

JMeter is used to test the server's performance and ensure it can handle high loads efficiently. The test plan includes:
- Load testing with concurrent users.
- Stress testing to determine the application's breaking point.
- Performance benchmarking to identify any bottlenecks.

## Postman Collection and Video
For a detailed demonstration of the API endpoints and their usage, a Postman collection and an explanatory video are provided. Please refer to the documentation directory for these resources.
