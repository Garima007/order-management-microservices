# Order Management Microservices

A backend system built with Java and Spring Boot demonstrating microservices architecture, service decomposition, containerization, and REST API design.

---

## Architecture Overview

The system is decomposed into four independently deployable services, each owning its own domain and data:

```
┌─────────────────┐     ┌─────────────────┐
│   user-service  │     │  order-service  │
│                 │     │                 │
│  User accounts  │◄────│  Order lifecycle│
│  & profiles     │     │  & management   │
└─────────────────┘     └────────┬────────┘
                                 │
              ┌──────────────────┼──────────────────┐
              │                                     │
   ┌──────────▼────────┐              ┌─────────────▼──────┐
   │ notification-     │              │  analytics-service  │
   │ service           │              │                     │
   │                   │              │  Order analytics    │
   │  Async alerts &   │              │  & reporting        │
   │  event handling   │              └────────────────────┘
   └───────────────────┘
```

### Services

| Service | Responsibility |
|---|---|
| **user-service** | User registration, authentication, and profile management |
| **order-service** | Order creation, status lifecycle, and business logic |
| **notification-service** | Async notifications triggered by order events |
| **analytics-service** | Order metrics and reporting |

---

## Tech Stack

- **Language:** Java 8+
- **Framework:** Spring Boot, Spring MVC, Spring Data JPA
- **Database:** PostgreSQL (per service)
- **Containerization:** Docker, Docker Compose
- **API Style:** REST

---

## Design Decisions

**Why separate services?**
Each service owns a single bounded context. Order lifecycle is decoupled from notification delivery — a notification failure does not affect order processing.

**Why Docker Compose for local setup?**
Allows the full system to spin up with a single command without external dependencies. Each service runs in its own container with isolated networking.

**Database per service**
Each service manages its own schema. No shared database — services communicate via REST APIs, keeping boundaries clean and deployments independent.

---

## Getting Started

### Prerequisites

- Java 8+
- Docker and Docker Compose

### Run the full system

```bash
git clone https://github.com/Garima007/order-management-microservices.git
cd order-management-microservices
docker-compose up --build
```

All services will start with their dependencies (PostgreSQL instances) resolved automatically via Docker Compose networking.

### Run a single service locally

```bash
cd order-service
./mvnw spring-boot:run
```

---

## API Overview

### User Service
| Method | Endpoint | Description |
|---|---|---|
| POST | `/api/users/register` | Register a new user |
| GET | `/api/users/{id}` | Get user by ID |

### Order Service
| Method | Endpoint | Description |
|---|---|---|
| POST | `/api/orders` | Create a new order |
| GET | `/api/orders/{id}` | Get order by ID |
| PUT | `/api/orders/{id}/status` | Update order status |
| GET | `/api/orders/user/{userId}` | Get all orders for a user |

### Notification Service
| Method | Endpoint | Description |
|---|---|---|
| POST | `/api/notifications` | Trigger a notification |

### Analytics Service
| Method | Endpoint | Description |
|---|---|---|
| GET | `/api/analytics/orders` | Get order metrics |

---

## Project Structure

```
order-management-microservices/
├── user-service/
│   ├── src/
│   ├── Dockerfile
│   └── pom.xml
├── order-service/
│   ├── src/
│   ├── Dockerfile
│   └── pom.xml
├── notification-service/
│   ├── src/
│   ├── Dockerfile
│   └── pom.xml
├── analytics-service/
│   ├── src/
│   ├── Dockerfile
│   └── pom.xml
└── docker-compose.yml
```

---

## Roadmap

- [ ] Add Kafka for async event streaming between order and notification service
- [ ] Add Redis caching layer on order-service for read-heavy queries
- [ ] Add API Gateway (Spring Cloud Gateway) as single entry point
- [ ] Add centralized config (Spring Cloud Config)
- [ ] Deploy to AWS EC2 with RDS (PostgreSQL)
- [ ] Add Prometheus + Grafana observability stack

---

## About

Built to demonstrate hands-on proficiency in microservices architecture, service decomposition, REST API design, and containerized local deployment using Java and Spring Boot.
