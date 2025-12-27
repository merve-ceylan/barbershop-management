# 💈 Barbershop Management System

A professional full-stack barbershop management system with customer appointment booking and business CRM. Built with **Spring Boot**, **MySQL**, **JWT Authentication**, and **Docker**.

[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.3.5-brightgreen.svg)](https://spring.io/projects/spring-boot)
[![Java](https://img.shields.io/badge/Java-17-orange.svg)](https://www.oracle.com/java/)
[![MySQL](https://img.shields.io/badge/MySQL-8.0-blue.svg)](https://www.mysql.com/)
[![Docker](https://img.shields.io/badge/Docker-Ready-blue.svg)](https://www.docker.com/)
[![License](https://img.shields.io/badge/License-MIT-yellow.svg)](https://opensource.org/licenses/MIT)

---

## 🚀 Project Status

**🎯 Backend Development: 85% Complete**

### ✅ Completed Features
- [x] 🔐 JWT Authentication & Authorization (Role-based: ADMIN, CUSTOMER, STAFF)
- [x] 👤 User Management (Registration, Login, Profile)
- [x] 💇 Service Management (CRUD operations)
- [x] 👨‍💼 Staff Management (Scheduling, Specialties, Working hours)
- [x] 📅 **Multi-Service Appointment System**
    - Multiple services per appointment
    - Different staff for each service
    - Automatic conflict detection
    - Working hours validation
- [x] 📊 Complete REST API with exception handling
- [x] 🐳 Docker & Docker Compose setup for MySQL
- [x] 📚 **Swagger/OpenAPI Documentation** ← NEW!
- [x] 🔒 Spring Security with JWT tokens
- [x] ✅ Request validation & error handling

### 🔄 In Progress
- [ ] 🎨 Frontend Development (React - Starting soon)
- [ ] ⭐ Review & Rating System
- [ ] 📈 Analytics & Business Reports

---

## 🎯 Key Features

### 🛍️ Customer Portal
- Browse available services with prices and duration
- **Book appointments with multiple services**
- Select preferred staff for each service
- View appointment history and status
- Cancel pending appointments
- Leave reviews after completed services

### 🏢 Business CRM (Admin)
- Customer management and search
- **Advanced appointment calendar** with multi-service view
- Service management (CRUD)
- Staff scheduling with conflict prevention
- Real-time availability checking
- Revenue analytics and reports
- Staff performance tracking

### 🔥 Unique Features
- **Multi-Service Bookings**: Customer can book haircut + shave in one appointment
- **Per-Service Staff Assignment**: Different staff member for each service
- **Smart Conflict Detection**: Prevents double-booking at staff level
- **Working Hours Validation**: Appointments only during staff working hours
- **Automatic Price Calculation**: Total price and duration calculated from services

---

## 🛠️ Tech Stack

### Backend
- **Java 17** - Modern Java features
- **Spring Boot 3.3.5** - Application framework
- **Spring Data JPA** - Database operations
- **Spring Security** - Authentication & authorization
- **JWT (JJWT 0.12.5)** - Token-based authentication
- **MySQL 8.0** - Relational database
- **Gradle** - Build automation
- **Lombok** - Reduce boilerplate code
- **SpringDoc OpenAPI 2.6.0** - API documentation

### DevOps
- **Docker** - MySQL containerization
- **Docker Compose** - Multi-container orchestration

### Frontend (Planned)
- **React 18** with Vite
- **Tailwind CSS** - Modern styling
- **Axios** - API communication
- **React Router** - Navigation

---

## 📚 API Documentation

### 🎉 Interactive API Documentation with Swagger

Complete API documentation is available through **Swagger UI**:

**🔗 Swagger UI:** http://localhost:8080/swagger-ui.html  
**📄 OpenAPI JSON:** http://localhost:8080/v3/api-docs

#### Available Endpoints:

**🔐 Authentication**
- `POST /api/auth/register` - Register new customer
- `POST /api/auth/login` - User login (returns JWT token)
- `GET /api/auth/test` - API health check

**💇 Services** (Public GET, Admin POST/PUT/DELETE)
- `GET /api/services` - List all active services
- `GET /api/services/{id}` - Get service details
- `POST /api/services` - Create service (Admin)
- `PUT /api/services/{id}` - Update service (Admin)
- `DELETE /api/services/{id}` - Delete service (Admin)

**👨‍💼 Staff** (Public GET, Admin POST/PUT/DELETE)
- `GET /api/staff` - List all active staff
- `GET /api/staff/{id}` - Get staff details
- `POST /api/staff` - Create staff (Admin)
- `PUT /api/staff/{id}` - Update staff (Admin)
- `DELETE /api/staff/{id}` - Deactivate staff (Admin)

**📅 Appointments** (Requires Authentication)
- `POST /api/appointments` - Create appointment (multi-service support)
- `GET /api/appointments/{id}` - Get appointment details
- `GET /api/appointments/my-appointments` - Get user's appointments
- `GET /api/appointments` - Get all appointments (Admin)
- `GET /api/appointments/by-date` - Get appointments by date
- `PUT /api/appointments/{id}/cancel` - Cancel appointment
- `PUT /api/appointments/{id}/confirm` - Confirm appointment (Admin)
- `PUT /api/appointments/{id}/complete` - Complete appointment (Admin)

---

## 📦 Installation & Setup

### Prerequisites
- ☕ Java 17 or higher
- 🐘 Docker Desktop (for MySQL)
- 🛠️ Gradle 8.x (or use wrapper)
- 🔧 Git

### Quick Start

#### 1. Clone the Repository
git clone https://github.com/merve-ceylan/barbershop-management.git
cd barbershop-management

#### 2. Start MySQL with Docker
docker-compose up -d

This will:
- Download MySQL 8.0 image
- Create `barbershop_db` database
- Set up user credentials
- Expose port **3307** (not 3306 to avoid conflicts)

#### 3. Verify MySQL is Running
docker ps

#### 4. Run the Application
./gradlew bootRun

#### 5. Access the Application
- **Application:** http://localhost:8080
- **Swagger UI:** http://localhost:8080/swagger-ui.html
- **API Docs JSON:** http://localhost:8080/v3/api-docs

---

## 🧪 Testing the API

### Using Swagger UI (Recommended)

1. **Open Swagger UI:** http://localhost:8080/swagger-ui.html

2. **Register a new user:**
    - Go to `Authentication` → `POST /api/auth/register`
    - Click "Try it out"
    - Use this example:
      {
      "email": "test@example.com",
      "password": "test123",
      "firstName": "Test",
      "lastName": "User",
      "phone": "5551234567"
      }

3. **Login and get JWT token:**
    - Go to `POST /api/auth/login`
    - Login with your credentials
    - Copy the `accessToken` from response

4. **Authorize Swagger:**
    - Click 🔓 **Authorize** button (top right)
    - Enter: `Bearer YOUR_TOKEN_HERE`
    - Click "Authorize"

5. **Create an appointment:**
    - Go to `POST /api/appointments`
    - Try the example request body
    - All authenticated endpoints now work!

---

## 🗄️ Database Schema

### Core Entities
┌─────────────┐       ┌──────────────────┐       ┌──────────────┐
│    User     │       │   Appointment    │       │   Service    │
├─────────────┤       ├──────────────────┤       ├──────────────┤
│ id          │──────<│ customer_id      │       │ id           │
│ email       │       │ date_time        │       │ name         │
│ password    │       │ status           │       │ description  │
│ first_name  │       │ notes            │       │ price        │
│ role        │       └──────────────────┘       │ duration     │
└─────────────┘                │                 └──────────────┘
│                         ^
│                         │
v                         │
┌─────────────────────┐             │
│ AppointmentItem     │             │
├─────────────────────┤             │
│ id                  │             │
│ appointment_id      │─────────────┘
│ service_id          │─────────────┐
│ staff_id            │             │
│ scheduled_time      │             │
│ price               │             │
│ duration            │             │
│ status              │             v
└─────────────────────┘       ┌──────────────┐
│    Staff     │
├──────────────┤
│ id           │
│ name         │
│ specialties  │
│ work_start   │
│ work_end     │
└──────────────┘

---

## 🔐 Authentication & Security

### JWT Authentication Flow

1. **Register** → Create account (CUSTOMER role by default)
2. **Login** → Receive JWT token (valid for 24 hours)
3. **Use Token** → Include in `Authorization: Bearer <token>` header
4. **Protected Routes** → Token verified on each request

### Role-Based Access Control

| Role | Permissions |
|------|-------------|
| **CUSTOMER** | ✅ Book appointments<br>✅ View own appointments<br>✅ Cancel own appointments<br>✅ View services/staff |
| **ADMIN** | ✅ All customer permissions<br>✅ Manage services (CRUD)<br>✅ Manage staff (CRUD)<br>✅ View all appointments<br>✅ Confirm/complete appointments |
| **STAFF** | ✅ View assigned appointments<br>✅ Complete appointments<br>✅ View schedule |

### Security Features
- ✅ Password encryption with BCrypt
- ✅ JWT token expiration (24 hours)
- ✅ CORS configuration
- ✅ SQL injection prevention (JPA)
- ✅ Input validation on all endpoints
- ✅ Role-based endpoint protection

---

## 🐳 Docker Configuration

### MySQL Container Details
Service: mysql
Image: mysql:8.0
Container: barbershop_mysql
Port: 3307 (host) → 3306 (container)
Database: barbershop_db
User: barbershop_user
Password: barbershop_pass
Root Password: root123

### Useful Docker Commands
Start MySQL
docker-compose up -d
Stop MySQL
docker-compose down
View logs
docker-compose logs mysql
Access MySQL shell
docker exec -it barbershop_mysql mysql -u barbershop_user -p
Remove MySQL data (⚠️ CAUTION: Deletes all data)
docker-compose down -v

---

## 📝 Development Roadmap

### Phase 1: Backend (85% Complete) ✅
- [x] Project setup & database design
- [x] User authentication & JWT
- [x] Service management
- [x] Staff management
- [x] Multi-service appointment system
- [x] Swagger API documentation
- [ ] Review & rating system
- [ ] Analytics & reports

### Phase 2: Frontend (Starting Soon) 🚧
- [ ] React project setup
- [ ] Authentication pages (Login/Register)
- [ ] Service listing & details
- [ ] Staff listing
- [ ] Multi-step booking flow
- [ ] Customer dashboard
- [ ] Responsive design

### Phase 3: Advanced Features 🔮
- [ ] Admin dashboard
- [ ] Email notifications (Spring Mail)
- [ ] Calendar view integration
- [ ] Payment integration (Stripe)
- [ ] Real-time updates (WebSocket)
- [ ] SMS notifications

### Phase 4: Production 🚀
- [ ] Unit & integration tests
- [ ] CI/CD pipeline (GitHub Actions)
- [ ] Deploy backend (Railway/Heroku)
- [ ] Deploy frontend (Vercel/Netlify)
- [ ] Production monitoring

---

## 👤 Author

**Merve Ceylan**
- GitHub: [@merve-ceylan](https://github.com/merve-ceylan)
- LinkedIn: [Merve Ceylan](https://linkedin.com/in/merve-ceylan)


---

## 🎯 Project Highlights

### What Makes This Project Special?

1. **Multi-Service Appointment System**
    - Unlike simple booking systems, supports multiple services per appointment
    - Each service can have different staff and timing
    - Real-world complexity handled elegantly

2. **Smart Conflict Detection**
    - Prevents double-booking at staff level
    - Considers service duration automatically
    - Working hours validation

3. **Production-Ready Architecture**
    - Clean separation of concerns (Controller → Service → Repository)
    - Comprehensive error handling
    - JWT authentication best practices
    - RESTful API design

4. **Developer-Friendly**
    - Complete Swagger documentation
    - Docker-based development environment
    - Easy setup and testing

---

**⭐ If you find this project helpful, please give it a star!**

**🚀 Status:** Backend Complete | Frontend In Progress

**Last Updated:** December 2025