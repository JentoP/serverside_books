## Project Overview

This is a Spring Boot full-stack application consisting of:
- **Backend**: Spring Boot 3.3.5 with Java 21, Spring Security, JPA/Hibernate, and H2/PostgreSQL database support
- **Frontend**: React 18 application with React Query, Bootstrap, and React Router
- **Architecture**: RESTful API with DTO pattern, service layer, repository layer, and comprehensive security configuration

### Demo
- Production demo URL available on Canvas
- Login with `vera/vera`, `marie/password`, or `admin/admin`
- Or sign up to register as a new user

## Prerequisites

- **Java**: Java 21 (on PATH)
- **Node.js**: v22.3.0 or compatible
- **npm**: 10.8.1 or compatible
- **IDE**: IntelliJ IDEA (recommended)

## Quick Start

### Running the Complete Application

**Option 1: Build and Run (Recommended)**
```bash
# Build entire application (backend + frontend)
./mvnw clean package

# Run the application
java -jar target/bookserver-0.0.1-SNAPSHOT.jar
```

**Option 2: Run from IntelliJ**
- Open the project as a Maven project
- Run `BookserverApplication.main()`

**Option 3: Maven command**
```bash
./mvnw package
java -jar target/bookserver-0.0.1-SNAPSHOT.jar be.thomasmore.bookserver.BookserverApplication
```

**Access the application**: http://localhost:8080

> **Note**: You must run at least `./mvnw compile` before the client is served by the server.

### Backend Development

```bash
# Compile backend only
./mvnw compile

# Run tests
./mvnw test

# Run specific test class
./mvnw test -Dtest=BookControllerGetAllBooksTest

# Run tests in specific package
./mvnw test -Dtest="be.thomasmore.bookserver.controllers.book.*"
```

### Frontend Development

```bash
# Navigate to frontend directory
cd src/main/frontend

# Install dependencies
npm install

# Start development server (with hot reload)
npm start
# Access at: http://localhost:3000

# Build frontend for production
npm run build

# Run frontend tests
npm test
```

## Application Architecture

### Core Entities and Relationships

- **Book**: Central entity with title, price, and many-to-many relationship with Authors
- **Author**: Linked to multiple books
- **Serie**: Groups of related books
- **Genre**: Book categorization
- **User**: Authentication and authorization with role-based access

### Layer Structure

- **Controllers**: REST endpoints (`/api/*`) with Swagger documentation
- **Services**: Business logic layer with interfaces and implementations
- **Repositories**: Data access layer using Spring Data JPA
- **DTOs**: Data Transfer Objects with separate converters for API responses
- **Models**: JPA entities with Lombok annotations and validation

### Security Configuration

- CSRF protection enabled with cookie-based tokens
- Session-based authentication with database user store
- Role-based authorization (admin/user roles)
- GET requests on `/api/**` are public, POST/PUT/DELETE require authentication
- H2 console accessible in development mode

### Database Profiles

- **dev** (default): H2 in-memory database with `data.sql` initialization
- **test**: Separate H2 instance for testing, no data initialization
- **prod**: PostgreSQL with Flyway migrations (database URL required)

## API Design

### RESTful Endpoints

- Resources follow pattern: `/api/{resource}` and `/api/{resource}/{id}`
- Detailed DTOs for single resource retrieval, simplified DTOs for collections
- Nested resources accessible via: `/api/{resource}/{id}/{nested-resource}`

### Example API Calls

**Get all books (browser)**
```
GET http://localhost:8080/api/books
```

### API Testing with Postman

- Import collection: `Books.postman_collection.json`
- Or import Swagger docs: http://localhost:8080/v2/api-docs
- **CSRF Protection**: The collection automatically handles CSRF tokens
  - Create an environment first (e.g., "dev")
  - XSRF-TOKEN cookie is automatically copied to X-XSRF-TOKEN header
- **Important**: Always keep the Postman collection up-to-date in the repo

## Development URLs

| Service | URL | Notes |
|---------|-----|-------|
| **Full Application** | http://localhost:8080 | Complete app (after build) |
| **Frontend Dev Server** | http://localhost:3000 | Hot reload during development |
| **API Documentation** | http://localhost:8080/swagger-ui/index.html | Swagger UI |
| **H2 Console** | http://localhost:8080/h2-console | Dev mode only |
| **API Docs (JSON)** | http://localhost:8080/v2/api-docs | For Postman import |

### H2 Database Console

Access at: http://localhost:8080/h2-console (development mode only)

**Connection Settings:**
- JDBC URL: `jdbc:h2:mem:books`
- Username: `sa`
- Password: (empty)

## Authentication

### Default Users

Defined in `data.sql`:
- `vera/vera` - Regular user
- `marie/password` - Regular user
- `admin/admin` - Admin user

Users can also register new accounts via the signup endpoint.

## Testing Strategy

- Integration tests inherit from `AbstractIntegrationTest`
- Tests use `@ActiveProfiles("test")` for test database configuration
- MockMvc for controller testing with automatic CSRF token handling
- Test classes organized by controller and operation (e.g., `BookControllerCreateTest`)

## Frontend Integration

- React frontend served by Spring Boot at `/` after Maven build
- Proxy configuration routes API calls to backend during development
- Frontend build automatically copied to `target/classes/public` via Maven plugins

## Project Structure

```
bookserver/
├── src/
│   ├── main/
│   │   ├── java/be/thomasmore/bookserver/
│   │   │   ├── controllers/      # REST endpoints
│   │   │   ├── services/          # Business logic
│   │   │   ├── repositories/      # Data access
│   │   │   ├── model/             # JPA entities
│   │   │   └── dto/               # Data Transfer Objects
│   │   ├── resources/
│   │   │   ├── application.properties
│   │   │   └── data.sql           # Initial data
│   │   └── frontend/              # React application
│   │       ├── src/
│   │       ├── public/
│   │       └── package.json
│   └── test/                      # Test classes
├── pom.xml                        # Maven configuration
└── Books.postman_collection.json  # Postman API tests
```

## Common Development Tasks

### Adding a New Entity

1. Create model class with JPA annotations
2. Create repository interface
3. Create DTOs (detailed and simplified)
4. Create DTO converter
5. Create service interface and implementation
6. Create controller with REST endpoints
7. Add test classes
8. Update `data.sql` if needed

### Running with Different Profiles

```bash
# Development (H2, default)
./mvnw spring-boot:run

# Test profile
./mvnw spring-boot:run -Dspring-boot.run.profiles=test

# Production (PostgreSQL)
./mvnw spring-boot:run -Dspring-boot.run.profiles=prod
```

## Troubleshooting

### Frontend not loading at localhost:8080
- Ensure you've run `./mvnw compile` or `./mvnw package` at least once
- Check that frontend build files are in `target/classes/public`

### CSRF errors in Postman
- Ensure you've created a Postman environment
- The collection automatically handles tokens
- For manual requests: copy XSRF-TOKEN cookie to X-XSRF-TOKEN header

### Database issues
- Check H2 console at http://localhost:8080/h2-console
- Verify JDBC URL: `jdbc:h2:mem:books`
- Ensure `data.sql` is being loaded (dev profile only)

## Additional Resources

- [Spring Boot Documentation](https://spring.io/projects/spring-boot)
- [React Documentation](https://react.dev)
- [Spring Data JPA](https://spring.io/projects/spring-data-jpa)
- [Spring Security](https://spring.io/projects/spring-security)