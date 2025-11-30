# Forum API — Installation & Verification Guide

## 1. Clone the Repository

```bash
git clone https://github.com/
cd your-project
```

---

## 2. Install PostgreSQL

Create a database:

```sql
CREATE DATABASE forum;
```

---

## 3. Configure Spring Boot

`application.yml`:

```properties
spring:
    datasource:
        url: jdbc:postgresql://localhost:5432/forum
        username: postgres
        password: 3327
    
    jpa:
        hibernate:
            ddl-auto: validate
        show-sql: true
    
    flyway:
        enabled: true
        locations: classpath:db/migration
        clean-disabled: false
jwt:
    secret: "my-very-strong-secret-key-at-least-32-chars"
    expiration: 86400000   # 24 часа
```

---

## 4. Run the Application

```bash
mvn spring-boot:run
```

Server starts at:

```
http://localhost:8080
```

---

## 5. Check Migrations (Flyway)

On successful startup:

```
Successfully applied n migrations
```

---

## 6. Import Postman Collection

Import file:

```
Forum API.postman_collection.json
```

Postman automatically sets:

- baseUrl
- token
- postId
- commentId
- categoryId
- userId  

## 7. Authorize the User

### Step 1 — Register

```
POST /api/auth/register
```

Body:
```json
{
  "email": "test@mail.com",
  "username": "john",
  "password": "123456"
}
```

### Step 2 — Login

```
POST /api/auth/login
```

Token is saved automatically.

---

## 8. Test All Endpoints

### Users

- `GET /api/user/me`
- `PUT /api/user/me`

---

### Categories

- `POST /api/admin/categories` → saves `categoryId`
- `GET /api/categories`
- `GET /api/categories/{slug}`
- `PATCH /api/admin/categories/{id}`
- `DELETE /api/admin/categories/{id}`

---

### Posts

#### Create Post
```
POST /api/posts
```

Body:
```json
{
  "title": "Hello",
  "content": "My first post",
  "categoryId": "{{categoryId}}"
}
```

Auto-saves `postId`.

---

### Comments

- `POST /api/posts/{{postId}}/comments`
- `GET /api/posts/{{postId}}/comments`
- `DELETE /api/mod/comments/{{commentId}}`

---

### Likes

- `POST /api/posts/{{postId}}/like`
- `DELETE /api/posts/{{postId}}/like`

---

### Moderator (role required: MODERATOR)

- `POST /api/mod/posts/{{postId}}/pin`
- `POST /api/mod/posts/{{postId}}/lock`
- `DELETE /api/mod/posts/{{postId}}`
- `DELETE /api/mod/comments/{{commentId}}`

---

### Search

- `GET /api/search/posts?query=text`
- `GET /api/search/users?query=john`

---

## 9. API Is Ready to Use

You can:

* Register users  
* Create categories  
* Create/edit/delete posts  
* Comment  
* Like  
* Moderate  
* Upload avatars  
* Search data  

