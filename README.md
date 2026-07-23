# Order Management Microservices

A production-grade microservices architecture demonstrating distributed system design patterns, resilience strategies, and observability implementation. Built with Spring Boot, Kafka, PostgreSQL, and Docker.

**Status:** Foundation Complete (Week 0) | Resilience Patterns In Progress (Week 1-2)

---

## 🎯 Project Goals

This project is built for two purposes:
1. **Interview Preparation:** Demonstrates senior backend engineering competencies
2. **Production Learning:** Real patterns used in scale systems (Netflix, Uber, Amazon)

---

## 📐 Architecture Overview

```
┌─────────────────────────────────────────────────────────────────┐
│                     API Gateway (Week 4)                        │
│                   Routes: /orders, /users, etc                  │
└─────────────────┬──────────────────────────────────────────────┘
                  │
        ┌─────────┴──────────┬────────────┬──────────────┐
        │                    │            │              │
    ┌───▼────┐        ┌──────▼─┐    ┌───▼─────┐   ┌────▼──────┐
    │ Order  │        │ User   │    │Notif.   │   │ Analytics │
    │Service │        │Service │    │Service  │   │ Service   │
    └───┬────┘        └────────┘    └────┬────┘   └────┬──────┘
        │                                 │             │
        │      ┌──────────────────────────┴─────────────┘
        │      │
        │      │  Kafka + Zookeeper
        │      │  (Async Messaging)
        │      │
        └──────┘

Service Discovery: Eureka Server (port 8761)
Databases: PostgreSQL (per-service pattern)
Containerization: Docker Compose
```

---

## 🔧 Tech Stack

| Component | Technology | Version | Purpose |
|-----------|-----------|---------|---------|
| **Framework** | Spring Boot | 3.5.3 | Microservice foundation |
| **Java** | OpenJDK | 21 | Language & runtime |
| **Service Discovery** | Netflix Eureka | Spring Cloud | Dynamic service lookup |
| **Async Messaging** | Apache Kafka | 7.4.0 | Event-driven communication |
| **Database** | PostgreSQL | 17 | Data persistence (per-service) |
| **Inter-service Calls** | Spring Cloud OpenFeign | Spring Cloud | Declarative REST clients |
| **Containerization** | Docker + Docker Compose | Latest | Local & cloud deployment |
| **Monitoring** | Spring Boot Actuator | Spring Boot | Health checks & endpoints |
| **Resilience** | Resilience4j | Week 1-2 TODO | Circuit breaker pattern |
| **Tracing** | Zipkin | Week 5-6 TODO | Distributed request tracing |
| **Metrics** | Prometheus + Micrometer | Week 5-6 TODO | Performance monitoring |

---

## 📦 Microservices

### 1. **Order Service** (Port 8081)
Handles order creation, management, and orchestration.

**Responsibilities:**
- Create, retrieve, update order status
- Validate user via User Service (OpenFeign + Circuit Breaker)
- Publish order events to Kafka (`order-created`, `order-updated`)
- Listen to payment/inventory events for saga coordination (Week 3)

**Database:** PostgreSQL `orderdb`

**Key Dependencies:**
- Spring Web (REST APIs)
- Spring Data JPA (Database)
- Spring Kafka (Event publishing)
- Spring Cloud Netflix Eureka (Service discovery)
- Spring Cloud OpenFeign (User Service calls)
- Resilience4j (Circuit breaker - Week 1-2)

**API Endpoints:**
```
POST /api/orders/create          - Create new order
GET  /api/orders/{orderId}       - Get order details
PUT  /api/orders/{orderId}       - Update order status
GET  /api/orders/user/{userId}   - Get user's orders
```

---

### 2. **User Service** (Port 8080)
Manages user registration, authentication, and profile management.

**Responsibilities:**
- User registration & login
- User profile retrieval
- User validation for order creation

**Database:** PostgreSQL `userdb`

**Key Dependencies:**
- Spring Web (REST APIs)
- Spring Data JPA (Database)
- Spring Cloud Netflix Eureka (Service discovery)

**API Endpoints:**
```
POST /api/users/register         - Register new user
POST /api/users/login            - User login
GET  /api/users/{userId}         - Get user details
```

---

### 3. **Notification Service**
Consumes order events and sends notifications.

**Responsibilities:**
- Listen to Kafka `order-created` topic
- Send email/SMS notifications
- Handle notification failures gracefully (Week 3 - DLQ)

**Database:** None (stateless)

**Key Dependencies:**
- Spring Kafka (Event consumption)
- Spring Cloud Netflix Eureka (Service discovery)

**Configuration:**
- Kafka Consumer Group: `notification-group-v99`
- Auto Offset Reset: `earliest` (resume from last processed message)

---

### 4. **Analytics Service**
Processes order events for analytics and business intelligence.

**Responsibilities:**
- Listen to Kafka events
- Store analytics data
- Track order trends, revenue, etc.

**Database:** PostgreSQL `analyticsdb`

**Key Dependencies:**
- Spring Kafka (Event consumption)
- Spring Data JPA (Store analytics)
- Spring Cloud Netflix Eureka (Service discovery)

---

### 5. **Eureka Server** (Port 8761)
Service registry for dynamic service discovery.

**Responsibilities:**
- Register all microservices
- Provide service location info to clients
- Health check all registered services

**UI Dashboard:** `http://localhost:8761`

---

## 🚀 Getting Started

### Prerequisites
- Docker & Docker Compose
- Java 21 (if running locally without Docker)
- Maven 3.8+ (if building locally)

### Quick Start

**1. Clone the repository:**
```bash
git clone https://github.com/Garima007/order-management-microservices.git
cd order-management-microservices
```

**2. Start all services:**
```bash
docker-compose up --build
```

This will start:
- Eureka Server (port 8761)
- Order Service (port 8081)
- User Service (port 8080)
- Notification Service
- Analytics Service
- PostgreSQL (3 instances)
- Kafka + Zookeeper
- (Week 4: API Gateway)
- (Week 5-6: Zipkin, Prometheus)

**3. Verify services are running:**
```bash
# Eureka Dashboard
http://localhost:8761

# Health checks
curl http://localhost:8080/actuator/health    # User Service
curl http://localhost:8081/actuator/health    # Order Service
```

**4. Create an order (test flow):**
```bash
# 1. Register user
curl -X POST http://localhost:8080/api/users/register \
  -H "Content-Type: application/json" \
  -d '{"name":"John Doe","email":"john@example.com","password":"pass123"}'

# 2. Create order
curl -X POST http://localhost:8081/api/orders/create \
  -H "Content-Type: application/json" \
  -d '{"userId":1,"amount":100.00,"items":["item1","item2"]}'

# 3. Check notification service logs
docker logs order-management-microservices-notification-service-1
```

---

## 📊 Current Implementation Status

### ✅ Implemented (Week 0)

| Feature | Status | Details |
|---------|--------|---------|
| Eureka Service Discovery | ✅ | All services registered & discoverable |
| PostgreSQL Databases | ✅ | Per-service databases, migrations working |
| Kafka Event Publishing | ✅ | Order Service → `order-created` topic |
| Kafka Event Consumption | ✅ | Notification & Analytics services listening |
| REST APIs | ✅ | User & Order services have endpoints |
| Docker Compose Setup | ✅ | All services containerized & running |
| Inter-Service Communication | ✅ | OpenFeign client for Order → User calls |
| Health Endpoints | ✅ | Actuator `/health` configured |

### ⭕ In Progress (Week 1-2)

| Feature | Status | Target | Details |
|---------|--------|--------|---------|
| Circuit Breaker | 🔨 IN PROGRESS | Week 2 | Resilience4j for Order → User calls |
| Fallback Logic | 🔨 IN PROGRESS | Week 2 | Graceful degradation when service down |
| Retry Configuration | ⏳ TODO | Week 2 | Automatic retry with exponential backoff |

### 📋 Upcoming (Week 3-12)

| Feature | Status | Target | Week | Details |
|---------|--------|--------|------|---------|
| Saga Pattern | ⏳ TODO | Week 3 | Choreography-based distributed transactions |
| Dead Letter Queue | ⏳ TODO | Week 3 | Handle failed Kafka messages |
| API Gateway | ⏳ TODO | Week 4 | Spring Cloud Gateway, single entry point |
| Distributed Tracing | ⏳ TODO | Week 5-6 | Zipkin for request flow visualization |
| Metrics & Monitoring | ⏳ TODO | Week 5-6 | Prometheus + Micrometer for observability |
| Configuration Server | ⏳ TODO | Week 6 | Centralized config management |
| AWS Deployment | ⏳ TODO | Week 4 | EC2, RDS, ALB, CloudWatch |
| Load Balancing | ⏳ TODO | Week 6 | Client-side & server-side LB |
| Security | ⏳ TODO | Week 7 | JWT authentication, encryption |
| Testing | ⏳ TODO | Week 7-8 | Unit, integration, contract tests |

---

## 🏗️ Folder Structure

```
order-management-microservices/
│
├── eureka-server/                 # Service Registry
│   ├── pom.xml
│   ├── Dockerfile
│   └── src/main/java/...
│
├── order-service/                 # Order Management
│   ├── pom.xml
│   ├── Dockerfile
│   ├── src/main/java/
│   │   ├── client/                # OpenFeign client for User Service
│   │   ├── config/                # Configuration (Kafka, Eureka)
│   │   ├── controller/            # REST endpoints
│   │   ├── dto/                   # Data transfer objects
│   │   ├── entity/                # JPA entities
│   │   ├── event/                 # Event models
│   │   ├── kafka/                 # Kafka producer
│   │   ├── repository/            # Data repositories
│   │   ├── service/               # Business logic
│   │   └── OrderServiceApplication.java
│   └── src/main/resources/
│       └── application.yml        # Configuration
│
├── user-service/                  # User Management
│   ├── pom.xml
│   ├── Dockerfile
│   └── src/main/java/...
│
├── notification-service/          # Notification Processing
│   ├── pom.xml
│   ├── Dockerfile
│   └── src/main/java/...
│
├── analytics-service/             # Analytics Processing
│   ├── pom.xml
│   ├── Dockerfile
│   └── src/main/java/...
│
├── docker-compose.yml             # Container orchestration
├── .gitignore
└── README.md                      
```

---

## 🔌 API Gateway Setup (Week 4)

Once added, the API Gateway will provide single entry point:

```bash
# Before (direct service calls)
POST http://localhost:8080/api/users/register
POST http://localhost:8081/api/orders/create

# After (through gateway)
POST http://localhost:8080/api/users/register        # Gateway routes to user-service
POST http://localhost:8080/api/orders/create         # Gateway routes to order-service
```

---

## 📈 Observability & Monitoring (Week 5-6)

### Distributed Tracing (Zipkin)
Once implemented, trace order creation across services:

```bash
# Visit Zipkin UI
http://localhost:9411

# Trace flow:
Order Service → User Service (OpenFeign call)
           ↓
Order Service → Kafka Producer
           ↓
Notification Service → Kafka Consumer
Analytics Service → Kafka Consumer
```

### Metrics (Prometheus)
Monitor system health:

```bash
# Prometheus UI
http://localhost:9090

# Key metrics:
- order_created_total (counter)
- order_creation_time_seconds (histogram)
- kafka_consumer_lag (gauge)
- circuitbreaker_state (gauge)
```

### Structured Logging
Correlate requests across services using trace IDs:

```json
{
  "timestamp": "2026-07-23T10:30:45.123Z",
  "level": "INFO",
  "trace_id": "abc123def456",
  "service": "order-service",
  "message": "Order created",
  "order_id": 42,
  "user_id": 7,
  "amount": 100.00
}
```

---

## 🛡️ Resilience Patterns

### Circuit Breaker (Week 1-2)
Prevents cascading failures when Order Service calls User Service:

```
Scenario: User Service crashes
├─ Request 1-3: CLOSED (calls succeed or fail quickly)
├─ Request 4: Fail → Circuit opens
├─ Request 5+: OPEN (fast fail, no call made)
├─ After 30s: HALF_OPEN (test 1 call)
├─ If success: CLOSED (resume normal)
└─ If fail: OPEN again
```

**Benefit:** Prevent thread pool exhaustion, fast error feedback

### Saga Pattern (Week 3)
Distributed transaction with automatic rollback:

```
Order Creation Flow:
1. Order Service creates order (PENDING)
2. Publishes OrderCreatedEvent
3. Payment Service consumes event, charges user
4. Publishes PaymentCompletedEvent
5. Order Service listens, updates order (CONFIRMED)

Failure Scenario:
1. Order Service creates order (PENDING)
2. Publishes OrderCreatedEvent
3. Payment Service charges user → FAILS
4. Publishes PaymentFailedEvent
5. Order Service listens, updates order (CANCELLED)
→ Automatic rollback, no orphaned transactions
```

**Benefit:** Eventual consistency without distributed transactions locks

### Dead Letter Queue (Week 3)
Handle Kafka consumer failures:

```
Normal Flow:
Order Service → Kafka Topic → Notification Service (success)

Error Flow:
Order Service → Kafka Topic → Notification Service (fails)
                                 ↓
                         Retry 3 times
                                 ↓
                         Still fails?
                                 ↓
                        DLQ Topic (investigation)
                                 ↓
                        Alert ops team
                                 ↓
                        Manual replay or fix
```

**Benefit:** No lost messages, visibility into failures

---

## 🧪 Testing

### Local Testing
```bash
# 1. Start all services
docker-compose up

# 2. Test User Service
curl -X POST http://localhost:8080/api/users/register \
  -H "Content-Type: application/json" \
  -d '{"name":"Test User","email":"test@example.com"}'

# 3. Test Order Service
curl -X POST http://localhost:8081/api/orders/create \
  -H "Content-Type: application/json" \
  -d '{"userId":1,"amount":50.0}'

# 4. Check Kafka messages
docker exec order-management-microservices-kafka-1 \
  kafka-console-consumer --bootstrap-server localhost:9092 \
  --topic order-created --from-beginning

# 5. Verify Eureka registration
curl http://localhost:8761/eureka/apps
```

### Circuit Breaker Testing (Week 2)
```bash
# 1. Stop user service
docker-compose stop user-service

# 2. Call order creation → should fail fast (circuit breaker)
curl -X POST http://localhost:8081/api/orders/create

# 3. Check actuator for circuit breaker state
curl http://localhost:8081/actuator/health

# 4. Restart user service
docker-compose up -d user-service

# 5. Wait 30s for half-open state, retry order creation
# Should succeed when user service is back
```

---

## 📚 Learning Resources

**Microservices Architecture:**
- [Building Microservices by Sam Newman](https://samnewman.io/books/building_microservices/)
- [Spring Microservices in Action](https://www.manning.com/books/spring-microservices-in-action)

**Resilience Patterns:**
- [Resilience4j Documentation](https://resilience4j.readme.io/)
- [Release It! by Michael Nygard](https://pragprog.com/titles/mnee2/release-it-second-edition/)

**Distributed Systems:**
- [Designing Data-Intensive Applications by Martin Kleppmann](https://dataintensive.net/)
- [Distributed Systems by Maarten van Steen](https://www.distributed-systems.net/)

**Kafka:**
- [Apache Kafka Documentation](https://kafka.apache.org/documentation/)
- [Kafka: The Definitive Guide](https://www.confluent.io/resources/kafka-the-definitive-guide/)

**Spring Cloud:**
- [Spring Cloud Documentation](https://spring.io/projects/spring-cloud)
- [Spring Cloud Netflix](https://spring.io/projects/spring-cloud-netflix)

---

## 🎓 Interview Preparation Map

This project is structured to answer common backend interview questions:

| Question | Where Demonstrated | Week Ready |
|----------|-------------------|-----------|
| "Design microservices for e-commerce" | Entire project | Week 4 |
| "How do you handle service failures?" | Circuit Breaker | Week 2 |
| "How do you ensure data consistency?" | Saga Pattern | Week 3 |
| "How do you debug distributed systems?" | Distributed Tracing | Week 6 |
| "How do you scale microservices?" | API Gateway, Load Balancing | Week 4-6 |
| "How do you monitor production?" | Metrics, Logging, Tracing | Week 6 |
| "How would you deploy to cloud?" | AWS architecture | Week 4 |
| "How do you handle Kafka failures?" | DLQ, Error handling | Week 3 |
| "How do you ensure idempotency?" | Saga pattern implementation | Week 3 |
| "How do you optimize performance?" | Metrics, caching, batching | Week 6 |

---

## 🔄 Development Workflow

### Adding a New Feature (Example: Circuit Breaker)

**1. Create feature branch**
```bash
git checkout -b feature/circuit-breaker
```

**2. Update pom.xml**
```xml
<dependency>
  <groupId>io.github.resilience4j</groupId>
  <artifactId>resilience4j-spring-boot3</artifactId>
  <version>2.1.0</version>
</dependency>
```

**3. Add configuration (application.yml)**
```yaml
resilience4j:
  circuitbreaker:
    instances:
      userService:
        registerHealthIndicator: true
        failureRateThreshold: 50
        minimumNumberOfCalls: 5
        automaticTransitionFromOpenToHalfOpenEnabled: true
        waitDurationInOpenState: 10s
```

**4. Implement in code**
```java
@Component
public class UserClient {
  
  @CircuitBreaker(name = "userService", fallbackMethod = "fallback")
  public UserDTO getUser(Long userId) {
    return restTemplate.getForObject(...);
  }
  
  public UserDTO fallback(Long userId, Exception e) {
    return new UserDTO(userId, "Unknown", "unknown@example.com");
  }
}
```

**5. Test locally**
```bash
docker-compose up
# Stop user-service, call order-service, watch circuit break
```

**6. Commit & push**
```bash
git add -A
git commit -m "feat: add circuit breaker for user-service calls"
git push origin feature/circuit-breaker
```

**7. Create Pull Request**
- Explain why this change is needed
- Link to interview prep roadmap
- Include testing evidence

---

## 🤝 Contributing

This project is structured for learning and interview preparation. To contribute:

1. Fork the repository
2. Create a feature branch (`git checkout -b feature/your-feature`)
3. Implement the feature with tests
4. Commit with clear messages (`git commit -m "feat: add X pattern"`)
5. Push to branch (`git push origin feature/your-feature`)
6. Open Pull Request with description

---

## 📝 Commit Message Convention

Follow clear commit messages for traceability:

```
feat: add circuit breaker for user-service resilience
fix: handle Kafka deserialization errors
docs: update README with saga pattern explanation
test: add circuit breaker unit tests
refactor: extract Kafka config to separate class
chore: update dependencies to latest versions
```

---

## ⚙️ Configuration Reference

### Environment Variables (docker-compose.yml)

**Order Service:**
```yaml
environment:
  DB_URL: jdbc:postgresql://postgres-order:5432/orderdb
  DB_USERNAME: orderuser
  DB_PASSWORD: order123
  EUREKA_CLIENT_SERVICE_URL_DEFAULT_ZONE: http://eureka-server:8761/eureka
  KAFKA_BOOTSTRAP_SERVERS: kafka:9092
```

**Notification Service:**
```yaml
environment:
  KAFKA_BOOTSTRAP_SERVERS: kafka:9092
  EUREKA_CLIENT_SERVICE_URL_DEFAULT_ZONE: http://eureka-server:8761/eureka
```

### Kafka Topics (auto-created)
```
order-created         # Order creation events
order-updated         # Order status updates (Week 3)
payment-events        # Payment responses (Week 3)
notification-dlq      # Failed notifications (Week 3)
analytics-events      # Analytics tracking (Week 6)
```

---

## 🐛 Troubleshooting

### Services Not Starting
```bash
# Check logs
docker-compose logs order-service

# Restart all services
docker-compose restart

# Full rebuild
docker-compose down
docker-compose up --build
```

### Eureka Shows "UNKNOWN" Status
```bash
# Wait 30-60 seconds for services to register
# Check individual service health
curl http://localhost:8081/actuator/health
curl http://localhost:8080/actuator/health
```

### Kafka Consumer Not Processing Messages
```bash
# Check consumer group status
docker exec order-management-microservices-kafka-1 \
  kafka-consumer-groups --bootstrap-server localhost:9092 \
  --group notification-group-v99 --describe

# Reset offset to earliest
docker exec order-management-microservices-kafka-1 \
  kafka-consumer-groups --bootstrap-server localhost:9092 \
  --group notification-group-v99 --reset-offsets --to-earliest --execute
```

### Database Connection Failed
```bash
# Check PostgreSQL is running
docker-compose ps postgres-order

# Verify credentials in docker-compose.yml
# Check connection from container
docker exec order-management-microservices-postgres-order-1 \
  psql -U orderuser -d orderdb -c "SELECT 1"
```

---

## 📊 Week-by-Week Progress Tracking

- [x] Week 0: Foundation (Eureka, Kafka, PostgreSQL, Docker)
- [ ] Week 1-2: Circuit Breaker (In Progress)
- [ ] Week 3: Saga Pattern
- [ ] Week 4: API Gateway + AWS
- [ ] Week 5-6: Observability Stack
- [ ] Week 7-10: Mock Interviews
- [ ] Week 11-12: Refinement + Job Applications

---

## 📄 License

This project is for educational and interview preparation purposes.

---

## 👨‍💼 Author

**Garima Bajpai**
- 9+ years backend engineer (Java, Spring Boot)
- Experience: UKG, Sopra Steria, Falqor
- Target: ₹40 LPA+ Senior Backend roles at product companies

---

## 📞 Contact & Questions

For questions about this project or interview prep:
- GitHub Issues: [Create an issue](https://github.com/Garima007/order-management-microservices/issues)
- Email: [Your email]
- LinkedIn: [Your profile]

---

## 🎯 Next Steps

1. **Week 1-2:** Implement Circuit Breaker
   - [ ] Add Resilience4j dependency
   - [ ] Implement @CircuitBreaker on UserClient
   - [ ] Add fallback logic
   - [ ] Test with service down
   - [ ] Commit to GitHub

2. **Week 3:** Implement Saga Pattern
   - [ ] Add Payment Service
   - [ ] Implement saga coordination
   - [ ] Add DLQ for failed messages
   - [ ] Test rollback scenarios
   - [ ] Commit to GitHub

3. **Week 4:** Add API Gateway + AWS
   - [ ] Create API Gateway service
   - [ ] Configure routes
   - [ ] Learn AWS deployment
   - [ ] Document architecture
   - [ ] Commit to GitHub

See [12-Week Roadmap](./ROADMAP.md) for full details.

---

**Last Updated:** July 23, 2026  
**Current Phase:** Week 1-2 (Circuit Breaker Implementation)  
**Status:** Building Production Patterns | Interview-Ready in 12 weeks
