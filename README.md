# 💈 Barber Shop Management System

A professional barber shop management system with customer booking portal and business CRM. Built with Spring Boot, MySQL, and Docker.

## 🚀 Project Status

🔨 **Currently in Development** - Core backend functionality completed and tested.

### Completed Features
- ✅ Core domain models (User, Service, Appointment, AppointmentItem, Staff, Review)
- ✅ Multi-service appointment system (multiple services per booking)
- ✅ Flexible staff assignment (different staff per service)
- ✅ JPA repositories with custom queries
- ✅ Service layer with business logic and validations
- ✅ REST API controllers for all operations
- ✅ Spring Security configuration (development mode)
- ✅ Exception handling with global error handling
- ✅ Docker configuration for MySQL
- ✅ Request/Response DTOs
- ✅ Password encryption with BCrypt
- ✅ All endpoints tested and working

### In Progress
- 🔄 JWT authentication & authorization
- 🔄 Swagger API documentation
- 🔄 Frontend development

## 🎯 Features

### Customer Portal
- Browse available services
- Book appointments with multiple services
- Select preferred staff for each service
- View appointment history
- Leave reviews and ratings

### Admin/Business CRM
- Customer management
- Appointment calendar and management
- Service management (CRUD operations)
- Staff scheduling and management
- Revenue analytics and reports
- Staff performance tracking

## 🛠️ Tech Stack

### Backend
- **Java 17**
- **Spring Boot 3.x**
- **Spring Data JPA** - Database operations
- **Spring Security** - Authentication & authorization
- **JWT** - Token-based authentication
- **MySQL 8** - Relational database
- **Lombok** - Reduce boilerplate code
- **Maven** - Dependency management

### DevOps
- **Docker** - Containerization
- **Docker Compose** - Multi-container orchestration

### Frontend (Planned)
- React / Thymeleaf (TBD)
- Tailwind CSS / Bootstrap

## 📋 Prerequisites

- Java 17 or higher
- Maven 3.6+
- Docker Desktop
- Git

## 🚀 Getting Started

### 1. Clone the Repository

```bash
git clone https://github.com/YOUR_USERNAME/barbershop-management-system.git
cd barbershop-management-system
```

### 2. Start MySQL with Docker

```bash
docker-compose up -d
```

This will start MySQL on port 3307.

### 3. Run the Application

```bash
mvn spring-boot:run
```

Or run from your IDE (IntelliJ IDEA recommended).

### 4. Verify Setup

The application will start on `http://localhost:8080`

Check the console for successful table creation:
```
Hibernate: create table users ...
Hibernate: create table services ...
Hibernate: create table appointments ...
Hibernate: create table appointment_items ...
```

## 📊 Database Schema

### Key Entities

- **User** - Customers, staff, and admins with role-based access
- **Service** - Available barbershop services (haircut, shave, etc.)
- **Staff** - Barber staff with working hours and specialties
- **Appointment** - Customer bookings with date/time
- **AppointmentItem** - Individual services within an appointment (allows multiple services with different staff)
- **Review** - Customer feedback on completed appointments

### Entity Relationships

```
User (Customer) ──► Appointment ──► AppointmentItem ──► Service
                                           │
                                           └──► Staff
```

Each appointment can have multiple items, and each item can have a different staff member assigned.

## 🔒 Security (Planned)

- JWT-based authentication
- Role-based access control (CUSTOMER, STAFF, ADMIN)
- Password encryption with BCrypt
- Secure API endpoints

## 📝 API Documentation (Coming Soon)

API documentation will be available via Swagger UI at `/swagger-ui.html`

### Main Endpoints

#### Authentication
- `POST /api/auth/register` - Register new customer
- `POST /api/auth/login` - User login
- `GET /api/auth/test` - API health check

#### Services (Barbershop Services)
- `GET /api/services` - Get all active services
- `GET /api/services/{id}` - Get service by ID
- `POST /api/services` - Create new service (Admin)
- `PUT /api/services/{id}` - Update service (Admin)
- `DELETE /api/services/{id}` - Delete service (Admin)

#### Staff
- `GET /api/staff` - Get all active staff
- `GET /api/staff/{id}` - Get staff by ID
- `POST /api/staff` - Create new staff (Admin)
- `PUT /api/staff/{id}` - Update staff (Admin)
- `DELETE /api/staff/{id}` - Deactivate staff (Admin)

#### Appointments
- `POST /api/appointments` - Create new appointment
- `GET /api/appointments/{id}` - Get appointment by ID
- `GET /api/appointments/my-appointments` - Get user's appointments
- `GET /api/appointments` - Get all appointments (Admin)
- `GET /api/appointments/by-date?date=` - Get appointments by date
- `PUT /api/appointments/{id}/cancel` - Cancel appointment
- `PUT /api/appointments/{id}/confirm` - Confirm appointment (Admin)
- `PUT /api/appointments/{id}/complete` - Complete appointment (Admin)

### Example Request: Create Appointment

```bash
POST http://localhost:8080/api/appointments
Content-Type: application/json

{
  "appointmentDateTime": "2025-11-25T14:00:00",
  "items": [
    {
      "serviceId": 1,
      "staffId": 1,
      "scheduledTime": "2025-11-25T14:00:00"
    },
    {
      "serviceId": 2,
      "staffId": 1,
      "scheduledTime": "2025-11-25T14:30:00"
    }
  ],
  "notes": "Short haircut please"
}
```

## 🧪 Testing (Planned)

```bash
mvn test
```

### Current Testing Status
- ✅ Manual testing with Postman completed
- ✅ All CRUD operations verified
- ✅ Multi-service appointment booking tested
- ✅ Staff conflict detection validated
- 🔄 Unit tests (in progress)
- 🔄 Integration tests (planned)

## 📦 Deployment (Planned)

The application will be deployed to:
- [ ] Heroku / Railway
- [ ] AWS EC2
- [ ] Google Cloud Run

## 🗺️ Roadmap

- [x] Project setup and configuration
- [x] Database design and entity modeling
- [x] Repository layer with custom queries
- [x] Service layer with business logic
- [x] Exception handling and validation
- [x] REST API controllers
- [x] Spring Security configuration
- [x] Password encryption (BCrypt)
- [x] Multi-service appointment support
- [x] Staff scheduling and conflict detection
- [x] Manual testing with Postman
- [ ] JWT authentication and authorization
- [ ] Unit and integration tests
- [ ] API documentation (Swagger)
- [ ] Email notifications
- [ ] Frontend development (React/Thymeleaf)
- [ ] Deployment
- [ ] CI/CD pipeline

## 🤝 Contributing

This is a portfolio project, but suggestions and feedback are welcome!

## 📧 Contact

GitHub: [@merve-ceylan]([https://github.com/merve-ceylan])


## 📄 License

This project is open source and available under the [MIT License](LICENSE).

---

⭐ **If you find this project interesting, please give it a star!** ⭐
