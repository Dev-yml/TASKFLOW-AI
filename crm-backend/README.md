# CRM Backend - AI-Powered Task Manager + CRM + Real-Time Chat

A production-ready, scalable backend application built with Spring Boot, featuring JWT authentication, user management, task management, and real-time chat capabilities.

## 🚀 Features

### ✅ Completed Modules

#### Authentication & User Management
- ✅ User Registration with email validation
- ✅ User Login with JWT token generation
- ✅ JWT-based stateless authentication
- ✅ Role-based access control (ADMIN, MANAGER, USER)
- ✅ User profile management
- ✅ Password change functionality
- ✅ Soft delete users (INACTIVE status)
- ✅ BCrypt password encryption
- ✅ Custom exception handling

#### Task Management
- ✅ Create, Read, Update, Delete tasks
- ✅ Task status management (TODO, IN_PROGRESS, COMPLETED, CANCELLED)
- ✅ Task priority levels (LOW, MEDIUM, HIGH, URGENT)
- ✅ Task comments system
- ✅ Task assignment to users

### 🔄 In Progress
- Real-Time Chat Module
- Notifications System
- Analytics & Reporting

## 🛠️ Tech Stack

- **Java 21** - Latest LTS version
- **Spring Boot 3.3.4** - Application framework
- **Spring Security** - Authentication & authorization
- **Spring Data JPA** - Database access
- **PostgreSQL** - Primary database
- **Redis** - Caching layer
- **JWT (jjwt 0.12.3)** - Token-based authentication
- **Lombok** - Reduce boilerplate code
- **Jakarta Validation** - Request validation
- **Maven** - Dependency management

## 📁 Project Structure

```
crm-backend/
├── src/main/java/com/arjun/crm/
│   ├── controller/              # REST API Controllers
│   │   ├── AuthController.java
│   │   ├── UserController.java
│   │   └── TaskController.java
│   ├── service/                 # Business Logic Interfaces
│   │   ├── AuthService.java
│   │   ├── UserService.java
│   │   └── TaskService.java
│   ├── service/impl/            # Service Implementations
│   │   ├── AuthServiceImpl.java
│   │   ├── UserServiceImpl.java
│   │   └── TaskServiceImpl.java
│   ├── repository/              # Data Access Layer
│   │   ├── UserRepository.java
│   │   ├── TaskRepository.java
│   │   └── TaskCommentRepository.java
│   ├── entity/                  # JPA Entities
│   │   ├── User.java
│   │   ├── Task.java
│   │   └── TaskComment.java
│   ├── dto/
│   │   ├── request/             # Request DTOs
│   │   └── response/            # Response DTOs
│   ├── security/                # Security Components
│   │   ├── JwtService.java
│   │   ├── JwtAuthenticationFilter.java
│   │   └── CustomUserDetailsService.java
│   ├── config/                  # Configuration Classes
│   │   ├── SecurityConfig.java
│   │   └── RedisConfig.java
│   ├── exception/               # Exception Handling
│   │   ├── GlobalExceptionHandler.java
│   │   └── Custom Exceptions...
│   ├── enums/                   # Enumerations
│   │   ├── Role.java
│   │   ├── UserStatus.java
│   │   ├── TaskStatus.java
│   │   └── TaskPriority.java
│   └── CrmBackendApplication.java
├── src/main/resources/
│   ├── application.properties
│   └── schema.sql
├── AUTH_API_DOCUMENTATION.md    # Complete API docs
├── DATABASE_SCHEMA.md           # Database schema
├── SECURITY_FLOW.md             # Security architecture
├── SETUP_INSTRUCTIONS.md        # Setup guide
├── POSTMAN_COLLECTION.json      # API testing
└── pom.xml
```

## 🚦 Quick Start

### Prerequisites
- Java 21
- Maven 3.8+
- PostgreSQL 14+
- Redis 7+

### 1. Clone Repository
```bash
git clone <repository-url>
cd crm-backend
```

### 2. Setup Database
```bash
# Create PostgreSQL database
psql -U postgres
CREATE DATABASE crm_db;
\q
```

### 3. Configure Application
Update `src/main/resources/application.properties`:
```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/crm_db
spring.datasource.username=postgres
spring.datasource.password=yourpassword
```

### 4. Start Redis
```bash
redis-server
```

### 5. Build & Run
```bash
mvn clean install
mvn spring-boot:run
```

Application will start on: `http://localhost:8080`

## 📚 API Documentation

### Authentication Endpoints

#### Register User
```http
POST /api/auth/register
Content-Type: application/json

{
  "fullName": "John Doe",
  "email": "john@example.com",
  "password": "password123",
  "role": "USER"
}
```

#### Login User
```http
POST /api/auth/login
Content-Type: application/json

{
  "email": "john@example.com",
  "password": "password123"
}
```

**Response:**
```json
{
  "success": true,
  "message": "Login successful",
  "data": {
    "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
    "type": "Bearer",
    "user": {
      "id": 1,
      "fullName": "John Doe",
      "email": "john@example.com",
      "role": "USER",
      "status": "ACTIVE"
    }
  }
}
```

### User Management Endpoints

#### Get Current User
```http
GET /api/users/me
Authorization: Bearer <token>
```

#### Update Profile
```http
PUT /api/users/profile
Authorization: Bearer <token>
Content-Type: application/json

{
  "fullName": "John Updated"
}
```

#### Change Password
```http
PUT /api/users/change-password
Authorization: Bearer <token>
Content-Type: application/json

{
  "currentPassword": "password123",
  "newPassword": "newPassword456"
}
```

#### Delete User (Admin Only)
```http
DELETE /api/users/{id}
Authorization: Bearer <token>
```

For complete API documentation, see [AUTH_API_DOCUMENTATION.md](AUTH_API_DOCUMENTATION.md)

## 🔒 Security

### JWT Authentication
- Stateless authentication using JWT tokens
- Token expiration: 24 hours
- BCrypt password hashing (strength: 10)
- Secure token validation on every request

### Role-Based Access Control
- **ADMIN**: Full system access, can delete users
- **MANAGER**: Team management capabilities
- **USER**: Basic access to own resources

### Security Features
- Password encryption with BCrypt
- JWT token-based authentication
- Protected endpoints with Spring Security
- Input validation with Jakarta Validation
- Global exception handling
- Soft delete for users (INACTIVE status)

For detailed security flow, see [SECURITY_FLOW.md](SECURITY_FLOW.md)

## 🗄️ Database Schema

### Users Table
```sql
CREATE TABLE users (
    id BIGSERIAL PRIMARY KEY,
    full_name VARCHAR(100) NOT NULL,
    email VARCHAR(100) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    role VARCHAR(20) NOT NULL,
    status VARCHAR(20) NOT NULL DEFAULT 'ACTIVE',
    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP NOT NULL
);

CREATE UNIQUE INDEX idx_user_email ON users(email);
```

For complete schema, see [DATABASE_SCHEMA.md](DATABASE_SCHEMA.md)

## 🧪 Testing

### Using Postman
1. Import `POSTMAN_COLLECTION.json`
2. Create environment with:
   - `base_url`: `http://localhost:8080`
   - `token`: (auto-populated after login)
3. Run requests in order:
   - Register User
   - Login User
   - Get Current User

### Using cURL

**Register:**
```bash
curl -X POST http://localhost:8080/api/auth/register \
  -H "Content-Type: application/json" \
  -d '{
    "fullName": "John Doe",
    "email": "john@example.com",
    "password": "password123",
    "role": "USER"
  }'
```

**Login:**
```bash
curl -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{
    "email": "john@example.com",
    "password": "password123"
  }'
```

**Get User (with token):**
```bash
curl -X GET http://localhost:8080/api/users/me \
  -H "Authorization: Bearer <your-token>"
```

## 📦 Dependencies

### Core Dependencies
```xml
<!-- Spring Boot Starters -->
spring-boot-starter-web
spring-boot-starter-security
spring-boot-starter-data-jpa
spring-boot-starter-data-redis
spring-boot-starter-validation

<!-- Database -->
postgresql

<!-- JWT -->
jjwt-api (0.12.3)
jjwt-impl (0.12.3)
jjwt-jackson (0.12.3)

<!-- Utilities -->
lombok
```

## 🔧 Configuration

### Application Properties
```properties
# Server
server.port=8080

# Database
spring.datasource.url=jdbc:postgresql://localhost:5432/crm_db
spring.datasource.username=postgres
spring.datasource.password=yourpassword

# JPA
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true

# Redis
spring.data.redis.host=localhost
spring.data.redis.port=6379

# JWT
jwt.secret=<your-secret-key>
jwt.expiration=86400000
```

## 🐛 Troubleshooting

### Port Already in Use
```properties
# Change port in application.properties
server.port=8081
```

### Database Connection Failed
```bash
# Check PostgreSQL status
sudo systemctl status postgresql

# Verify connection
psql -U postgres -d crm_db
```

### Redis Connection Failed
```bash
# Check Redis status
redis-cli ping

# Start Redis
redis-server
```

For more troubleshooting, see [SETUP_INSTRUCTIONS.md](SETUP_INSTRUCTIONS.md)

## 📖 Documentation

- [Setup Instructions](SETUP_INSTRUCTIONS.md) - Complete setup guide
- [API Documentation](AUTH_API_DOCUMENTATION.md) - All API endpoints
- [Security Flow](SECURITY_FLOW.md) - Security architecture
- [Database Schema](DATABASE_SCHEMA.md) - Database design
- [Postman Collection](POSTMAN_COLLECTION.json) - API testing

## 🎯 Architecture Principles

### Clean Architecture
- **Controller Layer**: REST API endpoints
- **Service Layer**: Business logic
- **Repository Layer**: Data access
- **Entity Layer**: Domain models
- **DTO Layer**: Data transfer objects

### Best Practices
- ✅ Constructor injection (no field injection)
- ✅ DTO pattern (no entity exposure)
- ✅ Service interfaces and implementations
- ✅ Global exception handling
- ✅ Input validation
- ✅ Proper REST API naming
- ✅ Transaction management
- ✅ Logging with SLF4J

## 🚀 Deployment

### Production Checklist
- [ ] Change JWT secret key
- [ ] Update database credentials
- [ ] Enable HTTPS/SSL
- [ ] Set `spring.jpa.hibernate.ddl-auto=validate`
- [ ] Configure environment variables
- [ ] Enable rate limiting
- [ ] Setup monitoring and logging
- [ ] Configure CORS properly
- [ ] Use production-grade database
- [ ] Setup backup strategy

## 📝 License

This project is licensed under the MIT License.

## 👥 Contributors

- Arjun - Lead Developer

## 📞 Support

For issues or questions:
- Check documentation files
- Review troubleshooting section
- Check application logs

---

**Built with ❤️ using Spring Boot**
