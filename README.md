# Investment Transaction Management System

A secure REST API for managing investment buy/sell transactions, built with Java, Spring Boot and PostgreSQL.

---

## Tech Stack

- Language : Java 17
- Framework : Spring Boot 
- Security : Spring Security, JWT 
- Database : PostgreSQL 
- ORM : Spring Data JPA / Hibernate 
- Reporting : Apache POI (SXSSF) 
- Testing : JUnit 5, Mockito 
- Build Tool : Maven 

---

## Features

- JWT-based authentication and stateless authorization
- Role-based access control (USER / ADMIN)
- Buy and sell stock transactions with automatic total amount calculation
- Paginated transaction history per user
- Admin view of all transactions across all users
- Memory-efficient streaming Excel export for large datasets using Apache POI SXSSF
- Global exception handling with structured error responses
- Bean validation on all API inputs

---

## Getting Started

### Prerequisites
- Java 17+
- PostgreSQL
- Maven

### 1. Clone the repository

git clone https://github.com/Arya-Mahesh/Investment-Transaction-Management.git
cd Investment-Transaction-Management


### 2. Create the database

CREATE DATABASE auth_db;


### 3. Set environment variables

export DB_URL=jdbc:postgresql://localhost:5432/auth_db
export DB_USERNAME=postgres
export DB_PASSWORD=your_db_password
export JWT_SECRET=your_long_random_secret_min_32_chars
export ADMIN_PASSWORD=your_admin_password


> Or set them in IntelliJ via `Run → Edit Configurations → Environment Variables`

### 4. Run the application
./mvnw spring-boot:run

The server starts on `http://localhost:8090`

> A default admin account (`username: admin`) is automatically created on first startup.


## API Endpoints

### Auth

 POST  `/auth/register`  Register a new user 
 POST  `/auth/login` Login and get JWT token 


#### Register

POST /auth/register
Content-Type: application/json

{
    "username": "arya",
    "password": "pass123"
}


#### Login

POST /auth/login
Content-Type: application/json

{
    "username": "arya",
    "password": "pass123"
}

Returns a JWT token string. Use this token as `Bearer <token>` in the `Authorization` header for all protected endpoints.

### Transactions

 POST  `/transaction/buy`  -USER - Record a buy transaction 
 POST  `/transaction/sell`  -USER - Record a sell transaction 
 GET  `/transaction/history`  -USER - View own transaction history (paginated) 
 GET  `/transaction/all`  -ADMIN - View all users' transactions (paginated) 
 GET  `/transaction/export`  -ADMIN - Export all transactions as Excel file 


#### Buy / Sell

POST /transaction/buy
Authorization: Bearer <token>
Content-Type: application/json

{
    "stockName": "Tata Steel",
    "quantity": 10,
    "price": 150.0
}


#### Transaction History

GET /transaction/history?page=0&size=10&sort=createdAt,desc
Authorization: Bearer <token>


#### Sample Response
```json
{
    "content": [
        {
            "id": 1,
            "username": "arya",
            "stockName": "Tata Steel",
            "transactionType": "Buy",
            "quantity": 10,
            "price": 150.0,
            "totalAmount": 1500.0,
            "createdAt": "2026-05-12T14:39:09"
        }
    ],
    "totalElements": 1,
    "totalPages": 1,
    "first": true,
    "last": true
}
```

## Security Design

- Passwords are hashed using **BCrypt**
- JWT tokens expire after **1 hour**
- Role is always set to `USER` on self-registration — admin accounts cannot be self-created
- Secrets and credentials are loaded from **environment variables**, never hardcoded

---

## Environment Variables Reference

| Variable | Description | Default (local) |
|---|---|---|
| `DB_URL` | PostgreSQL connection URL | `jdbc:postgresql://localhost:5432/auth_db` |
| `DB_USERNAME` | Database username | `postgres` |
| `DB_PASSWORD` | Database password | *(required)* |
| `JWT_SECRET` | Secret key for signing JWT tokens (min 32 chars) | *(required)* |
| `ADMIN_PASSWORD` | Default admin account password | `admin123` |

