💈 Barber Shop Management System
A professional barber shop management system with customer booking portal and business CRM. Built with Spring Boot, MySQL, and Docker.
🚀 Project Status
🔨 Currently in Development - This is an ongoing portfolio project showcasing full-stack development skills.
Completed Features

✅ Core domain models (User, Service, Appointment, Staff)
✅ Multi-service appointment system (multiple services per booking)
✅ Flexible staff assignment (different staff per service)
✅ JPA repositories with custom queries
✅ Docker configuration for MySQL
✅ Exception handling infrastructure
✅ Request/Response DTOs

In Progress

🔄 Service layer implementation
🔄 JWT authentication & authorization
🔄 REST API endpoints
🔄 Frontend development

🎯 Features
Customer Portal

Browse available services
Book appointments with multiple services
Select preferred staff for each service
View appointment history
Leave reviews and ratings

Admin/Business CRM

Customer management
Appointment calendar and management
Service management (CRUD operations)
Staff scheduling and management
Revenue analytics and reports
Staff performance tracking

🛠️ Tech Stack
Backend

Java 17
Spring Boot 3.x
Spring Data JPA - Database operations
Spring Security - Authentication & authorization
JWT - Token-based authentication
MySQL 8 - Relational database
Lombok - Reduce boilerplate code
Maven - Dependency management

DevOps

Docker - Containerization
Docker Compose - Multi-container orchestration

Frontend (Planned)

React / Thymeleaf (TBD)
Tailwind CSS / Bootstrap

📋 Prerequisites

Java 17 or higher
Maven 3.6+
Docker Desktop
Git

🚀 Getting Started
1. Clone the Repository
bashgit clone https://github.com/merve-ceylan/barbershop-management-system.git
cd barbershop-management-system
2. Start MySQL with Docker
bashdocker-compose up -d
This will start MySQL on port 3307.
3. Run the Application
bashmvn spring-boot:run
Or run from your IDE (IntelliJ IDEA recommended).
4. Verify Setup
The application will start on http://localhost:8080
Check the console for successful table creation:
Hibernate: create table users ...
Hibernate: create table services ...
Hibernate: create table appointments ...
Hibernate: create table appointment_items ...
📊 Database Schema
Key Entities

User - Customers, staff, and admins with role-based access
Service - Available barbershop services (haircut, shave, etc.)
Staff - Barber staff with working hours and specialties
Appointment - Customer bookings with date/time
AppointmentItem - Individual services within an appointment (allows multiple services with different staff)
Review - Customer feedback on completed appointments

Entity Relationships
User (Customer) ──► Appointment ──► AppointmentItem ──► Service
                                           │
                                           └──► Staff
Each appointment can have multiple items, and each item can have a different staff member assigned.
🔒 Security (Planned)

JWT-based authentication
Role-based access control (CUSTOMER, STAFF, ADMIN)
Password encryption with BCrypt
Secure API endpoints

📝 API Documentation (Coming Soon)
API documentation will be available via Swagger UI at /swagger-ui.html
🧪 Testing (Planned)
bashmvn test
📦 Deployment (Planned)
The application will be deployed to:

 Heroku / Railway
 AWS EC2
 Google Cloud Run

🗺️ Roadmap

 Project setup and configuration
 Database design and entity modeling
 Repository layer with custom queries
 Service layer with business logic
 Security implementation (JWT)
 REST API controllers
 Exception handling and validation
 Email notifications
 Frontend development
 Unit and integration tests
 API documentation (Swagger)
 Deployment
 CI/CD pipeline

🤝 Contributing
This is a portfolio project, but suggestions and feedback are welcome!
📧 Contact
Merve Ceylan
GitHub: @merve-ceylan

📄 License
This project is open source and available under the MIT License.

⭐ If you find this project interesting, please give it a star! ⭐
