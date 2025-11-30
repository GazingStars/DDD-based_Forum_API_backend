
# Architecture Overview

The project is built using **DDD (Domain-Driven Design)** and **Clean Architecture**, structured into several layers:

---

## **1. Domain Layer (Core Business Logic)**

Contains pure domain objects with **no dependencies on Spring, databases, or infrastructure**:

- Aggregates: `User`, `Post`, `Comment`, `Category`, `Like`, `Avatar`
- Value Objects: `Email`, `Username`, `PostContent`, `CategorySlug`, `LikeTargetId`, etc.
- Domain Services: `PostDomainService`
- Domain Repository Interfaces

Characteristics:
- No database logic
- No Spring annotations
- Only business rules and invariants

The domain layer is the **heart of the system**.

---

## **2. Application Layer (Use Cases)**

Contains services that coordinate actions requested by controllers:

- `UserService`
- `PostService`
- `CommentService`
- `CategoryService`
- `AuthService`
- `LikeService`
- `AvatarService`

This layer is responsible for:

- Permission checks (roles USER / MODERATOR / ADMIN)
- Invoking domain methods
- Orchestrating domain logic
- Managing transactions (`@Transactional`)

---

## **3. Infrastructure Layer**

Contains adapters and technical implementations:

- JPA entities + Spring Data repositories:  
  `UserEntity`, `PostEntity`, `CommentEntity`, `CategoryEntity`, `LikeEntity`, `AvatarEntity`  
  Repository implementations like `SpringDataUserRepo`, `SpringDataPostRepo`, etc.

- Mappers between Domain ↔ JPA models

- Security components:  
  `JwtAuthenticationFilter`, `AuthUserPrincipal`, `JwtProvider`, `SecurityConfig`

Resources include:

- Flyway migrations: `db/migration/VXXX__*.sql`
- File storage (avatars): local `/uploads` or S3 (optional)

---

## **4. Web / API Layer**

REST controllers:

- **AuthController**
- **UserProfileController**
- **PostController**
- **CommentController**
- **LikeController**
- **CategoryController**
- **AdminController**
- **ModeratorController**
- **SearchController**
- **AvatarController**

Responsibilities of this layer:

- Handling HTTP requests
- Parsing and validating DTOs
- Returning Response Models (DTOs)
- Contains *no business logic*

---

## **Technology Stack**

- Spring Boot 3.5
- Spring Security + JWT
- Spring Data JPA + Hibernate
- PostgreSQL + Flyway
- Testcontainers
- DDD-style modular architecture
- Domain module + infrastructure + application layer  

